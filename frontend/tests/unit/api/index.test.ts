import { act } from "@testing-library/react";
import { editFootprint, fetchAllOtherUsersTraces, fetchAllUserTraces, fetchFootprintById, fetchInitialFootprints, fetchTraceById, postSignin, postSignUp } from "../../../api";
import { SigninRequestType } from "../../../dto/auth";
import { FootprintEditRequestType, FootprintRequestType } from "../../../dto/footprint";
import mockAxios from "../../../__mocks__/axios";

const footprintRequestStub: FootprintEditRequestType = {
  startTime: "",
  endTime: "",
  rating: 0,
  photos: [],
  place: {
    name: "",
    address: "",
  },
  tagId: 0,
  memo: "",
};

const authRequestStub: SigninRequestType = {
  username: "",
  password: "",
};

it("fetch footprints", async () => {
  mockAxios.post.mockResolvedValue({ data: "mock" });
  await act(async () => {
    const data = await fetchInitialFootprints(["hi"]);
    expect(data).toBe("mock");
  });
});

it("fetch editFootprint", async () => {
  mockAxios.put.mockResolvedValue({ data: "mock" });
  await act(async () => {
    const data = await editFootprint(5, footprintRequestStub);
    expect(data).toBe("mock");
  });
});

it("fetch fetchTraceById", async () => {
  mockAxios.get.mockResolvedValue({ data: "mock" });
  await act(async () => {
    const data = await fetchTraceById(5);
    expect(data).toBe("mock");
  });
});

it("fetch fetchFootprintById", async () => {
  mockAxios.get.mockResolvedValue({ data: "mock" });
  await act(async () => {
    const data = await fetchFootprintById(5);
    expect(data).toBe("mock");
  });
});

it("fetch fetchAllUserTraces", async () => {
  mockAxios.get.mockResolvedValue({ data: "mock" });
  await act(async () => {
    const data = await fetchAllUserTraces("user1");
    expect(data).toBe("mock");
  });
});

it("fetch fetchAllOtherUsersTraces", async () => {
  mockAxios.get.mockResolvedValue({ data: "mock" });
  await act(async () => {
    const data = await fetchAllOtherUsersTraces();
    expect(data).toBe("mock");
  });
});

it("post postSignin", async () => {
  mockAxios.post.mockResolvedValue({ data: "mock" });
  await act(async () => {
    const data = await postSignin(authRequestStub);
    expect(data).toBe("mock");
  });
});

it("post postSignUp", async () => {
  mockAxios.post.mockResolvedValue({ data: "mock" });
  await act(async () => {
    const data = await postSignUp(authRequestStub);
    expect(data).toBe("mock");
  });
});