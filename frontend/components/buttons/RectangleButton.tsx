import { IconLoader2 } from "@tabler/icons";
import classNames from "classnames";

interface IProps {
  onClick: () => void;
  text: string;
  isLoading: boolean;
  className?: string;
  disabled?: boolean;
}

const RectangleButton = ({ onClick, text, isLoading, className, disabled }: IProps) => {
  return (
    <button
      className={classNames(
        className,
        "rounded-lg border border-navy-200/5 bg-navy-700 p-3 text-navy-200 transition-colors ",
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
          text
        )}
      </span>
    </button>
  );
};

export default RectangleButton;
