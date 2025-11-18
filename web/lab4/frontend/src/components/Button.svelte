<script lang="ts">
	import type { Snippet } from 'svelte';
	import type { HTMLButtonAttributes } from 'svelte/elements';

	type Variant = 'action' | 'danger';
	type Size = 'sm' | 'md' | 'lg';

	interface Props {
		loading?: boolean;
		disabled?: boolean;
		variant?: Variant;
		size?: Size;
		children: Snippet;
	}

	const {
		children,
		loading = false,
		disabled = false,
		variant = 'action',
		size = 'md',
		...rest
	}: Props & HTMLButtonAttributes = $props();
</script>

<button
	class="{variant} {size}"
	disabled={disabled || loading}
	{...rest}
>
	<span style="visibility: {loading ? 'hidden' : 'visible'}">
		{@render children()}
	</span>

	{#if loading}
		<svg class="spinner" viewBox="0 0 24 24">
			<circle
				cx="12"
				cy="12"
				r="10"
				stroke="currentColor"
				stroke-width="3"
				fill="none"
				stroke-dasharray="15, 45"
				stroke-dashoffset="0"
			/>
		</svg>
	{/if}
</button>

<style lang="scss">
  button {
    cursor: pointer;
    border-radius: 12px;
    border: none;
    transition: all 0.3s ease;
		position: relative;

		span {
      display: inline-flex;
      gap: 0.5rem;
      align-items: center;
    }

    &:disabled {
      opacity: 0.5;

      &:hover {
        cursor: not-allowed;
      }
    }

    :global(svg) {
      width: 2em;
      height: 2em;
      flex-shrink: 0;
    }
  }

  .action {
    color: white;

    :global(svg path) {
      fill: white;
    }

    background: linear-gradient(145deg, #8c7ae6, #6c5ce7);
    box-shadow: 0 10px 25px rgba(108, 92, 231, 0.3);
  }

  .danger {
    color: white;

    :global(svg path) {
      fill: white;
    }

    background: linear-gradient(145deg, #e84118, #c23616);
    box-shadow: 0 10px 25px rgba(194, 54, 22, 0.3);
  }

  .sm {
    padding: 0.25em 0.75em;
    font-size: 0.875em;
  }

  .md {
    padding: 0.5em 1em;
    font-size: 1em;
  }

  .lg {
    padding: 0.75em 1.5em;
    font-size: 1.125em;
  }

  .spinner {
    width: 1.5em;
    height: 1.5em;
    animation: rotate 1s linear infinite;
		position: absolute;
    inset: 0;
    margin: auto;
  }

  @keyframes rotate {
    to { transform: rotate(360deg); }
  }
</style>