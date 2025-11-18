type Operator = '+' | '-' | '*' | '/' | '^';
type RPNToken = number | string;
type Variables = { [key: string]: number };
type OperatorFunctions = { [key in Operator]: (a: number, b: number) => number };
type Precedence = { [key in Operator]: number };

export default class ExpressionParser {
    private readonly operators: OperatorFunctions;
    private readonly precedence: Precedence;

    constructor() {
        this.operators = {
            '+': (a: number, b: number) => a + b,
            '-': (a: number, b: number) => a - b,
            '*': (a: number, b: number) => a * b,
            '/': (a: number, b: number) => a / b,
            '^': (a: number, b: number) => Math.pow(a, b)
        };

        this.precedence = {
            '+': 1,
            '-': 1,
            '*': 2,
            '/': 2,
            '^': 3
        };
    }

    public tokenize(expression: string): string[] {
        const tokens = expression.match(/(\d+\.?\d*|[a-zA-Z_]\w*|[+\-*/^()]|\S)/g);
        return tokens || [];
    }

    public parseToRPN(tokens: string[]): RPNToken[] {
        const output: RPNToken[] = [];
        const operators: string[] = [];

        for (const token of tokens) {
            if (this.isNumber(token)) {
                output.push(parseFloat(token));
            } else if (this.isVariable(token)) {
                output.push(token);
            } else if (this.isOperator(token)) {
                while (operators.length > 0 &&
                operators[operators.length - 1] !== '(' &&
                this.precedence[operators[operators.length - 1] as Operator] >=
                this.precedence[token as Operator]) {
                    output.push(operators.pop()!);
                }
                operators.push(token);
            } else if (token === '(') {
                operators.push(token);
            } else if (token === ')') {
                while (operators.length > 0 && operators[operators.length - 1] !== '(') {
                    output.push(operators.pop()!);
                }
                if (operators.length === 0) {
                    throw new Error('Несбалансированные скобки');
                }
                operators.pop(); // Убираем '('
            }
        }

        while (operators.length > 0) {
            const op = operators.pop()!;
            if (op === '(') {
                throw new Error('Несбалансированные скобки');
            }
            output.push(op);
        }

        return output;
    }

    public evaluateRPN(rpn: RPNToken[], variables: Variables = {}): number {
        const stack: number[] = [];

        for (const item of rpn) {
            if (typeof item === 'number') {
                stack.push(item);
            } else if (this.isVariable(item)) {
                if (item in variables) {
                    if (variables[item] === null || variables[item] === undefined) {
                        throw new Error(`Переменная без значения: ${item}`);
                    }
                    stack.push(variables[item]);
                } else {
                    throw new Error(`Неизвестная переменная: ${item}`);
                }
            } else if (this.isOperator(item)) {
                if (stack.length < 2) {
                    throw new Error('Недостаточно операндов');
                }
                const b = stack.pop()!;
                const a = stack.pop()!;
                stack.push(this.operators[item as Operator](a, b));
            }
        }

        if (stack.length !== 1) {
            throw new Error('Некорректное выражение');
        }

        return stack[0];
    }

    public evaluate(expression: string, variables: Variables = {}): number {
        const tokens = this.tokenize(expression);
        const rpn = this.parseToRPN(tokens);
        return this.evaluateRPN(rpn, variables);
    }

    private isNumber(token: string): boolean {
        return !isNaN(parseFloat(token)) && isFinite(parseFloat(token));
    }

    private isVariable(token: string): boolean {
        return /^[a-zA-Z_]\w*$/.test(token);
    }

    private isOperator(token: string): boolean {
        return token in this.operators;
    }
}