import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { render } from "@testing-library/react";
import { TraceDetailResponseType } from "../../../dto/trace";
import FootprintsCreate from "../../../pages/footprints/create";
import MyPage from "../../../pages/user/[username]";

jest.mock("next/router", () => ({
  useRouter() {
    return {
      route: "/",
      pathname: "",
      query: "",
      asPath: "",
      push: jest.fn(),
    };
  },
}));

const traceStub: TraceDetailResponseType[] = [
  {
    id: 1,
    title: "test",
    date: "2021-08-01",
    likesCount: 0,
    isLiked: false,
    footprints: [],
    viewCount: 0,
    owner: {
      username: "test",
      followerCount: 0,
      followingCount: 0,
      imageUrl: "test",
      traceCount: 0,
    },
  },
];

jest.mock("@tanstack/react-query", () => ({
  ...jest.requireActual("@tanstack/react-query"),
  useQuery: jest.fn(() => {
    return {
      data: traceStub,
    };
  }),
}));

const queryClient = new QueryClient();

describe("user page", () => {
  it("should render user page without errors", () => {
    const { container } = render(
      <QueryClientProvider client={queryClient}>
        <MyPage />
      </QueryClientProvider>,
    );
    expect(window).toBeTruthy();
  });
});
