export abstract class Constraint {
    abstract validate(value: any): boolean;

    abstract getError(): string;
}

export class MinConstraint extends Constraint {
    constructor(
        private readonly minValue: number,
        private readonly strict: boolean = false
    ) {
        super();
    }

    validate(value: any) {
        if (this.strict) return value > this.minValue
        return value >= this.minValue
    }

    getError() {
        return `Value must be > ${this.minValue}`
    }
}

export class MaxConstraint extends Constraint {
    constructor(
        private readonly maxValue: number,
        private readonly strict: boolean = false
    ) {
        super();
    }

    validate(value: any) {
        if (this.strict) return value < this.maxValue
        return value <= this.maxValue
    }

    getError() {
        return `Value must be < ${this.maxValue}`
    }
}

export class RequiredConstraint extends Constraint {
    validate(value: any) {
        if (value === null || value === undefined) return false
        if (value instanceof Set) return value.size > 0
        if (Array.isArray(value)) return value.length > 0
        if (typeof value === "string") return value.trim() !== ""

        return true
    }

    getError() {
        return `Field required`
    }
}
