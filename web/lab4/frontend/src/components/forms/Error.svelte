<script lang="ts">
	import type { Snippet } from 'svelte';

	interface Props {
		children: Snippet;
	}

	const { children }: Props = $props();
</script>

<p class="error">{@render children()}</p>

<style lang="scss">
	.error {
    display: inline-block;
    margin-top: 3px;
    color: #c23616;
    font-size: 0.7em;
	}
</style>