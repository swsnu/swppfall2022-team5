

export interface SigninRequestType {
    username: string;
    password: string;
};

export interface SigninResponseType {
    accessToken: string;
};

export interface TokenVerifyRequestType {
    token: string;
};

export interface TokenVerifyResponseType {
    valid: boolean;
};