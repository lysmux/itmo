<script lang="ts">
	import type { Snippet } from 'svelte';
	import type { HTMLBaseAttributes } from 'svelte/elements';

	type Direction = 'row' | 'column';
	type Gap = 'sm' | 'md' | 'lg';
	type JustifyContent =
		| 'flex-start'
		| 'flex-end'
		| 'center'
		| 'space-between'
		| 'space-around'
		| 'space-evenly';
	type AlignItems =
		| 'stretch'
		| 'flex-start'
		| 'flex-end'
		| 'center'
		| 'baseline';

	interface Props {
		direction?: Direction;
		gap?: Gap;
		justifyContent?: JustifyContent;
		alignItems?: AlignItems;
		children: Snippet;
	}

	const {
		children,
		direction = 'row',
		gap = 'md',
		justifyContent = 'flex-start',
		alignItems = 'stretch',
		...rest
	}: Props & HTMLBaseAttributes = $props();
</script>

<div
	class="group {direction} gap--{gap}"
	style:justify-content={justifyContent}
	style:align-items={alignItems}
	{...rest}
>
	{@render children()}
</div>

<style lang="scss">
  .group {
    display: flex;
		width: 100%;
  }

  .row {
    flex-direction: row
  }

  .column {
    flex-direction: column;
  }

	.gap--sm {gap: 15px;}
	.gap--md {gap: 20px;}
	.gap--lg {gap: 25px;}

  .align-x--start{justify-content: flex-start;}
  .align-x--center {justify-content: center;}
  .align-x--end {justify-content: flex-end;}

  .align-y--start {align-items: flex-start;}
  .align-y--center {align-items: center;}
  .align-y--end {align-items: flex-end;}
</style>