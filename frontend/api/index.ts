import { FootprintPredictionType } from "../dto/recommendations";
import { TraceDetailResponseType, TraceLikeResponseType, TraceRequestType, TraceViewResponseType } from "../dto/trace";
import { apiClient } from "./client";
import moment from "moment";
import { TagType } from "../dto/tag";
import { FootprintEditRequestType, FootprintRequestType, FootprintResponseType } from "../dto/footprint";
import { SigninRequestType, SigninResponseType, TokenVerifyRequestType, TokenVerifyResponseType } from "../dto/auth";
import { UserFollowingResponse, UserResponseType } from "../dto/user";
import { PlaceType } from "../dto/place";

export const fetchInitialFootprints = async (photoIds: string[]) => {
  return (await apiClient.post<FootprintPredictionType[]>("/traces/create", photoIds)).data;
};

export const createTrace = async (traceRequest: TraceRequestType) => {
  return (await apiClient.post<String>("/traces", traceRequest)).data;
};

export const editFootprint = async (footprintId: number, footprintRequest: FootprintEditRequestType) => {
  return (await apiClient.put<String>(`/footprints/${footprintId}`, footprintRequest)).data;
};

export const whoAmI = async () => {
  return (await apiClient.get<UserResponseType>("/me")).data;
};

export const fetchAllUserTraces = async (username: string) => {
  return (await apiClient.get<TraceDetailResponseType[]>(`/traces/user/${username}`)).data;
};

export const fetchAllOtherUsersTraces = async () => {
  return (await apiClient.get<TraceDetailResponseType[]>("/traces/explore")).data;
};

export const fetchTraceByDate = async (date: Date) => {
  return (await apiClient.get<TraceDetailResponseType>(`/traces/date/${moment(date).format("YYYY-MM-DD")}`)).data;
};

export const fetchTraceById = async (traceId: number) => {
  return (await apiClient.get<TraceDetailResponseType>(`/traces/id/${traceId}`)).data;
};

export const fetchTags = async () => {
  return (await apiClient.get<TagType[]>(`/tags`)).data;
};

export const fetchFootprintById = async (footprintId: number) => {
  return (await apiClient.get<FootprintResponseType>(`/footprints/${footprintId}`)).data;
};

export const postSignin = async (signinRequest: SigninRequestType) => {
  return (await apiClient.post<SigninResponseType>("/signin", signinRequest)).data;
};

export const postSignUp = async (signinRequest: SigninRequestType) => {
  return (await apiClient.post<SigninResponseType>("/signup", signinRequest)).data;
};

export const checkToken = async (tokenVerifyRequest: TokenVerifyRequestType) => {
  return (await apiClient.post<TokenVerifyResponseType>("/token", tokenVerifyRequest)).data;
};

export const likeTrace = async (traceId: number) => {
  return (await apiClient.post<TraceLikeResponseType>(`/traces/${traceId}/likes`)).data;
};

export const unlikeTrace = async (traceId: number) => {
  return (await apiClient.delete<TraceLikeResponseType>(`/traces/${traceId}/likes`)).data;
};

export const fetchRegionByCoordinates = async (latitude: number, longitude: number) => {
  return (await apiClient.get<String>(`/place`, { params: { latitude: latitude, longitude: longitude } })).data;
};

export const updateViewCount = async (traceId: number) => {
  return (await apiClient.post<TraceViewResponseType>(`/traces/view/${traceId}`)).data;
};

export const fetchUserByUsername = async (username: string) => {
  return (await apiClient.get<UserResponseType>(`/user/${username}`)).data;
};

export const fetchMe = async () => {
  return (await apiClient.get<UserResponseType>(`/me`)).data;
};

export const fetchIsFollowing = async (username: string) => {
  return (await apiClient.get<UserFollowingResponse>(`/user/followings/${username}/status`)).data;
};

export const followUser = async (username: string) => {
  return (await apiClient.post<void>(`/user/followings`, { username: username })).data;
};

export const unfollowUser = async (username: string) => {
  return (await apiClient.delete<void>(`/user/followings`, { data: { username: username } })).data;
};

export const deleteFootprint = async (footprintId: number) => {
  return (await apiClient.delete<void>(`/footprints/${footprintId}`)).data;
};

export const fetchPlacesByKeywordAndLocation = async (
  keyword: string,
  latitude: number,
  longitude: number,
  tagIds: number[],
) => {
  return (
    await apiClient.post<PlaceType[]>(`/place/search`, {
      keyword: keyword,
      latitude: latitude,
      longitude: longitude,
      tagIDs: tagIds,
    })
  ).data;
};
