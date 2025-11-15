export interface RegisterRequest {
	username: string;
	password: string;
}

export interface LoginRequest {
	username: string;
	password: string;
}

export interface VKCallbackRequest {
	code: string;
	deviceId: string;
	challengeVerifier: string;
}

export interface AuthResponse {
	accessToken: string;
	expiresIn: number;
}

export interface User {
	id: string;
	username: string;
}