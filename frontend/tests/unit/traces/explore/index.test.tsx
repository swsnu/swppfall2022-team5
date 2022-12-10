import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { render } from "@testing-library/react";
import '@testing-library/jest-dom'
import Explore from "../../../../pages/traces/explore";

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

describe("Footprints explore", () => {
    it("should render main page without errors", () => {
        render(
            <QueryClientProvider client={queryClient}>
            <Explore />
            </QueryClientProvider>,
        );
        expect(window).toBeTruthy();
    });
});