import $ from "jquery";
import PlotDrawer from "./plotDrawer";
import {R_OBSERVER} from "../global";
import {CheckResponse} from "../types";
import superagent from "superagent";
import {addTableData} from "../table";

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


$("#plot > canvas").on("click", function (e) {
    if (R_OBSERVER.value === null) {
        alert("Please, enter R value")

        return
    }

    const rect = this.getBoundingClientRect();
    const canvasX = e.clientX - rect.left;
    const canvasY = e.clientY - rect.top;

    const x = (((canvasX - rect.width / 2) / drawer.options.step / 4) * R_OBSERVER.value).toFixed(4);
    const y = ((-(canvasY - rect.height / 2) / drawer.options.step / 4) * R_OBSERVER.value).toFixed(4);

    superagent
        .get("?")
        .query({
            x: [x],
            y: y,
            r: [R_OBSERVER.value],
        })
        .then(res => {
            const result = res.body as CheckResponse;

            result.checks.forEach(check => {
                addTableData($("#results")[0] as HTMLTableElement, [new Date(result.time).toLocaleTimeString(), check.x, check.y, check.r, check.contains, result.executionTime])
            })
        })
})