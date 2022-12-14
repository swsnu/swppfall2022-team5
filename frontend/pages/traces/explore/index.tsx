import { IconSearch } from "@tabler/icons";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import toast from "react-hot-toast";
import { fetchAllOtherUsersTraces, searchTracesByKeyword } from "../../../api";
import RectangleButton from "../../../components/buttons/RectangleButton";
import Container from "../../../components/containers/Container";
import NavbarContainer from "../../../components/containers/NavbarContainer";
import NavigationBar from "../../../components/navbar/NavigationBar";
import TracesNotFound from "../../../components/placeholder/TracesNotFound";
import { TracePreview } from "../../../components/trace/TracePreview";
import { TracePreviewTitle } from "../../../components/trace/TracePreviewTitle";

export default function Explore() {
  const [query, setQuery] = useState("");
  const searchResult = useQuery(["search"], () => {
    if (!query) {
      return fetchAllOtherUsersTraces();
    }
    return searchTracesByKeyword(query);
  });

  const handleSearch = () => {
    if (!query) {
      toast.error("검색어를 입력해주세요.");
      return;
    }
    searchResult.refetch();
  };

  return (
    <Container>
      <NavbarContainer className="z-20">
        <NavigationBar title="탐색하기" />
      </NavbarContainer>

      {searchResult.isSuccess && searchResult.data.length == 0 && <TracesNotFound />}

      <div className="mx-5 mt-5 flex items-center rounded-lg border border-navy-600 shadow-lg">
        <IconSearch className="mx-2" />
        <input
          value={query}
          onChange={(e) => {
            setQuery(e.target.value);
          }}
          placeholder="검색어를 입력하세요."
          className="w-full bg-transparent px-2 py-2 text-sm text-navy-200 outline-none"
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              handleSearch();
            }
          }}
        ></input>
        <RectangleButton
          onClick={function (): void {
            handleSearch();
          }}
          text={"검색"}
          className="m-1 flex-shrink-0 py-1"
          isLoading={searchResult.isLoading}
        />
      </div>

      <div className="m-5 space-y-5">
        {searchResult.data?.map((trace) => {
          return (
            <div key={trace.id}>
              <TracePreview {...trace} />
              <TracePreviewTitle {...trace} />
            </div>
          );
        })}
      </div>
    </Container>
  );
}
