import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { render } from "@testing-library/react";
import { FootprintResponseType } from "../../../../dto/footprint";
import { PhotoType } from "../../../../dto/photo";
import { PlaceType } from "../../../../dto/place";
import { TagType } from "../../../../dto/tag";
import { TraceDetailResponseType } from "../../../../dto/trace";
import TraceDetail from "../../../../pages/traces/detail/[traceId]";

const queryClient = new QueryClient();

jest.mock("next/router", () => ({
    useRouter() {
        return ({
            route: "/",
            pathname: "",
            query: {"traceId": "1"},
            asPath: "",
        });
    },
}));

describe("Trace detail", () => {
    it("should render main page without errors", () => {

        render(
            <QueryClientProvider client={queryClient}>
              <TraceDetail />
            </QueryClientProvider>,
        );

        expect(window).toBeTruthy();
        
    });

    
});