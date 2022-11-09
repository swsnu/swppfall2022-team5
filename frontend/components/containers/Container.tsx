import { PropsWithChildren } from "react";

const Container = ({ children }: PropsWithChildren) => {
  return (
    <div className="min-h-screen bg-[#0a0a0f] text-navy-100">
      <div className="relative mx-auto min-h-screen bg-navy-900 sm:container">{children}</div>
    </div>
  );
};

export default Container;
