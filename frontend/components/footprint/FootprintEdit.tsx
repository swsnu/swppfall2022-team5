import { IconSearch } from "@tabler/icons";
import Moment from "react-moment";
import { FootprintPredictionType } from "../../dto/recommendations";
import TagButton from "../buttons/TagButton";
import Photo from "./Photo";

interface IProps extends FootprintPredictionType {}

const Label = ({ text }: { text: string }) => {
  return <div className="mt-3 mb-1 text-sm text-navy-500">{text}</div>;
};

const FootprintEdit = (props: IProps) => {
  return (
    <div className="p-5 text-navy-200 transition-colors">
      <div className="flex gap-3 overflow-x-auto scrollbar-hide">
        {props.photoList.map((photo) => {
          return <Photo key={photo.id} {...photo} />;
        })}
      </div>
      <div className="px-1">
        <Label text="시간" />
        <div className="text-sm">
          <Moment format="lll" date={props.startTime} /> ~ <Moment date={props.endTime} format="LT" />
        </div>

        <Label text="장소" />
        <div className="flex gap-3 overflow-x-auto scrollbar-hide">
          {props.recommendedPlaceList.map((place) => {
            return <TagButton key={place.address} className="flex-shrink-0" text={place.name} onClick={() => {}} />;
          })}
        </div>
        <div className="mt-2">
          <TagButton text="직접 추가하기" icon={IconSearch} onClick={() => {}} />
        </div>

        <Label text="분류" />
        <div className="flex flex-wrap gap-x-3 gap-y-2">
          {props.recommendedPlaceList.map((place) => {
            return <TagButton key={place.address} text={place.name} onClick={() => {}} />;
          })}
        </div>

        <Label text="메모" />
        <textarea
          className="w-full rounded-lg border border-navy-800 bg-navy-800 p-3 text-sm outline-none transition-colors focus:border-navy-700"
          placeholder="이 장소에 대한 간단한 메모를 남겨보세요."
        />
      </div>
    </div>
  );
};

export default FootprintEdit;
