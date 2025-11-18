<!-- Skeleton.svelte -->
<script lang="ts">
	let { children } = $props()
</script>

<!--
@component

Shows a skeleton of the provided children. Each passed element will be rendered
as a skeleton, with a shimmer effect applied to it.

If you need to use additional elements in the skeleton snippet but don’t want
them to be rendered, add a `.wrapper` class to them.

Example usage:

```svelte
  <Skeleton>
    <div class="wrapper lines">
      <div></div>
      <div></div>
      <div></div>
    </div>
  </Skeleton>

  <style>
    .lines {
      display: flex;
      flex-direction: column;
      gap: 8px;
      > * {
        height: 16px;
        width: 160px;
        border-radius: 4px;
      }
    }
  </style>
```
-->

<div class="skeleton">{@render children()}</div>

<style>
    @keyframes skeleton-shimmer {
        0% {
            background-position-x: 200%;
        }
        100% {
            background-position-x: 0%;
        }
    }
    .skeleton {
        --_default-color: #e8e8e8;
        --_shimmer-color: #fff;

        display: contents;

        :global(:not(.wrapper)) {
            animation: skeleton-shimmer 2s ease-out infinite;
            background: linear-gradient(
                    100deg,
                    var(--_default-color),
                    var(--_default-color) 50%,
                    var(--_shimmer-color) 60%,
                    var(--_default-color) 70%
            );
            background-size: 200% 100%;
            background-attachment: fixed;
        }
    }
</style>
