import Script from "next/script";
import { Map, MapMarker, useInjectKakaoMapApi } from "react-kakao-maps-sdk"

export interface PlaceType {
    id: number,
    name: string,
    city: string,
    country: string,
    district: string,
    longitude: number,
    latitude: number,
}

export function Place(props: PlaceType) {
    return (
        <div className="my-3 rounded">
            <h2 className="text-left"> {props.name} </h2>
            <h3 className="text-left"> {props.city + "," + props.country + "," + props.district} </h3>
            <Map center={{lat: props.latitude, lng: props.longitude}}
                className="mt-1 w-full h-40 rounded"
                level={10}
            >
            </Map>
        </div>
    );
}