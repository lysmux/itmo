import useObserver from "./observer";
import {Coordinates} from "./types";
import {MaxConstraint, MinConstraint, RequiredConstraint} from "./validator/constrains";
import Validator from "./validator/validator";
import $ from "jquery";
import {R_OBSERVER} from "./global";
import {clearPoints, processPoints} from "./api";


const resultsTable = document.getElementById("results") as HTMLTableElement;
const errorsBlock = $("#errors-block")
const checkBtn = $("#check-btn");
const clearBtn = $("#clear-btn");

const formData = useObserver<Partial<Coordinates>>({
    x: new Set(),
    y: null,
    r: new Set()
})
const rules = {
    x: [
        new RequiredConstraint(),
    ],
    y: [
        new RequiredConstraint(),
        new MinConstraint(-3),
        new MaxConstraint(5),
    ],
    r: [
        new RequiredConstraint(),
    ],
}
const validator = new Validator(formData, rules)
validator.isValid.onChange(valid => checkBtn.prop("disabled", !valid))
validator.errors.onChange(() => {
    errorsBlock.html("")
    const errors = validator.errors.value

    for (let field in errors) {
        for (let error of errors[field]) {
            errorsBlock.append($('<p>')
                .html(`<span>${field}</span>: ${error.error}`))
        }
    }
})

formData.onChange(data => {
    let value: number = null
    if (data.r.size == 1) value = data.r.values().next().value

    R_OBSERVER.value = value
})

clearBtn.on("click", (e) => {
    e.preventDefault()

    clearPoints()
})

checkBtn.on("click", (e) => {
    e.preventDefault()

    processPoints(
        Array.from(formData.value.x),
        formData.value.y,
        Array.from(formData.value.r)
    )
});

$("input[name=x]").on("change", (e) => {
    const target = e.target as HTMLInputElement;
    const value = Number(target.value);
    const xValues = formData.value.x;

    if (target.checked) xValues.add(value);
    else xValues.delete(value);

    formData.value.x = new Set(xValues)
})

$("input[name=y]").on("change", (e) => {
    const target = e.target as HTMLInputElement;
    const number = Number(target.value)
    if (isNaN(number)) {
        target.value = target.value.slice(0, target.value.length - 1);
    }

    formData.value.y = isNaN(number) ? null : number;
})


$("input[name=r]").on("change", (e) => {
    const target = e.target as HTMLInputElement;
    const value = Number(target.value);
    const eValues = formData.value.r;

    if (target.checked) eValues.add(value);
    else eValues.delete(value);

    formData.value.r = new Set(eValues)
})
