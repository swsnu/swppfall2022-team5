import { useQuery } from "@tanstack/react-query";
import { fetchAllOtherUsersTraces } from "../../../api";
import Container from "../../../components/containers/Container";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import NavigationBar from "../../../components/navbar/NavigationBar";
import TracesNotFound from "../../../components/placeholder/TracesNotFound";
import OwnerInfo from "../../../components/trace/OwnerInfo";
import { TracePreview } from "../../../components/trace/TracePreview";

export default function Explore() {
    const traceExploreResult = useQuery(["explore"], fetchAllOtherUsersTraces);
    
    return (
        <Container>
            <NavbarContainer className="z-20">
                <NavigationBar title="탐색하기" />
            </NavbarContainer>

            {traceExploreResult.isSuccess && traceExploreResult.data.length == 0 && <TracesNotFound />}

            <div className="divide-y divide-navy-700/50 pb-20">
                {traceExploreResult.data?.map((trace) => {
                    return ( 
                    <div key = {trace.id}>
                        <TracePreview {...trace} />
                        <OwnerInfo username={String(trace.ownerName)} />
                    </div>
                    );
                })}
            </div>
        </Container>
    );
}