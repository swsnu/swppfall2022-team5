import { Transition } from "@headlessui/react";
import { Fragment, PropsWithChildren } from "react";

const TransitionContainer = ({ children }: PropsWithChildren) => {
  return (
    <Transition
      as={Fragment}
      enter="transition ease-out duration-200"
      enterFrom="opacity-0 translate-y-0"
      enterTo="opacity-100 translate-y-1"
      leave="transition ease-in duration-150"
      leaveFrom="opacity-100 translate-y-1"
      leaveTo="opacity-0 translate-y-0"
    >
      {children}
    </Transition>
  );
};

export default TransitionContainer;
