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

            <div className="divide-y divide-navy-700/50 pb-10">
                {traceExploreResult.data?.map((trace) => {
                    return ( 
                    <div key = {trace.id} className="py-2">
                        <TracePreview {...trace} />
                        <div className="flex">
                            <OwnerInfo username={String(trace.ownerName)} />
                            <div className="pl-40 pt-3 items-center flex divide-x divide-navy-500 text-xs leading-3 text-navy-400">
                                    <span className="px-2">좋아요 {0}</span>
                                    <span className="px-2">{trace.viewCount}명 조회</span>
                            </div>
                        </div>
                        
                    </div>
                    );
                })}
            </div>
        </Container>
    );
}