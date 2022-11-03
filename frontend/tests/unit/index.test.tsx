import { render } from "@testing-library/react";
import { useRouter } from "next/router";
import Home from "../../pages/index";

jest.mock("next/router", () => ({
  useRouter: jest.fn(),
}));

describe("Home", () => {
  it("redirect to /footprints", () => {
    const push = jest.fn();

    (useRouter as jest.Mock).mockImplementation(() => ({ push }));

    render(<Home />);
    expect(push).toHaveBeenCalledWith("/footprints");
  });
});
