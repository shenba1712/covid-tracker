import {District} from "./district";
import {TimeSeries} from "../base/time-series";

export class State {
  stateName: string;
  districts: District[];
  total: TimeSeries;
  lastUpdatedDate: string;
}
