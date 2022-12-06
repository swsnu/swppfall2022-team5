import { IconLoader2, TablerIcon } from "@tabler/icons";
import classNames from "classnames";

interface IProps {
  onClick: () => void;
  text: string;
  isLoading: boolean;
  className?: string;
  disabled?: boolean;
  icon?: TablerIcon;
  destructive?: boolean;
}

const RectangleButton = ({ onClick, text, isLoading, className, disabled, icon: Icon, destructive }: IProps) => {
  return (
    <button
      data-testid={text}
      className={classNames(
        "rounded-lg border border-navy-200/5 bg-navy-700 p-3 text-navy-200 transition-colors ",
        className,
        { "hover:bg-navy-600": !disabled },
      )}
      onClick={onClick}
      disabled={disabled}
    >
      <span className={classNames({ "opacity-20": disabled })}>
        {isLoading ? (
          <div className="flex justify-center gap-x-3">
            <IconLoader2 className="animate-spin" />
            <span>로딩 중</span>
          </div>
        ) : (
          <div className="flex justify-center gap-2">
            {Icon && <Icon width={20} />}
            <span className={classNames({ "text-red-400": !!destructive })}>{text}</span>
          </div>
        )}
      </span>
    </button>
  );
};

export default RectangleButton;
