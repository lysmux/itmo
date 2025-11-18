import apiClient from '$lib/api/api.ts';
import { goto } from '$app/navigation';
import { resolve } from '$app/paths';

export const logout = async () => {
	await apiClient.post('/auth/logout');
	await goto(resolve('/auth/login'));
};