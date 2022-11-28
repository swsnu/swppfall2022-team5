interface ErrorInfo {
  code: number;
  message: string;
}

interface ErrorDto {
  error: ErrorInfo;
}

export default interface ErrorResponse {
  response: {
    data: ErrorDto;
  };
}
