import ProfilePhoto from "./ProfilePhoto";

const UserProfile = (profile: {username: String}) => {
    return (
        <div className="flex">
            <div className="px-7 pt-7">
                <ProfilePhoto imageUrl={""} ></ProfilePhoto>
                <div className="px-5 py-1 text-xl font-semibold">{profile.username}</div>
            </div>
            <div className="px-7 py-8 text-xl font-bold">
                팔로워
                <p className="text-center py-3 text-4xl">0</p>
            </div>
            <div className="px-7 py-8 text-xl font-bold">
                팔로잉
                <p className="text-center py-3 text-4xl">0</p>
            </div>
        </div>
    )
}

export default UserProfile;