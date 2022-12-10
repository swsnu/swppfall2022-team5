import { useMutation, useQuery } from "@tanstack/react-query";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { checkToken, postSignin, postSignUp } from "../../api";
import RectangleButton from "../../components/buttons/RectangleButton";
import Container from "../../components/containers/Container";
import TextField from "../../components/textfield/TextField";
import { SigninRequestType, TokenVerifyResponseType } from "../../dto/auth";
import ErrorResponse from "../../dto/exception";
import { useAuthStore } from "../../store/auth";

export default function Signin() {
  const [inputId, setInputId] = useState("");
  const [inputPassword, setInputPassword] = useState("");
  const mutation = useMutation((signinRequest: SigninRequestType) => postSignin(signinRequest));
  const signupMutation = useMutation((signinRequest: SigninRequestType) => postSignUp(signinRequest));
  const router = useRouter();
  const setToken = useAuthStore((state) => state.setToken);
  const userToken = useAuthStore((state) => state.userToken);

  const checkTokenMutation = useMutation((arg: any) => checkToken({ token: userToken }), {
    onSuccess(data: TokenVerifyResponseType, variables, context) {
      setToken(userToken);
    },
    onError(error: ErrorResponse, variables, context) {
      setToken("");
    },
  });

  useEffect(() => {
    checkTokenMutation.mutate(1);
  }, [checkTokenMutation]);

  return (
    <Container>
      <div className="mx-5 flex min-h-screen items-center justify-center">
        <div>
          <div className="text-center">
            <div className="pt-10 text-3xl font-bold text-navy-200">당신의 발자취를 이곳에 기록하세요</div>
            <div className="text-md text-navy-300">
              <div className="mt-2">추억이 담긴 사진을 편하게 정리하고, 열람하세요.</div>
              <div className="">사진을 한번에 업로드하세요. 자동으로 분류하여 정리해 드립니다.</div>
            </div>
          </div>
          <div className="mt-2 flex flex-col gap-2">
            <TextField value={inputId} onChange={setInputId} placeholder="아이디를 입력하세요." />
            <TextField
              type={"password"}
              value={inputPassword}
              onChange={setInputPassword}
              placeholder="비밀번호를 입력하세요."
            />
          </div>

          <div className="mt-3 flex gap-3">
            <RectangleButton
              onClick={() => {
                mutation.mutate(
                  {
                    username: inputId,
                    password: inputPassword,
                  },
                  {
                    onSuccess: (response) => {
                      setToken(response.accessToken);
                      router.push("/footprints");
                      toast.success("로그인되었습니다.");
                    },
                    onError: (error) => {
                      toast.error((error as ErrorResponse).response.data.error.message);
                    },
                  },
                );
              }}
              text={"로그인"}
              isLoading={false}
              className="w-3/5 p-1"
              disabled={false}
            />
            <RectangleButton
              onClick={() => {
                signupMutation.mutate(
                  {
                    username: inputId,
                    password: inputPassword,
                  },
                  {
                    onSuccess: (response) => {
                      setToken(response.accessToken);
                      router.push("/footprints");
                      toast.success("회원가입에 성공하였습니다.");
                    },
                    onError: (error) => {
                      toast.error((error as ErrorResponse).response.data.error.message);
                    },
                  },
                );
              }}
              text={"회원가입"}
              isLoading={false}
              className="w-3/5 p-1"
              disabled={false}
            />
          </div>
        </div>
      </div>
    </Container>
  );
}
