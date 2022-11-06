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
import NavbarContainer from "../../components/containers/NavbarContainer";

export default function Footprints() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { selectedDate, selectedWeek, setSelectedDate } = useCalendarStore((state) => state, shallow);

  return (
    <Container>
      <NavbarContainer className="pb-4">
        <NavigationBar />
        <MonthDropdown selectedDate={selectedDate} />
        <WeekCalendar week={selectedWeek} setSelectedDate={setSelectedDate} selectedDate={selectedDate} showWeekday />
      </NavbarContainer>

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

      <div className="mb-20 divide-y divide-navy-700/50">
        {dummyFootprints.map((footprint) => {
          return <FootprintPreview key={footprint.id} {...footprint} />;
        })}
        {dummyFootprints.map((footprint) => {
          return <FootprintPreview key={footprint.id} {...footprint} />;
        })}
        {dummyFootprints.map((footprint) => {
          return <FootprintPreview key={footprint.id} {...footprint} />;
        })}
        {dummyFootprints.map((footprint) => {
          return <FootprintPreview key={footprint.id} {...footprint} />;
        })}
      </div>

      <UploadModal isOpen={isModalOpen} setIsOpen={setIsModalOpen} onConfirm={() => {}} />
    </Container>
  );
}
