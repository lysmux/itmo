const inputX = document.getElementById("inputX")
const inputY = document.getElementById("inputY")
const inputR = document.getElementById("inputR")

const inputXErrors = document.getElementById("inputX__errors")
const inputYErrors = document.getElementById("inputY__errors")
const inputRErrors = document.getElementById("inputR__errors")

const submitBtn = document.getElementById("submitBtn")
const resultsTable = document.getElementById("resultsTable").getElementsByTagName("tbody")[0]

const formData = makeObservableObject({
    x: 1,
    y: 1,
    r: 4
})

formData.x.bindInput(inputX)
formData.y.bindInput(inputY)
formData.r.bindInput(inputR)


const plotCanvas = document.getElementById("plot")
const plot = new Plot(plotCanvas)
formData.r.bind(plot.radius)

const rules = {
    x: [
        new RequiredConstraint(),
        new OnlyDigitsConstraint()
    ],
    y: [
        new RequiredConstraint(),
        new OnlyDigitsConstraint()
    ],
    r: [
        new RequiredConstraint(),
        new OnlyDigitsConstraint()
    ],
}
const validator = new Validator(formData, rules)

validator.isValid.onChange(value => submitBtn.disabled = !value)
const validationManager = new ValidationManager(validator)

validationManager
    .registerField("x", inputX, inputXErrors)
    .registerField("y", inputY, inputYErrors)
    .registerField("r", inputR, inputRErrors)

submitBtn.addEventListener("click", function (event) {
    event.preventDefault()
    console.log(formData)

    plot.addShape(new Point(new Position(formData.x.get(), formData.y.get()), 5))

    const newRow = resultsTable.insertRow()
    newRow.insertCell().textContent = formData.x.get()
    newRow.insertCell().textContent = formData.y.get()
    newRow.insertCell().textContent = formData.r.get()

    newRow.insertCell().textContent = ""
})
