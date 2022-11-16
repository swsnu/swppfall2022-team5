import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { fireEvent, render, renderHook, screen, waitFor } from "@testing-library/react";
import "@testing-library/jest-dom";
import { act } from "react-dom/test-utils";
import { FootprintPredictionType } from "../../../../dto/recommendations";
import FootprintsCreate from "../../../../pages/footprints/create";
import { useFootprintCreateStore } from "../../../../store/footprint";
import mockAxios from "../../../../__mocks__/axios";

const queryClient = new QueryClient();

const mockPredictions: FootprintPredictionType[] = [
  {
    meanLatitude: 0,
    meanLongitude: 0,
    meanTime: "2022-10-08T11:32:42.000+00:00",
    startTime: "2022-10-08",
    endTime: "2022-10-08T11:32:42.000+00:00",
    photoList: [],
    recommendedPlaceList: [],
  },
];

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

describe("Footprints create", () => {
  it("should render main page without errors", () => {
    render(
      <QueryClientProvider client={queryClient}>
        <FootprintsCreate />
      </QueryClientProvider>,
    );
    expect(window).toBeTruthy();
  });

  it("should render FootprintCreate components without erorrs", () => {
    const { container } = render(
      <QueryClientProvider client={queryClient}>
        <FootprintsCreate />
      </QueryClientProvider>,
    );

    const { result } = renderHook(() => useFootprintCreateStore());
    act(() => {
      result.current.setPendingFootprintRequests(mockPredictions);
    });
    expect(container.getElementsByClassName("divide-y-2 divide-navy-700/50")).toBeTruthy();
  });

  it("clicks button : onSuccess", async () => {
    const { container } = render(
      <QueryClientProvider client={queryClient}>
        <FootprintsCreate />
      </QueryClientProvider>,
    );
    const { result } = renderHook(() => useFootprintCreateStore());
    act(() => {
      result.current.setPendingFootprintRequests(mockPredictions);
    });

    const button = container.getElementsByClassName("grow")[0];
    const aa: string = "";
    mockAxios.post.mockImplementation(() => Promise.resolve({ data: "" }));
    await act(async () => {
      fireEvent.click(button);
    });
  });

  //   it("clicks button: onError", async () => {
  //     const { container } = render(
  //         <QueryClientProvider client={queryClient}>
  //           <FootprintsCreate />
  //         </QueryClientProvider>,
  //       );
  //     const { result } = renderHook(() => useFootprintCreateStore());
  //     act( () => {
  //         result.current.setPendingFootprintRequests(mockPredictions);
  //     });

  //     const button = container.getElementsByClassName('grow')[0];

  //     mockAxios.post.mockImplementationOnce(() =>
  //         Promise.reject({
  //             data: ""
  //         })
  //     );
  //     await act( async () => {
  //         fireEvent.click(button);
  //     });
  //   });
});
