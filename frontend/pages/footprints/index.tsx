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
      <div className="sticky top-0 right-0 bg-navy-800/90 pb-4 backdrop-blur-md">
        <NavigationBar />
        <MonthDropdown selectedDate={selectedDate} />
        <WeekCalendar week={selectedWeek} setSelectedDate={setSelectedDate} selectedDate={selectedDate} showWeekday />
      </div>
      <div>
        <div className="p-4 text-navy-200 ">
          {"s auctor tellus non metus posuere, eget sagittis diam vestibulum. Nunc a luctus risus, sed blandit est. Suspendisse vestibulum sapien ac orci luctus congue. Sed nisl purus, luctus ut leo gravida, imperdiet bibendum mauris. Vivamus sit amet ullamcorper magna. Morbi elementum massa vitae massa finibus, at tincid".repeat(
            50,
          )}
        </div>
      </div>
    </Container>
  );
}
