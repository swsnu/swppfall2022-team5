import Container from "../../components/containers/Container";
import RectangleButton from '../../components/buttons/RectangleButton';
import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { SigninRequestType } from '../../dto/auth';
import { postSignin, postSignUp } from '../../api';
import { useRouter } from 'next/router';
import toast from 'react-hot-toast';
import { useAuthStore } from '../../store/auth';
import { apiClient } from "../../api/client";
import { ErrorType } from "../../api/exception/errorType";
import Image from "next/image";
import image from "../../public/img/tutorial.jpeg"

export default function Signin() {
    const [inputId, setInputId] = useState("")
    const [inputPassword, setInputPassword] = useState("")
    const queryClient = useQueryClient()
    const mutation = useMutation((signinRequest: SigninRequestType) => postSignin(signinRequest))
    const signupMutation = useMutation((signinRequest: SigninRequestType) => postSignUp(signinRequest))
    const router = useRouter();
    const setToken = useAuthStore((state) => state.setToken);

    return (
        <Container>
            <div className='mx-5'>
                <div className="pt-10 text-2xl font-bold">
                    당신의 발자취를 이곳에 기록하세요
                </div>
                <div className="mt-2 text-xs text-gray-400">
                    추억이 담긴 사진을 편하게 정리하고, 열람하세요.
                </div>
                <div className="text-xs text-gray-400">
                    사진을 한번에 업로드하세요. 자동으로 분류하여 정리해 드립니다.
                </div>
                <div className="grid justify-items-center">
                    <Image
                        className="mt-5 rounded-3xl"
                        src={image}
                        alt="Sample Image"
                        width={300}>
                    </Image>
                </div>
                <div className="mt-5 grid grid-cols-1 place-items-center">
                    <input 
                        className="text-white-900 text-sm bg-black w-3/5 p-2 enabled:hover:border-white-400 rounded-xl"
                        placeholder='아이디를 입력하세요.'
                        onChange={event => setInputId(event.currentTarget.value)}
                    />
                    <input 
                        className="text-white-900 text-sm bg-black w-3/5 p-2 enabled:hover:border-white-400 rounded-xl mt-2"
                        placeholder='비밀번호를 입력하세요.'
                        type='password'
                        onChange={event => setInputPassword(event.currentTarget.value)}
                    />
                </div>

                <div className='flex justify-center mt-3'>
                    <RectangleButton
                        onClick={() => {
                            mutation.mutate(
                                {
                                    username: inputId,
                                    password: inputPassword
                                },
                                {
                                    onSuccess: (response) => {
                                        apiClient.defaults.headers.common['Authorization'] = `Bearer ${response.accessToken}`
                                        setToken(response.accessToken)
                                        toast.success("로그인되었습니다.")
                                        router.push("/footprints")
                                    },
                                    onError: (error) => {
                                        if((error as any).response.data.error['code'] == ErrorType.INVALID_USER_INFO) {
                                            toast.error("아이디 혹은 비밀번호가 잘못되었어요 😢");
                                        }
                                        else {
                                            toast.error("무언가 잘못되었어요 😢");
                                        }
                                    }
                                }
                            )

                        }}
                        text={"로그인"}
                        isLoading={false}
                        className='w-3/5 p-1 rounded-2xl'
                        disabled={false}
                    />
                </div>

                <div className='flex justify-center mt-2'>
                    <RectangleButton
                        onClick={() => {
                            signupMutation.mutate(
                                {
                                    username: inputId,
                                    password: inputPassword
                                },
                                {
                                    onSuccess: (response) => {
                                        apiClient.defaults.headers.common['Authorization'] = `Bearer ${response.accessToken}`
                                        setToken(response.accessToken)
                                        router.push("/footprints")
                                        toast.success("회원가입에 성공하였습니다.")
                                    },
                                    onError: (error) => {
                                        if((error as any).response.data.error['code'] == ErrorType.DUPLICATE_USERNAME) {
                                            toast.error("이미 존재하는 아이디입니다.");
                                        }
                                        else {
                                            toast.error("무언가 잘못되었어요 😢");
                                        }
                                    }
                                }
                            )

                        }}
                        text={"회원가입"}
                        isLoading={false}
                        className='w-3/5 p-1 rounded-2xl'
                        disabled={false}
                    />
                </div>
            </div>
            
        
        </Container>
    )
}