
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

export interface PlaceType {
    id: number,
    city: string,
    country: string,
    district: string,
}

export interface TagType {
    id: number,
    name: string,
}

export function Footprint(props: any) {
    return (
        <div className="mx-3 mb-3">
            <Place />
            <TagList />
            <PhotoList />
            <Memo />
        </div>
    );
}