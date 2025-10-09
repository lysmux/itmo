import {TABLE_DATA_OBSERVER} from "./global";
import $ from "jquery"

const resultsTable = $("#results")[0] as HTMLTableElement;

type TableRow = Record<string, any>;

TABLE_DATA_OBSERVER.onChange(results => {
    const rows = results.flatMap(result => {
        return result.checks.map(item => {
            return {
                "Время": new Date(result.time).toLocaleTimeString(),
                "X": item.x,
                "Y": item.y,
                "R": item.r,
                "Попадание": item.contains ? "Да" : "Нет",
                "Время выполнения": `${result.executionTime} нс`,
            }
        })
    })

    renderTable(resultsTable, rows)
})

function renderTable(table: HTMLTableElement, rows: TableRow[]) {
    table.innerHTML = "";

    const head = table.createTHead()
    const body = table.createTBody()

    const headRow = head.insertRow()

    if (rows.length === 0) {
        headRow.insertCell().textContent = "Нет данных"
        return
    }

    const keys = new Set(rows.flatMap(row => Object.keys(row)))

    keys.forEach(key => {
        headRow.insertCell().innerText = key
    })

    rows.forEach(row => {
        const bodyRow = body.insertRow()

        keys.forEach(key => {
            const cell = bodyRow.insertCell()
            const value = row[key]

            if (value === undefined || value === null) {
                cell.textContent = "-"
            } else {
                cell.textContent = value
            }
        })
    })
}