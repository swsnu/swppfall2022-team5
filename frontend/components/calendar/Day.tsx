interface IProps {
  weekday: string;
  day: number;
}

const DayButton = ({ weekday, day }: IProps) => {
  return (
    <div className="text-center">
      <div className="text-sm">{weekday}</div>
      <div className="mt-1 rounded-full py-1 hover:cursor-pointer hover:bg-navy-800">
        {day}
      </div>
    </div>
  );
};

export default DayButton;
