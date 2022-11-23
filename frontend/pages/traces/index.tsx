import { useQuery } from "@tanstack/react-query";
import { fetchAllMyTraces } from "../../api";
import Container from "../../components/containers/Container";
import NavbarContainer from "../../components/containers/NavbarContainer";
import NavigationBar from "../../components/navbar/NavigationBar";
import TracesNotFound from "../../components/placeholder/TracesNotFound";
import { TracePreview } from "../../components/trace/TracePreview";


export default function Traces() {
    const traceResult = useQuery(["traces"], fetchAllMyTraces);
    
    return (
        <Container>
            <NavbarContainer className="z-20 pb-4">
                <NavigationBar title="마이페이지" />
            </NavbarContainer>

            {!traceResult.data && <TracesNotFound />}

            <div className="divide-y divide-navy-700/50 pb-20">
                {traceResult.data?.sort((a, b) => a.date.localeCompare(b.date))
                .map((trace) => {
                    return <TracePreview key={trace.id} {...trace} />;
                })}
            </div>
        </Container>
    );
}