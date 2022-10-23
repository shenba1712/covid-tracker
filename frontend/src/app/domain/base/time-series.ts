import {Delta} from "./delta";

export class TimeSeries {
  active: number;
  deceased: number;
  confirmed: number;
  recovered: number;
  date: string;
  delta: Delta;
}
