import Image from "next/image";

export interface PhotoType {
  id: number;
  imageUrl: string;
  coordinates: number;
  time: Date;
}

export function PhotoList(props: { photoList: Array<PhotoType> }) {
  return (
    <div className="my-3 flex overflow-x-auto scrollbar-hide">
      {props.photoList.map((photo) => (
        <div className="mx-2 h-40 w-max flex-shrink-0" key={photo.id}>
          <Image
            src={photo.imageUrl}
            alt=""
            className="h-fit w-full rounded-lg object-fill"
            width={160}
            height={160}
          ></Image>
          <p className="mx-1 w-full text-left">
            {("0" + photo.time.getHours()).slice(-2) + ":" + ("0" + photo.time.getMinutes()).slice(-2)}
          </p>
        </div>
      ))}
    </div>
  );
}
