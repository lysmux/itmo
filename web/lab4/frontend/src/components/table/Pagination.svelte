<script lang="ts">
	interface Props {
		currentPage: number;
		lastPage: number;
	}

	let {
		currentPage = $bindable(),
		lastPage
	}: Props = $props();

</script>

<div class="pagination">
	<button disabled={currentPage === 0} onclick={() => currentPage = 0}>&lt;&lt;</button>
	<button disabled={currentPage === 0} onclick={() => currentPage--}>&lt;</button>
	<button disabled>{currentPage + 1}</button>
	<button disabled={currentPage === lastPage} onclick={() => currentPage++}>&gt;</button>
	<button disabled={currentPage === lastPage} onclick={() => currentPage = lastPage}>&gt;&gt;</button>
</div>

<style lang="scss">
  .pagination {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-block: 10px;

    button {
      padding: 8px 16px;
      border-radius: 4px;
			border: none;
      font-size: 0.8em;

      background-color: #3a3a3a;
      color: #b0b0b0;

      &:hover {
        background-color: #5a5a5a;
      }

      &:disabled {
        color: #666;
        background-color: #1a1a1a;
        pointer-events: none;
      }

			&:nth-child(3) {
        background: linear-gradient(145deg, #707070, #505050);
        color: white;
        font-weight: 700;

        &:hover {
          background-color: #0056b3;
        }
      }
    }
  }
</style>