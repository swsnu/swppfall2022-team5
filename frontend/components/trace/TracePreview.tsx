import { useRouter } from "next/router";
import { TraceDetailResponseType } from "../../dto/trace";
import PhotoPreview from "../trace/PhotoPreview";

interface IProps extends TraceDetailResponseType {}

export function TracePreview(props: IProps) {
    const router = useRouter();
    
    return (
        <div
            className="p-5 text-navy-100 transition-colors hover:cursor-pointer hover:bg-navy-800/50"
            onClick={() => {
                router.push(`/traces/detail/${props.id}`);
            }}
        >
            <div className="ml-1">
                <div className="mt-2">{props.title}</div>
                <div className="my-2 text-xs text-navy-500">{props.date}</div>
            </div>
            <PhotoPreview key={props.footprints[0].photos[0].id} {...props.footprints[0].photos[0]} />
            
        </div>
    )
}
