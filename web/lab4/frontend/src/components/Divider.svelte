<script lang="ts">
	import type { Snippet } from 'svelte';

	interface Props {
		children?: Snippet;
	}

	const { children }: Props = $props();
</script>

<div class="divider">
	{#if children}
		<span>{@render children()}</span>
	{/if}
</div>

<style lang="scss">
  .divider {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 12px 0;
		width: 100%;

    &::after {
      content: "";
      position: absolute;
      transform: translateY(-50%);
      width: 100%;
      height: 2px;
      background: #1b1f26;
    }

    span {
      color: #909eab;
			font-size: 0.8em;
      padding: 0 8px;
      background: var(--cardColor);
      z-index: 1;
    }
  }
</style>