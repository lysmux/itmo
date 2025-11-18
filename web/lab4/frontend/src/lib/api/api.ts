import axios, { type InternalAxiosRequestConfig } from 'axios';
import { goto } from '$app/navigation';
import { resolve } from '$app/paths';

const ACCESS_TOKEN_EXPIRE_AT_KEY = 'accessTokenExpireAt';

const apiClient = axios.create({
	baseURL: 'https://tunnel.lysmux.dev/api',
	timeout: 10000,
	withCredentials: true
});

const refreshApiClient = axios.create({
	baseURL: 'https://tunnel.lysmux.dev/api',
	withCredentials: true
});

async function refreshAccessToken() {
	try {
		const res = await refreshApiClient.post('/auth/refresh');
		const { expiresIn }: { expiresIn: number } = res.data;
		setAccessTokenExpiresAt(expiresIn);
	} catch (err) {
		await goto(resolve("/auth/login"))
		throw new Error('Unable to refresh access token.');
	}
}

function getAccessTokenExpiresAt(): number | null {
	const raw = localStorage.getItem(ACCESS_TOKEN_EXPIRE_AT_KEY);
	if (!raw) return null;
	const val = Number(raw);
	return isNaN(val) ? null : val;
}

function setAccessTokenExpiresAt(expiresInSec: number): void {
	const expiresAt = Date.now() + expiresInSec;
	localStorage.setItem(ACCESS_TOKEN_EXPIRE_AT_KEY, expiresAt.toString());
}

function clearAccessTokenExpiresAt(): void {
	localStorage.removeItem(ACCESS_TOKEN_EXPIRE_AT_KEY);
}

function isAccessTokenValid(): boolean {
	const expiresAt = getAccessTokenExpiresAt();
	return expiresAt !== null && Date.now() < expiresAt;
}

apiClient.interceptors.request.use(
	async (config: InternalAxiosRequestConfig & { requireAuth?: boolean }) => {
		const isAuthRoute = config.url?.startsWith('/auth/');

		if (isAuthRoute && !config.requireAuth) {
			return config;
		}

		if (!isAccessTokenValid()) {
			await refreshAccessToken();
		}
		return config;
	},
	(error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
	(response) => response,
	async (error) => {
		const originalRequest = error.config as InternalAxiosRequestConfig & {
			_retry?: boolean;
			requireAuth?: boolean;
		};
		const isAuthRoute = originalRequest.url?.startsWith('/auth/');
		if (isAuthRoute && !originalRequest.requireAuth) {
			return Promise.reject(error);
		}

		if (error.response?.status === 401 && !originalRequest._retry) {
			originalRequest._retry = true;

			try {
				await refreshAccessToken();
				return apiClient(originalRequest);
			} catch {
				clearAccessTokenExpiresAt();
				return Promise.reject(error);
			}
		}
		return Promise.reject(error);
	}
);

export { setAccessTokenExpiresAt, clearAccessTokenExpiresAt };
export default apiClient;