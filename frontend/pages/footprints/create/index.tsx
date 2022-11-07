import Container from "../../../components/containers/Container";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import FootprintEdit from "../../../components/footprint/FootprintEdit";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { dummyPredictedFootprints } from "../../../data/recommendations";
import { useFootprintCreateStore } from "../../../store/footprint";

const FootprintsCreate = () => {
  const pendingFootprintRequest = useFootprintCreateStore((state) => state.pendingFootprintRequests);

  return (
    <Container>
      <NavbarContainer>
        <NavigationBar title="기록 추가" />
      </NavbarContainer>
      <div className="divide-y-2 divide-navy-700/50">
        {pendingFootprintRequest.map((prediction) => {
          return <FootprintEdit key={prediction.startTime} {...prediction} />;
        })}
      </div>
    </Container>
  );
};

export default FootprintsCreate;
