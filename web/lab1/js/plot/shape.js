function compute(value, radius, step = 1) {
    if (typeof value === "string") value = eval(value.replaceAll("R", radius * step))
    return value
}

class Position {
    constructor(x, y) {
        this.x = x
        this.y = y
    }

    compute(radius, step = 1) {
        return [compute(this.x, radius, step), compute(this.y, radius, step)]
    }
}

class Shape {
    draw(radius, step, ctx) {
    }
}

class Point extends Shape {
    constructor(position, radius) {
        super();

        this.position = position;
        this.radius = radius;
    }

    draw(radius, step, ctx) {
        const [x, y] = this.position.compute(radius, step);

        ctx.arc(x, y, this.radius, 0, 2 * Math.PI)
        ctx.fill()
    }
}

class Line extends Shape {
    constructor(fromPosition, toPosition) {
        super();

        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }

    draw(radius, step, ctx) {
        const [fromX, fromY] = this.fromPosition.compute(radius, step);
        const [toX, toY] = this.toPosition.compute(radius, step);

        ctx.beginPath();
        ctx.moveTo(fromX, fromY)
        ctx.lineTo(toX, toY)
        ctx.stroke()
    }
}

class Label extends Shape {
    constructor(text, position, computeText) {
        super();

        this.text = text
        this.position = position
        this.computeText = computeText
    }

    draw(radius, step, ctx) {
        const [x, y] = this.position.compute(radius, step)
        const text = this.computeText ? compute(this.text, radius) : this.text

        ctx.save()
        ctx.scale(1, -1);

        if (x !== 0) ctx.textAlign = "center"
        if (y !== 0) ctx.textBaseline = "middle"

        ctx.fillText(text, x, -y)
        ctx.restore()
    }
}

class Arc extends Shape {
    constructor(position, radius, startAngle, endAngle) {
        super();

        this.position = position;
        this.arcRadius = radius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }


    draw(radius, step, ctx) {
        const [x, y] = this.position.compute(radius, step)
        const arcRadius = compute(this.arcRadius, radius, step)

        ctx.beginPath();
        ctx.moveTo(x, y);
        ctx.arc(x, y, arcRadius, this.startAngle, this.endAngle);
        ctx.fill();
    }
}

class Polygon extends Shape {
    constructor(positions) {
        super();

        this.positions = positions;
    }

    draw(radius, step, ctx) {
        ctx.beginPath();

        ctx.moveTo(...this.positions[0].compute(radius, step));
        this.positions.forEach(position => ctx.lineTo(...position.compute(radius, step)))
        ctx.closePath();
        ctx.fill();
    }
}