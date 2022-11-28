import { Menu } from "@headlessui/react";
import { TablerIcon } from "@tabler/icons";
import classNames from "classnames";
import IconButton from "../buttons/IconButton";
import TransitionContainer from "../containers/TransitionContainer";

interface MenuItem {
  onClick: () => void;
  text: string;
  icon: TablerIcon;
}

interface IProps {
  icon: TablerIcon;
  orientation: "right" | "left";
  menuItems: MenuItem[];
}

const DropDownMenuContainer = ({ icon, orientation, menuItems }: IProps) => {
  return (
    <Menu as="div" className="relative inline-block text-left">
      <Menu.Button>
        <IconButton icon={icon} />
      </Menu.Button>
      <TransitionContainer>
        <Menu.Items
          className={classNames(
            "absolute mt-2 w-56 divide-y divide-gray-100 rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none",
            {
              "right-0 origin-top-right": orientation === "right",
              "left-0 origin-top-left": orientation === "left",
            },
          )}
        >
          <div className="px-1 py-1">
            {menuItems.map((item) => {
              return (
                <Menu.Item key={item.text}>
                  {({ active }) => (
                    <button
                      onClick={item.onClick}
                      className={classNames("group flex w-full items-center rounded-md px-2 py-2 text-sm", {
                        "bg-navy-400 text-white": active,
                        "text-gray-900": !active,
                      })}
                    >
                      <item.icon className="mr-2 h-5 w-5" />
                      {item.text}
                    </button>
                  )}
                </Menu.Item>
              );
            })}
          </div>
        </Menu.Items>
      </TransitionContainer>
    </Menu>
  );
};

export default DropDownMenuContainer;
