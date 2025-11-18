import {Arc, type DrawOptions, Label, Line, Point, Polygon, Shape} from "./shape";

const SCALE = 4

const DEFAULT_LABELS = [
    new Label({x: 1, y: 0}, "{R}", {isTemplate: true, evaluateFormula: true}),
    new Label({x: 0.5, y: 0}, "{R/2}", {isTemplate: true, evaluateFormula: true}),
    new Label({x: -1, y: 0}, "-{R}", {isTemplate: true, evaluateFormula: true}),
    new Label({x: -0.5, y: 0}, "-{R/2}", {isTemplate: true, evaluateFormula: true}),

    new Label({x: 0, y: 1}, "{R}", {isTemplate: true, evaluateFormula: true}),
    new Label({x: 0, y: 0.5}, "{R/2}", {isTemplate: true, evaluateFormula: true}),
    new Label({x: 0, y: -1}, "-{R}", {isTemplate: true, evaluateFormula: true}),
    new Label({x: 0, y: -0.5}, "-{R/2}", {isTemplate: true, evaluateFormula: true}),
]

const DEFAULT_SHAPES = [
    new Arc({x: 0, y: 0}, 0.5, -Math.PI / 2, 0),
    new Polygon([
        {x: 0, y: 0},
        {x: 0, y: 1},
        {x: 0.5, y: 0}
    ]),
    new Polygon([
        {x: 0, y: -0.5},
        {x: -1, y: -0.5},
        {x: -1, y: 0},
        {x: 0, y: 0},
    ]),
]

interface PlotOptions {
    step: number,
    defaultR?: number,
    gridColor: string
    axisColor: string,
    labelColor: string
    shapeColor: string,
    customShapeColor: string,
}

const DEFAULT_PLOT_OPTIONS: PlotOptions = {
    step: 40,
    gridColor: "grey",
    axisColor: "black",
    labelColor: "darkgreen",
    shapeColor: "lightblue",
    customShapeColor: "black"
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
				private radius: number | undefined,
        public options?: Partial<PlotOptions>,
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

		setRadius(radius: number) {
			this.radius = radius;
		}

    update() {
        this.ctx.setTransform(1, 0, 0, -1, this.canvas.width / 2, this.canvas.height / 2);

        this.options.step = Math.round(Math.min(this.sizes.xSize, this.sizes.ySize) / 10);
        this.ctx.font = `${this.options.step / 30}em sans-serif`

        this.ctx.clearRect(
            this.sizes.xMin,
            this.sizes.yMin,
            this.sizes.xSize,
            this.sizes.ySize,
        )

        this._drawShapes()
        this._drawGrid()
        this._drawAxis()
        this._drawLabels()
        this._drawCustomShapes()
    }

    draw(shape: Shape, options?: Partial<DrawOptions>) {
        if (shape.color !== undefined) {
            this.withStyle({fill: shape.color}, () => {
                shape.draw(this.ctx, {
                    scale: this.options.step * SCALE,
                    R: this.radius,
                    ...options
                })
            })
        } else {
            shape.draw(this.ctx, {
                scale: this.options.step * SCALE,
                R: this.radius,
                ...options
            })
        }
    }

    setCustomShapes(shapes: Shape[]) {
        this.customShapes = shapes
        this.update()
    }

    withStyle({fill, stroke}: Style, callback: () => void) {
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

    _drawCustomShapes() {
        this.withStyle({fill: this.options.customShapeColor}, () => {
            this.customShapes.forEach(shape => this.draw(shape))
        })
    }

    _drawGrid() {
        const xMin = this.sizes.xMin / SCALE
        const xMax = this.sizes.xMax / SCALE

        const yMin = this.sizes.yMin / SCALE
        const yMax = this.sizes.yMax / SCALE

        this.withStyle({
            fill: this.options.gridColor
        }, () => {
            for (let x = 0; x < this.sizes.xMax / this.options.step; x++) {
                this.draw(new Line({x: x / SCALE, y: yMin}, {x: x / SCALE, y: yMax}))
                this.draw(new Line({x: -x / SCALE, y: yMin}, {x: -x / SCALE, y: yMax}))
            }
            for (let y = 0; y < this.sizes.yMax / this.options.step; y++) {
                this.draw(new Line({x: xMin, y: y / SCALE}, {x: xMax, y: y / SCALE}))
                this.draw(new Line({x: xMin, y: -y / SCALE}, {x: xMax, y: -y / SCALE
                }))
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

            this.draw(new Point({x: 0, y: 0}, 1), {scale: 4})
        })
    }
}