import { isSameDate } from "../../utils/calendar";
import DayButton from "./DayButton";

interface IProps {
  week: Date[];
  setSelectedDate: (date: Date) => void;
  selectedDate: Date;
  showWeekday: boolean;
}

const WeekCalendar = ({ week, setSelectedDate, selectedDate, showWeekday }: IProps) => {
  return (
    <div className="mx-5 columns-7">
      {week.map((date) => {
        return (
          <DayButton
            key={date.toISOString()}
            date={date}
            onClick={() => {
              setSelectedDate(date);
            }}
            isActive={isSameDate(selectedDate, date)}
            isCurrentMonth={
              selectedDate.getFullYear() === date.getFullYear() && selectedDate.getMonth() === date.getMonth()
            }
            showWeekday={showWeekday}
          />
        );
      })}
    </div>
  );
};

export default WeekCalendar;
