import Container from "../../../components/containers/Container";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchTraceById, likeTrace, unlikeTrace } from "../../../api";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import { FootprintPreview } from "../../../components/footprint/FootprintPreview";
import { useRouter } from "next/router";
import { useState } from "react";

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

  return (
    <Container>
      <NavbarContainer className="">
        <NavigationBar title={traceResult.data?.title} />
      </NavbarContainer>

      <div>
        <div>서울 마포구 연남동</div>
        <div>트레이스의 제목이 여기로 들어갑니다.</div>
      </div>

      <div className="bg-yellow-200">지도는 여기에</div>

      <div>
        <button onClick={handleLike}>{traceResult.data?.likesCount}</button>
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
