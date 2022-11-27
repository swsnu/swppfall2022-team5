import { IconCalendarMinus } from "@tabler/icons";

const TracesNotFound = () => {
  return (
    <div className="flex flex-col items-center justify-center pt-32 text-navy-700">
      <IconCalendarMinus size={150} stroke={2} />
      <div className="mt-5 text-center text-navy-400/50">
        <div className="mt-3 text-center text-xl font-bold">Trace가 존재하지 않습니다</div>
      </div>
    </div>
  );
};

export default TracesNotFound;
