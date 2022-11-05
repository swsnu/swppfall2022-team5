import { PhotoType } from "./photo";
import { PlaceType } from "./place";
import { TagType } from "./tag";

export interface FootprintType {
  id: number;
  startTime: string;
  endTime: string;
  rating: number;
  photos: PhotoType[];
  place: PlaceType;
  tag: TagType;
  memo: string;
}

export interface FootprintPredictionsType {
  predictions: FootprintPredictionType[];
}

export interface FootprintPredictionType {
  startTime: string; // ISO8601 format, photos 중 가장 빠른 시각
  endTime: string; // ISO8601 format, photos 중 가장 늦은 시각
  predictedPlaces: PlaceType[];
  photos: PhotoType[]; // 좌표 기준으로 그룹화 (오차범위는 50m 정도?)
}
