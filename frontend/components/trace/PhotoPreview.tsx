import Image from "next/image";

const PhotoPreview = (photo: { imageUrl: string }) => {
  return (
    <div className="relative h-32 w-100 flex-shrink-0 overflow-hidden rounded-3xl border border-navy-100/5">
      <Image fill src={photo.imageUrl} alt={""} className="object-cover" sizes="33vw" priority />
    </div>
  );
};

export default PhotoPreview;