import Image from "next/image";

const ProfilePhoto = (photo: { imageUrl: string }) => {
    return (
        <div className="relative h-24 w-24 flex-shrink-0 overflow-hidden rounded-full border border-gray-700">
            <Image fill src={photo.imageUrl} alt={""} className="object-cover" sizes="33vw" priority />
        </div>
    )
}

export default ProfilePhoto;