export interface Coordinates {
    x: Set<number>,
    y: number,
    r: Set<number>
}

export interface CheckResponse {
    contains: boolean,
    executionTime: number
}