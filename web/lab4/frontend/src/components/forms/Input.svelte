<script lang="ts">
	const props: InputProps = $props();
</script>

<input
	class="input"
	type={props.type}
	placeholder={props.placeholder}
	value={props.value}
	disabled={props.disabled}
	required={props.required}
/>