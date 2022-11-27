import UserDropdown from "../user/UserDropdown";
import MenuDropdown from "./MenuDropdown";

interface IProps {
  title?: string;
}

const NavigationBar = ({ title }: IProps) => {
  return (
    <div className="flex items-center justify-between px-3 py-2">
      <MenuDropdown />
      <div className="font-semibold">{title}</div>
      <UserDropdown />
    </div>
  );
};

export default NavigationBar;
