import Container from "../../../components/containers/Container";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { useQuery } from "@tanstack/react-query";
import { fetchTraceById } from "../../../api";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import { FootprintPreview } from "../../../components/footprint/FootprintPreview";
import { useRouter } from "next/router";

export default function TraceDetail() {
  const router = useRouter();
  const { traceId } = router.query;

  const traceResult = useQuery(["footprints", traceId], () => {
    return fetchTraceById(Number(traceId));
  });

  return (
    <Container>
      <NavbarContainer className="">
        <NavigationBar title={traceResult.data?.title} />
      </NavbarContainer>

      <div className="divide-y divide-navy-700/50 pb-20">
        {traceResult.isSuccess &&
          traceResult.data.footprints?.map((footprint) => {
            return <FootprintPreview key={footprint.id} {...footprint} />;
          })}
      </div>
    </Container>
  );
}
