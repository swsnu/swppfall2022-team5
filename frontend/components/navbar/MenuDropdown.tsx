import { IconHome, IconMapSearch, IconMenu2 } from "@tabler/icons";
import { useRouter } from "next/router";
import DropDownMenuContainer from "../containers/DropdownMenuContainer";

const MenuDropdown = () => {
  const router = useRouter();

  return (
    <DropDownMenuContainer
      icon={IconMenu2}
      orientation="left"
      menuItems={[
        {
          icon: IconHome,
          text: "홈",
          onClick: () => {
            router.push("/footprints");
          },
        },
        {
          icon: IconMapSearch,
          text: "탐색하기",
          onClick: () => {},
        },
      ]}
    />
  );
};

export default MenuDropdown;
