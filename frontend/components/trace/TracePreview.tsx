import Image from "next/image";
import { useRouter } from "next/router";
import { TraceDetailResponseType } from "../../dto/trace";

interface IProps extends TraceDetailResponseType {}

export function TracePreview(props: IProps) {
  const router = useRouter();

  var representativeFootprint = props.footprints[0];
  var thumnailPhoto = representativeFootprint?.photos[0];
  var placeList = props.footprints.map((fp) => fp.place.name).join(" - ");

  return (
    <div
      className="transition-all hover:scale-[101%] hover:cursor-pointer"
      onClick={() => {
        router.push(`/traces/detail/${props.id}`);
      }}
    >
      <div className="relative overflow-hidden rounded-3xl border border-white/10">
        <div className="absolute right-0 left-0 bottom-0 z-10 border-t border-black/5 bg-black/20 backdrop-blur-xl">
          <div className="py-2 px-4 leading-5">
            <div className="font-medium">{props.title}</div>
            <div className="text-sm text-gray-300">{placeList}</div>
          </div>
        </div>
        <div className="w-100 relative h-36">
          <img src={thumnailPhoto?.imageUrl} alt={""} className="object-cover" />
        </div>
      </div>
    </div>
  );
}
