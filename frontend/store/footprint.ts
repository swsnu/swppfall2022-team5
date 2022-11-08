import produce from "immer";
import { v4 } from "uuid";
import create from "zustand";
import { FootprintRequestType } from "../dto/footprint";
import { FootprintPredictionType } from "../dto/recommendations";

interface FootprintCreateState {
  pendingFootprintRequests: FootprintRequestType[];
  setPendingFootprintRequests: (predictions: FootprintPredictionType[]) => void;
  getFootprintByID: (uuid: string) => FootprintRequestType | undefined;
  setFootprintByIDWith: (uuid: string) => (props: Partial<FootprintRequestType>) => void;
}

export const useFootprintCreateStore = create<FootprintCreateState>()((set, get) => ({
  pendingFootprintRequests: [],
  setPendingFootprintRequests: (predictions) => {
    set(
      produce((state: FootprintCreateState) => {
        state.pendingFootprintRequests = predictions.map((prediction) => {
          return {
            uuid: v4(),
            startTime: prediction.startTime,
            endTime: prediction.endTime,
            rating: 3,
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
  getFootprintByID: (uuid) => {
    return get().pendingFootprintRequests.find((f) => f.uuid === uuid);
  },
  setFootprintByIDWith: (uuid) => (props) => {
    set(
      produce((state: FootprintCreateState) => {
        const index = state.pendingFootprintRequests.findIndex((f) => f.uuid === uuid);
        if (index !== -1) {
          state.pendingFootprintRequests[index] = { ...state.pendingFootprintRequests[index], ...props };
        }
      }),
    );
  },
}));
