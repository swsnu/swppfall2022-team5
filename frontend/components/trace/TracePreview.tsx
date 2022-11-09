import { useRouter } from "next/router";
import { TraceDetailResponse } from "../../dto/trace";
import Photo from "../footprint/Photo";

interface IProps extends TraceDetailResponse {}

export function TracePreview(props: IProps) {
    const router = useRouter();
    console.log(props.footprints[0].photos)
    return (
        <div
            className="p-5 text-navy-100 transition-colors hover:cursor-pointer hover:bg-navy-800/50"
            onClick={() => {
                router.push(`/traces/detail/${props.id}`);
            }}
        >
            <div className="ml-1">
                <div className="mt-2">{props.title}</div>
                <div className="mt-1 text-xs text-navy-500">{props.date}</div>
            </div>
            <Photo key={props.footprints[0].photos[0].id} {...props.footprints[0].photos[0]} />
            
        </div>
    )
}
