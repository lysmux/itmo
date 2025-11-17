<script lang="ts">

	import type { AuthResponse, LoginRequest } from '$lib/api/users.ts';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';
	import axios from 'axios';
	import { toasts } from 'svelte-toasts';
	import { z } from 'zod';
	import VKIcon from '../../../components/icons/VKIcon.svelte';
	import { generateRedirectUrl } from '$lib/redirect.ts';
	import PassKeyIcon from '../../../components/icons/PassKeyIcon.svelte';
	import { vkAuth } from '$lib/auth/vk.ts';
	import { passKeyAuth } from '$lib/auth/passkey.ts';

	let touched = $state({
		username: false,
		password: false
	});
	const loginData: LoginRequest = $state({
		username: "",
		password: "",
	});
	const schema = z.object({
		username: z.string().min(1, "Поля обязательно"),
		password: z.string().min(1, "Поля обязательно")
	});
	const parsed = $derived.by(() => {
		return schema.safeParse({
			username: loginData.username,
			password: loginData.password
		});
	});
	const isValid = $derived(parsed.success);
	const errors = $derived(parsed.success ? {} : parsed.error.flatten().fieldErrors);

	const login = async (event: Event) => {
		event.preventDefault();
		if (!isValid) return;

		axios.post<AuthResponse>('/api/auth/login', {
			username: loginData.username,
			password: loginData.password
		}).then(response => {
			localStorage.setItem('accessTokenExpireIn', String(response.data.expiresIn));
			goto(resolve('/users/me'));
		}).catch(error => {
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
	};
</script>

<div class="container">
	<h1>Вход</h1>
	<p class="has-account">Нет аккаунта? <a href={resolve("/auth/register")}>Зарегистрироваться</a></p>

	<form onsubmit={login}>
		<div class="block">
			<label for="username">Имя пользователя</label>
			<input
				bind:value={loginData.username}
				class="input"
				type="text"
				onblur={() => {touched.username = true}}
			/>
			{#if errors.username && touched.username}
				<p class="error">{errors.username[0]}</p>
			{/if}
		</div>
		<div class="block">
			<label for="password">Пароль</label>
			<input
				bind:value={loginData.password}
				class="input"
				type="password"
				onblur={() => {touched.password = true}}
			/>
			{#if errors.password && touched.password}
				<p class="error">{errors.password[0]}</p>
			{/if}
		</div>
		<div class="buttons-block">
			<button class="action" type="submit" disabled={!isValid}>Войти</button>
		</div>
	</form>

	<div class="deriver">
		<p>или с помощью</p>
	</div>

	<div class="alternative">
		<button onclick={(e) => {e.preventDefault(); vkAuth()}}>
			<VKIcon/>
			ВКонтакте
		</button>
		<button onclick={(e) => {e.preventDefault(); passKeyAuth()}}>
			<PassKeyIcon/>
			PassKey
		</button>
	</div>
</div>

<style lang="scss">
  .container {
    max-width: 700px;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 10px;
  }

  .alternative {
    display: flex;
    align-items: center;
    gap: 10px;

    button {
      display: inline-flex;
      align-items: center;
      gap: 0.5rem;
      border: 1px solid black;
      border-radius: 15px;
      padding: 10px;

      :global(svg) {
        width: 2em;
        height: 2em;
        flex-shrink: 0;
      }
    }
  }

  .has-account {
    color: gray;
    font-size: 0.8em;

    a {
      text-decoration: none;
      color: #6c5ce7;
      font-weight: 600;
    }
  }

  .deriver {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 24px 0;
    width: 100%;

    p {
      padding: 0 8px;
      color: white;
      z-index: 1;
    }

    &::after {
      content: "";
      position: absolute;
      top: 50%;
      transform: translateY(-50%);
      left: 0;
      width: 100%;
      height: 1px;
      background: gray;
    }
  }

  h1 {
    font-size: 2rem;
    font-weight: 600;
    color: white;
  }

  form {
    display: flex;
    flex-direction: column;
    font-size: 1em;
    gap: 20px;
    position: relative;
    width: 100%;
  }

  .block {
    label {
      color: #6c5ce7;
      display: block;
      text-align: center;
      font-size: 1em;
      font-weight: 700;
      margin-bottom: 10px;
    }
  }

  .buttons-block {
    display: flex;
    justify-content: center;
  }

  button {
    cursor: pointer;
    border-radius: 12px;
    border: none;
    font-weight: 700;
    font-size: 18px;
    padding: 16px 20px;
    transition: all 0.3s ease;

    &:disabled {
      opacity: 0.5;

      &:hover {
        cursor: not-allowed;
      }

      &:active {
        pointer-events: none;
      }
    }
  }

  .action {
    color: white;
    background: linear-gradient(145deg, #8c7ae6, #6c5ce7);
    box-shadow: 0 10px 25px rgba(108, 92, 231, 0.3);
  }

  .danger {
    color: white;
    background: linear-gradient(145deg, #e84118, #c23616);
    box-shadow: 0 10px 25px rgba(194, 54, 22, 0.3);
  }

  .input {
    background: rgba(40, 55, 80, 0.4);
    color: white;

    &::placeholder {
      color: rgba(255, 255, 255, 0.5);
    }

    &:focus {
      background: rgba(45, 60, 85, 0.5);
      border-color: #6c5ce7;
    }

    padding: 18px 20px;
    width: 100%;
    border-radius: 12px;
    outline: none;
    font-size: 0.5em;
    border: 2px solid rgba(255, 255, 255, 0.15);

    &::-webkit-outer-spin-button,
    &::-webkit-inner-spin-button {
      -webkit-appearance: none;
      margin: 0;
    }
  }

  .error {
    color: #c23616;
    font-size: 0.6em;
  }
</style>