import { act, renderHook } from "@testing-library/react";
import shallow from "zustand/shallow";
import { useCalendarStore } from "../../../store/calendar";

it("sets calendar expanded", () => {
  const { result } = renderHook(() => useCalendarStore());

  act(() => {
    const date = new Date(2022, 5, 2);
    result.current.setSelectedDate(date);
  });
  expect(result.current.selectedDate.getDay()).toBe(4);
  expect(result.current.selectedDate.getHours()).toBe(0);
});

it("navigates calendar month", () => {
  const { result } = renderHook(() => useCalendarStore());
  act(() => {
    result.current.setPreviewYearMonth(2022, 11);
  });
  expect(result.current.previewYearMonth.year).toBe(2022);
  expect(result.current.previewYearMonth.month).toBe(11);
  act(() => {
    result.current.selectNextMonth();
  });
  expect(result.current.previewYearMonth.year).toBe(2023);
  expect(result.current.previewYearMonth.month).toBe(0);
  act(() => {
    result.current.selectPrevMonth();
  });
  expect(result.current.previewYearMonth.month).toBe(11);
  expect(result.current.previewYearMonth.year).toBe(2022);
});
