import { PhotoType } from "../../dto/photo";
import Image from "next/image";

const Photo = (photo: { imageUrl: string }) => {
  return (
    <div className="relative flex-shrink-0 overflow-hidden rounded-xl border border-navy-100/5">
      <img src={photo.imageUrl} alt={""} className="h-32 w-40 object-cover" />
    </div>
  );
};

export default Photo;
