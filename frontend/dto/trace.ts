import { FootprintRequestType, FootprintResponseType } from "./footprint";
import { UserResponseType } from "./user";

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
  owner: UserResponseType;
  likesCount: number;
  isLiked: boolean;
  footprints: FootprintResponseType[];
  viewCount: number;
  isPublic: boolean;
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
