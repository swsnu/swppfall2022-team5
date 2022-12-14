import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import produce from "immer";
import moment from "moment";
import { useRouter } from "next/router";
import { useState } from "react";
import toast from "react-hot-toast";

import Moment from "react-moment";
import {
  deleteFootprint,
  editFootprint,
  fetchFootprintById,
  fetchPlacesByKeywordAndLocation,
  fetchTags,
} from "../../../api";
import RectangleButton from "../../../components/buttons/RectangleButton";
import TagButton from "../../../components/buttons/TagButton";
import Container from "../../../components/containers/Container";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import { Label, ratings } from "../../../components/footprint/FootprintCreate";
import Photo from "../../../components/footprint/Photo";
import PlaceSearchBox from "../../../components/footprint/PlaceSearchBox";
import KakaoMap from "../../../components/map/KakaoMap";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { tagToEmoji } from "../../../data/emojiMap";
import { FootprintEditRequestType, FootprintResponseType } from "../../../dto/footprint";

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

  const createMutation = useMutation({
    mutationFn: ({ footprintId, request }: { footprintId: number; request: FootprintEditRequestType }) =>
      editFootprint(footprintId, request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["footprints", footprintId] });
      router.push("/footprints");
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (footprintId: number) => deleteFootprint(footprintId),
    onSuccess: () => {
      router.push("/footprints");
      toast.success("??????????????? ?????????????????????.");
    },
  });

  const [query, setQuery] = useState("");

  const searchResult = useQuery(["search", query], () => {
    const meanLatitude =
      (footprint?.photos.reduce((acc, cur) => acc + cur.latitude, 0) ?? 0) / (footprint?.photos.length ?? 1);
    const meanLongitude =
      (footprint?.photos.reduce((acc, cur) => acc + cur.longitude, 0) ?? 0) / (footprint?.photos.length ?? 1);
    return fetchPlacesByKeywordAndLocation(query, meanLatitude, meanLongitude, []);
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
          <Label text="??????" />
          <div className="text-sm">
            <Moment format="lll" date={footprint.startTime} /> ~ <Moment date={footprint.endTime} format="LT" />
          </div>

          <Label text="??????" />
          <div className="text-sm">{footprint.place.name}</div>
          <div className="mt-2 overflow-hidden rounded-xl">
            <KakaoMap coordinates={footprint.photos} />
          </div>
          <hr className="my-2 border-navy-700" />

          <div className="">
            <PlaceSearchBox query={query} setQuery={setQuery} />
            <div className="mt-2 flex gap-3 overflow-x-auto scrollbar-hide">
              {searchResult.data?.map((place) => {
                return (
                  <TagButton
                    key={place.name}
                    className="flex-shrink-0"
                    text={place.distance ? `${place.name} (${place.distance}m)` : `${place.name}`}
                    onClick={() => {
                      setFootprint(
                        produce(footprint, (state) => {
                          state.place.name = place.name;
                          state.place.address = place.address;
                        }),
                      );
                    }}
                    isActive={place.name === footprint.place?.name && place.address === footprint.place.address}
                  />
                );
              })}
            </div>
          </div>

          <Label text="??????" />
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
                  text={`${tagToEmoji[tag.tagName] ?? "???"} ${tag.tagName}`}
                  isActive={footprint.tag.tagId === tag.tagId}
                />
              );
            })}
          </div>

          <Label text="??????" />
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

          <Label text="??????" />
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
            placeholder="??? ????????? ?????? ????????? ????????? ???????????????."
          />
        </div>

        <div className="flex">
          <RectangleButton
            onClick={() => {
              createMutation.mutate({
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
            text={"????????????"}
            isLoading={createMutation.isLoading}
            className="mx-1 mt-5 grow"
          />
          <RectangleButton
            onClick={() => {
              deleteMutation.mutate(Number(footprintId));
            }}
            text="????????????"
            isLoading={deleteMutation.isLoading}
            className="mx-1 mt-5 px-5"
            destructive
          />
        </div>
      </div>
    </Container>
  );
};

export default FootprintDetail;
