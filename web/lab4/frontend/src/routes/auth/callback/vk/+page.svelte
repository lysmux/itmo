<script lang="ts">
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';
	import type { AuthResponse, VKCallbackRequest } from '$lib/api/users.ts';
	import apiClient, { setAccessTokenExpiresAt } from '$lib/api/api.ts';
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
			goto(resolve('/auth/login'));
		}

		const callbackData: VKCallbackRequest = {
			code: code as string,
			deviceId: deviceId as string,
			challengeVerifier: challengeVerifier as string
		};

		apiClient.post<AuthResponse>('/auth/callback/vk', callbackData)
			.then(response => response.data)
			.then(data => {
				setAccessTokenExpiresAt(data.expiresIn);
				goto(resolve('/'));
			})
			.catch(() => {
				toasts.add({
					title: `Ошибка входа`,
					description: 'Не удалось войти через ВКонтакте',
					placement: 'top-right',
					duration: 6000,
					showProgress: true,
					type: 'error'
				});
				goto(resolve('/auth/login'));
			});
	});
</script>

<div>Вход через ВКонтакте...</div>

<style lang="scss">
	div {
		color: white;
		font-size: 2em;
		font-weight: 700;
	}
</style>