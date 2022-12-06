import { render } from "@testing-library/react";
import TracesNotFound from "../../../components/placeholder/TracesNotFound";

it("renders placeholder", () => {
  render(<TracesNotFound />);
  expect(window).toBeTruthy();
});
