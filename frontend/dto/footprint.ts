import { PhotoType } from "./photo";
import { PlaceType } from "./place";
import { RecommendedPlaceType } from "./recommendations";
import { TagType } from "./tag";

export interface FootprintRequestType {
  uuid: string;
  startTime: string;
  endTime: string;
  rating: number;
  memo: string;
  tagId: number;
  photos: {
    imagePath: string;
    imageUrl: string;
  }[];
  place?: {
    name: string;
    address: string;
  };
  recommendedPlaces: RecommendedPlaceType[];
}

export interface FootprintResponseType {
  id: number;
  startTime: string;
  endTime: string;
  rating: number;
  photos: PhotoType[];
  place: PlaceType;
  tag: TagType;
  memo: string;
}
