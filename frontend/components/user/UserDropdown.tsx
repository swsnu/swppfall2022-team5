import { Popover, Transition } from "@headlessui/react";
import { IconChevronDown, IconUser } from "@tabler/icons";
import { Fragment } from "react";
import TransitionContainer from "../containers/TransitionContainer";

interface IProps {
  onClickSignout: () => void;
  onClickMyPage: () => void;
  onClickInfo: () => void;
}

const UserDropdown = ({ onClickSignout, onClickMyPage, onClickInfo }: IProps) => {
  return (
    <div className="mt-1 mr-2">
      <Popover>
        <Popover.Button className="focus:outline-none">
          <div className="flex items-center hover:opacity-80">
            <IconUser />
          </div>
        </Popover.Button>
        <TransitionContainer>
          <Popover.Panel className="absolute right-0 mr-3 ">
            <div className="rounded-lg border border-navy-200/5 bg-navy-800 py-3 px-4 shadow-md">
              <div className="mb-2 flex items-center hover:opacity-80" role="button" onClick={() => onClickMyPage()}>
                마이페이지
              </div>
              <div className="mb-2 flex items-center hover:opacity-80" role="button">
                내 정보
              </div>
              <div className="flex items-center hover:opacity-80" role="button" onClick={() => onClickSignout()}>
                로그아웃
              </div>
            </div>
          </Popover.Panel>
        </TransitionContainer>
      </Popover>
    </div>
  );
};

export default UserDropdown;
