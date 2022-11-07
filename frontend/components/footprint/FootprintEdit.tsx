import { IconSearch } from "@tabler/icons";
import { sendStatusCode } from "next/dist/server/api-utils";
import Moment from "react-moment";
import { FootprintRequestType } from "../../dto/footprint";
import { FootprintPredictionType } from "../../dto/recommendations";
import { useFootprintCreateStore } from "../../store/footprint";
import TagButton from "../buttons/TagButton";
import Photo from "./Photo";

interface IProps extends FootprintRequestType {}

const Label = ({ text }: { text: string }) => {
  return <div className="mt-3 mb-1 text-sm text-navy-500">{text}</div>;
};

const FootprintEdit = (props: IProps) => {
  const updateFootprint = useFootprintCreateStore((state) => state.setFootprintByIDWith)(props.uuid);

  return (
    <div className="p-5 text-navy-200 transition-colors">
      <div className="flex gap-3 overflow-x-auto scrollbar-hide">
        {props.photos.map((photo) => {
          return <Photo key={photo.imagePath} {...photo} />;
        })}
      </div>
      <div className="px-1">
        <Label text="시간" />
        <div className="text-sm">
          <Moment format="lll" date={props.startTime} /> ~ <Moment date={props.endTime} format="LT" />
        </div>

        <Label text="장소" />
        <div className="flex gap-3 overflow-x-auto scrollbar-hide">
          {props.recommendedPlaces.map((place) => {
            return <TagButton key={place.name} className="flex-shrink-0" text={place.name} onClick={() => {}} />;
          })}
        </div>
        <div className="mt-2">
          <TagButton text="직접 추가하기" icon={IconSearch} onClick={() => {}} />
        </div>

        <Label text="분류" />
        <div className="flex flex-wrap gap-x-3 gap-y-2">
          {props.recommendedPlaces.map((place) => {
            return <TagButton key={place.name} text={place.name} onClick={() => {}} />;
          })}
        </div>

        <Label text="메모" />
        <textarea
          className="w-full rounded-lg border border-navy-800 bg-navy-800 p-3 text-sm outline-none transition-colors focus:border-navy-700"
          value={props.memo}
          onChange={(e) => {
            updateFootprint({ memo: e.target.value });
          }}
          placeholder="이 장소에 대한 간단한 메모를 남겨보세요."
        />
      </div>
    </div>
  );
};

export default FootprintEdit;
