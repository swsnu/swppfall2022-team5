import { renderHook } from "@testing-library/react";
import { act } from "react-dom/test-utils";
import { useFootprintCreateStore } from "../../../store/footprint";

jest.mock("uuid", () => ({
  v4: () => {
    return "dummyUUID";
  },
}));

describe("test footprint create store", () => {
  it("sets pending footprint requests", () => {
    const { result } = renderHook(() => useFootprintCreateStore());

    act(() => {
      result.current.setPendingFootprintRequests([
        {
          meanLatitude: 5,
          meanLongitude: 10,
          meanTime: "time",
          startTime: "startTime",
          endTime: "endTime",
          photoList: [],
          recommendedPlaceList: [],
        },
      ]);
      result.current.setFootprintByIDWith("dummyUUID")({ memo: "hi", startTime: "hello" });
    });

    const footprint = result.current.getFootprintByID("dummyUUID");
    expect(footprint?.memo).toBe("hi");
    expect(footprint?.startTime).toBe("hello");
  });
});
