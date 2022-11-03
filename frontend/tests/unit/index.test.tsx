import { render } from '@testing-library/react';
import '@testing-library/jest-dom'
import Home from "../../pages";

/**
 * @jest-environment react
 */

describe("index", () => {
  it("is empty unit test", () => { });
  it("should render properly", () => {
    expect(render(<Home />)).toBeTruthy();
  })
});
