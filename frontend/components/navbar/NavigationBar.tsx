import { IconMenu2 } from "@tabler/icons";
import { useQuery } from "@tanstack/react-query";
import { useRouter } from "next/router";
import toast from "react-hot-toast";
import { whoAmI } from "../../api";
import { useAuthStore } from "../../store/auth";
import IconButton from "../buttons/IconButton";
import UserDropdown from "../user/UserDropdown";

interface IProps {
  title?: string;
}

const NavigationBar = ({ title }: IProps) => {
  const router = useRouter();
  const setToken = useAuthStore.getState().setToken;
  const myName = useQuery(["whoAmI"], whoAmI);
  return (
    <div className="flex items-center justify-between px-3 py-5">
      <IconButton icon={IconMenu2} onClick={() => {}} />
      <div className="text-xl font-semibold ">{title}</div>
      <UserDropdown
        onClickSignout={() => {
          setToken("");
          router.push("/signin");
          toast.success("로그아웃 되었습니다.");
        }}
        onClickInfo={() => {}}
        onClickMyPage={() => {
          router.push(`/user/${myName.data?.username}`);
        }}
      />
    </div>
  );
};

export default NavigationBar;
