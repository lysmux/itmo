import "../scss/style.scss"
import "./header";
import "./plot/plot";
import "./toast"
import {Result} from "./types";

import $ from "jquery";
import {RESULTS_OBSERVER} from "./global";

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

$(updateResults)

const parentObserver = new MutationObserver(function (mutations) {
    mutations.forEach(function (mutation) {
        mutation.addedNodes.forEach(function (node) {
            if (node.nodeType === Node.ELEMENT_NODE && (node as HTMLElement).id === 'results') {
                updateResults()
            }
        });
    });
});

parentObserver.observe(document.body, {
    childList: true,
    subtree: true
});
