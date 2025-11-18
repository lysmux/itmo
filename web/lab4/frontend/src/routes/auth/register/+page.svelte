<script lang="ts">
	import { resolve } from '$app/paths';
	import { goto } from '$app/navigation';
	import { z } from 'zod';
	import apiClient, { setAccessTokenExpiresAt } from '$lib/api/api.ts';
	import type { AuthResponse, RegisterRequest } from '$lib/api/users.ts';
	import { vkAuth } from '$lib/auth/vk.ts';

	import { toasts } from 'svelte-toasts';
	import VKIcon from '../../../components/icons/VKIcon.svelte';
	import AuthForm from '../../../components/AuthForm.svelte';
	import FormField from '../../../components/forms/FormField.svelte';
	import Input from '../../../components/forms/Input.svelte';
	import Button from '../../../components/Button.svelte';

	const registerData: RegisterRequest = $state({
		username: '',
		password: '',
		confirmPassword: ''
	});
	const schema = z.object({
		username: z.string()
			.min(1, 'Поля обязательно')
			.min(4, 'Имя пользователя должно быть не короче 4 символов'),
		password: z.string()
			.min(1, 'Поля обязательно')
			.min(6, 'Пароль должен быть не короче 6 символов'),
		confirmPassword: z.string().min(1, 'Поля обязательно')
	}).refine(
		(data) => data.password === data.confirmPassword,
		{
			message: 'Пароли должны совпадать',
			path: ['confirmPassword']
		}
	);

	const parsed = $derived.by(() => {
		return schema.safeParse(registerData);
	});
	const isValid = $derived(parsed.success);
	const errors = $derived(parsed.success ? {} : parsed.error.flatten().fieldErrors);

	let loading = $state(false);

	const register = () => {
		if (!isValid) return;
		loading = true;

		apiClient.post<AuthResponse>('/auth/password/register', {
			username: registerData.username,
			password: registerData.password,
		})
			.then(response => response.data)
			.then(data => {
				setAccessTokenExpiresAt(data.expiresIn);
				goto(resolve('/'));
			})
			.catch(() => {
				toasts.add({
					title: `Ошибка регистрации`,
					description: 'Пользователь уже существует',
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

<AuthForm onSubmit={register}>
	{#snippet title()}Регистрация{/snippet}
	{#snippet fields()}
		<FormField label="Имя пользователя" id="username">
			<Input
				id="username"
				bind:value={registerData.username}
				error={errors.username?.[0]}
			/>
		</FormField>
		<FormField label="Пароль" id="password">
			<Input
				id="password"
				bind:value={registerData.password}
				type="password"
				error={errors.password?.[0]}
			/>
		</FormField>
		<FormField label="Повтор пароля" id="confirm-password">
			<Input
				id="confirm-password"
				bind:value={registerData.confirmPassword}
				type="password"
				error={errors.confirmPassword?.[0]}
			/>
		</FormField>
	{/snippet}

	{#snippet buttons()}
		<Button
			loading={loading}
			disabled={!isValid}
			type="submit"
			variant="action"
		>Зарегистрироваться
		</Button>
	{/snippet}

	{#snippet alternatives()}
		<Button onclick={vkAuth} size="sm">
			<VKIcon />
			ВКонтакте
		</Button>
	{/snippet}

	{#snippet changeAction()}
		Есть аккаунт? <a href={resolve("/auth/login")}>Войти</a>
	{/snippet}
</AuthForm>
