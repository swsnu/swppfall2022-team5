import Moment from "react-moment";
import { FootprintType } from "../../dto/footprint";
import Image from "next/image";
import "moment/locale/ko";
import Photo from "./Photo";

interface IProps extends FootprintType {}

export function FootprintPreview(props: IProps) {
  return (
    <div className="text-navy-100">
      <div className="mb-2 flex items-center gap-2">
        <div className="text-2xl">{props.tag.emoji}</div>
        <Moment className="text-sm" date={props.startTime} locale="ko" format="LT" />
      </div>
      <div className="flex gap-3 overflow-x-auto ">
        {props.photos.map((photo) => {
          return <Photo key={photo.id} {...photo} />;
        })}
      </div>
      <div className="ml-1">
        <div className="mt-2">{props.place.name}</div>
        <div className="mt-1 text-xs text-navy-500">{props.place.address}</div>
        <div className="mt-2 text-sm">{props.memo}</div>
      </div>
    </div>
  );
}
