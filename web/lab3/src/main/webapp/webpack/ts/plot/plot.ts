import $ from "jquery";
import PlotDrawer from "./plotDrawer";
import {R_OBSERVER, RESULTS_OBSERVER} from "../global";
import {addToast, TOAST_VARIANTS} from "../toast";
import {Point} from "./shape";

const canvas = $("#plot > canvas")[0] as HTMLCanvasElement
const drawer = new PlotDrawer(canvas)

function resizeCanvas() {
    const {width, height} = $("#plot")[0].getBoundingClientRect();
    canvas.width = width;
    canvas.height = height;

    drawer.update()
}

window.addEventListener("resize", () => resizeCanvas());
window.addEventListener("load", () => resizeCanvas());

document.addEventListener("rUpdated", (e) => {
    R_OBSERVER.value = Number((e as CustomEvent).detail.r)
});

$("#plot > canvas").on("click", function (e) {
    if (R_OBSERVER.value === null) {
        addToast({
            title: "Невозможно обработать клик",
            message: "Выберите 1 радиус",
            style: TOAST_VARIANTS.warning
        })

        return
    }

    const rect = this.getBoundingClientRect();
    const canvasX = e.clientX - rect.left;
    const canvasY = e.clientY - rect.top;

    const x = ((canvasX - rect.width / 2) / drawer.options.step / 4) * R_OBSERVER.value;
    const y = (-(canvasY - rect.height / 2) / drawer.options.step / 4) * R_OBSERVER.value;

    const xInput = $('[name$=":xVal"]')
    const yInput = $('[name$=":yVal"]')
    const checkBtn = $('[name$=":check-btn"]')

    const xPrev = xInput.val()
    const yPrev = yInput.val()

    xInput.val(Math.round(x * 100) / 100)
    yInput.val(Math.round(y * 100) / 100)

    checkBtn.trigger("click")

    xInput.val(xPrev)
    yInput.val(yPrev).trigger("change") // костыль, чтобы на сервере вернулись значения инпутов хД

    $('#results .pagination a:nth-child(5)').trigger('click') // еще один костыль, чтобы менялась страница таблицы
})

RESULTS_OBSERVER.onChange(results => {
    drawer.setCustomShapes(results.map(r => {
        return new Point(
            {x: r.x, y: r.y},
            0.025,
            true,
            r.contains ? "green" : "red"
        )
    }))
})