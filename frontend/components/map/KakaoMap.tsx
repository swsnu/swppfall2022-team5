import { PhotoType } from "../../dto/photo";
import { Map, MapMarker } from "react-kakao-maps-sdk";
interface IProps {
  photos: PhotoType[];
}

const KakaoMap = ({ photos }: IProps) => {
  return (
    <Map
      className="h-40"
      center={{
        lat: photos[0].latitude,
        lng: photos[0].longitude,
      }}
    >
      {photos.map((photo) => {
        return (
          <MapMarker
            key={photo.imagePath}
            position={{
              lat: photo.latitude,
              lng: photo.longitude,
            }}
          />
        );
      })}
    </Map>
  );
};

export default KakaoMap;
