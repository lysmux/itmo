import jsx from "../../jsx/pragma";
import {ArrayObserver} from "../../observer";
import ref from "../../jsx/ref";
import styles from "./Table.module.scss"

type TableRow = Record<string, any>;

interface TableProps {
    valuesObserver: ArrayObserver<TableRow>
}

export default function Table({valuesObserver}: TableProps) {
    const tableRef = ref<HTMLTableElement>()

    tableRef.onChange((table) => {
        if (table === null) return
        valuesObserver.onChange(
            (rows) => {
                renderTable(table, rows)
            }
        )
    })

    return <div className={styles.container}>
        <table ref={tableRef} className={styles.table}></table>
    </div>
}

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