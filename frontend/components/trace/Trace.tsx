import Container from "../containers/Container";
import { Footprint, FootprintType } from "../footprint/Footprint";

export function Trace(props: { footprintList: Array<FootprintType>; modifying: boolean }) {
  return (
    <div>
      {props.footprintList.map((fp) => (
        <div key={fp.id} className="mx-3 mt-3 mb-6 rounded-lg border-2  border-solid border-navy-500 ">
          <Footprint {...fp} modifying={props.modifying} />
        </div>
      ))}
    </div>
  );
}
