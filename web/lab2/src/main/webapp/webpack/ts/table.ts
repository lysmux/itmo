export function addTableData(table: HTMLTableElement, row: any[]) {
    const body = table.querySelector("tbody");
    const bodyRow = body.insertRow();

    row.forEach(value => {
        const cell = bodyRow.insertCell()
        cell.textContent = value
    });
}

export function clearTableData(table: HTMLTableElement) {
    const body = table.querySelector("tbody");
    body.innerHTML = "";
}