import classNames from "classnames";
import { PropsWithChildren } from "react";

const NavbarContainer = ({ children, className }: PropsWithChildren & { className?: string }) => {
  return (
    <div
      className={classNames(
        "sticky top-0 left-0 right-0 z-50 border-b border-navy-700/50 bg-navy-800/90 backdrop-blur-md",
        className,
      )}
    >
      {children}
    </div>
  );
};

export default NavbarContainer;
