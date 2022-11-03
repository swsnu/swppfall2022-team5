import classNames from "classnames";

interface IProps {
  onClick: () => void;
  text: string;
  isLoading: boolean;
  className?: string;
}

const RectangleButton = ({ onClick, text, isLoading, className }: IProps) => {
  return (
    <div className="grid justify-items-stretch px-3">
      <button
        className={classNames(
          className,
          "rounded-lg border border-navy-200/5 bg-navy-700 p-3 text-navy-200 transition-colors hover:bg-navy-600",
        )}
        onClick={onClick}
      >
        {text}
      </button>
    </div>
  );
};

export default RectangleButton;
