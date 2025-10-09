export interface Coordinates {
    x: Set<number>,
    y: number,
    r: Set<number>
}

export interface CheckResponse {
    x: number,
    y: number,
    r: number,
    contains: boolean,
    time: string,
    executionTime: number
}