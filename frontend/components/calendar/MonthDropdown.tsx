import { Popover } from "@headlessui/react";
import { IconChevronDown } from "@tabler/icons";
import Moment from "react-moment";
import TransitionContainer from "../containers/TransitionContainer";
import FullCalendar from "./FullCalendar";

interface IProps {
  selectedDate: Date;
}

const MonthDropdown = ({ selectedDate }: IProps) => {
  return (
    <div className="mx-5 mb-3">
      <Popover>
        <Popover.Button className="focus:outline-none">
          <div className="flex items-center hover:opacity-80">
            <Moment className="select-none pr-1 text-lg font-semibold" date={selectedDate} format="YYYY년 M월" />
            <IconChevronDown />
          </div>
        </Popover.Button>
        <TransitionContainer>
          <Popover.Panel className="absolute left-1/2 z-10 mt-3 w-screen max-w-md -translate-x-1/2 transform px-4 sm:px-0 md:max-w-lg">
            <div className="overflow-hidden rounded-lg border border-navy-200/5 bg-navy-800 py-5 px-4 shadow-md">
              <FullCalendar />
            </div>
          </Popover.Panel>
        </TransitionContainer>
      </Popover>
    </div>
  );
};

export default MonthDropdown;
