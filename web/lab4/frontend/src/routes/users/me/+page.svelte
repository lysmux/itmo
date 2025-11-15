<script lang="ts">
	import { page } from '$app/state';
	import type { User } from '$lib/api/users.ts';
	import { onMount } from 'svelte';
	import axios from 'axios';
	import { toasts } from 'svelte-toasts';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';

	let user = $state<User | null>(null);

	onMount(() => {
		axios.get<User>('/api/users/me', {
			headers: {
				'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
			}
		})
			.then((response) => response.data)
			.then(response => {
				user = response;
			})
			.catch(error => {
				toasts.add({
					title: `API ERROR | ${error.status}`,
					description: error.response?.data.message || error.message,
					duration: 6000,
					placement: 'top-right',
					theme: 'dark',
					showProgress: true,
					type: 'error'
				});
				goto(resolve("/auth/login"));
			});
	});
</script>


<div>
	{#if (user)}
		<p>ID: {user.id}</p>
		<p>Username: {user.username}</p>
	{/if}
</div>