import useObserver from "./observer";
import {CheckResponse, Coordinates} from "./types";
import {MaxConstraint, MinConstraint, RequiredConstraint} from "./validator/constrains";
import Validator from "./validator/validator";
import $ from "jquery";
import superagent from "superagent";
import {addTableData, clearTableData} from "./table";
import {R_OBSERVER} from "./global";

const resultsTable = document.getElementById("results") as HTMLTableElement;
const checkBtn = document.getElementById("check-btn") as HTMLButtonElement;

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
validator.isValid.onChange(valid => checkBtn.disabled = !valid)

validator.errors.onChange(() => {
    $("#errors-block").html("")
    const errors = validator.errors.value

    for (let field in errors) {
        for (let error of errors[field]) {
            $("#errors-block")
                .append($('<p>')
                    .html(`<span>${field}</span>: ${error.error}`))
        }
    }
})

function onXChange(e: InputEvent) {
    const target = e.target as HTMLInputElement;
    const value = Number(target.value);
    const xValues = formData.value.x;

    if (target.checked) xValues.add(value);
    else xValues.delete(value);

    formData.value.x = new Set(xValues)
}

function onYChange(e: InputEvent) {
    const target = e.target as HTMLInputElement;
    const number = Number(target.value)
    if (isNaN(number)) {
        target.value = target.value.slice(0, target.value.length - 1);
    }

    formData.value.y = isNaN(number) ? null : number;
}

function onRChange(e: InputEvent) {
    const target = e.target as HTMLInputElement;
    const value = Number(target.value);
    const eValues = formData.value.r;

    if (target.checked) eValues.add(value);
    else eValues.delete(value);

    formData.value.r = new Set(eValues)
}

formData.onChange(data => {
    let value: number = null
    if (data.r.size == 1) value = data.r.values().next().value

    R_OBSERVER.value = value
})

$("#clear-btn").on("click", (e) => {
    e.preventDefault()

    superagent.post("/clear").then(res => {})
    clearTableData(resultsTable)
})

$("#check-btn").on("click", (e) => {
    e.preventDefault()

    superagent
        .get("?")
        .query({
            x: Array.from(formData.value.x),
            y: formData.value.y,
            r: Array.from(formData.value.r),
        })
        .then(res => {
            const result = res.body as CheckResponse;

            result.checks.forEach(check => {
                addTableData(resultsTable, [new Date(result.time).toLocaleTimeString(), check.x, check.y, check.r, check.contains, result.executionTime])
            })
        })
});

document.querySelectorAll("input[name=x]").forEach(el => {
    el.addEventListener("change", onXChange)
})

document.querySelectorAll("input[name=y]").forEach(el => {
    el.addEventListener("change", onYChange)
})

document.querySelectorAll("input[name=r]").forEach(el => {
    el.addEventListener("change", onRChange)
})
