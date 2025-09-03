class Observable {
    constructor (initialValue) {
        this._value = initialValue
        this._isUpdating = false
        
        this.subscribers = new Set()
    }

    set(value) {
        if (value !== this._value && !this._isUpdating) {
            this._value = value
            this._notify()
        }
    }

    get() {return this._value}

    onChange(callback) {
        this.subscribers.add(callback)
        callback(this._value)
    }

    bindInput(input) {
        input.addEventListener("input", event => this.set(input.value))
        this.onChange(value => input.value = value)
    }
    
    bind(targetObservable) {
        this.onChange(value => {
            this._isUpdating = true
            targetObservable.set(value)
            this._isUpdating = false
        })
    }

    _notify() {
        this.subscribers.forEach(subscriber => {
            try {
                subscriber(this._value)
            } catch (error) {
                console.error(`Error notify subscriber '${subscriber.name}'`, error)
            }
        })
    }
}

function makeObservableObject(obj) {
    let observableObj = {}
    
    for (let key in obj) {
        let value = obj[key]
        observableObj[key] = new Observable(value)
    }

    return observableObj
}