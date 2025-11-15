export function generateRedirectUrl(
	baseUrl: string,
	params: Record<string, string | number | boolean>
): string {
	const url = new URL(baseUrl, window.location.origin);

	Object.entries(params).forEach(([key, value]) => {
		if (value !== null && value !== undefined) {
			url.searchParams.set(key, String(value));
		}
	});

	return url.toString();
}