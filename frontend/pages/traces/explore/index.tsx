import { useQuery } from "@tanstack/react-query";
import { fetchAllOtherUsersTraces } from "../../../api";
import Container from "../../../components/containers/Container";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import NavigationBar from "../../../components/navbar/NavigationBar";
import TracesNotFound from "../../../components/placeholder/TracesNotFound";
import { TracePreview } from "../../../components/trace/TracePreview";
import { TracePreviewTitle } from "../../../components/trace/TracePreviewTitle";

export default function Explore() {
  const traceExploreResult = useQuery(["explore"], fetchAllOtherUsersTraces);

  return (
    <Container>
      <NavbarContainer className="z-20">
        <NavigationBar title="탐색하기" />
      </NavbarContainer>

      {traceExploreResult.isSuccess && traceExploreResult.data.length == 0 && <TracesNotFound />}

      <div className="m-5 space-y-5">
        {traceExploreResult.data?.map((trace) => {
          return (
            <div key={trace.id}>
              <TracePreview {...trace} />
              <TracePreviewTitle {...trace} />
            </div>
          );
        })}
      </div>
    </Container>
  );
}
