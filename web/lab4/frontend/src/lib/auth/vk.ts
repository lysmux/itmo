import { generateRedirectUrl } from '$lib/utils/redirect.ts';

export function generatePkcePair() {
	const array = new Uint8Array(32);
	crypto.getRandomValues(array);
	let verifier = btoa(String.fromCharCode(...array))
		.replace(/\+/g, '-')
		.replace(/\//g, '_')
		.replace(/=+$/, '');

	// Обрезаем до 128, если вдруг (маловероятно)
	if (verifier.length > 128) verifier = verifier.substring(0, 128);

	// 2. code_challenge = BASE64URL(SHA256(verifier))
	const encoder = new TextEncoder();
	const data = encoder.encode(verifier);
	return crypto.subtle.digest('SHA-256', data).then(hash => {
		const hashArray = Array.from(new Uint8Array(hash));
		const challenge = btoa(String.fromCharCode(...hashArray))
			.replace(/\+/g, '-')
			.replace(/\//g, '_')
			.replace(/=+$/, '');
		return { codeVerifier: verifier, codeChallenge: challenge };
	});
}

export const vkAuth = async () => {
	const state = crypto.randomUUID();
	const {codeVerifier, codeChallenge} = await generatePkcePair()

	sessionStorage.setItem("stateVerifier", state);
	sessionStorage.setItem("challengeVerifier", codeVerifier);

	const params = {
		response_type: "code",
		client_id: 54324524,
		redirect_uri: "https://tunnel.lysmux.dev/auth/callback/vk",
		state: state,
		code_challenge: codeChallenge,
		code_challenge_method: "S256"
	}

	location.assign(generateRedirectUrl("https://id.vk.ru/authorize", params))
}