import { TablerIcon } from "@tabler/icons";
import classNames from "classnames";

interface IProps {
  onClick: () => void;
  icon: TablerIcon;
  text: string;
  className?: string;
}

const FloatingButton = ({ onClick, icon: Icon, className, text }: IProps) => {
  return (
    <button
      className={classNames(
        className,
        "rounded-full border border-navy-200/5 bg-navy-700 px-4 py-3 text-navy-200 transition-colors hover:bg-navy-600",
      )}
    >
      <div className="flex items-center justify-center">
        <Icon />
        {!!text && <div className="ml-2">{text}</div>}
      </div>
    </button>
  );
};

export default FloatingButton;
