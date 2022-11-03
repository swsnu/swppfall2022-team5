import MonthDropdown from "../../components/calendar/MonthDropdown";
import WeekCalendar from "../../components/calendar/WeekCalendar";
import Container from "../../components/containers/Container";
import NavigationBar from "../../components/navbar/NavigationBar";
import { useCalendarStore } from "../../store/calendar";
import { getWeekContainingDate } from "../../utils/calendar";
import shallow from "zustand/shallow";

export default function Footprints() {
  const { selectedDate, selectedWeek, setSelectedDate } = useCalendarStore((state) => state, shallow);
  return (
    <Container>
      <div className="sticky top-0 right-0">
        <div className="h-48 bg-navy-800">
          <NavigationBar />
          <MonthDropdown selectedDate={selectedDate} />
          <WeekCalendar week={selectedWeek} setSelectedDate={setSelectedDate} selectedDate={selectedDate} showWeekday />
        </div>
      </div>

      <div className="p-4 text-navy-200">{"아무말 입니다 ".repeat(500)}</div>
    </Container>
  );
}
