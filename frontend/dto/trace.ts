import { FootprintRequestType, FootprintResponseType } from "./footprint";

export interface TraceRequestType {
  title: string;
  date: string;
  public: boolean;
  footprintList: FootprintRequestType[];
}

export interface TraceDetailResponseType {
  id: number;
  date: string;
  title: string;
  ownerName: string;
  footprints: FootprintResponseType[];
  viewCount: number;
}

export interface TraceViewResponseType {
  viewCount: number;
}
