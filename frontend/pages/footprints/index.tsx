import { useState } from "react";
import { BottomSheet } from "react-spring-bottom-sheet";
import shallow from "zustand/shallow";
import RectangleButton from "../../components/buttons/RectangleButton";
import MonthDropdown from "../../components/calendar/MonthDropdown";
import WeekCalendar from "../../components/calendar/WeekCalendar";
import Container from "../../components/containers/Container";
import NavigationBar from "../../components/navbar/NavigationBar";
import { useCalendarStore } from "../../store/calendar";

import "react-spring-bottom-sheet/dist/style.css";
import FloatingButton from "../../components/buttons/FloatingButton";
import { IconPlus } from "@tabler/icons";

export default function Footprints() {
  const [open, setOpen] = useState(false);
  const { selectedDate, selectedWeek, setSelectedDate } = useCalendarStore((state) => state, shallow);
  return (
    <Container>
      <div className="sticky top-0 left-0 right-0 border-b border-navy-700/50 bg-navy-800/90 pb-4 backdrop-blur-md">
        <NavigationBar />
        <MonthDropdown selectedDate={selectedDate} />
        <WeekCalendar week={selectedWeek} setSelectedDate={setSelectedDate} selectedDate={selectedDate} showWeekday />
      </div>
      <div className="flex justify-center">
        <FloatingButton
          icon={IconPlus}
          text="기록 추가하기"
          onClick={() => {
            setOpen(true);
          }}
          className="fixed bottom-0 my-5 text-right"
        />
      </div>

      <div className="p-4 text-navy-200">{"fw e fa".repeat(30)}</div>
    </Container>
  );
}
