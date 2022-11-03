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
      <NavigationBar />
      <MonthDropdown selectedDate={selectedDate} />
      <WeekCalendar week={selectedWeek} setSelectedDate={setSelectedDate} selectedDate={selectedDate} showWeekday />
    </Container>
  );
}
