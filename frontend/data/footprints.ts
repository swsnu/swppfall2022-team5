import { FootprintType } from "../dto/footprint";

export const dummyFootprints: FootprintType[] = [
  {
    id: 1,
    startTime: "2022-11-05T13:15:30Z",
    endTime: "2022-11-05T14:15:30Z",
    rating: 4,
    photos: [
      {
        id: 1,
        imageUrl:
          "https://images.unsplash.com/photo-1661347333238-0c8fe353e59e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
        timestamp: "2022-11-05T14:15:30Z",
        longitude: 37.3306,
        latitude: 126.593,
      },
      {
        id: 2,
        imageUrl:
          "https://images.unsplash.com/photo-1667594439073-c63131bb63fa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80",
        timestamp: "2022-11-05T16:15:30Z",
        longitude: 37.3306,
        latitude: 126.593,
      },
    ],
    place: {
      id: 1,
      name: "그릴도하",
      address: "서울 용산구 이태원로 268 2층",
      city: "서울",
      country: "관악구",
      district: "관악구청",
      longitude: 37.3306,
      latitude: 126.593,
    },
    tag: {
      id: 1,
      tagName: "맛집",
    },
    memo: "호무스가 독특했고 양갈비가 아주 맛있었는데 양이 너무 적었다! 그리고 너무 비싸다...",
  },
];
