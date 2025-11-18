<script>
	import Dropdown from './Dropdown.svelte';
	import { resolve } from '$app/paths';
	import { logout } from '$lib/api/auth.js';
</script>

<header>
	<div class="heading">
		<h1 class="title">Разыграев Кирилл Сергеевич</h1>
		<h3 class="subtitle">Группа: P3215</h3>
		<h3 class="subtitle">Вариант: 223944</h3>
	</div>

	<Dropdown>
		{#snippet toggle()}▼{/snippet}

		{#snippet items()}
			<li><a href={resolve("/")}>График</a></li>
			<li><a href={resolve("/users/me")}>Профиль</a></li>
			<li><a href="#" onclick={logout}>Выйти</a></li>
		{/snippet}
	</Dropdown>
</header>

<style lang="scss">
  header {
    background: linear-gradient(135deg, rgba(124, 58, 237, 0.18), rgba(6, 182, 212, 0.12));
    text-align: center;
    padding: 10px 32px;
    position: relative;
    display: flex;
    align-items: center;
  }

  .heading {
    flex: 1;
  }

  .title {
    color: #e6eef8;
    font-weight: 700;
    font-size: 1em;
  }

  .subtitle {
    color: #ffffffcc;
    font-size: 0.8em;
    font-weight: 400;
  }
</style>