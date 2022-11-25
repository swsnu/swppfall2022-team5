import Image from "next/image";

const OwnerInfo = (profile: { username: string }) => {
    return (
        <div className="flex">
            <div className="ml-10 relative h-12 w-12 overflow-hidden rounded-full border border-gray-700">
                <Image fill src={""} alt={""} className="object-cover" sizes="33vw" priority />
                
            </div>
            <div className="text-base p-3 text-center">
                {profile.username}
            </div>
        </div>
    )
}

export default OwnerInfo;