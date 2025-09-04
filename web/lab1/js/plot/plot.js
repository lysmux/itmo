DEFAULT_SHAPES = [
    new Arc(new Position(0, 0), "R", 0, Math.PI / 2),
    new Polygon([
        new Position(0, 0),
        new Position("R/2", 0),
        new Position(0, "-R/2")
    ]),
    new Polygon([
        new Position(0, "R"),
        new Position("-R", "R"),
        new Position("-R", 0),
        new Position(0, 0),
    ]),
]

DEFAULT_LABELS = [
    new Label("R", new Position("R", 0), true),
    new Label("R/2", new Position("R/2", 0), true),
    new Label("-R", new Position("-R", 0), true),
    new Label("-R/2", new Position("-R/2", 0), true),
    
    new Label("R", new Position(0, "R"), true),
    new Label("R/2", new Position(0, "R/2"), true),
    new Label("-R", new Position(0, "-R"), true),
    new Label("-R/2", new Position(0, "-R/2"), true),
]

class Plot {
    constructor(canvas) {
        this.step = 40
        this.gridColor = "grey"
        this.axisColor = "black"
        this.labelColor = "darkgreen"
        this.shapeColor = "lightblue"

        this.radius = new Observable(4)
        this.canvas = canvas

        this.ctx = canvas.getContext("2d")
        this.ctx.setTransform(1, 0, 0, -1, this.canvas.width / 2, this.canvas.height / 2);
        this.ctx.font = "30px sans-serif"

        this.update()
        this.radius.onChange(value => this.update())
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
    
    drawShape(shape) {shape.draw(this.radius.get(), this.step, this.ctx)}

    update() {
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
        
        this.drawShape(new Point(new Position(0, 0), 5))
    }

    withStyle(action, fillStyle, strokeStyle = undefined) {
        if (strokeStyle === undefined) strokeStyle = fillStyle

        const origFillStyle = this.ctx.fillStyle
        const origStrokeStyle = this.ctx.strokeStyle

        this.ctx.fillStyle = fillStyle
        this.ctx.strokeStyle = strokeStyle

        try {
            action()
        } finally {
            this.ctx.fillStyle = origFillStyle
            this.ctx.strokeStyle = origStrokeStyle
        }
    }

    _drawGrid() {
        this.withStyle(() => {
            for (let x = 0; x < this.canvas.width / 2; x += this.step) {
                this.drawShape(new Line(
                    new Position(x, this.sizes.yMin),
                    new Position(x, this.sizes.yMax),
                ))
                this.drawShape(new Line(
                    new Position(-x, this.sizes.yMin),
                    new Position(-x, this.sizes.yMax),
                ))
            }
            for (let y = 0; y < this.canvas.height / 2; y += this.step) {
                this.drawShape(new Line(
                    new Position(this.sizes.xMin, y),
                    new Position(this.sizes.xMax, y),
                ))
                this.drawShape(new Line(
                    new Position(this.sizes.xMin, -y),
                    new Position(this.sizes.xMax, -y),
                ))
            }
        }, this.gridColor)
    }

    _drawShapes() {
        this.withStyle(() => {
            DEFAULT_SHAPES.forEach(shape => this.drawShape(shape))
        }, this.shapeColor)
    }

    _drawLabels() {
        this.withStyle(() => {
            DEFAULT_LABELS.forEach(shape => this.drawShape(shape))
        }, this.labelColor)
    }

    _drawAxis() {
        this.withStyle(() => {
            this.drawShape(new Line(
                new Position(0, this.sizes.yMin),
                new Position(0, this.sizes.yMax),
            ))
            this.drawShape(new Line(
                new Position(this.sizes.xMin, 0),
                new Position(this.sizes.xMax, 0),
            ))
            
            this.drawShape(new Polygon([
                new Position(this.sizes.xMax - 10, 5),
                new Position(this.sizes.xMax, 0),
                new Position(this.sizes.xMax - 10, -5),
            ]))
            
            this.drawShape(new Polygon([
                new Position(5, this.sizes.yMax - 10),
                new Position(0, this.sizes.yMax),
                new Position(-5, this.sizes.yMax  - 10),
            ]))

            this.drawShape(new Label("X", new Position(this.sizes.xMax - 30, 10)),)
            this.drawShape(new Label("Y", new Position(10, this.sizes.yMax - 30)))
        }, this.axisColor)
    }
}