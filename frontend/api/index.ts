import { FootprintPredictionType } from "../dto/recommendations";
import { TraceDetailResponse, TraceRequestType } from "../dto/trace";
import { apiClient } from "./client";
import moment from "moment";
import { TagType } from "../dto/tag";
import { FootprintEditRequestType, FootprintRequestType, FootprintResponseType } from "../dto/footprint";

export const fetchInitialFootprints = async (photoIds: string[]) => {
  return (await apiClient.post<FootprintPredictionType[]>("/traces/create", photoIds)).data;
};

export const createTrace = async (traceRequest: TraceRequestType) => {
  return (await apiClient.post<String>("/traces", traceRequest)).data;
};

export const editFootprint = async (footprintId: number, footprintRequest: FootprintEditRequestType) => {
  return (await apiClient.put<String>(`/footprints/${footprintId}`, footprintRequest)).data;
};

export const fetchAllMyTraces = async () => {
  return (await apiClient.get<TraceDetailResponse[]>("/traces")).data;
}

export const fetchAllOtherUsersTraces = async () => {
  return (await apiClient.get<TraceDetailResponse[]>("/traces/explore")).data;
}

export const fetchTraceByDate = async (date: Date) => {
  return (await apiClient.get<TraceDetailResponse>(`/traces/date/${moment(date).format("YYYY-MM-DD")}`)).data;
};

export const fetchTraceById = async (traceId: number) => {
  return (await apiClient.get<TraceDetailResponse>(`/traces/id/${traceId}`)).data;
};

export const fetchTags = async () => {
  return (await apiClient.get<TagType[]>(`/tags`)).data;
};

export const fetchFootprintById = async (footprintId: number) => {
  return (await apiClient.get<FootprintResponseType>(`/footprints/${footprintId}`)).data;
};
