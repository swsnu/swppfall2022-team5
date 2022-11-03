import create from "zustand";
import produce from "immer";
import { getWeekContainingDate, getWeeksInMonth } from "../utils/calendar";

interface CalendarState {
  selectedDate: Date;
  selectedWeek: Date[];
  previewYearMonth: {
    year: number;
    month: number;
  };
  setCalendarExpanded: (value: boolean) => void;
  setSelectedDate: (date: Date) => void;
  selectNextMonth: () => void;
  selectPrevMonth: () => void;
}

export const useCalendarStore = create<CalendarState>()((set, get) => ({
  selectedDate: (() => {
    const date = new Date();
    date.setHours(0, 0, 0, 0); // erase time info
    return date;
  })(),
  selectedWeek: getWeekContainingDate(new Date()),
  previewYearMonth: {
    year: new Date().getFullYear(),
    month: new Date().getMonth(),
  },
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
        state.selectedWeek = getWeekContainingDate(date);
      }),
    );
  },
  selectNextMonth: () => {
    set(
      produce((state) => {
        if (state.previewYearMonth.month === 11) {
          state.previewYearMonth.month = 0;
          state.previewYearMonth.year++;
          return;
        }
        state.previewYearMonth.month++;
      }),
    );
  },
  selectPrevMonth: () => {
    set(
      produce((state) => {
        if (state.previewYearMonth.month === 0) {
          state.previewYearMonth.month = 11;
          state.previewYearMonth.year--;
          return;
        }
        state.previewYearMonth.month--;
      }),
    );
  },
}));
