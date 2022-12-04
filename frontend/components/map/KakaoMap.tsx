import { PhotoType } from "../../dto/photo";
import { Map, MapMarker } from "react-kakao-maps-sdk";

interface Coordinates {
  latitude: number;
  longitude: number;
}

interface IProps {
  coordinates: Coordinates[];
}

const KakaoMap = ({ coordinates }: IProps) => {
  return (
    <Map
      className="h-40"
      center={{
        lat: coordinates[0].latitude,
        lng: coordinates[0].longitude,
      }}
    >
      {coordinates.map((coordinate) => {
        return (
          <MapMarker
            key={`${coordinate.latitude}-${coordinate.longitude}`}
            position={{
              lat: coordinate.latitude,
              lng: coordinate.longitude,
            }}
          />
        );
      })}
    </Map>
  );
};

export default KakaoMap;
