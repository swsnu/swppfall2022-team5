import axios from "axios";
import toast from "react-hot-toast";

export const apiClient = axios.create({ baseURL: process.env.NEXT_PUBLIC_API_URL });
