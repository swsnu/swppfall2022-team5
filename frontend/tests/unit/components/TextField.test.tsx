import { fireEvent, getByTestId, render, renderHook, screen } from "@testing-library/react";
import { HTMLInputTypeAttribute, useState } from "react";
import TextField from "../../../components/textfield/TextField";

describe("TextField", () => {
  it("should render without errors", () => {
    const { container } = render(
      <TextField
        value={""}
        onChange={function (value: string): void {
          throw new Error("Function not implemented.");
        }}
        placeholder={""}
      />,
    );
    expect(window).toBeTruthy();
  });

  it("should work well :)", () => {
    const onChange = jest.fn();
    const { getByTestId } = render(<TextField value="" onChange={onChange} placeholder={""} />);
    const input = getByTestId("textfield");

    // Verify the initial value of the input
    expect((input as HTMLInputElement).value).toBe("");

    // Simulate typing in the input
    fireEvent.change(input, { target: { value: "John Doe" } });

    // Verify that the onChange callback has been called
    expect(onChange).toHaveBeenCalled();
    expect(onChange).toHaveBeenCalledWith("John Doe");

    // Verify that the value of the input has not changed
    expect((input as HTMLInputElement).value).toBe("");
  });
});
