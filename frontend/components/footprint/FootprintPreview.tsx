import Moment from "react-moment";
import { FootprintType } from "../../dto/footprint";
import Image from "next/image";
import "moment/locale/ko";
import Photo from "./Photo";
import { useRouter } from "next/router";

interface IProps extends FootprintType {}

export function FootprintPreview(props: IProps) {
  const router = useRouter();
  return (
    <div
      className="p-5 text-navy-100 transition-colors hover:cursor-pointer hover:bg-navy-800/50"
      onClick={() => {
        router.push(`/footprints/detail/${props.id}`);
      }}
    >
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
