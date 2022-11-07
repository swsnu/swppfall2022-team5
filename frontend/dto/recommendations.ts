import { PhotoType } from "./photo";

export interface FootprintPredictionType {
  meanLatitude: number;
  meanLongitude: number;
  meanTime: string;
  startTime: string;
  endTime: string;
  photoList: PhotoType[];
  recommendedPlaceList: RecommendedPlaceType[];
}

export interface RecommendedPlaceType {
  name: string;
  address: string;
  distance: number;
  category: string;
}
