<script lang="ts">
	import '../app.scss';
	import { page } from '$app/state';
	import favicon from '$lib/assets/favicon.svg';
	import Header from '../components/Header.svelte';
	import { FlatToast, ToastContainer } from 'svelte-toasts';

	let { children } = $props();
</script>

<svelte:head>
	<link rel="icon" href={favicon} />
</svelte:head>

<div class="container">
	{#if !page.url.pathname.startsWith('/auth')}
		<Header />
	{/if}
	<ToastContainer let:data={data}>
		<FlatToast {data} />
	</ToastContainer>
	<main>
		{@render children()}
	</main>
</div>

<style lang="scss">
  .container {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
  }

  main {
    width: 95%;
    margin-inline: auto;
    margin-top: 20px;

    flex-grow: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
</style>