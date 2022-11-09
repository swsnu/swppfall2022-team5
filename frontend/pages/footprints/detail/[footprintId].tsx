import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import produce from "immer";
import moment from "moment";
import { useRouter } from "next/router";
import { useState } from "react";
import Moment from "react-moment";
import { editFootprint, fetchFootprintById, fetchTags } from "../../../api";
import RectangleButton from "../../../components/buttons/RectangleButton";
import TagButton from "../../../components/buttons/TagButton";
import Container from "../../../components/containers/Container";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import FootprintCreate, { Label, ratings } from "../../../components/footprint/FootprintCreate";
import Photo from "../../../components/footprint/Photo";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { tagToEmoji } from "../../../data/emojiMap";
import { FootprintEditRequestType, FootprintRequestType, FootprintResponseType } from "../../../dto/footprint";
import { Map, MapMarker, useInjectKakaoMapApi } from "react-kakao-maps-sdk";

const FootprintDetail = () => {
  const router = useRouter();
  const { footprintId } = router.query;
  const queryClient = useQueryClient();
  const [footprint, setFootprint] = useState<FootprintResponseType>();
  const footprintResult = useQuery(
    ["footprints", footprintId],
    () => {
      return fetchFootprintById(Number(footprintId));
    },
    {
      enabled: !!footprintId,
      onSuccess: (data) => {
        setFootprint(data);
      },
    },
  );
  const tagResult = useQuery(["tags"], fetchTags);

  const mutation = useMutation({
    mutationFn: ({ footprintId, request }: { footprintId: number; request: FootprintEditRequestType }) =>
      editFootprint(footprintId, request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["footprints", footprintId] });
      router.push("/footprints");
    },
  });

  if (!!!footprint) {
    return null;
  }

  return (
    <Container>
      <NavbarContainer>
        <NavigationBar title={moment(footprintResult.data?.startTime).locale("ko").format("lll")} />
      </NavbarContainer>
      <div className="p-5 text-navy-200 transition-colors">
        <div className="flex gap-3 overflow-x-auto scrollbar-hide">
          {[...footprint.photos]
            .sort((a, b) => a.timestamp.localeCompare(b.timestamp))
            .map((photo) => {
              return <Photo key={photo.imagePath} {...photo} />;
            })}
        </div>
        <div className="px-1">
          <Label text="시간" />
          <div className="text-sm">
            <Moment format="lll" date={footprint.startTime} /> ~ <Moment date={footprint.endTime} format="LT" />
          </div>

          <Label text="장소" />
          <div className="text-sm">{footprint.place.name}</div>
          <div className="mt-2 overflow-hidden rounded-xl">
            <Map
              className="h-40"
              center={{
                lat: footprint.photos[0].latitude,
                lng: footprint.photos[0].longitude,
              }}
            >
              {footprint.photos.map((photo) => {
                return (
                  <MapMarker
                    key={photo.imagePath}
                    position={{
                      lat: photo.latitude,
                      lng: photo.longitude,
                    }}
                  />
                );
              })}
            </Map>
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
                    setFootprint(
                      produce(footprint, (state) => {
                        state.tag.tagId = tag.tagId;
                      }),
                    );
                  }}
                  text={`${tagToEmoji[tag.tagName] ?? "✨"} ${tag.tagName}`}
                  isActive={footprint.tag.tagId === tag.tagId}
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
                    setFootprint(
                      produce(footprint, (state) => {
                        state.rating = rating.score;
                      }),
                    );
                  }}
                  isActive={footprint.rating === rating.score}
                />
              );
            })}
          </div>

          <Label text="메모" />
          <textarea
            className="w-full rounded-lg border border-navy-800 bg-navy-800 p-3 text-sm outline-none transition-colors focus:border-navy-700"
            value={footprint.memo}
            onChange={(e) => {
              setFootprint(
                produce(footprint, (state) => {
                  state.memo = e.target.value;
                }),
              );
            }}
            placeholder="이 장소에 대한 간단한 메모를 남겨보세요."
          />
        </div>

        <div className="flex">
          <RectangleButton
            onClick={() => {
              mutation.mutate({
                footprintId: Number(footprintId),
                request: {
                  startTime: footprint.startTime,
                  endTime: footprint.endTime,
                  rating: footprint.rating,
                  memo: footprint.memo,
                  tagId: footprint.tag.tagId,
                  photos: footprint.photos.map((p) => ({
                    imagePath: p.imagePath,
                  })),
                  place: footprint.place,
                },
              });
            }}
            text={"저장하기"}
            isLoading={mutation.isLoading}
            className="mx-1 mt-5 grow"
          />
        </div>
      </div>
    </Container>
  );
};

export default FootprintDetail;
