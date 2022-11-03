import Moment from "react-moment";
import classNames from "classnames";

interface IProps {
  date: Date;
  onClick: () => void;
  isActive: boolean;
  isCurrentMonth: boolean;
}

const DayButton = ({ date, onClick, isActive, isCurrentMonth }: IProps) => {
  return (
    <div className="flex justify-center text-center">
      <div>
        <div className="text-sm text-navy-200">
          <Moment date={date} format="ddd" />
        </div>
        <div
          className={classNames(
            "mt-1 flex h-10 w-10 items-center justify-center rounded-full hover:cursor-pointer hover:bg-navy-800",
            { "bg-navy-700 hover:bg-navy-700": isActive, "opacity-50": !isCurrentMonth },
          )}
          onClick={onClick}
        >
          <Moment date={date} format="D" />
        </div>
      </div>
    </div>
  );
};

export default DayButton;
