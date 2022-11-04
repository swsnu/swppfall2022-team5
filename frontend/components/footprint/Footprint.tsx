import { Place, PlaceType } from "./Place";

export interface FootprintType {
    id: number,
    startTime: Date,
    endTime: Date,
    rating: number,
    photoList: Array<PhotoType>,
    place: PlaceType,
    memo: MemoType,
    tagList: Array<TagType>
}

export interface PhotoType {
    id: number,
    imageUrl: string,
    coordinates: number,
    longitude: number,
    latitude: number,
    timestamp: number
}

export interface MemoType {
    id: number,
    content: string,
}


export interface TagType {
    id: number,
    name: string,
}

let testPlace: PlaceType = {
    id: 1, name: "TestName", city: "TestCity", country: "TestCountry",
    district: "TestDistrict", latitude: 37.3306, longitude: 126.5930
}

export function Footprint(props: {place: PlaceType}) {
    return (
        <div className="mx-3 mb-3 w-auto h-auto">
            {/* <Place {...props.place} /> */}
            <Place {...testPlace} />
            {/* <Place />
            <TagList />
            <PhotoList />
            <Memo /> */}
        </div>
    );
}