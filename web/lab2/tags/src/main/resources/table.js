class TableSorter {
    constructor(tableId) {
        this.table = document.getElementById(tableId);
        this.currentSort = {
            column: null,
            direction: 'asc'
        };
        this.init();
    }

    init() {
        const headers = this.table.querySelectorAll('th[data-sortable="true"]');
        headers.forEach(header => {
            header.addEventListener('click', () => {
                this.sortTable(header.dataset.property);
            });
        });
    }

    sortTable(column) {
        const tbody = this.table.querySelector('tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));
        const header = this.table.querySelector(`th[data-property="${column}"]`);
        const columnIndex = Array.from(header.parentNode.children).indexOf(header);

        if (this.currentSort.column === column) {
            this.currentSort.direction = this.currentSort.direction === 'asc' ? 'desc' : 'asc';
        } else {
            this.currentSort.column = column;
            this.currentSort.direction = 'asc';
        }

        rows.sort((a, b) => {
            const aValue = a.children[columnIndex].textContent.trim();
            const bValue = b.children[columnIndex].textContent.trim();

            const aNum = Number(aValue);
            const bNum = Number(bValue);

            let comparison = 0;

            if (!isNaN(aNum) && !isNaN(bNum)) {
                comparison = aNum - bNum;
            } else {
                comparison = aValue.localeCompare(bValue, 'ru');
            }

            return this.currentSort.direction === 'asc' ? comparison : -comparison;
        });

        tbody.innerHTML = '';
        rows.forEach(row => tbody.appendChild(row));

        this.updateSortIndicators(header);
    }

    updateSortIndicators(currentHeader) {
        const allIndicators = this.table.querySelectorAll('._sort-indicator');
        allIndicators.forEach(indicator => {
            indicator.textContent = '↕';
        });

        const currentIndicator = currentHeader.querySelector('._sort-indicator');
        if (currentIndicator) {
            currentIndicator.textContent = this.currentSort.direction === 'asc' ? '↑' : '↓';
        }
    }
}
