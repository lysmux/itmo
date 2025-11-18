import { goto } from '$app/navigation';
import { resolve } from '$app/paths';
import apiClient, { setAccessTokenExpiresAt } from '$lib/api/api.ts';
import { toasts } from 'svelte-toasts';

export const passKeyAuth = async () => {
	const resp = await apiClient.get('/auth/passkey/login');
	const options = JSON.parse(resp.data.options);
	const challenge = base64urlToArrayBuffer(options.challenge);

	const publicKey: PublicKeyCredentialRequestOptions = {
		challenge: challenge,
		rpId: options.rpId,
		timeout: options.timeout || 60000,
		userVerification: options.userVerification
	};

	let credential;
	try {
		credential = (await navigator.credentials.get({
			publicKey: publicKey
		})) as PublicKeyCredential;
	} catch (err) {
		toasts.add({
			title: `Отменено`,
			description: 'Вход отменен',
			duration: 6000,
			placement: 'top-right',
			theme: 'dark',
			showProgress: true,
			type: 'warning'
		});
		return;
	}

	apiClient
		.post('/auth/passkey/login', {
			operationId: resp.data.operationId,
			loginResponseJSON: credential
		})
		.then((resp) => resp.data)
		.then((res) => {
			setAccessTokenExpiresAt(res.expireIn);
			goto(resolve('/users/me'));
		})
		.catch((err) => {
			toasts.add({
				title: `Не удалось выполнить вход`,
				description: 'Ключ недействителен',
				duration: 6000,
				placement: 'top-right',
				theme: 'dark',
				showProgress: true,
				type: 'error'
			});
		});
};

export const passKeyRegister = async () => {
	const resp = await apiClient.get('/auth/passkey/register', { requireAuth: true });

	if (resp.status !== 200) {
		toasts.add({
			title: `Отменено`,
			description: 'Не удалось создать ключ',
			duration: 6000,
			placement: 'top-right',
			theme: 'dark',
			showProgress: true,
			type: 'warning'
		});
		return;
	}
	const options = resp.data;

	// 2. Преобразуем challenge и user.id из base64url → ArrayBuffer
	const challenge = base64urlToArrayBuffer(options.challenge);
	const userHandle = base64urlToArrayBuffer(options.user.id);

	// 3. Формируем объект для navigator.credentials.create()
	const publicKeyCredentialCreationOptions: PublicKeyCredentialCreationOptions = {
		rp: options.rp,
		user: {
			id: userHandle,
			name: options.user.name,
			displayName: options.user.displayName
		},
		challenge,
		pubKeyCredParams: options.pubKeyCredParams.map((p: any) => ({
			type: p.type,
			alg: p.alg
		})),
		timeout: options.timeout,
		excludeCredentials: options.excludeCredentials.map((p) => {
			return {
				type: p.type,
				id: base64urlToArrayBuffer(p.id)
			};
		}),
		attestation: options.attestation || 'none',
		authenticatorSelection: options.authenticatorSelection
	};

	// 4. Вызываем API
	let credential;
	try {
		credential = (await navigator.credentials.create({
			publicKey: publicKeyCredentialCreationOptions
		})) as PublicKeyCredential;
	} catch (err) {
		toasts.add({
			title: `Отменено`,
			description: 'Добавление ключа отменено',
			duration: 6000,
			placement: 'top-right',
			theme: 'dark',
			showProgress: true,
			type: 'warning'
		});
		return;
	}

	// 6. Отправляем на сервер
	apiClient
		.post('/auth/passkey/register', credential, { requireAuth: true })
		.then((resp) => resp.data)
		.then((res) => {
			toasts.add({
				title: `Успешно`,
				description: 'Ключ добавлен',
				duration: 6000,
				placement: 'top-right',
				theme: 'dark',
				showProgress: true,
				type: 'success'
			});
		})
		.catch((err) => {
			toasts.add({
				title: `Не удалось добавить ключ`,
				description: 'Не удалось добавить ключ',
				duration: 6000,
				placement: 'top-right',
				theme: 'dark',
				showProgress: true,
				type: 'error'
			});
		});
};

export function base64urlToArrayBuffer(base64url: string): ArrayBuffer {
	let base64 = base64url
		.replace(/-/g, '+') // '-' → '+'
		.replace(/_/g, '/'); // '_' → '/'

	const padding = base64.length % 4;
	if (padding === 2) {
		base64 += '==';
	} else if (padding === 3) {
		base64 += '=';
	}

	let binaryString: string;
	try {
		binaryString = atob(base64);
	} catch (e) {
		throw new Error(`Invalid base64 string: ${base64}`);
	}

	const len = binaryString.length;
	const bytes = new Uint8Array(len);
	for (let i = 0; i < len; i++) {
		bytes[i] = binaryString.charCodeAt(i);
	}

	return bytes.buffer;
}
