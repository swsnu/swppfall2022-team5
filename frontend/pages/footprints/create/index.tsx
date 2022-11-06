import Container from "../../../components/containers/Container";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import FootprintEdit from "../../../components/footprint/FootprintEdit";
import NavigationBar from "../../../components/navbar/NavigationBar";
import { dummyPredictedFootprints } from "../../../data/recommendations";

const FootprintsCreate = () => {
  return (
    <Container>
      <NavbarContainer>
        <NavigationBar title="기록 추가" />
      </NavbarContainer>
      <div className="divide-y-2 divide-navy-700/50">
        {dummyPredictedFootprints.map((prediction) => {
          return <FootprintEdit key={prediction.meanTime} {...prediction} />;
        })}
        {dummyPredictedFootprints.map((prediction) => {
          return <FootprintEdit key={prediction.meanTime} {...prediction} />;
        })}
      </div>
    </Container>
  );
};

export default FootprintsCreate;
