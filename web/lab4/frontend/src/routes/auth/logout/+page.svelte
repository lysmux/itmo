<script lang="ts">
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';
	import axios from 'axios';
	import type { AuthResponse } from '$lib/api/users.ts';
	import { toasts } from 'svelte-toasts';

	onMount(() => {
		axios.post<AuthResponse>('/api/auth/logout')
			.then((response) => response.data)
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
			});
		goto(resolve('/auth/login'));
	});
</script>

<div>Logout</div>