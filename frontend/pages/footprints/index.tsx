import { useCallback, useState } from "react";
import shallow from "zustand/shallow";
import MonthDropdown from "../../components/calendar/MonthDropdown";
import WeekCalendar from "../../components/calendar/WeekCalendar";
import Container from "../../components/containers/Container";
import NavigationBar from "../../components/navbar/NavigationBar";
import { useCalendarStore } from "../../store/calendar";

import { IconPlus } from "@tabler/icons";
import { useQuery } from "@tanstack/react-query";
import { fetchTraceByDate } from "../../api";
import FloatingButton from "../../components/buttons/FloatingButton";
import NavbarContainer from "../../components/containers/NavbarContainer";
import { FootprintPreview } from "../../components/footprint/FootprintPreview";
import UploadModal from "../../components/upload-footprint/UploadModal";
import FootprintsNotFound from "../../components/placeholder/FootprintsNotFound";

export default function Footprints() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { selectedDate, selectedWeek, setSelectedDate } = useCalendarStore((state) => state, shallow);
  const footprintsResult = useQuery(["footprints", selectedDate], () => {
    return fetchTraceByDate(selectedDate);
  });

  const dat = footprintsResult.data;
  return (
    <Container>
      <NavbarContainer className="z-20 pb-4">
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
          className="fixed bottom-0 z-50 my-5 text-right"
        />
      </div>

      {footprintsResult.isSuccess && !!!footprintsResult.data.footprints && <FootprintsNotFound />}

      <div className="divide-y divide-navy-700/50 pb-20">
        {footprintsResult.isSuccess &&
          footprintsResult.data.footprints?.map((footprint) => {
            return <FootprintPreview key={footprint.id} {...footprint} />;
          })}
      </div>

      <UploadModal isOpen={isModalOpen} setIsOpen={setIsModalOpen} onConfirm={() => {}} />
    </Container>
  );
}
