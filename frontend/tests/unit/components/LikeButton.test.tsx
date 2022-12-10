import { fireEvent, render } from "@testing-library/react";
import LikeButton from "../../../components/buttons/LikeButton";

it("should render the like button", () => {
  const onClick = jest.fn();
  const { getByTestId } = render(<LikeButton isLiked={false} onClick={onClick} likesCount={0} />);
  const button = getByTestId("like-button");
  expect(button).toBeTruthy();

  fireEvent.click(button);
  expect(onClick).toHaveBeenCalled();
});

it("should render the like button", () => {
  const onClick = jest.fn();
  const { getByTestId } = render(<LikeButton isLiked={true} onClick={onClick} likesCount={0} />);
  const button = getByTestId("like-button");
  expect(button).toBeTruthy();

  fireEvent.click(button);
  expect(onClick).toHaveBeenCalled();
});
