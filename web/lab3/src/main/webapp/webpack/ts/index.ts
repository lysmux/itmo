import "../scss/style.scss"
import "./header";
import "./plot/plot";
import "./toast"
import {Result} from "./types";

import $ from "jquery";
import {RESULTS_OBSERVER} from "./global";

$(updateResults)
document.addEventListener("resultsUpdated", updateResults)

function updateResults() {
    const rows = $('#results tbody tr')

    RESULTS_OBSERVER.value = rows
        .map((idx, el) => $(el).find("td"))
        .filter((idx, cells) => cells.text() !== "")
        .map((idx, cells): Result => {
        return {
            x: Number(cells.eq(1).text()),
            y: Number(cells.eq(2).text()),
            r: Number(cells.eq(3).text()),
            contains: cells.eq(4).text().trim() == "Да"
        }
    }).get()
}