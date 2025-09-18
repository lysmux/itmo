import jsx from "../jsx/pragma";
import Toast, {ToastStyle} from "./toast/Toast";
import Plot from "./plot/Plot";
import styles from "./Content.module.scss"
import Form from "./form/Form";
import Table from "./table/Table";
import useObserver, {ArrayObserver} from "../observer";
import ref from "../jsx/ref";
import {getVar, setVar} from "../utils/context";
import {CheckResponse} from "./types";
import PlotDrawer from "./plot/plotDrawer";

export default function Content() {
    const plotDrawer = useObserver<PlotDrawer>()
    setVar("plotDrawer", plotDrawer)

    const tableValuesObserver = new ArrayObserver<Record<string, any>>([])

    const resultsObs = getVar<ArrayObserver<CheckResponse>>("resultsObs")
    resultsObs.onChange(results => {
        tableValuesObserver.value.length = 0

        results.forEach(result => {
            result.contains.forEach(item => {
                tableValuesObserver.value.push({
                    "Время": result.time,
                    "X": item.x,
                    "Y": item.y,
                    "R": item.r,
                    "Попадание": item.contains ? "Да" : "Нет",
                    "Время выполнения": `${result.executionTime} нс`,
                })
            })
        })
    })

    const toastContainerRef = ref<HTMLDivElement>()
    setVar("addToast", (options: {
        title: string;
        message: string;
        style: ToastStyle;
    }) => {
        toastContainerRef.value.prepend(
            <Toast title={options.title} message={options.message} style={options.style}/>
        )
    })

    return <table className={styles.content}>
        <tbody>
        <tr>
            <td>
                <div className={styles.plotBlock}><Plot drawer={plotDrawer}/></div>
            </td>
            <td>
                <div className={styles.formBlock}><Form/></div>
            </td>
        </tr>
        <tr>
            <td>
                <div ref={toastContainerRef} className={styles.toastContainer} />
            </td>
        </tr>
        <tr>
            <td colSpan="2">
                <div className={styles.tableBlock}>
                    <Table valuesObserver={tableValuesObserver}/>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
}