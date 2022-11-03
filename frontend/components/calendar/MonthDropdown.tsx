import Moment from "react-moment";
import { IconChevronDown } from "@tabler/icons";

interface IProps {
  onClick: () => void;
  selectedDate: Date;
}

const MonthDropdown = ({ onClick, selectedDate }: IProps) => {
  return (
    <div className="mx-7 mb-3">
      <div className="flex items-center hover:cursor-pointer hover:opacity-80 ">
        <Moment className="pr-1 text-lg font-semibold" locale="ko" date={selectedDate} format="YYYY년 MM월" />
        <IconChevronDown />
      </div>
    </div>
  );
};

export default MonthDropdown;
