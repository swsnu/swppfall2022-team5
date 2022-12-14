import { TraceDetailResponseType } from "../../dto/trace";
import Image from "next/image";
import Moment from "react-moment";
import Link from "next/link";
import { IconLock, IconLockOpen } from "@tabler/icons";

interface IProps extends TraceDetailResponseType {
  hideProfile?: boolean;
}

export const TracePreviewTitle = (props: IProps) => {
  return (
    <div className="flex items-center justify-between p-3">
      {props.hideProfile ? (
        <div />
      ) : (
        <Link href={`/user/${props.owner.username}`}>
          <div className="flex items-center gap-2">
            <div className="relative h-10 w-10 overflow-hidden rounded-full border border-gray-700">
              <img src={props.owner.imageUrl} alt={""} className="object-cover" />
            </div>
            <div>{props.owner.username}</div>
          </div>
        </Link>
      )}
      <div className="flex items-center divide-x divide-navy-500 text-xs leading-3 text-navy-400">
        <span className="px-2">{props.isPublic ? "전체 공개" : "비공개"}</span>
        <Moment date={props.date} fromNow className="px-2" />
        <span className="px-2">좋아요 {props.likesCount}</span>
        <span className="px-2">{props.viewCount}명 조회</span>
      </div>
    </div>
  );
};
