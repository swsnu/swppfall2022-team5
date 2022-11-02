import DayButton from "./Day";

const Calendar = () => {
  return (
    <div className="mx-5 columns-7">
      <DayButton weekday="월" day={20} />
      <DayButton weekday="화" day={21} />
      <DayButton weekday="수" day={22} />
      <DayButton weekday="목" day={23} />
      <DayButton weekday="금" day={24} />
      <DayButton weekday="토" day={25} />
      <DayButton weekday="일" day={26} />
    </div>
  );
};

export default Calendar;
