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
    private readonly METHODS_TO_INTERCEPT = [
        'push', 'pop', 'shift', 'unshift', 'splice',
        'sort', 'reverse', 'copyWithin', 'fill'
    ];

    private readonly proxy: T[];

    constructor(initial: T[]) {
        super(initial);

        this.proxy = this.createProxy(initial)
    }

    get value() {
        return this.proxy;
    }

    private createProxy(array: T[]): T[] {
        return new Proxy(array, {
            get: (target, prop) => {
                if (this.METHODS_TO_INTERCEPT.includes(prop as string)) {
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

class ObjectObserver<K extends string | number | symbol, V> extends Observer<Record<K, V>> {
    private readonly METHODS_TO_INTERCEPT = [
        'clear', 'delete', 'set', 'has', 'get', 'keys', 'values', 'entries'
    ];

    private readonly proxy: Record<K, V>;

    constructor(initial: Record<K, V>) {
        super(initial);

        this.proxy = this.createProxy(initial)
    }

    get value() {
        return this.proxy;
    }

    private createProxy(obj: Record<K, V>): Record<K, V> {
        return new Proxy(obj, {
            get: (target, prop) => {
                if (this.METHODS_TO_INTERCEPT.includes(prop as string)) {
                    return (...args: any[]) => {
                        const result = (target as any)[prop](...args);
                        this.notify({...target});
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
                        this.notify({...target});
                    }
                } else {
                    target[prop as any] = value;
                }
                return true;
            }
        });
    }
}

export {ArrayObserver, ObjectObserver, Observer};