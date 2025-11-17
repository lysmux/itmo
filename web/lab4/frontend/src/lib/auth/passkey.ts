import axios from 'axios';
import { goto } from '$app/navigation';
import { resolve } from '$app/paths';

export const passKeyAuth = async () =>{
	const resp = await axios.get('/api/auth/passkey/login');
	const options = JSON.parse(resp.data.options);
	const challenge = base64urlToArrayBuffer(options.challenge);

	const publicKey: PublicKeyCredentialRequestOptions = {
		challenge: challenge,
		rpId: options.rpId,
		timeout: options.timeout || 60000,
		userVerification: options.userVerification || 'preferred', // 'required' / 'discouraged'
	};

	const credential = await navigator.credentials.get({
		publicKey: publicKey
	}) as PublicKeyCredential;

	axios.post('/api/auth/passkey/login', {
		operationId: resp.data.operationId,
		loginResponseJSON: credential,
	})
		.then(resp => resp.data)
		.then(res => {
			localStorage.setItem('accessTokenExpireIn', String(res.expiresIn));
			goto(resolve('/users/me'));
		})


}

export const passKeyRegister = async () =>{
	const resp = await axios.get('/api/auth/passkey/register');


	if (resp.status !== 200) throw new Error('Failed to get registration options');
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
		excludeCredentials: options.excludeCredentials.map(p => {
			return {
				type: p.type,
				id: base64urlToArrayBuffer(p.id),
			}
		}),
		attestation: options.attestation || 'none',
		authenticatorSelection: options.authenticatorSelection
	};

	// 4. Вызываем API
	const credential = await navigator.credentials.create({
		publicKey: publicKeyCredentialCreationOptions
	}) as PublicKeyCredential;

	if (!credential) throw new Error('Registration cancelled');

	// 6. Отправляем на сервер
	const verifyResp = await axios.post('/api/auth/passkey/register', credential);

	if (verifyResp.status !== 200) {
		throw new Error(verifyResp.data || 'Registration failed');
	}

	console.log('✅ Passkey registered!');
}

export function base64urlToArrayBuffer(base64url: string): ArrayBuffer {
	// Шаг 1: заменяем base64url → base64
	let base64 = base64url
		.replace(/-/g, '+')  // '-' → '+'
		.replace(/_/g, '/'); // '_' → '/'

	// Шаг 2: добавляем padding (=), если нужно
	const padding = base64.length % 4;
	if (padding === 2) {
		base64 += '==';
	} else if (padding === 3) {
		base64 += '=';
	}
	// padding === 0 → ничего не добавляем
	// padding === 1 — невалидно (но на практике не встречается)

	// Шаг 3: декодируем base64 → binary string → Uint8Array
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

/**
 * Конвертирует ArrayBuffer → base64url-строку (без padding)
 */
export function arrayBufferToBase64url(buffer: ArrayBuffer): string {
	const bytes = new Uint8Array(buffer);
	let binary = '';
	for (let i = 0; i < bytes.length; i++) {
		binary += String.fromCharCode(bytes[i]);
	}

	const base64 = btoa(binary);
	return base64
		.replace(/\+/g, '-')  // '+' → '-'
		.replace(/\//g, '_')  // '/' → '_'
		.replace(/=+$/g, ''); // удаляем '=' в конце
}