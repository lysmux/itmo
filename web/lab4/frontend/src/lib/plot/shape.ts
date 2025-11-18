import ExpressionParser from "$lib/utils/expression.ts";

export interface Position {
    x: number;
    y: number;
}

export interface DrawOptions {
    scale: number;
    R: number;
}

const DEFAULT_DRAW_OPTIONS: DrawOptions = {
    scale: 1,
    R: null
}

export abstract class Shape {
    public color: string = undefined;

    draw(ctx: CanvasRenderingContext2D, options?: Partial<DrawOptions>): void {
        this._draw(ctx, {...DEFAULT_DRAW_OPTIONS, ...options});
    }

    abstract _draw(ctx: CanvasRenderingContext2D, options: DrawOptions): void;

    protected scaledPosition(position: Position, scale: number): Position {
        return {
            x: position.x * scale,
            y: position.y * scale,
        }
    }

    protected scaledValue(value: number, scale: number): number {
        return value * scale;
    }
}

export class Point extends Shape {
    constructor(
        private position: Position,
        private radius: number = 2,
        private dependR: boolean = false,
        color: string = undefined
    ) {
        super();
        this.color = color
    }

    _draw(ctx: CanvasRenderingContext2D, options: DrawOptions): void {
        let {x, y} = this.scaledPosition(this.position, options.scale);
        const radius = this.scaledValue(this.radius, options.scale)

        if (this.dependR) {
            x = x / options.R
            y = y / options.R
        }

        ctx.moveTo(x, y)
        ctx.beginPath()
        ctx.arc(x, y, radius, 0, 2 * Math.PI)
        ctx.fill()
        ctx.closePath()
    }
}

export class Line extends Shape {
    constructor(
        private from: Position,
        private to: Position,
    ) {
        super();
    }

    _draw(ctx: CanvasRenderingContext2D, options: DrawOptions): void {
        const {x: fromX, y: fromY} = this.scaledPosition(this.from, options.scale);
        const {x: toX, y: toY} = this.scaledPosition(this.to, options.scale);

        ctx.beginPath();
        ctx.moveTo(fromX, fromY)
        ctx.lineTo(toX, toY)
        ctx.stroke()
    }
}

export class Arc extends Shape {
    constructor(
        private position: Position,
        private radius: number,
        private startAngle: number = 0,
        private endAngle: number = Math.PI,
    ) {
        super();
    }

    _draw(ctx: CanvasRenderingContext2D, options: DrawOptions): void {
        const {x, y} = this.scaledPosition(this.position, options.scale);
        const radius = this.scaledValue(this.radius, options.scale);

        ctx.beginPath();
        ctx.moveTo(x, y);
        ctx.arc(x, y, radius, this.startAngle, this.endAngle);
        ctx.fill();
    }
}

export class Polygon extends Shape {
    constructor(
        private positions: Position[],
    ) {
        super();
    }

    _draw(ctx: CanvasRenderingContext2D, options: DrawOptions): void {
        ctx.beginPath();

        const {x: startX, y: startY} = this.scaledPosition(this.positions[0], options.scale);

        ctx.moveTo(startX, startY);
        this.positions.forEach(position => {
            const {x, y} = this.scaledPosition(position, options.scale);
            ctx.lineTo(x, y)
        })
        ctx.closePath();
        ctx.fill();
    }
}

export interface LabelOptions {
    isTemplate: boolean;
    evaluateFormula: boolean
}

const DEFAULT_LABEL_OPTIONS: LabelOptions = {
    isTemplate: false,
    evaluateFormula: false,
}

export class Label extends Shape {
    constructor(
        private position: Position,
        private text: string,
        private labelOptions?: Partial<LabelOptions>,
    ) {
        super();

        this.labelOptions = {...DEFAULT_LABEL_OPTIONS, ...labelOptions};
    }

    _draw(ctx: CanvasRenderingContext2D, options: DrawOptions): void {
        const {x, y} = this.scaledPosition(this.position, options.scale);
        const text = this.renderText(options);

        ctx.save()
        ctx.scale(1, -1);

        if (x !== 0) ctx.textAlign = "center"
        if (y !== 0) ctx.textBaseline = "middle"

        ctx.fillText(text, x, -y)
        ctx.restore()
    }

    renderText(data: Record<string, any>): string {
        if (!this.labelOptions.isTemplate) {
            return this.text;
        }

        return this.text.replace(/\{(.+)}/g, (match, key: string) => {
            if (this.labelOptions.evaluateFormula) {
                try {
                    return new ExpressionParser().evaluate(key, data).toString()
                } catch (e) {}
            }
            return key
        });
    }
}