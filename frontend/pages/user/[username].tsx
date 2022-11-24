import { useQuery } from "@tanstack/react-query";
import { useRouter } from "next/router";
import { fetchAllUserTraces } from "../../api";
import Container from "../../components/containers/Container";
import NavbarContainer from "../../components/containers/NavbarContainer";
import NavigationBar from "../../components/navbar/NavigationBar";
import TracesNotFound from "../../components/placeholder/TracesNotFound";
import { TracePreview } from "../../components/trace/TracePreview";
import UserProfile from "../../components/user/UserProfile";


export default function MyPage() {
    const router = useRouter();
    const { username } = router.query;
  
    const traceResult = useQuery(["traces", username], () => {
        return fetchAllUserTraces(String(username))
    });
    
    return (
        <Container>
            <NavbarContainer className="z-20">
                <NavigationBar title="마이페이지" />
            </NavbarContainer>

            <UserProfile username={String(username)}></UserProfile>

            <div className="text-3xl font-semibold px-5 pt-5">Traces</div>

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