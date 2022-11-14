import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { render } from "@testing-library/react";
import '@testing-library/jest-dom'
import FootprintDetail from "../../../../pages/footprints/detail/[footprintId]";
import { Map, MapMarker } from "react-kakao-maps-sdk";
import { Router } from "next/router";

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

describe("Footprints create", () => {
    it("should render main page without errors", () => {
        render(
            <QueryClientProvider client={queryClient}>
            <FootprintDetail />
            </QueryClientProvider>,
        );
        expect(window).toBeTruthy();
    });
});