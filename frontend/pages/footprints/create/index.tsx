import { useMutation } from "@tanstack/react-query";
import moment from "moment";
import { useRouter } from "next/router";
import toast from "react-hot-toast";
import { createTrace } from "../../../api";
import RectangleButton from "../../../components/buttons/RectangleButton";
import Container from "../../../components/containers/Container";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import FootprintEdit from "../../../components/footprint/FootprintEdit";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { TraceRequestType } from "../../../dto/trace";
import { useCalendarStore } from "../../../store/calendar";
import { useFootprintCreateStore } from "../../../store/footprint";

const FootprintsCreate = () => {
  const pendingFootprintRequests = useFootprintCreateStore((state) => state.pendingFootprintRequests);
  const setSelectedDate = useCalendarStore((state) => state.setSelectedDate);
  const mutation = useMutation((traceRequest: TraceRequestType) => createTrace(traceRequest));
  const router = useRouter();

  return (
    <Container>
      <NavbarContainer>
        <NavigationBar title="기록 추가" />
      </NavbarContainer>
      <div className="divide-y-2 divide-navy-700/50">
        {pendingFootprintRequests.map((prediction) => {
          return <FootprintEdit key={prediction.startTime} {...prediction} />;
        })}
      </div>
      <div className="mx-6 flex py-6">
        <RectangleButton
          onClick={() => {
            mutation.mutate(
              {
                title: "테스트 제목입니다.",
                date: moment(pendingFootprintRequests[0].startTime).format("YYYY-MM-DD"),
                footprintList: pendingFootprintRequests,
              },
              {
                onSuccess: () => {
                  setSelectedDate(new Date(pendingFootprintRequests[0].startTime));
                  router.push("/footprints");
                  toast.success("발자취를 성공적으로 저장했어요!");
                },
                onError: () => {
                  toast.error("장소를 선택해주세요.");
                },
              },
            );
          }}
          text={"저장하기"}
          isLoading={false}
          className="grow"
        />
      </div>
    </Container>
  );
};

export default FootprintsCreate;
