import { ErrorType } from "../../api/exception/errorType";

it("is about error type", () => {
  expect(ErrorType.WRONG_FORMAT).toBe(4000);
  expect(ErrorType.NOT_FOUND).toBe(4004);
  expect(ErrorType.INVALID_USER_INFO).toBe(4006);
});
