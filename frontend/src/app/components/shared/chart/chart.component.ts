import {
  Component,
  ElementRef,
  Input, OnChanges,
  OnDestroy,
  OnInit, SimpleChanges,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import * as d3 from 'd3';
import {ChartData} from "../../../domain/base/chart-data";

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ChartComponent implements OnInit, OnDestroy, OnChanges {

  @ViewChild('chart', {static: true}) private chartContainer: ElementRef;

  @Input() data: ChartData[];

  width = 960;
  height = 500;
  margin = 3;
  padding = 3;
  adj = 55;

  constructor() { }

  ngOnInit() { }

  ngOnChanges(changes: SimpleChanges) {
    if (!!this.data) {
      changes.data.isFirstChange() ? this.createChart() : this.updateData();
    }
  }

  ngOnDestroy() {
    d3.select("#chart").remove();
  }

  createChart() {
// append the svg object to the body of the page
    var svg = d3.select("#chart")
      .append('svg')
      .attr("preserveAspectRatio", "xMinYMin meet")
      .attr("viewBox", "-"
        + this.adj + " -"
        + this.adj + " "
        + (this.width + this.adj *3) + " "
        + (this.height + this.adj*3))
      .style("padding", this.padding)
      .style("margin", this.margin)
      .classed("svg-content", true);

    let xScale = d3.scaleTime().range([0,this.width]);
    xScale.domain(d3.extent(this.data, function(d){
      return new Date(d.date);
    }));

    let yScale = d3.scaleLinear().rangeRound([this.height, 0]);
    yScale.domain(d3.extent(this.data, function(c) {
      return c.count;
    }));

    let yAxis = d3.axisLeft(yScale);
    let xAxis = d3.axisBottom(xScale);

    svg.append("g")
      .attr("class", "axis")
      .attr("transform", "translate(0," + this.height + ")")
      .call(xAxis);

    svg.append("g")
      .attr("class", "axis")
      .call(yAxis);

    let line = d3.line<ChartData>()
      .x((d) => { return xScale(new Date(d['date'])); })
      .y((d) => { return yScale(d['count']); })
      .curve(d3.curveMonotoneX);

    svg.selectAll("lines")
      .data([this.data])
      .enter()
      .append("g")
      .append("path")
      .attr("d", line);

    let tooltip = d3.select(".container-fluid")
      .append("div")
      .style("position", "absolute")
      .style("z-index", "10")
      .style("visibility", "hidden")
      .style("background", "#000")
      .style("color", "#FFF")
      .style("padding", "10px")
      .text("date: count");

    svg.selectAll(".dot")
      .data(this.data)
      .enter().append("circle") // Uses the enter().append() method
      .attr("fill", "darkred")
      .attr("stroke", "none")
      .attr("cx", (d) => {
        return xScale(new Date(d.date));
      })
      .attr("cy", (d) => {
        return yScale(d.count);
      })
      .attr("r", 4)
      .on("mouseover", (d) => {
        tooltip.text("Date: "+ d['date'] + ", Total: " + d['count']);
        return tooltip.style("visibility", "visible");
      }).on("mousemove", () => {
      return tooltip.style("top", (d3.event.pageY-10)+"px")
        .style("left",(d3.event.pageX+10)+"px");
    }).on("mouseout", () => {
      return tooltip.style("visibility", "hidden");
    }).exit().remove();
  }

  updateData() {
    // Remove all elements of the svg div and then create the chart again.
    let svg = d3.select("#chart");
    svg.selectAll("*").remove();
    this.createChart();
  }

}
