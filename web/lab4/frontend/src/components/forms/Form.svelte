<script lang="ts">
	import type { HTMLFormAttributes } from 'svelte/elements';
	import type { Snippet } from 'svelte';

	const {
		children,
		...rest
	}: HTMLFormAttributes & { children: Snippet} = $props();
</script>

<form {...rest}>
	{@render children()}
</form>

<style lang="scss">
  form {
    width: 100%;
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
</style>