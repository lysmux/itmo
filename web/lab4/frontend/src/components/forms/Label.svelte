<script lang="ts">
	let { for: htmlFor, children } = $props();
</script>

<label for={htmlFor}>
	{@render children?.()}
</label>

<style lang="scss">
	label {
    color: #6c5ce7;
    display: block;
    font-size: 1em;
    font-weight: 700;
    margin-bottom: 10px;
	}
</style>