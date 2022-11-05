import { IconLock } from "@tabler/icons";
import FloatingButton from "../buttons/FloatingButton";
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
    tag: TagType,
    memo: string
}


interface Iprops extends FootprintType{
    modifying: boolean,
}

export function Footprint(props: Iprops) {
    return (
        <div className="mx-3 mb-3 w-auto h-auto">
            <div className="flex mt-3 mx-3 items-stretch">
                <FloatingButton 
                    icon={props.tag.icon} 
                    text={props.tag.name} 
                    onClick={() => {/* TODO: implement filter */}}
                />
                <h2 className="text-xl text-center h-full ml-2">
                    {('0' + props.startTime.getHours()).slice(-2) + ":" + ('0' + props.startTime.getMinutes()).slice(-2)}
                </h2>
            </div>
            <PhotoList photoList={props.photoList} />
            <Place {...props.place} />

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