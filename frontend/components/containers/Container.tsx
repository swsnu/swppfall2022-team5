import { PropsWithChildren } from "react";

const Container = ({ children }: PropsWithChildren) => {
  return (
    <div className="min-h-screen bg-navy-900 text-navy-100">
      <div className="mx-auto py-5 sm:container">{children}</div>
    </div>
  );
};

export default Container;
