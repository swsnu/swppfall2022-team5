import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { fireEvent, render, renderHook, screen } from "@testing-library/react";
import { act } from "react-dom/test-utils";
import FootprintCreate from "../../../components/footprint/FootprintCreate";
import { FootprintPredictionType } from "../../../dto/recommendations";
import { useFootprintCreateStore } from "../../../store/footprint";
import mockedAxios from "../../../__mocks__/axios";

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

it("renders placeholder", async () => {
  const { result } = renderHook(() => useFootprintCreateStore());
  act(() => {
    result.current.setPendingFootprintRequests(mockPredictions);
  });

  mockedAxios.get.mockImplementation(() => Promise.resolve({ data: [{ tagId: 0, tagName: "카페" }] }));

  const { container } = render(
    <QueryClientProvider client={queryClient}>
      <FootprintCreate
        uuid="1"
        startTime=""
        endTime=""
        rating={3}
        memo=""
        tagId={0}
        photos={[
          {
            imagePath: "path1",
            imageUrl: "https://www.gravatar.com/avatar/62eefdaa182b5523ac6a37d4551a0155?s=48&d=identicon&r=PG",
          },
        ]}
        place={{
          name: "placeName",
          address: "placeAddress",
        }}
        recommendedPlaces={[
          {
            name: "placeName",
            address: "placeAddress",
            distance: 5,
            category: "recCategory",
          },
        ]}
        meanLatitude={0}
        meanLongitude={0}
      />
    </QueryClientProvider>,
  );
  expect(window).toBeTruthy();

  const ratingButton = screen.getByText("🤩 좋아요");
  await act(async () => {
    fireEvent.click(ratingButton);
  });

  const placeButton = screen.getByText("placeName");
  await act(async () => {
    fireEvent.click(placeButton);
  });

  const tagButton = screen.getByText("☕ 카페");
  await act(async () => {
    fireEvent.click(tagButton);
  });
});
