import { FootprintPredictionType } from "../dto/recommendations";
import { TraceDetailResponse, TraceRequestType } from "../dto/trace";
import { apiClient } from "./client";
import moment from "moment";

export const fetchInitialFootprints = async (photoIds: string[]) => {
  return (await apiClient.post<FootprintPredictionType[]>("/traces/create", photoIds)).data;
};

export const createTrace = async (traceRequest: TraceRequestType) => {
  return (await apiClient.post<String>("/traces", traceRequest)).data;
};

export const fetchTraceByDate = async (date: Date) => {
  return (await apiClient.get<TraceDetailResponse>(`/traces/date/${moment(date).format("YYYY-MM-DD")}`)).data;
};

export const fetchTraceById = async (traceId: number) => {
  return await apiClient.get<TraceDetailResponse>(`/traces/id/${traceId}`);
};
