import { IconLogout, IconUser, IconUserCircle } from "@tabler/icons";
import { useQuery } from "@tanstack/react-query";
import { useRouter } from "next/router";
import toast from "react-hot-toast";
import { whoAmI } from "../../api";
import { useAuthStore } from "../../store/auth";
import DropDownMenuContainer from "../containers/DropdownMenuContainer";

const UserDropdown = () => {
  const myName = useQuery(["whoAmI"], whoAmI);
  const router = useRouter();
  const setToken = useAuthStore((state) => state.setToken);

  return (
    <DropDownMenuContainer
      icon={IconUser}
      orientation="right"
      menuItems={[
        {
          icon: IconUserCircle,
          text: "마이페이지",
          onClick: () => {
            router.push(`/user/${myName.data?.username}`);
          },
        },
        {
          icon: IconLogout,
          text: "로그아웃",
          onClick: () => {
            setToken("");
            router.push("/signin");
            toast.success("로그아웃 되었습니다.");
          },
        },
      ]}
    />
  );
};

export default UserDropdown;
