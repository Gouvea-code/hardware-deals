import axios from 'axios';
export function apiErrorMessage(error:unknown,fallback:string){if(axios.isAxiosError(error)){const message=(error.response?.data as {message?:string})?.message;if(message)return message;}return fallback;}
