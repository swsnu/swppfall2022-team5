import { FootprintPredictionType } from "../dto/recommendations";
import { apiClient } from "./client";

export const fetchInitialFootprints = async (photoIds: string[]) => {
  return (await apiClient.post<FootprintPredictionType[]>("/traces/create", photoIds)).data;
};
