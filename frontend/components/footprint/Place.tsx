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
        <div className="my-3">
            <h2 className="text-left"> {props.name} </h2>
            <h3 className="text-left"> {props.city + "," + props.country + "," + props.district} </h3>
            {/* Extract map part to components to use in edit page. use MapMarkerCluster for clusterred marker*/}
            {/* <div>
                <Map center={{lat: props.latitude, lng: props.longitude}}
                    className="mt-1 w-full h-40 rounded z-0"
                    level={10}
                    zoomable={false}
                    draggable={false}
                >
                    <MapMarker 
                        position={{lat: props.latitude, lng: props.longitude}}
                    />
                </Map>
            </div> */}
        </div>
    );
}