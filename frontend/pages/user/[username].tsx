import { useQuery } from "@tanstack/react-query";
import { useRouter } from "next/router";
import { fetchAllUserTraces, fetchUserByUsername } from "../../api";
import Container from "../../components/containers/Container";
import NavbarContainer from "../../components/containers/NavbarContainer";
import NavigationBar from "../../components/navbar/NavigationBar";
import TracesNotFound from "../../components/placeholder/TracesNotFound";
import { TracePreview } from "../../components/trace/TracePreview";
import { TracePreviewTitle } from "../../components/trace/TracePreviewTitle";
import UserProfile from "../../components/user/UserProfile";

export default function MyPage() {
  const router = useRouter();
  const { username } = router.query;

  const traceResult = useQuery(
    ["traces", username],
    () => {
      return fetchAllUserTraces(String(username));
    },
    { enabled: Boolean(username) },
  );

  const userResult = useQuery(
    ["user", username],
    () => {
      return fetchUserByUsername(String(username));
    },
    { enabled: !!username },
  );

  if (!userResult.isSuccess) {
    return <div>로딩중...</div>;
  }

  return (
    <Container>
      <NavbarContainer className="z-20">
        <NavigationBar title="프로필" />
      </NavbarContainer>

      <UserProfile {...userResult.data} />

      <div className="mx-6 mt-5 text-xl font-semibold">발자취 목록</div>

      {traceResult.isSuccess && traceResult.data?.length == 0 && <TracesNotFound />}

      <div className="space-y-5 p-5">
        {traceResult.data
          ?.sort((a, b) => b.date.localeCompare(a.date))
          .map((trace) => {
            return (
              <div key={trace.id}>
                <TracePreview {...trace} />
                <TracePreviewTitle {...trace} hideProfile />
              </div>
            );
          })}
      </div>
    </Container>
  );
}
