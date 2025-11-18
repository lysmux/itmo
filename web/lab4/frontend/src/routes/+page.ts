import type { PageLoad } from '../../.svelte-kit/types/src/routes/users/me/$types';
import apiClient from '$lib/api/api.ts';
import type { HitResult } from '$lib/api/hit.ts';

export const ssr = false;

export const load: PageLoad = async () => {
	return {
		hitResults: apiClient.get<HitResult[]>('/hits/list').then((res) => res.data)
	};
};