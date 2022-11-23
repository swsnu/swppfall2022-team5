import { useQuery } from "@tanstack/react-query";
import { useRouter } from "next/router";
import { checkToken } from "../api";
import { apiClient } from "../api/client";
import Container from "../components/containers/Container";
import { useAuthStore } from "../store/auth";

export default function Home() {
  const router = useRouter();
  const token = useAuthStore.getState().userToken
  useQuery(
    ['token valid', token],
    () => {
      return checkToken({token:token})
    },
    {
      onSuccess: (result) => {
        if (result.valid) {
          router.push("/footprints")
        }
        else {
          router.push("/signin")
        }
      }
    }
  )
  return <Container></Container>;
}
