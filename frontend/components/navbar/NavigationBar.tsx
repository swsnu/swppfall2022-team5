import { IconMenu2, IconUser } from "@tabler/icons";
import { useRouter } from "next/router";
import toast from "react-hot-toast";
import { useAuthStore } from "../../store/auth";
import IconButton from "../buttons/IconButton";
import UserDropdown from "../user/UserDropdown";

interface IProps {
  title?: string;
}

const NavigationBar = ({ title }: IProps) => {
  const router = useRouter()
  const setToken = useAuthStore.getState().setToken
  return (
    <div className="flex items-center justify-between px-3 py-2">
      <IconButton icon={IconMenu2} onClick={() => {}} />
      <div>{title}</div>
      <UserDropdown
        onClickSignout={() => {
          setToken("")
          router.push("/signin")
          toast.success("로그아웃 되었습니다.")
        }}
        onClickInfo={()=>{}}
        onClickMypage={()=>{}}
      />
    </div>
  );
};

export default NavigationBar;
