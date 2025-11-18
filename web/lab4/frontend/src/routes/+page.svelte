<script lang="ts">
	import { z } from 'zod';
	import { toasts } from 'svelte-toasts';
	import apiClient from '$lib/api/api.ts';
	import type { Coordinates, HitResult } from '$lib/api/hit.ts';
	import type { PageProps } from './$types';
	import { range } from '$lib/utils/utils.ts';

	import FormField from '../components/forms/FormField.svelte';
	import Button from '../components/Button.svelte';
	import Plot from '../components/Plot.svelte';
	import Table from '../components/table/Table.svelte';
	import Input from '../components/forms/Input.svelte';
	import Row from '../components/table/Row.svelte';
	import Cell from '../components/table/Cell.svelte';
	import Pagination from '../components/table/Pagination.svelte';
	import Error from '../components/forms/Error.svelte';

	let { data }: PageProps = $props();
	let hitResults: HitResult[] = $state([]);
	data.hitResults.then(results => hitResults = results);

	const coordinatesData: Partial<Coordinates> = $state({});
	const schema = z.object({
		x: z.number('Выберите X')
			.min(-5, 'Должен быть \u2265 -5')
			.max(3, 'Должен быть \u2264 3'),
		y: z.preprocess(val => {
				if (typeof val !== 'string') return val;
				return parseFloat(val.replace(/,/g, '.'));
			},
			z.coerce.number('Выберите Y')
				.min(-3, 'Должен быть \u2265 -3')
				.max(5, 'Должен быть \u2264 5')
		),
		r: z.int('Выберите R')
			.min(1, 'Должен быть \u2265 1')
			.max(3, 'Должен быть \u2264 3')
	});
	const parsed = $derived.by(() => {
		return schema.safeParse(coordinatesData);
	});
	const isValid = $derived(parsed.success);
	const errors = $derived(parsed.success ? {} : parsed.error.flatten().fieldErrors);

	let isLoading = $state(false);
	let perPage = $state(10);
	let currentPage = $state(0);

	const start = $derived(currentPage * perPage);
	const end = $derived(Math.min(start + perPage, hitResults.length));
	const slice = $derived(hitResults.slice(start, end));
	const lastPage = $derived(Math.max(Math.ceil(hitResults.length / perPage) - 1, 0));

	$effect(() => {
		if (currentPage > lastPage) currentPage = 0;
	})

	const checkHit = async () => {
		if (!isValid) return;
		isLoading = true;

		apiClient.post<HitResult>('/hits/check', coordinatesData)
			.then(response => response.data)
			.then(data => hitResults.push(data))
			.then(() => currentPage = lastPage)
			.finally(() => isLoading = false);
	};

	const checkHitFromPlot = (x: number, y: number) => {
		if (coordinatesData.r === undefined || errors.r !== undefined) {
			toasts.add({
				title: `Невозможно обработать клик`,
				description: 'Выберите радиус',
				placement: 'top-right',
				duration: 6000,
				showProgress: true,
				type: 'error'
			});
			return;
		}

		apiClient.post<HitResult>('/hits/check', {
			x: x,
			y: y,
			r: coordinatesData.r
		})
			.then(response => response.data)
			.then(data => hitResults.push(data))
			.then(() => currentPage = lastPage);
	};

	const clearResults = async () => {
		apiClient.post<HitResult>('/hits/clear')
			.then(() => hitResults.length = 0);
	};
</script>
<div class="container">
	<div class="block">
		<Plot onClick={checkHitFromPlot} hitResults={slice} radius={coordinatesData.r} />
	</div>
	<div class="block">
		<form class="form" onsubmit={e => {e.preventDefault(); checkHit();}}>
			<div class="fields">
				<div class="form-block">
					<FormField label="X">
						<div class="group">
							{#each range(-5, 3) as x (x)}
								<button
									type="button"
									class={["button", {selected: coordinatesData.x === x}]}
									onclick={() => coordinatesData.x=x}>{x}
								</button>
							{/each}
						</div>
						{#if errors.x}
							<Error>{errors.x?.[0]}</Error>
						{/if}
					</FormField>
				</div>

				<div class="form-block">
					<FormField label="R">
						<div class="group">
							{#each range(1, 3) as r (r)}
								<button
									type="button"
									class={["button", {selected: coordinatesData.r === r}]}
									onclick={() => coordinatesData.r=r}
								>{r}</button>
							{/each}
						</div>
						{#if errors.r}
							<Error>{errors.r?.[0]}</Error>
						{/if}
					</FormField>
				</div>

				<div class="form-block wide">
					<FormField id="y" label="Y">
						<Input
							id="y"
							error={errors.y?.[0]}
							maxlength={5}
							bind:value={coordinatesData.y}
						/>
					</FormField>
				</div>
			</div>

			<div class="buttons">
				<Button
					loading={isLoading}
					disabled={!isValid}
					type="submit"
					variant="action"
				>
					Проверить
				</Button>
				<Button
					type="button"
					variant="danger"
					onclick={clearResults}
				>
					Очистить результаты
				</Button>
			</div>
		</form>
	</div>
	<div class="block wide table">
		<Table>
			{#snippet head()}
				<Row>
					<Cell>Время</Cell>
					<Cell>X</Cell>
					<Cell>Y</Cell>
					<Cell>R</Cell>
					<Cell>Попадание</Cell>
					<Cell>Время выполнения</Cell>
				</Row>
			{/snippet}

			{#snippet body()}
				{#each slice as result (result.id)}
					<Row>
						<Cell>{new Date(result.time).toLocaleTimeString()}</Cell>
						<Cell>{result.x}</Cell>
						<Cell>{result.y}</Cell>
						<Cell>{result.r}</Cell>
						<Cell>{result.hit ? "Да" : "Нет"}</Cell>
						<Cell>{result.executionTime} нс</Cell>
					</Row>
				{/each}
			{/snippet}

			{#snippet pagination()}
				<Pagination bind:currentPage={currentPage} lastPage={lastPage} />
			{/snippet}
		</Table>
	</div>
</div>

<style lang="scss">
  .container {
    width: 100%;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 40px;
		
		@media screen and (max-width: 800px) {
      grid-template-columns: 1fr;
    }
  }

  .wide {
    grid-column-start: 1;
    grid-column-end: -1;
  }

  .block {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 100%;
    gap: 20px;
    background-color: var(--cardColor);
    border-radius: 16px;
    padding: 12px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  }

  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 24px;
    gap: 20px;
    width: 100%;

    .fields {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 20px;
      width: 100%;
    }

    .buttons {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 20px;
    }
  }

  .table {height: 450px;}

  .group {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 10px;
    width: 100%;
  }

  .button {
    width: 100%;
    height: 50px;
    position: relative;
    border: 2px solid rgba(255, 255, 255, 0.15);
    padding: 4px 11px;
    cursor: pointer;
    border-radius: 12px;
    transition: transform 0.3s ease,
    border-color 0.3s ease;
    font-size: 0.8em;

    &:hover {
      transform: translateY(-1px);
    }

    background: rgba(40, 55, 80, 0.4);
    color: white;

    &:hover {
      border-color: rgba(45, 60, 85, 0.5);
    }

    &.selected {
      background: linear-gradient(145deg, #3a2b9c, #5a4db9);
      border-color: #3a2b9c;
    }
  }
</style>