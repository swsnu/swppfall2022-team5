import Moment from "react-moment";
import classNames from "classnames";

interface IProps {
  date: Date;
  onClick: () => void;
  isActive: boolean;
  isCurrentMonth: boolean;
  showWeekday: boolean;
}

const DayButton = ({ date, onClick, isActive, isCurrentMonth, showWeekday }: IProps) => {
  return (
    <div className="flex select-none justify-center">
      <div>
        {showWeekday && (
          <div className="text-center text-sm text-navy-200">
            <Moment date={date} format="ddd" />
          </div>
        )}
        <button
          className={classNames("mt-1 flex h-10 w-10 items-center justify-center rounded-full hover:cursor-pointer ", {
            "bg-navy-700": isActive,
            "hover:bg-navy-100/5": !isActive,
            "opacity-50": !isCurrentMonth,
          })}
          onClick={onClick}
        >
          <Moment date={date} format="D" />
        </button>
      </div>
    </div>
  );
};

export default DayButton;
