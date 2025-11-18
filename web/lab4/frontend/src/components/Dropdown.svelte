<script lang="ts">
	import type { Snippet } from 'svelte';

	let isOpen = $state(false);
	let triggerRef: HTMLDivElement;
	let menuRef: HTMLUListElement;

	interface Props {
		toggle: Snippet;
		items: Snippet;
	}

	const {
		toggle,
		items,
	}: Props = $props();

	$effect.pre(() => {
		const handleClickOutside = (e) => {
			if (triggerRef && !triggerRef.contains(e.target) && !menuRef?.contains(e.target)) {
				isOpen = false;
			}
		};

		document.addEventListener('click', handleClickOutside);
		return () => document.removeEventListener('click', handleClickOutside);
	});
</script>


<div class="dropdown" bind:this={triggerRef}>
	<button
		type="button"
		class="dropdown-toggle"
		onclick={() => (isOpen = !isOpen)}
	>
		{@render toggle()}

	</button>

	{#if isOpen}
		<ul class="dropdown-menu" bind:this={menuRef}>
			{@render items()}
		</ul>
	{/if}
</div>

<style lang="scss">
  .dropdown {
    position: relative;
  }

  .dropdown-toggle {
    background-color: rgba(124, 58, 237, 0.18);
    padding: 16px;
    border-radius: 50%;
    border: none;
    cursor: pointer;
    font-size: 0.7em;
    font-weight: 700;
    color: white;

    &:hover {
      background-color: rgba(123, 76, 204, 0.18);
    }
  }

  .dropdown-menu {
    position: absolute;
    top: 100%;
    right: 0;
    background: white;
    border-radius: 12px;
    border: 1px solid #ccc;
    list-style: none;
    min-width: 160px;
		padding: 8px 0;

    :global(li:not(:last-child)) {
      border-bottom: 1px solid #ccc;
    }

		:global(li) {
			padding: 4px 8px;
		}

		:global(li a) {
      text-decoration: none;
      color: #333;
		}

    :global(li:hover) {
      background-color: #f0f0f0;
    }
  }
</style>