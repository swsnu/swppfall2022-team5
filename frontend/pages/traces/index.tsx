import { IconPlus } from "@tabler/icons";
import { useQuery } from "@tanstack/react-query";
import { useCallback, useState } from "react";
import { fetchAllMyTraces } from "../../api";
import FloatingButton from "../../components/buttons/FloatingButton";
import Container from "../../components/containers/Container";
import NavbarContainer from "../../components/containers/NavbarContainer";
import NavigationBar from "../../components/navbar/NavigationBar";
import FootprintsNotFound from "../../components/placeholder/FootprintsNotFound";
import { TracePreview } from "../../components/trace/TracePreview";
import UploadModal from "../../components/upload-footprint/UploadModal";


export default function Traces() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const traceResult = useQuery(["traces"], fetchAllMyTraces);
    
    return (
        <Container>
            <NavbarContainer className="z-20 pb-4">
                <NavigationBar />
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

            {!!!traceResult.data && <FootprintsNotFound />}

            <div className="divide-y divide-navy-700/50 pb-20">
                {traceResult.data?.sort((a, b) => a.date.localeCompare(b.date))
                .map((trace) => {
                    return <TracePreview key={trace.id} {...trace} />;
                })}
            </div>
            
            <UploadModal isOpen={isModalOpen} setIsOpen={setIsModalOpen} onConfirm={() => {}} />
        </Container>
    );
}