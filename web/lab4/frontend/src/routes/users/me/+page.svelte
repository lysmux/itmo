<script lang="ts">
	import { passKeyRegister } from '$lib/auth/passkey.ts';
	import Group from '../../../components/layout/Group.svelte';
	import Button from '../../../components/Button.svelte';
	import Skeleton from '../../../components/Skeleton.svelte';
	import type { PageProps } from './$types';
	import { logout } from '$lib/api/auth.ts';

	let { data }: PageProps = $props();
</script>

<div class="container">
	<div class="user-info">
		{#await data.user}
			<Skeleton>
				<div class="wrapper lines">
					<div></div>
					<div></div>
				</div>
			</Skeleton>
		{:then user}
			<Group direction="column">
				<p>ID: <span>{user.id}</span></p>
				<p>Имя пользователя: <span>{user.username}</span></p>
			</Group>
		{/await}
	</div>

	<div class="actions">
		<Button variant="danger" onclick={logout}>Выйти</Button>
		<Button onclick={() => passKeyRegister()}>Добавить PassKey</Button>
	</div>
</div>

<style>
    .container {
        max-width: 1200px;
        width: 100%;
        align-items: center;
        background-color: var(--cardColor);
        border-radius: 16px;
        padding: 32px 24px;
        box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);

        display: grid;
				grid-template-columns: 1fr 1fr;
				gap: 40px;

				@media screen and (max-width: 800px) {
						grid-template-columns: 1fr;
        }
    }

    .actions {
        display: flex;
        flex-direction: column;
        gap: 20px;
        align-items: center;
    }

    .user-info p {
        color: white;
        font-weight: 600;

        span {
            font-weight: 400;
        }
    }

    .user-info, .lines {
        display: flex;
        flex-direction: column;
        gap: 20px;
    }

    .lines > * {
        height: 1em;
        width: 320px;
        border-radius: 4px;

        &:last-child {
            width: 240px;
        }
    }
</style>