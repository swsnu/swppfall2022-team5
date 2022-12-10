import { QueryClient, QueryClientProvider, useMutation } from "@tanstack/react-query";
import Signin from "../../../../pages/signin";
import { fireEvent, render, screen, act, getByText } from "@testing-library/react";

const queryClient = new QueryClient();

jest.mock("next/router", () => ({
  useRouter() {
    return {
      route: "/",
      pathname: "",
      query: "",
      asPath: "",
    };
  },
}));

jest.mock("@tanstack/react-query", () => {
  return {
    ...jest.requireActual("@tanstack/react-query"),
    useMutation: jest.fn(() => {
      return {
        mutate: () => {},
      };
    }),
  };
});

describe("Sign in", () => {
  it("should render sign up page without errors", async () => {
    render(
      <QueryClientProvider client={queryClient}>
        <Signin />,
      </QueryClientProvider>,
    );

    await act(async () => {
      fireEvent.click(screen.getByText("로그인"));
    });
    await act(async () => {
      fireEvent.click(screen.getByText("회원가입"));
    });
    expect(window).toBeTruthy();
  });
});
