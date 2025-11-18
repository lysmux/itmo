<script lang="ts">
	import type { Snippet } from 'svelte';

	interface Props {
		head: Snippet,
		body?: Snippet,
		pagination?: Snippet,
	}

	const {
		head,
		body,
		pagination
	}: Props = $props();
</script>

<div class="wrapper">
	<div class="container">
		<table class="table">
			<thead>
			{@render head()}
			</thead>
			<tbody>
			{#if body}
				{@render body()}
			{/if}
			</tbody>
		</table>
	</div>

	{#if pagination}
		{@render pagination()}
	{/if}
</div>

<style lang="scss">
  .wrapper {
    background: #16151b;
    width: 100%;
    height: 100%;
  }

  .container {
    height: calc((100% - 60px)); // поменять на перменные. Тут из-за pagination padding
    overflow-y: auto;
    border-radius: 10px;

    &::-webkit-scrollbar {
      width: 8px;
    }

    &::-webkit-scrollbar-track {
      background: #f1f1f1;
      border-radius: 4px;
    }

    &::-webkit-scrollbar-thumb {
      background: #c1c1c1;
      border-radius: 4px;
    }

    &::-webkit-scrollbar-thumb:hover {
      background: #a8a8a8;
    }
  }

  .table {
    table-layout: fixed;
    border-collapse: collapse;
    width: 100%;

    :global(td), :global(th) {
      padding: 10px;
      text-align: center;
      vertical-align: middle;
      height: 50px;
    }

    thead {
      font-weight: bold;
      font-size: 1.2rem;

      position: sticky;
      top: 0;

      background: #2c2b2b;
      color: #ffffff;

      :global(td) {
        height: 70px;
        border-bottom: 2px solid #a5b9c5;
      }
    }

    tbody {
      :global(tr:not(:last-child) td) {
        border-bottom: 1px solid #a5b9c5;
      }

      :global(tr) {
        background: #16151b;
        color: #ffffff;

        &:hover {
          background: #ffffff;
          color: #000000;
        }
      }
    }
  }
</style>