<script lang="ts">
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';
	import { z } from 'zod';
	import apiClient, { setAccessTokenExpiresAt } from '$lib/api/api.ts';
	import { toasts } from 'svelte-toasts';
	import { vkAuth } from '$lib/auth/vk.ts';
	import { passKeyAuth } from '$lib/auth/passkey.ts';
	import type { AuthResponse, LoginRequest } from '$lib/api/users.ts';

	import VKIcon from '../../../components/icons/VKIcon.svelte';
	import PassKeyIcon from '../../../components/icons/PassKeyIcon.svelte';

	import FormField from '../../../components/forms/FormField.svelte';
	import Input from '../../../components/forms/Input.svelte';
	import Button from '../../../components/Button.svelte';
	import AuthForm from '../../../components/AuthForm.svelte';

	const loginData: LoginRequest = $state({
		username: '',
		password: ''
	});
	const schema = z.object({
		username: z.string().min(1, 'Поле обязательно'),
		password: z.string().min(1, 'Поле обязательно')
	});
	const parsed = $derived.by(() => {
		return schema.safeParse(loginData);
	});
	const isValid = $derived(parsed.success);
	const errors = $derived(parsed.success ? {} : parsed.error.flatten().fieldErrors);

	let loading = $state(false);

	const login = () => {
		if (!isValid) return;
		loading = true;

		apiClient.post<AuthResponse>('/auth/password/login', loginData)
			.then(response => response.data)
			.then(data => {
				setAccessTokenExpiresAt(data.expiresIn);
				goto(resolve('/'));
			})
			.catch(() => {
				toasts.add({
					title: `Ошибка аутентификации`,
					description: 'Логин или пароль неверны',
					placement: 'top-right',
					duration: 6000,
					showProgress: true,
					type: 'error'
				});
			})
			.finally(() => {
				loading = false;
			});
	};
</script>


<AuthForm onSubmit={login}>
	{#snippet title()}Вход{/snippet}
	{#snippet fields()}
		<FormField label="Имя пользователя" id="username">
			<Input
				id="username"
				name="username"
				bind:value={loginData.username}
				error={errors.username?.[0]}
			/>
		</FormField>

		<FormField label="Пароль" id="password">
			<Input
				id="password"
				name="password"
				type="password"
				bind:value={loginData.password}
				error={errors.password?.[0]}
			/>
		</FormField>
	{/snippet}

	{#snippet buttons()}
		<Button
			loading={loading}
			disabled={!isValid}
			type="submit"
			variant="action"
		>
			Войти
		</Button>
	{/snippet}

	{#snippet alternatives()}
		<Button onclick={vkAuth} size="sm">
			<VKIcon />
			ВКонтакте
		</Button>

		<Button onclick={passKeyAuth} size="sm">
			<PassKeyIcon />
			PassKey
		</Button>
	{/snippet}

	{#snippet changeAction()}
		Нет аккаунта? <a href={resolve("/auth/register")}>Зарегистрироваться</a>
	{/snippet}
</AuthForm>
