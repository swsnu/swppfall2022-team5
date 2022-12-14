import { TablerIcon } from "@tabler/icons";

interface IProps {
  icon: TablerIcon;
  onClick?: () => void;
}

const IconButton = ({ icon: Icon, onClick }: IProps) => {
  if (!onClick) {
    return (
      <div className="rounded-full p-2 hover:cursor-pointer hover:bg-navy-100/5">
        <Icon />
      </div>
    );
  }

  return (
    <button onClick={onClick} className="rounded-full p-2 hover:cursor-pointer hover:bg-navy-100/5">
      <Icon />
    </button>
  );
};

export default IconButton;
