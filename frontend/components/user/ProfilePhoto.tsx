import Image from "next/image";

const ProfilePhoto = (photo: { imageUrl: string }) => {
  return (
    <div className="relative h-24 w-24 flex-shrink-0 overflow-hidden rounded-full border border-gray-700">
      <img src={photo.imageUrl} alt={""} className="object-cover" />
    </div>
  );
};

export default ProfilePhoto;
