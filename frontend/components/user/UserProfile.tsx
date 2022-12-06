import { UserResponseType } from "../../dto/user";
import ProfilePhoto from "./ProfilePhoto";

interface IProps extends UserResponseType {}

const UserProfile = (props: IProps) => {
  return (
    <div className="mt-5 ml-5 flex items-center">
      <ProfilePhoto imageUrl={props.imageUrl} />
      <div className="ml-6 flex items-center">
        <div>
          <div className="text-lg">{props.username}</div>
          <div className="mt-1 flex flex-row divide-x divide-navy-500 text-sm leading-3 text-navy-400">
            <span className="pr-2">업로드한 발자취 {props.traceCount}개</span>
            <span className="px-2">팔로우 {props.followingCount}명</span>
            <span className="px-2">팔로워 {props.followerCount}명</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
