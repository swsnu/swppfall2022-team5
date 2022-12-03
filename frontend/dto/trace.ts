import { FootprintRequestType, FootprintResponseType } from "./footprint";

export interface TraceRequestType {
  title: string;
  // date: string;
  public: boolean;
  footprintList: FootprintRequestType[];
  likesCount: number;
}

export interface TraceDetailResponseType {
  id: number;
  date: string;
  title: string;
  ownerName: string;
  likesCount: number;
  isLiked: boolean;
  footprints: FootprintResponseType[];
}

export interface TraceLikeResponseType {
  likesCount: number;
  isLiked: boolean;
}
