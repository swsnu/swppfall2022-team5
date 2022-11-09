import { TablerIcon } from "@tabler/icons";
import classNames from "classnames";

interface IProps {
  onClick: () => void;
  text: string;
  icon?: TablerIcon;
  className?: string;
  isActive: boolean;
}

const TagButton = ({ onClick, text, icon: Icon, className, isActive }: IProps) => {
  return (
    <div
      className={classNames(
        "rounded-lg border border-navy-700 px-2 py-1 text-sm text-navy-200 transition-colors hover:cursor-pointer ",
        className,
        {
          "bg-navy-600": isActive,
          "hover:bg-navy-700/60": !isActive,
        },
      )}
      onClick={onClick}
    >
      <div className="flex items-center">
        {!!Icon && <Icon className="mr-1" size={17} />}
        {text}
      </div>
    </div>
  );
};

export default TagButton;
