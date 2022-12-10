import { fireEvent, render, screen } from "@testing-library/react";
import { useRouter } from "next/router";
import { FootprintPreview } from "../../../components/footprint/FootprintPreview";
import { PhotoType } from "../../../dto/photo";
import { PlaceType } from "../../../dto/place";
import { TagType } from "../../../dto/tag";

jest.mock("next/router", () => ({
  useRouter: jest.fn(),
}));

const placeStub: PlaceType = {
  id: 0,
  name: "",
  address: "",
  city: "",
  country: "",
  district: "",
  longitude: 0,
  latitude: 0,
  distance: 0,
};

const tagStub: TagType = {
  tagId: 0,
  tagName: "",
};

const photoStub1: PhotoType = {
  id: 1,
  imageUrl: "https://naver.com",
  imagePath: "",
  timestamp: "",
  longitude: 0,
  latitude: 0,
};

const photoStub2: PhotoType = {
  id: 2,
  imageUrl: "https://naver.com",
  imagePath: "",
  timestamp: "",
  longitude: 0,
  latitude: 0,
};

it("should route to detail page", () => {
  const push = jest.fn();

  (useRouter as jest.Mock).mockImplementation(() => ({ push }));

  render(
    <FootprintPreview
      id={10}
      startTime={""}
      endTime={""}
      rating={0}
      photos={[photoStub1, photoStub2]}
      place={placeStub}
      tag={tagStub}
      memo={"memo here"}
    />,
  );

  fireEvent.click(screen.getByTestId("footprint-preview"));
  expect(push).toBeCalledWith(`/footprints/detail/${10}`);
});
