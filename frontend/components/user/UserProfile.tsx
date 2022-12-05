import ProfilePhoto from "./ProfilePhoto";

const UserProfile = (profile: { username: String, numTrace: number, following: number, follower: number }) => {
  return (
    <div className="mt-5 ml-5 flex items-center">
      <ProfilePhoto imageUrl="https://images.unsplash.com/photo-1669072596436-15df4a8c083d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=987&q=80" />
      <div className="ml-6 flex items-center">
        <div>
          <div className="text-lg">{profile.username}</div>
          <div className="mt-1 flex flex-row divide-x divide-navy-500 text-sm leading-3 text-navy-400">
            <span className="pr-2">업로드한 발자취 {profile.numTrace}개</span>
            <span className="px-2">팔로우 {profile.following}명</span>
            <span className="px-2">팔로워 {profile.follower}명</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
