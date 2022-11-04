import {TablerIcon } from "@tabler/icons";
import FloatingButton from "../buttons/FloatingButton";

export interface TagType {
    id: number,
    icon: TablerIcon,
    name: string,
}

export function TagList(props: { tagList: Array<TagType> }) {
    return (
        <div className="my-3 pb-2 flex overflow-x-auto scrollbar-hide">
            {props.tagList.map((tag) => (
                <FloatingButton
                    onClick={() => {
                        // TODO: Link to filter with tag page when not editing
                     }}
                    icon={tag.icon}
                    className="text-right mx-1 flex-shrink-0"
                    text={tag.name}
                />
            ))}
        </div>
    );
}