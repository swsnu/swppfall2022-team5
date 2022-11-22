import { useRouter } from "next/router";
import { useEffect } from "react";
import Container from "../components/containers/Container";

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    router.push("/signin");
  });

  return <Container></Container>;
}
