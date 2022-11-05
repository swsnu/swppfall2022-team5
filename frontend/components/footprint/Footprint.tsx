import { IconLock } from "@tabler/icons";
import FloatingButton from "../buttons/FloatingButton";
import { PhotoList, PhotoType } from "../photolist/PhotoList";
import { TagList, TagType } from "../taglist/TagList";
import { Place, PlaceType } from "./Place";

export interface FootprintType {
  id: number;
  startTime: Date;
  endTime: Date;
  rating: number;
  photoList: Array<PhotoType>;
  place: PlaceType;
  tag: TagType;
  memo: string;
}

interface IProps extends FootprintType {
  modifying: boolean;
}

export function Footprint(props: IProps) {
  return (
    <div className="mx-3 mb-3 h-auto w-auto">
      <div className="mx-3 mt-3 flex items-stretch">
        <FloatingButton
          icon={props.tag.icon}
          text={props.tag.name}
          onClick={() => {
            /* TODO: implement filter */
          }}
        />
        <h2 className="ml-2 h-full text-center text-xl">
          {("0" + props.startTime.getHours()).slice(-2) + ":" + ("0" + props.startTime.getMinutes()).slice(-2)}
        </h2>
      </div>
      <PhotoList photoList={props.photoList} />
      <Place {...props.place} />

      <div>
        <h2>Memo</h2>
        <div className="flex flex-nowrap">
          <textarea
            className="h-fit w-fit rounded bg-navy-800/90 px-2"
            defaultValue={props.memo}
            disabled={!props.modifying}
          ></textarea>
          <h1
            className="ml-1 h-max text-center text-3xl"
            onClick={() => {
              /* TODO: add select rate on modify */
            }}
          >
            {["😝", "😀", "🤔", "😕", "😢"].reverse()[props.rating]}
          </h1>
        </div>
      </div>
    </div>
  );
}
