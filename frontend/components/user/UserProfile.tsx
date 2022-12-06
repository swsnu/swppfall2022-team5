import { IconUserMinus, IconUserPlus } from "@tabler/icons";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchIsFollowing, fetchMe, followUser, unfollowUser } from "../../api";
import { UserResponseType } from "../../dto/user";
import { useAuthStore } from "../../store/auth";
import RectangleButton from "../buttons/RectangleButton";
import ProfilePhoto from "./ProfilePhoto";

interface IProps extends UserResponseType {}

const UserProfile = (props: IProps) => {
  const meResult = useQuery(["me"], () => {
    return fetchMe();
  });

  const isMe = meResult.data?.username == props.username;

  const isFollowingResult = useQuery(
    ["isFollowing", props.username],
    () => {
      return fetchIsFollowing(props.username);
    },
    { enabled: !isMe },
  );

  const queryClient = useQueryClient();
  const followMutation = useMutation((targetUsername: string) => followUser(targetUsername), {
    onSuccess(data, variables, context) {
      queryClient.invalidateQueries({ queryKey: ["isFollowing"] });
      queryClient.invalidateQueries({ queryKey: ["user"] });
    },
  });
  const unfollowMutation = useMutation((targetUsername: string) => unfollowUser(targetUsername), {
    onSuccess(data, variables, context) {
      queryClient.invalidateQueries({ queryKey: ["isFollowing"] });
      queryClient.invalidateQueries({ queryKey: ["user"] });
    },
  });

  if (!meResult.isSuccess || !isFollowingResult.isSuccess) {
    return <div>로딩중...</div>;
  }

  const isFollowing = isFollowingResult.data.isFollowing;
  const isFollowed = isFollowingResult.data.isFollowed;

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
          <div className="mt-2">
            {isMe ? (
              <RectangleButton
                onClick={function (): void {
                  throw new Error("Function not implemented.");
                }}
                text={"프로필 수정"}
                className="py-1"
                isLoading={false}
              />
            ) : (
              <RectangleButton
                onClick={function (): void {
                  if (isFollowing) {
                    unfollowMutation.mutate(props.username);
                  } else {
                    followMutation.mutate(props.username);
                  }
                }}
                text={isFollowing ? "팔로우 취소" : isFollowed ? "맞팔로우" : "팔로우"}
                className="py-1"
                isLoading={isFollowingResult.isLoading || followMutation.isLoading || unfollowMutation.isLoading}
                icon={isFollowing ? IconUserMinus : IconUserPlus}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
