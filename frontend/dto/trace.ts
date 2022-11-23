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
  ownerId: number;
  footprints: FootprintResponseType[];
}
