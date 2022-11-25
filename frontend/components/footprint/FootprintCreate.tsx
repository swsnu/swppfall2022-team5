import { useQuery } from "@tanstack/react-query";
import Moment from "react-moment";
import { fetchTags } from "../../api";
import { tagToEmoji } from "../../data/emojiMap";
import { FootprintRequestType } from "../../dto/footprint";
import { useFootprintCreateStore } from "../../store/footprint";
import TagButton from "../buttons/TagButton";
import TextArea from "../textfield/TextArea";
import Photo from "./Photo";

interface IProps extends FootprintRequestType {}

export const Label = ({ text }: { text: string }) => {
  return <div className="mt-3 mb-1 text-sm text-navy-400">{text}</div>;
};

interface Rating {
  score: number;
  text: string;
}

export const ratings: Rating[] = [
  { score: 2, text: "🤩 좋아요" },
  { score: 1, text: "🤔 보통이에요" },
  { score: 0, text: "😢 별로예요" },
];

const FootprintCreate = (props: IProps) => {
  const updateFootprint = useFootprintCreateStore((state) => state.setFootprintByIDWith)(props.uuid);
  const tagResult = useQuery(["tags"], fetchTags);

  return (
    <div className="p-5 text-navy-200 transition-colors">
      <div className="flex gap-3 overflow-x-auto scrollbar-hide">
        {[...props.photos]
          .sort((a, b) => a.imagePath.localeCompare(b.imagePath))
          .map((photo) => {
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
            return (
              <TagButton
                key={place.name}
                className="flex-shrink-0"
                text={place.name}
                onClick={() => {
                  updateFootprint({ place: { name: place.name, address: place.address } });
                }}
                isActive={place.name === props.place?.name && place.address === props.place.address}
              />
            );
          })}
        </div>
        {/* <div className="mt-2">
          <TagButton text="직접 추가하기" icon={IconSearch} onClick={() => {}} />
        </div> */}

        <Label text="태그" />
        <div className="flex flex-wrap gap-x-3 gap-y-2">
          {tagResult.data?.map((tag) => {
            return (
              <TagButton
                key={tag.tagId}
                onClick={() => {
                  updateFootprint({ tagId: tag.tagId });
                }}
                text={`${tagToEmoji[tag.tagName] ?? "✨"} ${tag.tagName}`}
                isActive={props.tagId === tag.tagId}
              />
            );
          })}
        </div>

        <Label text="평점" />
        <div className="flex flex-wrap gap-x-3 gap-y-2">
          {ratings.map((rating) => {
            return (
              <TagButton
                key={rating.score}
                text={rating.text}
                onClick={() => {
                  updateFootprint({ rating: rating.score });
                }}
                isActive={props.rating === rating.score}
              />
            );
          })}
        </div>

        <Label text="메모" />
        <TextArea
          value={props.memo}
          onChange={(value) => {
            updateFootprint({ memo: value });
          }}
          placeholder="이 장소에 대한 간단한 메모를 남겨보세요."
        />
      </div>
    </div>
  );
};

export default FootprintCreate;
