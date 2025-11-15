<script lang="ts">
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';
	import axios from 'axios';
	import type { AuthResponse, VKCallbackRequest } from '$lib/api/users.ts';
	import { toasts } from 'svelte-toasts';

	onMount(() => {
		const urlParams = new URLSearchParams(window.location.search);
		const state = urlParams.get('state');
		const code = urlParams.get('code');
		const deviceId = urlParams.get('device_id');

		const challengeVerifier = sessionStorage.getItem('challengeVerifier');
		const stateVerifier = sessionStorage.getItem('stateVerifier');

		if (
			state === null
			|| code === null
			|| deviceId === null
			|| stateVerifier === null
			|| state !== stateVerifier
		) {
			goto(resolve('/auth/login'), { replaceState: true });
		}

		const callbackData: VKCallbackRequest = {
			code: code as string,
			deviceId: deviceId as string,
			challengeVerifier: challengeVerifier as string,
		};

		axios.post<AuthResponse>('/api/auth/callback/vk', callbackData)
			.then((response) => response.data)
			.then((response: AuthResponse) => {
				localStorage.setItem('accessTokenExpireIn', String(response.expiresIn));
				goto(resolve('/users/me'));
			})
			.catch((error) => {
				console.log(error);
				toasts.add({
					title: `API ERROR | ${error.status}`,
					description: error.response?.data.message || error.message,
					duration: 6000,
					placement: 'top-right',
					theme: 'dark',
					showProgress: true,
					type: 'error'
				});

				goto(resolve('/auth/login'));
			});
	});
</script>

<div>VK</div>