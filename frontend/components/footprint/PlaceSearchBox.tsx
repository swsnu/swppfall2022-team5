import { IconSearch, TablerIcon } from "@tabler/icons";
import { useQuery } from "@tanstack/react-query";
import classNames from "classnames";
import { useState } from "react";
import { PlaceType } from "../../dto/place";

interface IProps {
  className?: string;
  query: string;
  setQuery: (query: string) => void;
}

const PlaceSearchBox = (props: IProps) => {
  return (
    <div
      className={classNames(
        "rounded-lg border border-navy-700 px-2 py-1 text-sm text-navy-200 transition-colors hover:cursor-pointer ",
        { [props.className ?? ""]: !!props.className },
      )}
    >
      <div className="flex">
        <IconSearch className="mr-1" size={17} />
        <input
          className="flex-1 bg-transparent outline-none"
          placeholder="직접 추가하기"
          value={props.query}
          onChange={(e) => {
            props.setQuery(e.target.value);
          }}
        />
      </div>
    </div>
  );
};

export default PlaceSearchBox;
