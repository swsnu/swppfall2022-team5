import { useCallback, useState } from "react";
import shallow from "zustand/shallow";
import MonthDropdown from "../../components/calendar/MonthDropdown";
import WeekCalendar from "../../components/calendar/WeekCalendar";
import Container from "../../components/containers/Container";
import NavigationBar from "../../components/navbar/NavigationBar";
import { useCalendarStore } from "../../store/calendar";

import { IconLock, IconPlus } from "@tabler/icons";
import FloatingButton from "../../components/buttons/FloatingButton";
import UploadModal from "../../components/upload-footprint/UploadModal";
import { dummyFootprints } from "../../data/footprints";
import { FootprintPreview } from "../../components/footprint/FootprintPreview";

export default function Footprints() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { selectedDate, selectedWeek, setSelectedDate } = useCalendarStore((state) => state, shallow);

  return (
    <Container>
      <div className="sticky top-0 left-0 right-0 z-50 border-b border-navy-700/50 bg-navy-800/90 pb-4 backdrop-blur-md">
        <NavigationBar />
        <MonthDropdown selectedDate={selectedDate} />
        <WeekCalendar week={selectedWeek} setSelectedDate={setSelectedDate} selectedDate={selectedDate} showWeekday />
      </div>

      <div className="flex justify-center">
        <FloatingButton
          icon={IconPlus}
          text="기록 추가하기"
          onClick={useCallback(() => {
            setIsModalOpen(true);
          }, [])}
          className="fixed bottom-0 my-5 text-right"
        />
      </div>

      <div className="p-5">
        {dummyFootprints.map((footprint) => {
          return <FootprintPreview key={footprint.id} {...footprint} />;
        })}
      </div>

      <UploadModal isOpen={isModalOpen} setIsOpen={setIsModalOpen} onConfirm={() => {}} />
    </Container>
  );
}
