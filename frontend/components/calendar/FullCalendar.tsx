import { IconChevronLeft, IconChevronRight } from "@tabler/icons";
import shallow from "zustand/shallow";
import { useCalendarStore } from "../../store/calendar";
import { getWeeksInMonth } from "../../utils/calendar";
import IconButton from "../buttons/IconButton";
import WeekCalendar from "./WeekCalendar";

interface IProps {}

const FullCalendar = ({}: IProps) => {
  const { selectedDate, selectedWeek, setSelectedDate, previewYearMonth, selectNextMonth, selectPrevMonth } =
    useCalendarStore((state) => state, shallow);

  return (
    <div className="">
      <div className="flex items-center justify-center pb-5 text-lg font-semibold">
        <IconButton icon={IconChevronLeft} onClick={selectPrevMonth} />
        <div className="mx-2 select-none">
          {previewYearMonth.year}년 {previewYearMonth.month + 1}월
        </div>
        <IconButton icon={IconChevronRight} onClick={selectNextMonth} />
      </div>
      {getWeeksInMonth(previewYearMonth).map((week, i) => {
        return (
          <WeekCalendar
            key={i}
            week={week}
            setSelectedDate={setSelectedDate}
            selectedDate={selectedDate}
            showWeekday={i === 0}
          />
        );
      })}
    </div>
  );
};

export default FullCalendar;
