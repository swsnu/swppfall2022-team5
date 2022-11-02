interface IProps {
  weekday: string;
  day: number;
}

const DayButton = ({ weekday, day }: IProps) => {
  return (
    <div className="flex justify-center text-center">
      <div>
        <div className="text-sm text-navy-200">{weekday}</div>
        <div className="mt-1 flex h-10 w-10 items-center justify-center rounded-full hover:cursor-pointer hover:bg-navy-800">
          {day}
        </div>
      </div>
    </div>
  );
};

export default DayButton;
