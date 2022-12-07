export interface UserResponseType {
  username: string;
  followingCount: number;
  followerCount: number;
  imageUrl: string;
  traceCount: number;
}

export interface UserFollowingResponse {
  isFollowing: boolean;
  isFollowed: boolean;
}
