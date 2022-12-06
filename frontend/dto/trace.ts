import { FootprintRequestType, FootprintResponseType } from "./footprint";

export interface TraceRequestType {
  title: string;
  // date: string;
  public: boolean;
  footprintList: FootprintRequestType[];
}

export interface TraceDetailResponseType {
  id: number;
  date: string;
  title: string;
  ownerName: string;
  likesCount: number;
  isLiked: boolean;
  footprints: FootprintResponseType[];
  viewCount: number;
}

export interface TraceViewResponseType {
  viewCount: number;
}

export interface TraceLikeResponseType {
  likesCount: number;
  isLiked: boolean;
}

export interface TraceViewResponseType {
  viewCount: number;
}
