import create from "zustand";
import produce from "immer";
import { getWeekContainingDate, getWeeksInMonth } from "../utils/calendar";
import { FootprintPredictionType } from "../dto/recommendations";
import { FootprintRequestType, FootprintType } from "../dto/footprint";

interface FootprintCreateState {
  pendingFootprintRequests: FootprintRequestType[];
  setPendingFootprintRequests: (predictions: FootprintPredictionType[]) => void;
}

export const useFootprintCreateStore = create<FootprintCreateState>()((set, get) => ({
  pendingFootprintRequests: [],
  setPendingFootprintRequests: (predictions) => {
    set(
      produce((state: FootprintCreateState) => {
        state.pendingFootprintRequests = predictions.map((prediction) => {
          return {
            startTime: prediction.startTime,
            endTime: prediction.endTime,
            rating: 0,
            memo: "",
            tag: "",
            photos: prediction.photoList.map((photo) => ({ imagePath: photo.imagePath, imageUrl: photo.imageUrl })),
            place: undefined,
            recommendedPlaces: prediction.recommendedPlaceList,
          };
        });
      }),
    );
  },
}));
