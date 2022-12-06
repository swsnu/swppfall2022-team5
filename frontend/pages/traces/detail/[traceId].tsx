import Container from "../../../components/containers/Container";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchTraceById, likeTrace, unlikeTrace, fetchRegionByCoordinates, updateViewCount } from "../../../api";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import { FootprintPreview } from "../../../components/footprint/FootprintPreview";
import { useRouter } from "next/router";
import { useState } from "react";
import KakaoMap from "../../../components/map/KakaoMap";
import LikeButton from "../../../components/buttons/LikeButton";

// react component named RegionTitle
// that takes latitude and longitude and displays the region name
const RegionTitle = ({ latitude, longitude }: { latitude: number; longitude: number }) => {
  const regionResult = useQuery(["regions", longitude, latitude], () => {
    return fetchRegionByCoordinates(latitude, longitude);
  });

  if (!regionResult.isSuccess) {
    return null;
  }

  console.log(regionResult.data);
  return <div className="text-2xl font-bold">{regionResult.data}</div>;
};

export default function TraceDetail() {
  const router = useRouter();
  const { traceId } = router.query;
  const queryClient = useQueryClient();

  const traceResult = useQuery(
    ["traces", traceId],
    () => {
      return fetchTraceById(Number(traceId));
    },
    { enabled: !!traceId },
  );

  useQuery(
    ["updateViewCount", traceId],
    () => {
      return updateViewCount(Number(traceId));
    },
    {
      enabled: !!traceId,
    },
  );

  const likeMutation = useMutation((traceId: number) => likeTrace(traceId));
  const unlikeMutation = useMutation((traceId: number) => unlikeTrace(traceId));

  const handleLike = () => {
    // if the user has liked the trace, unlike it
    if (!traceResult.isSuccess) {
      return;
    }
    if (traceResult.data.isLiked) {
      unlikeMutation.mutate(Number(traceId), {
        onSuccess(data, variables, context) {
          queryClient.invalidateQueries({ queryKey: ["traces", traceId] });
        },
      });
    } else {
      likeMutation.mutate(Number(traceId), {
        onSuccess(data, variables, context) {
          queryClient.invalidateQueries({ queryKey: ["traces", traceId] });
        },
      });
    }
  };

  if (!traceResult.isSuccess) {
    return null;
  }

  const coordinates = traceResult.data.footprints.map((footprint) => {
    return {
      latitude: footprint.photos[0].latitude,
      longitude: footprint.photos[0].longitude,
    };
  });

  return (
    <Container>
      <NavbarContainer className="">
        <NavigationBar title={traceResult.data?.title} />
      </NavbarContainer>

      <div className="m-5 flex items-center justify-between">
        <div>
          <RegionTitle latitude={coordinates[0].latitude} longitude={coordinates[0].longitude} />
          <div className="text-navy-300">{traceResult.data?.title}</div>
        </div>
        <div>
          <LikeButton
            isLiked={traceResult.data.isLiked}
            onClick={handleLike}
            likesCount={traceResult.data.likesCount}
          />
        </div>
      </div>

      <div className="bg-yellow-200">
        <KakaoMap coordinates={coordinates} />
      </div>

      <div className="divide-y divide-navy-700/50 pb-20">
        {traceResult.isSuccess &&
          traceResult.data.footprints?.map((footprint) => {
            return <FootprintPreview key={footprint.id} {...footprint} />;
          })}
      </div>
    </Container>
  );
}
