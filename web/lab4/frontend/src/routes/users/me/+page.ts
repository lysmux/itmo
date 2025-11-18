import apiClient from '$lib/api/api.ts';
import type { PageLoad } from './$types';
import type { User } from '$lib/api/users.ts';

export const ssr = false;

export const load: PageLoad = async () => {
	return {
		user: apiClient.get<User>('/users/me').then(res => res.data)
	}
}