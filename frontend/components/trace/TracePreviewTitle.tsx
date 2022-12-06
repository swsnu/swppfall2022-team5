import { TraceDetailResponseType } from "../../dto/trace";
import Image from "next/image";
import Moment from "react-moment";
import Link from "next/link";

interface IProps extends TraceDetailResponseType {}

export const TracePreviewTitle = (props: IProps) => {
  return (
    <div className="flex items-center justify-between p-3">
      <Link href={`/user/${props.owner.username}`}>
        <div className="flex items-center gap-2">
          <div className="relative h-10 w-10 overflow-hidden rounded-full border border-gray-700">
            <Image fill src={props.owner.imageUrl} alt={""} className="object-cover" sizes="33vw" priority />
          </div>
          <div>{props.owner.username}</div>
        </div>
      </Link>
      <div className="flex items-center divide-x divide-navy-500 text-xs leading-3 text-navy-400">
        <Moment date={props.date} fromNow className="px-2" />
        <span className="px-2">좋아요 {0}</span>
        <span className="px-2">{props.viewCount}명 조회</span>
      </div>
    </div>
  );
};
