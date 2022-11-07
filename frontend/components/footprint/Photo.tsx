import { PhotoType } from "../../dto/photo";
import Image from "next/image";

const Photo = (photo: { imageUrl: string }) => {
  return (
    <div className="relative h-32 w-40 flex-shrink-0 overflow-hidden rounded-xl border border-navy-100/5">
      <Image fill src={photo.imageUrl} alt={""} className="object-cover" sizes="33vw" priority />
    </div>
  );
};

export default Photo;
