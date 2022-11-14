import { render } from "@testing-library/react";
import FullCalendar from "../../../components/calendar/FullCalendar";

it("renders full calendar", () => {
  render(<FullCalendar />);
  expect(window).toBeTruthy();
});
