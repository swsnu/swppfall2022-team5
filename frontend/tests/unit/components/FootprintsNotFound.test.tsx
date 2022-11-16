import { render } from "@testing-library/react";
import FootprintsNotFound from "../../../components/placeholder/FootprintsNotFound";

it("renders placeholder", () => {
  render(<FootprintsNotFound />);
  expect(window).toBeTruthy();
});
