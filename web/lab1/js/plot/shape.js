function compute(value, radius) {
    if (typeof value === "string") value = eval(value.replaceAll("R", radius))
    return value
}

class Position {
    constructor(x, y) {
        this.x = x
        this.y = y
    }

    compute(radius) {
        return [compute(this.x, radius), compute(this.y, radius)]
    }
}

class Shape {
    draw(radius, ctx) {}
}

class Point extends Shape {
    constructor(position, radius) {
        super();
        
        this.position = position;
        this.radius = radius;
    }
    
    draw(radius, ctx) {
        const [x, y] = this.position.compute(radius);
        
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
    
    draw(radius, ctx) {
        const [fromX, fromY] = this.fromPosition.compute(radius);
        const [toX, toY] = this.toPosition.compute(radius);

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

    draw(radius, ctx) {
        const [x, y] = this.position.compute(radius)
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


    draw(radius, ctx) {
        const [x, y] = this.position.compute(radius)
        const arcRadius = compute(this.arcRadius, radius)

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
    
    draw(radius, ctx) {
        ctx.beginPath();
        
        ctx.moveTo(...this.positions[0].compute(radius));
        this.positions.forEach(position => ctx.lineTo(...position.compute(radius)))
        ctx.closePath();
        ctx.fill();
    }
}