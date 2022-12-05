import { useQuery } from "@tanstack/react-query";
import { useRouter } from "next/router";
import { fecthFollowCount, fetchAllUserTraces } from "../../api";
import Container from "../../components/containers/Container";
import NavbarContainer from "../../components/containers/NavbarContainer";
import NavigationBar from "../../components/navbar/NavigationBar";
import TracesNotFound from "../../components/placeholder/TracesNotFound";
import { TracePreview } from "../../components/trace/TracePreview";
import UserProfile from "../../components/user/UserProfile";

export default function MyPage() {
  const router = useRouter();
  const { username } = router.query;

  const traceResult = useQuery(
    ["traces", username], () => {
      if (!username) {
        return;
      }
      return fetchAllUserTraces(String(username));
    },
    {
      onError: () => {
        router.back()
      }
    }
  );

  const followCount = useQuery(
    ["follow", username], () => {
      return fecthFollowCount(String(username));
    },
    {
      enabled: traceResult.isSuccess,
    }
  );

  return (
    <Container>
      <NavbarContainer className="z-20">
        <NavigationBar title="프로필" />
      </NavbarContainer>

      {followCount.isSuccess && <UserProfile username={String(username)} numTrace={traceResult.data?.length!!} following={followCount.data?.followingCount!!} follower={followCount.data?.followerCount!!} ></UserProfile>}

      <div className="mx-6 mt-5 text-xl font-semibold">발자취 목록</div>

      {traceResult.isSuccess && traceResult.data?.length == 0 && <TracesNotFound />}

      <div className="space-y-5 p-5">
        {traceResult.data
          ?.sort((a, b) => a.date.localeCompare(b.date))
          .map((trace) => {
            return <TracePreview key={trace.id} {...trace} />;
          })}
      </div>
    </Container>
  );
}
