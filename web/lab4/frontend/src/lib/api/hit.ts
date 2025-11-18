export interface HitResult {
	id: string;
	x: number;
	y: number;
	r: number;
	hit: boolean;
	executionTime: number;
	time: string;
}

export interface Coordinates {
	x: number;
	y: number;
	r: number;
}