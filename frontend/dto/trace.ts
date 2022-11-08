import { FootprintRequestType } from "./footprint";

export interface TraceRequestType {
  title: string;
  date: string;
  footprintList: FootprintRequestType[];
}
