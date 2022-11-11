import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { render } from "@testing-library/react";

import Footprints from "../../../pages/footprints";

const queryClient = new QueryClient();

describe("Home", () => {
  it("should render main page without errors", () => {
    render(<QueryClientProvider client={queryClient}>
      <Footprints />
    </QueryClientProvider>
    )
    expect(window).toBeTruthy();
  });
});
