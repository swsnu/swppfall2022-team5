import { TablerIcon } from "@tabler/icons";

interface IProps {
  icon: TablerIcon;
  onClick: () => void;
}

const IconButton = ({ icon: Icon, onClick }: IProps) => {
  return (
    <div onClick={onClick} className="rounded-full p-2 hover:cursor-pointer hover:bg-navy-100/5">
      <Icon />
    </div>
  );
};

export default IconButton;
