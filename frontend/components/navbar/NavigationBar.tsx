import { IconMenu2, IconUser } from "@tabler/icons";
import IconButton from "../buttons/IconButton";
const NavigationBar = () => {
  return (
    <div className="flex justify-between px-5 pt-5 pb-3">
      <IconButton icon={IconMenu2} onClick={() => {}} />
      <IconButton icon={IconUser} onClick={() => {}} />
    </div>
  );
};

export default NavigationBar;
