import create from "zustand";
import produce from "immer";
import { getWeekContainingDate, getWeeksInMonth } from "../utils/calendar";

interface CalendarState {
  selectedDate: Date;
  selectedWeek: Date[];
  selectedMonth: Date[][];
  calendarExpanded: boolean;
  setCalendarExpanded: (value: boolean) => void;
  setSelectedDate: (date: Date) => void;
}

export const useCalendarStore = create<CalendarState>()((set, get) => ({
  selectedDate: new Date(),
  selectedWeek: getWeekContainingDate(new Date()),
  selectedMonth: getWeeksInMonth(new Date().getFullYear(), new Date().getMonth()),
  calendarExpanded: true,
  setCalendarExpanded: (value) => {
    set(
      produce((state) => {
        state.calendarExpanded = value;
      }),
    );
  },
  setSelectedDate: (date) => {
    set(
      produce((state) => {
        state.selectedDate = date;
      }),
    );
  },
}));
