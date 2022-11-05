import { TablerIcon } from "@tabler/icons";
import FloatingButton from "../buttons/FloatingButton";

export interface TagType {
  id: number;
  icon: TablerIcon;
  name: string;
}

export function TagList(props: { tagList: Array<TagType> }) {
  return (
    <div className="my-3 flex overflow-x-auto pb-2 scrollbar-hide">
      {props.tagList.map((tag) => (
        <FloatingButton
          key={tag.id}
          onClick={() => {
            // TODO: Link to filter with tag page when not editing
          }}
          icon={tag.icon}
          className="mx-1 flex-shrink-0 text-right"
          text={tag.name}
        />
      ))}
    </div>
  );
}
