import { IconLock } from "@tabler/icons";
import { PhotoList, PhotoType } from "../photolist/PhotoList";
import { TagList, TagType } from "../taglist/TagList";
import { Place, PlaceType } from "./Place";

export interface FootprintType {
    id: number,
    startTime: Date,
    endTime: Date,
    rating: number,
    photoList: Array<PhotoType>,
    place: PlaceType,
    tagList: Array<TagType>,
    memo: string
}


interface Iprops extends FootprintType{
    modifying: boolean,
}

export function Footprint(props: Iprops) {
    return (
        <div className="mx-3 mb-3 w-auto h-auto">
            <Place {...props.place} />
            <TagList tagList={props.tagList}/>
            <PhotoList photoList={props.photoList} />

            <div>
                <h2>Memo</h2>
                <div className="flex flex-nowrap">
                    <textarea
                        className="rounded w-fit h-fit bg-navy-800/90 px-2"
                        defaultValue={props.memo}
                        disabled={!props.modifying}
                    ></textarea>
                    <h1
                        className="text-3xl h-max text-center ml-1"
                        onClick={() => {/* TODO: add select rate on modify */}}
                    >
                        {["😝","😀","🤔","😕","😢"].reverse()[props.rating]}
                    </h1>
                </div>
            </div>
        </div>
    );
}