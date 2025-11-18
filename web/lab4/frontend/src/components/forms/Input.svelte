<script lang="ts">
	import type { HTMLInputAttributes } from 'svelte/elements';
	import Error from './Error.svelte';

	interface Props {
		error?: string | undefined;
	}

	let {
		value = $bindable(),
		error,
		...props
	}: HTMLInputAttributes & Props = $props();

	let touched = $state(false);
</script>


<input
	bind:value
	onblur={() => touched = true}
	{...props}
/>
{#if error && touched}
	<Error>{error}</Error>
{/if}

<style lang="scss">
  input {
    background: rgba(40, 55, 80, 0.4);
    color: white;

    &::placeholder {
      color: rgba(255, 255, 255, 0.5);
    }

    &:focus {
      background: rgba(45, 60, 85, 0.5);
      border-color: #6c5ce7;
    }

    padding: 18px 20px;
    width: 100%;
    border-radius: 12px;
    outline: none;
    font-size: 0.5em;
    border: 2px solid rgba(255, 255, 255, 0.15);

    &::-webkit-outer-spin-button,
    &::-webkit-inner-spin-button {
      -webkit-appearance: none;
      margin: 0;
    }
  }
</style>