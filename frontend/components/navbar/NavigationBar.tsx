import { IconMenu2, IconUser } from "@tabler/icons";
import IconButton from "../buttons/IconButton";

interface IProps {
  title?: string;
}

const NavigationBar = ({ title }: IProps) => {
  return (
    <div className="flex items-center justify-between px-3 pt-5 pb-3">
      <IconButton icon={IconMenu2} onClick={() => {}} />
      <div>{title}</div>
      <IconButton icon={IconUser} onClick={() => {}} />
    </div>
  );
};

export default NavigationBar;
