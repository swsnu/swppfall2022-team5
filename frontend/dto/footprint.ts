import { PhotoType } from "./photo";
import { PlaceType } from "./place";
import { TagType } from "./tag";

export interface FootprintType {
  id: number;
  startTime: string;
  endTime: string;
  rating: number;
  photos: Array<PhotoType>;
  place: PlaceType;
  tag: TagType;
  memo: string;
}
