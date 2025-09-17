import {Shape, Line, Point, Label, Polygon, DrawOptions, Arc} from "./shape";
import {Observer} from "../../observer";

const DEFAULT_LABELS = [
    new Label({x: 4, y: 0}, "{R}", {isTemplate: true, evaluateFormula: true}),
    new Label({x: 2, y: 0}, "{R}/2", {isTemplate: true, evaluateFormula: true}),
    new Label({x: -4, y: 0}, "-{R}", {isTemplate: true, evaluateFormula: true}),
    new Label({x: -2, y: 0}, "-{R}/2", {isTemplate: true, evaluateFormula: true}),

    new Label({x: 0, y: 4}, "{R}", {isTemplate: true, evaluateFormula: true}),
    new Label({x: 0, y: 2}, "{R}/2", {isTemplate: true, evaluateFormula: true}),
    new Label({x: 0, y: -4}, "-{R}", {isTemplate: true, evaluateFormula: true}),
    new Label({x: 0, y: -2}, "-{R}/2", {isTemplate: true, evaluateFormula: true}),
]

const DEFAULT_SHAPES = [
    new Arc({x: 0, y: 0}, 4, 0, Math.PI / 2),
    new Polygon([
        {x: 0, y: 0},
        {x: 2, y: 0},
        {x: 0, y: -2}
    ]),
    new Polygon([
        {x: 0, y: 4},
        {x: -4, y: 4},
        {x: -4, y: 0},
        {x: 0, y: 0},
    ]),
]

interface PlotOptions {
    step: number,
    gridColor: string
    axisColor: string,
    labelColor: string
    shapeColor: string
}

const DEFAULT_PLOT_OPTIONS: PlotOptions = {
    step: 40,
    gridColor: "grey",
    axisColor: "black",
    labelColor: "darkgreen",
    shapeColor: "lightblue"
}

interface Style {
    fill?: string
    stroke?: string
}

export default class PlotDrawer {
    private readonly ctx: CanvasRenderingContext2D;
    private customShapes: Shape[] = [];

    constructor(
        private canvas: HTMLCanvasElement,
        private rObserver: Observer<number>,
        private options?: Partial<PlotOptions>,
    ) {
        this.options = {...DEFAULT_PLOT_OPTIONS, ...options};
        this.ctx = canvas.getContext("2d");
    }

    get sizes() {
        return {
            xMin: -this.canvas.width / 2,
            yMin: -this.canvas.height / 2,
            xMax: this.canvas.width / 2,
            yMax: this.canvas.height / 2,
            xSize: this.canvas.width,
            ySize: this.canvas.height,
        }
    }

    update() {
        this.ctx.setTransform(1, 0, 0, -1, this.canvas.width / 2, this.canvas.height / 2);

        this.options.step = Math.round(Math.min(this.sizes.xSize, this.sizes.ySize) / 12);
        this.ctx.font = `${this.options.step / 30}em sans-serif`

        this._drawShapes()
        this._drawGrid()
        this._drawAxis()
        this._drawLabels()
    }

    draw(shape: Shape, options?: Partial<DrawOptions>) {
        shape.draw(this.ctx, {
            scale: this.options.step,
            r: this.rObserver.value,
            ...options
        })
    }

    withStyle({ fill, stroke }: Style, callback: () => void) {
        if (stroke === undefined) stroke = fill

        const origFillStyle = this.ctx.fillStyle
        const origStrokeStyle = this.ctx.strokeStyle

        this.ctx.fillStyle = fill
        this.ctx.strokeStyle = stroke

        try {
            callback()
        } finally {
            this.ctx.fillStyle = origFillStyle
            this.ctx.strokeStyle = origStrokeStyle
        }
    }

    _drawShapes() {
        this.withStyle({fill: this.options.shapeColor}, () => {
            DEFAULT_SHAPES.forEach(shape => this.draw(shape))
        })
    }

    _drawGrid() {
        this.withStyle({
            fill: this.options.gridColor
        }, () => {
            for (let x = 0; x < this.sizes.xMax / this.options.step; x++) {
                this.draw(new Line({x: x, y: this.sizes.yMin}, {x: x, y: this.sizes.yMax}))
                this.draw(new Line({x: -x, y: this.sizes.yMin}, {x: -x, y: this.sizes.yMax}))
            }
            for (let y = 0; y < this.sizes.yMax / this.options.step; y++) {
                this.draw(new Line({x: this.sizes.xMin, y: y}, {x: this.sizes.xMax, y: y}))
                this.draw(new Line({x: this.sizes.xMin, y: -y}, {x: this.sizes.xMax, y: -y}))
            }
        })
    }

    _drawLabels() {
        this.withStyle({fill: this.options.labelColor}, () => {
            DEFAULT_LABELS.forEach(shape => this.draw(shape))
        })
    }

    _drawAxis() {
        this.withStyle({fill: this.options.axisColor}, () => {
            this.draw(new Line({x: 0, y: this.sizes.yMin}, {x: 0, y: this.sizes.yMax}))
            this.draw(new Line({x: this.sizes.xMin, y: 0}, {x: this.sizes.xMax, y: 0}))

            this.draw(new Polygon([
                {x: this.sizes.xMax - 10, y: 5},
                {x: this.sizes.xMax, y: 0},
                {x: this.sizes.xMax - 10, y: -5},
            ]), {scale: 1})

            this.draw(new Polygon([
                {x: 5, y: this.sizes.yMax - 10},
                {x: 0, y: this.sizes.yMax},
                {x: -5, y: this.sizes.yMax - 10}
            ]), {scale: 1})

            const labelOffsetPrimary = this.options.step / 2
            const labelOffsetSecondary = this.options.step / 2.5

            this.draw(new Label({x: this.sizes.xMax - labelOffsetPrimary, y: labelOffsetSecondary}, "X"), {scale: 1})
            this.draw(new Label({x: labelOffsetSecondary, y: this.sizes.yMax - labelOffsetPrimary}, "Y"), {scale: 1})

            this.draw(new Point({x: 0, y:0}, 1), {scale: 4})
        })
    }
}