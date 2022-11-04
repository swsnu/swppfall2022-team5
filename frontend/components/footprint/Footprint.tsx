import { IconLock } from "@tabler/icons";
import { TagList, TagType } from "../taglist/TagList";
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




let testPlace: PlaceType = {
    id: 1, name: "TestName", city: "TestCity", country: "TestCountry",
    district: "TestDistrict", latitude: 37.3306, longitude: 126.5930
}

let testTagList: Array<TagType> = [
    {id: 1, icon:IconLock, name: "Tag1"},
    {id: 2, icon:IconLock, name: "Tag2"},
    {id: 3, icon:IconLock, name: "Tag3"},
    {id: 1, icon:IconLock, name: "Tag1"},
    {id: 2, icon:IconLock, name: "Tag2"},
    {id: 3, icon:IconLock, name: "Tag3"},
    {id: 1, icon:IconLock, name: "Tag1"},
    {id: 2, icon:IconLock, name: "Tag2"},
    {id: 3, icon:IconLock, name: "Tag3"},
    {id: 1, icon:IconLock, name: "Tag1"},
    {id: 2, icon:IconLock, name: "Tag2"},
    {id: 3, icon:IconLock, name: "Tag3"},
    {id: 1, icon:IconLock, name: "Tag1"},
    {id: 2, icon:IconLock, name: "Tag2"},
    {id: 3, icon:IconLock, name: "Tag3"},
]


export function Footprint(props: FootprintType) {
    return (
        <div className="mx-3 mb-3 w-auto h-auto">
            {/* <Place {...props.place} /> */}
            {/* <TagList tagList={props.tagList} /> */}
            <Place {...testPlace} />
            <TagList tagList={testTagList}/>
            {/* <Place />
            <TagList />
            <PhotoList />
            <Memo /> */}
        </div>
    );
}