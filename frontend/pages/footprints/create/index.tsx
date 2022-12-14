import { useMutation } from "@tanstack/react-query";
import moment from "moment";
import { useRouter } from "next/router";
import { useState } from "react";
import toast from "react-hot-toast";
import { createTrace } from "../../../api";
import RectangleButton from "../../../components/buttons/RectangleButton";
import Container from "../../../components/containers/Container";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import FootprintCreate from "../../../components/footprint/FootprintCreate";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { TraceRequestType } from "../../../dto/trace";
import { useCalendarStore } from "../../../store/calendar";
import { useFootprintCreateStore } from "../../../store/footprint";
import PlaceSearchBox from "../../../components/footprint/PlaceSearchBox";
import { IconLock, IconLockOpen } from "@tabler/icons";

const FootprintsCreate = () => {
  const pendingFootprintRequests = useFootprintCreateStore((state) => state.pendingFootprintRequests);
  const setPendingFootprintRequests = useFootprintCreateStore((state) => state.setPendingFootprintRequests);
  const setSelectedDate = useCalendarStore((state) => state.setSelectedDate);
  const mutation = useMutation((traceRequest: TraceRequestType) => createTrace(traceRequest));
  const router = useRouter();

  const [title, setTitle] = useState("");
  const [isPublic, setIsPublic] = useState(true);

  return (
    <Container>
      <NavbarContainer>
        <NavigationBar title="기록 추가" />
      </NavbarContainer>

      <div className="flex items-center justify-between space-x-4 px-5">
        <input
          className="w-full bg-transparent py-4 text-2xl font-bold text-navy-200 placeholder-navy-300/50 focus:outline-none"
          value={title}
          placeholder="제목을 입력해주세요."
          onChange={(e) => {
            setTitle(e.target.value);
          }}
        ></input>

        <button
          onClick={() => {
            setIsPublic(!isPublic);
          }}
        >
          {isPublic ? <IconLockOpen /> : <IconLock />}
        </button>
      </div>

      <hr className="border border-navy-400/10" />

      <div className="divide-y-2 divide-navy-700/50">
        {pendingFootprintRequests.map((prediction) => {
          return <FootprintCreate key={prediction.startTime} {...prediction} />;
        })}
      </div>
      <div className="mx-6 flex py-6">
        <RectangleButton
          onClick={() => {
            if (!title) {
              toast.error("제목을 입력해주세요.");
              return;
            }
            mutation.mutate(
              {
                title: title,
                public: isPublic,
                footprintList: pendingFootprintRequests,
              },
              {
                onSuccess: () => {
                  setPendingFootprintRequests([]);
                  setSelectedDate(new Date(pendingFootprintRequests[0].startTime));
                  router.push("/footprints");
                  toast.success("발자취를 성공적으로 저장했어요!");
                },
                onError: () => {
                  toast.error("요청이 실패했습니다.");
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
