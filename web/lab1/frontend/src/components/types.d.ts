export interface Coordinates {
    x: Set<number>,
    y: number,
    r: Set<number>
}

export interface ContainsResponse {
    x: number,
    y: number,
    r: number,
    contains: boolean,
}

export interface CheckResponse {
    contains: ContainsResponse[],
    executionTime: number
}