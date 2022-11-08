import { FootprintPredictionType } from "../dto/recommendations";
import { TraceRequestType } from "../dto/trace";
import { apiClient } from "./client";

export const fetchInitialFootprints = async (photoIds: string[]) => {
  return (await apiClient.post<FootprintPredictionType[]>("/traces/create", photoIds)).data;
};

export const createTrace = async (traceRequest: TraceRequestType) => {
  return (await apiClient.post<String>("/traces", traceRequest)).data;
};
