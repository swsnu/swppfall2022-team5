import Image from "next/image";

export interface PhotoType {
    id: number,
    imageUrl: string,
    coordinates: number,
    time: Date,
}

export function PhotoList(props: {photoList: Array<PhotoType>}) {
    return (
        <div className="my-3 flex overflow-x-auto scrollbar-hide">
            {props.photoList.map((photo) => (
                <div className="flex-shrink-0 w-max h-40 mx-2">
                    <Image
                        src={photo.imageUrl}
                        alt=""
                        className="object-fill rounded-lg w-full h-fit"
                        width={160}
                        height={160}
                    >
                    </Image>
                    <p className="w-full text-left mx-1">
                        {('0' + photo.time.getHours()).slice(-2) + ":" + ('0' + photo.time.getMinutes()).slice(-2)}
                    </p>
                </div>
            ))}
        </div>
    );
}