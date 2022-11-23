import { Popover, Transition } from "@headlessui/react";
import { IconChevronDown, IconUser } from "@tabler/icons";
import { Fragment } from "react";

interface IProps {
    onClickSignout: () => void
    onClickMyPage: () => void
}

const UserDropdown = ({onClickSignout, onClickMyPage}: IProps) => {
    return (
        <div className="mt-1 mr-2">
            <Popover>
                <Popover.Button className="focus:outline-none">
                    <div className="flex items-center hover:opacity-80">
                        <IconUser />
                    </div>
                </Popover.Button>
                <Transition
                    as={Fragment}
                    enter="transition ease-out duration-200"
                    enterFrom="opacity-0 translate-y-0"
                    enterTo="opacity-100 translate-y-1"
                    leave="transition ease-in duration-150"
                    leaveFrom="opacity-100 translate-y-1"
                    leaveTo="opacity-0 translate-y-0"
                >
                <Popover.Panel className="absolute right-0 mr-3 ">
            <div className="rounded-lg border border-navy-200/5 bg-navy-800 py-3 px-4 shadow-md">
                <div className="flex items-center hover:opacity-80 mb-2" role='button' onClick={() => onClickMyPage()}> 
                    마이페이지
                </div>
                <div className="flex items-center hover:opacity-80 mb-2" role='button'> 
                    내 정보
                </div>
                <div className="flex items-center hover:opacity-80" role='button' onClick={() => onClickSignout()}> 
                    로그아웃
                </div>
            </div>
            </Popover.Panel>
                </Transition>
            </Popover>
        </div>
    )
}

export default UserDropdown;