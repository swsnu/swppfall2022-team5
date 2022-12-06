import Image from "next/image";

const OwnerProfilePhoto = (photo: { imageUrl: string }) => {
    return (
        <div className="ml-10 relative h-12 w-12 overflow-hidden rounded-full border border-gray-700">
            <Image fill src={photo.imageUrl} alt={""} className="object-cover" sizes="33vw" priority />
        </div>
    )
}

export default OwnerProfilePhoto;