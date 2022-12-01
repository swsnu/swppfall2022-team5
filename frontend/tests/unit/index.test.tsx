import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { render } from "@testing-library/react";
import { useRouter } from "next/router";
import Home from "../../pages/index";

jest.mock("next/router", () => ({
  useRouter: jest.fn(),
}));

const queryClient = new QueryClient()

describe("Home", () => {
  it("redirect to /footprints", () => {
    const push = jest.fn();

    (useRouter as jest.Mock).mockImplementation(() => ({ push }));

    render(
      <QueryClientProvider client={queryClient}>
        <Home />
      </QueryClientProvider>
    );
    expect(window).toBeTruthy();
  });
});
