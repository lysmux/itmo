export const range = (from: number, to: number) => {
	return Array.from({ length: to - from + 1 }, (_, i) => from + i)
}
