import { useMutation } from "@tanstack/react-query";
import { useRouter } from "next/router";
import { createTrace } from "../../../api";
import RectangleButton from "../../../components/buttons/RectangleButton";
import Container from "../../../components/containers/Container";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import FootprintEdit from "../../../components/footprint/FootprintEdit";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { TraceRequestType } from "../../../dto/trace";
import { useFootprintCreateStore } from "../../../store/footprint";

const FootprintsCreate = () => {
  const pendingFootprintRequests = useFootprintCreateStore((state) => state.pendingFootprintRequests);
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
      <RectangleButton
        onClick={() => {
          mutation.mutate(
            {
              title: "테스트 제목입니다.",
              date: pendingFootprintRequests[0].startTime,
              footprintList: pendingFootprintRequests,
            },
            {
              onSuccess: () => {
                router.push("/footprints");
              },
            },
          );
        }}
        text={""}
        isLoading={false}
      />
    </Container>
  );
};

export default FootprintsCreate;
