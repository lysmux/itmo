
export function createRange(start: number, end: number) {
    return Array.from({ length: end - start }, (_, i) => start + i)
}
