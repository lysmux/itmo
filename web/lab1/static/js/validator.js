class Constraint {
    validate(value) {
        return true
    }

    getError() {
        return "some error"
    }
}

class MinConstraint extends Constraint {
    constructor(minValue, strict = false) {
        super();
        this.minValue = minValue
        this.strict = strict
    }

    validate(value) {
        if (this.strict) return value > this.minValue
        return value >= this.minValue
    }

    getError() {
        return `Value must be > ${this.minValue}`
    }
}

class MaxConstraint extends Constraint {
    constructor(maxValue, strict = false) {
        super();
        this.maxValue = maxValue
        this.strict = strict
    }

    validate(value) {
        if (this.strict) return value < this.maxValue
        return value <= this.maxValue
    }

    getError() {
        return `Value must be < ${this.maxValue}`
    }
}

class RequiredConstraint extends Constraint {
    validate(value) {
        if (value === null || value === undefined) return false
        if (Array.isArray(value)) return value.length > 0
        if (typeof value === "string") return value.trim() !== ""

        return true
    }

    getError() {
        return `Field required`
    }
}


class OnlyDigitsConstraint extends Constraint {
    validate(value) {
        return /^\d+(\.\d+)?$/.test(value)
    }

    getError() {
        return `Value must be only digits`
    }
}


class ValidationError {
    constructor(fieldName, error) {
        this.fieldName = fieldName
        this.error = error
    }
}

class Validator {
    constructor(data, rules) {
        this.data = data
        this.rules = rules
        this.isValid = new Observable(false)
        this.errors = new Observable({})

        this.errors.onChange(value => {
            let hasErrors = Object.values(value).some(err => err.length > 0)
            this.isValid.set(!hasErrors)
        })

        this._registerListeners()
        this._validateForm()
    }

    _validateForm() {
        for (let fieldName in this.data) {
            let fieldValue = this.data[fieldName]
            this._validate(fieldName, fieldValue.get())
        }
    }

    _validate(fieldName, value) {
        let constraints = this.rules[fieldName]
        let errors = []

        constraints.forEach(constraint => {
            if (!constraint.validate(value)) {
                errors.push(new ValidationError(fieldName, constraint.getError()))
            }
        });

        this.errors.set({...this.errors.get(), [fieldName]: errors})
    }

    _registerListeners() {
        for (let fieldName in this.data) {
            let fieldValue = this.data[fieldName]
            fieldValue.onChange(value => {
                this._validate(fieldName, value)
            })
        }
    }
}

class ValidationManager {
    constructor(validator) {
        this.validator = validator
        this.fieldConfigs = {}
        
        this.validator.errors.onChange(value => {
            for (let fieldName in value) this.updateFieldErrors(fieldName)
        })
    }

    registerField(fieldName, inputEl, errorContainer) {
        this.fieldConfigs[fieldName] = errorContainer
        this.updateFieldErrors(fieldName)
        return this
    }

    updateFieldErrors(fieldName) {
        const errorContainer = this.fieldConfigs[fieldName]
        if (!errorContainer) return

        let fieldErrors = this.validator.errors.get()[fieldName] || []
        let errorBlocks = fieldErrors.map(err => this.constructErrorBlock(err.error))
        errorContainer.replaceChildren(...errorBlocks)
    }

    constructErrorBlock(text) {
        let paragraph = document.createElement("p")
        paragraph.innerHTML = text

        return paragraph
    }
}