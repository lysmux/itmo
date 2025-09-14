type Listener<T> = (value: T) => void;


class Observer<T> {
    private _value: T;
    private _listeners: Set<Listener<T>> = new Set<Listener<T>>();

    constructor(initial: T) {
        this._value = initial;
    }

    get value() {
        return this._value;
    }

    set value(newValue: T) {
        if (this._value !== newValue) {
            this._value = newValue;

            this.notify(newValue)
        }
    }

    onChange(listener: Listener<T>) {
        this._listeners.add(listener);
        listener(this._value);
    }

    notify(newValue: T) {
        this._listeners.forEach(listener => listener(newValue));
    }
}

class ArrayObserver<T> extends Observer<T[]> {
    private proxy: T[];

    constructor(initial: T[]) {
        super(initial);

        this.proxy = this.createProxy(initial)
    }

    get value() {
        return this.proxy;
    }

    private createProxy(array: T[]): T[] {
        const methodsToIntercept = [
            'push', 'pop', 'shift', 'unshift', 'splice',
            'sort', 'reverse', 'copyWithin', 'fill'
        ];

        return new Proxy(array, {
            get: (target, prop) => {
                if (methodsToIntercept.includes(prop as string)) {
                    return (...args: any[]) => {
                        const result = (target as any)[prop](...args);
                        this.notify([...target]);
                        return result;
                    };
                }
                return target[prop as any];
            },
            set: (target, prop, value) => {
                const numericProp = Number(prop);
                if (!isNaN(numericProp) || prop === 'length') {
                    const oldValue = target[prop as any];
                    target[prop as any] = value;
                    if (oldValue !== value) {
                        this.notify([...target]);
                    }
                } else {
                    target[prop as any] = value;
                }
                return true;
            }
        });
    }
}

export {ArrayObserver, Observer};