import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { fireEvent, render, screen } from "@testing-library/react";
import { act } from "react-dom/test-utils";
import UserProfile from "../../../components/user/UserProfile";
import mockedAxios from "../../../__mocks__/axios";

const queryClient = new QueryClient();

it("renders User Profile: my profile", () => {

  mockedAxios.get.mockImplementation(() => 
  Promise.resolve(
    { data: { 
        username: "user1", 
        followingCount: 3, 
        followerCount: 3, 
        imageUrl: "https://naver.com",
        traceCount: 3,
     } }
  ));

  render(
    <QueryClientProvider client={queryClient}>
        <UserProfile
            username="user1"
            followerCount={3}
            followingCount={4}
            imageUrl="https://naver.com"
            traceCount={3}
        />
    </QueryClientProvider>
  );
  expect(window).toBeTruthy();
});

it("renders User Profile: other's profile", async () => {

    mockedAxios.get.mockImplementation(() => 
    Promise.resolve(
      { data: { 
          username: "user2", 
          followingCount: 5, 
          followerCount: 6, 
          imageUrl: "https://naver.com",
          traceCount: 7,
       } }
    ));
  
    const { container } = render(
        <QueryClientProvider client={queryClient}>
            <UserProfile
                username="user3"
                followerCount={3}
                followingCount={4}
                imageUrl="https://naver.com"
                traceCount={3}
            />
        </QueryClientProvider>
    );
    expect(window).toBeTruthy();

    const button = container.getElementsByClassName("py-1")[0];
  
    // await act(async () => {
    //   fireEvent.click(button);
    // });
  });
