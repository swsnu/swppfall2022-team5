import Container from "../../../components/containers/Container";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchTraceById, likeTrace, unlikeTrace } from "../../../api";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import { FootprintPreview } from "../../../components/footprint/FootprintPreview";
import { useRouter } from "next/router";
import { useState } from "react";
import KakaoMap from "../../../components/map/KakaoMap";
import LikeButton from "../../../components/buttons/LikeButton";

export default function TraceDetail() {
  const router = useRouter();
  const { traceId } = router.query;
  const queryClient = useQueryClient();

  const traceResult = useQuery(["traces", traceId], () => {
    return fetchTraceById(Number(traceId));
  });

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
          <div className="text-2xl font-bold">서울 마포구 연남동</div>
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
