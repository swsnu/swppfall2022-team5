import { FootprintPredictionType } from "../dto/recommendations";

export const dummyPredictedFootprints: FootprintPredictionType[] = [
  {
    recommendedPlaceList: [
      {
        name: "여수해상맛집",
        address: "전남 여수시 돌산읍 우두리 794-89",
        distance: 12,
        category: "음식점",
      },
      {
        name: "안동국밥 여수돌산점",
        address: "전남 여수시 돌산읍 우두리 794-89",
        distance: 25,
        category: "음식점",
      },
      {
        name: "더빈스바이유씨씨",
        address: "전남 여수시 돌산읍 우두리 794-89",
        distance: 38,
        category: "음식점",
      },
      {
        name: "카페보니또 여수케이블카돌산점",
        address: "전남 여수시 돌산읍 우두리 794-89",
        distance: 38,
        category: "카페",
      },
      {
        name: "여수해산물튀김",
        address: "전남 여수시 돌산읍 우두리 794-89",
        distance: 40,
        category: "음식점",
      },
      {
        name: "카페데스띠노",
        address: "전남 여수시 돌산읍 우두리 794-89",
        distance: 43,
        category: "카페",
      },
    ],
    photoList: [
      {
        id: 12,
        imageUrl:
          "https://images.unsplash.com/photo-1667684550725-71e60ab8368e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2532&q=80",
        longitude: 127.74120277777777,
        latitude: 34.73079722222222,
        timestamp: "2021-08-01T20:44:03.000+00:00",
      },
      {
        id: 13,
        imageUrl:
          "https://images.unsplash.com/photo-1667703795484-a187736411f3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80",
        longitude: 127.74118055555556,
        latitude: 34.73082777777778,
        timestamp: "2021-08-01T20:44:25.000+00:00",
      },
    ],
    meanLatitude: 34.73080185185185,
    meanLongitude: 127.74120277777779,
    meanTime: "2021-08-01T20:44:25.666+00:00",
    startTime: "2021-08-01T20:44:03.000+00:00",
    endTime: "2021-08-01T20:44:49.000+00:00",
  },
];
