import { IconCalendarMinus } from "@tabler/icons";

const FootprintsNotFound = () => {
  return (
    <div className="flex flex-col items-center justify-center pt-32 text-navy-700">
      <IconCalendarMinus size={150} stroke={2} />
      <div className="mt-5 text-center text-navy-500/50">
        <div className="mt-3 text-center text-2xl font-bold">발자취가 존재하지 않습니다</div>
        <div className="mt-3 text-center">
          아래 <span>기록 추가하기</span> 버튼을 클릭해서
          <br /> 새로운 사진을 업로드해보세요.
        </div>
      </div>
    </div>
  );
};

export default FootprintsNotFound;
