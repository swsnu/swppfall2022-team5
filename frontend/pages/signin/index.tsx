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
                <div className="mt-10 text-xl font-bold">
                    당신의 발자취를 이곳에 기록하세요
                </div>
                <div className="mt-2 text-xs font-light">
                    적당히 멋진 문구
                </div>
                <div className="text-xs font-light">
                    적당히 멋진 문구 2
                </div>
                <div className='flex justify-center mt-6 w-4/6 h-4/6 '>
                    적당한 이미지
                    {/* <Image
                        src={SamplePic}
                        alt=""
                    /> */}
                </div>
                
                <div className="mt-10">
                    <label className="block text-white-900 text-sm font-bold mb-2">
                    아이디
                    </label>
                    <input 
                        className="text-white-900 bg-black focus:outline-none"
                        placeholder='아이디를 입력하세요.'
                        onChange={event => setInputId(event.currentTarget.value)}
                    />
                </div>
                <div className="mt-3">
                    <label className="block text-white-900 text-sm font-bold mb-2">
                    비밀번호
                    </label>
                    <input 
                        className="text-white-900 bg-black focus:outline-none"
                        placeholder='비밀번호를 입력하세요.'
                        type='password'
                        onChange={event => setInputPassword(event.currentTarget.value)}
                    />
                </div>

                <div className='flex justify-center mt-5'>
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
                        className=''
                        disabled={false}
                    />
                </div>

                <div className='flex justify-center mt-5'>
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
                        className=''
                        disabled={false}
                    />
                </div>
            </div>
            
        
        </Container>
    )
}