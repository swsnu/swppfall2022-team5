import { IconMenu2, IconUser } from "@tabler/icons";
import IconButton from "../buttons/IconButton";

interface IProps {
  title?: string;
}

const NavigationBar = ({ title }: IProps) => {
  return (
    <div className="flex items-center justify-between px-3 py-2">
      <IconButton icon={IconMenu2} onClick={() => {}} />
      <div>{title}</div>
      <IconButton icon={IconUser} onClick={() => {}} />
    </div>
  );
};

export default NavigationBar;
