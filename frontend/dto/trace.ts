import { FootprintRequestType, FootprintResponseType } from "./footprint";

export interface TraceRequestType {
  title: string;
  date: string;
  footprintList: FootprintRequestType[];
}

export interface TraceDetailResponse {
  id: number;
  date: string;
  title: string;
  ownerId: number;
  footprints: FootprintResponseType[];
}
