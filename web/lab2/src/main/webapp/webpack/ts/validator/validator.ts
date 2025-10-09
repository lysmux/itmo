import {ObjectObserver, Observer} from "../observer";
import {Constraint} from "./constrains";


interface ValidationError {
    fieldName: string,
    error: string
}

export default class Validator {
    public readonly isValid = new Observer(false);
    public readonly errors = new ObjectObserver<{
        [fieldName: string]: ValidationError[];
    }>({});

    constructor(
        private readonly data: Observer<any>,
        private readonly rules: Record<string, Constraint[]>
    ) {
        this.errors.onChange(value => {
            let hasErrors = Object.values(value).some(err => err.length > 0)
            this.isValid.value = !hasErrors
        })

        data.onChange(() => this.validateForm())
    }

    private validateForm() {
        for (let fieldName in this.data.value) {
            let fieldValue = this.data.value[fieldName]
            this.validate(fieldName, fieldValue)
        }
    }

    private validate(fieldName: string, value: any) {
        let constraints = this.rules[fieldName]
        let errors: ValidationError[] = []

        constraints.forEach(constraint => {
            if (!constraint.validate(value)) {
                errors.push({fieldName, error: constraint.getError()})
            }
        });

        this.errors.value = {...this.errors.value, [fieldName]: errors};
    }
}
