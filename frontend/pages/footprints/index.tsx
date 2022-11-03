import MonthDropdown from "../../components/calendar/MonthDropdown";
import WeekCalendar from "../../components/calendar/WeekCalendar";
import Container from "../../components/containers/Container";
import NavigationBar from "../../components/navbar/NavigationBar";
import { useCalendarStore } from "../../store/calendar";
import { getWeekContainingDate } from "../../utils/calendar";
import shallow from "zustand/shallow";
import RectangleButton from "../../components/buttons/RectangleButton";

export default function Footprints() {
  const { selectedDate, selectedWeek, setSelectedDate } = useCalendarStore((state) => state, shallow);
  return (
    <Container>
      <div className="sticky top-0 left-0 right-0 bg-navy-800/90 pb-4 backdrop-blur-md">
        <NavigationBar />
        <MonthDropdown selectedDate={selectedDate} />
        <WeekCalendar week={selectedWeek} setSelectedDate={setSelectedDate} selectedDate={selectedDate} showWeekday />
      </div>

      <RectangleButton text="기록 추가하기" onClick={() => {}} isLoading={false} className="my-5 shadow-lg" />

      <div className="p-4 text-navy-200 ">{"fw e fa".repeat(3000)}</div>
    </Container>
  );
}
