<script lang="ts">
	import type { Snippet } from 'svelte';
	import Divider from './Divider.svelte';

	interface Props {
		title: Snippet;
		fields: Snippet;
		buttons: Snippet;
		changeAction?: Snippet;
		alternatives?: Snippet;
		onSubmit?: () => void;
	}

	const {
		title,
		fields,
		buttons,
		changeAction,
		alternatives,
		onSubmit
	}: Props = $props();

</script>


<div class="container">
	<h1 class="title">{@render title()}</h1>

	<form onsubmit={e => {e.preventDefault(); onSubmit?.()}}>
		<div class="fields">{@render fields()}</div>
		<div class="buttons">{@render buttons()}</div>
	</form>

	{#if alternatives}
		<Divider>или</Divider>

		<div class="alternatives">{@render alternatives()}</div>
	{/if}

	{#if changeAction}
		<div class="change-action">{@render changeAction()}</div>
	{/if}
</div>

<style lang="scss">
  .container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    max-width: 700px;
    width: 100%;
    gap: 20px;
    background-color: var(--cardColor);
    border-radius: 16px;
    padding: 32px 24px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  }

  h1 {
    font-size: 2.5rem;
    font-weight: 600;
    color: white;
  }

	form {
		width: 100%;
	}

  .fields {
    display: flex;
    flex-direction: column;
    gap: 30px;
    margin-top: 32px;
  }

  .buttons {
    display: flex;
    flex-direction: column;
		align-items: center;
    gap: 20px;
    margin-top: 32px;
  }

 	.alternatives {
		display: flex;
    flex-wrap: wrap;
    justify-content: center;
		gap: 35px;
	}

  .change-action {
    margin-top: 12px;
    color: gray;
    font-size: 0.8em;
    text-align: center;

    :global(a) {
      text-decoration: none;
      color: #6c5ce7;
      font-weight: 600;
    }
  }
</style>