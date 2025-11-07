const IS_PROXY_PROP = "__proxy"

type Listener<T> = (value: T) => void;


class Observer<T> {
    private _value: T;
    private _listeners: Set<Listener<T>> = new Set<Listener<T>>();

    private isBind: boolean = false;

    constructor(initial?: T) {
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

    bind(target: Observer<T>) {
        if (this.isBind) {
            throw new Error("Observer already bind");
        }

        target.onChange(value => this.value = value);
        this.isBind = true;
    }
}

function isProxy(obj: any) {
    return !!obj && obj[IS_PROXY_PROP] === true;
}


class ArrayObserver<T> extends Observer<T[]> {
    private proxy: T[];

    constructor(initial?: T[]) {
        super(initial || []);

        this.proxy = this.createProxy(initial)
    }

    get value() {
        return this.proxy;
    }

    set value(newValue: T[]) {
        delete this.proxy;

        if (isProxy(newValue)) this.proxy = newValue;
        this.proxy = this.createProxy(newValue);
        this.notify(newValue)
    }

    private createProxy(array: T[]): T[] {
        const proxy = new Proxy(array, {
            set: (target, prop, value) => {
                const numericProp = Number(prop);
                if (!isNaN(numericProp) || prop === 'length') {
                    target[prop] = value;
                    this.notify([...target]);
                } else {
                    target[prop] = value;
                }
                return true;
            }
        });
        proxy[IS_PROXY_PROP] = true

        return proxy;
    }
}

class ObjectObserver<T extends object> extends Observer<T> {
    private proxy: T;

    constructor(initial: T) {
        super(initial);
        this.proxy = this.createProxy(initial);
    }

    get value() {
        return this.proxy;
    }

    set value(newValue: T) {
        if (isProxy(newValue)) this.proxy = newValue;
        this.proxy = this.createProxy(newValue);
        this.notify(newValue)
    }

    private createProxy(obj: T): T {
        return new Proxy(obj, {
            set: (target, prop, value) => {
                const oldValue = target[prop];
                target[prop] = value;

                if (oldValue !== value) {
                    this.notify({...target});
                }
                return true;
            },
            deleteProperty: (target, prop) => {
                const oldValue = target[prop];
                const result = delete target[prop];

                if (oldValue !== undefined) {
                    this.notify({...target});
                }
                return result;
            }
        });
    }
}

function useObserver<T>(initial?: T): Observer<T>;
function useObserver<T extends object>(initial?: T): ObjectObserver<T>;
function useObserver<T>(initial?: T[]): ArrayObserver<T>;

function useObserver<T>(initial?: any): Observer<any> {
    if (Array.isArray(initial)) {
        return new ArrayObserver(initial);
    } else if (typeof initial === 'object' && initial !== null) {
        return new ObjectObserver(initial);
    }
    return new Observer(initial);
}

export {ArrayObserver, ObjectObserver, Observer};
export default useObserver;