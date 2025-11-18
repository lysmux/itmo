<script lang="ts">
	import Label from './Label.svelte';
	import type { Snippet } from 'svelte';

	const {
		id,
		children,
		label
	}: {
		id?: string;
		label?: string;
		children: Snippet,
	} = $props();
</script>

<div>
	{#if label}
		<Label for={id}>{label}</Label>
	{/if}

	{@render children()}
</div>
