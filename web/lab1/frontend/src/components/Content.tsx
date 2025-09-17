import jsx from "../jsx/pragma";
import Toast, {ToastStyle} from "./toast/Toast";
import Plot from "./plot/Plot";
import styles from "./Content.module.scss"
import Form from "./form/Form";
import Table from "./table/Table";
import {ArrayObserver} from "../observer";
import ref from "../jsx/ref";
import {setVar} from "../utils/context";
import {CheckResponse} from "./types";

export default function Content() {
    const tableValuesObserver = new ArrayObserver<Record<string, any>>([])
    setVar("addResult", (
        {x, y, r}: {x: number, y: number, r: number},
        result: CheckResponse
    ) => {
        tableValuesObserver.value.push({
            "Время": new Date().toLocaleTimeString(),
            "X": x,
            "Y": y,
            "R": r,
            "Попадание": result.contains ? "Да" : "Нет",
            "Время выполнения": `${result.executionTime} нс`,
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
                <div className={styles.plotBlock}><Plot/></div>
            </td>
            <td>
                <div className={styles.formBlock}><Form/></div>
            </td>
        </tr>
        <tr>
            <td>
                <div ref={toastContainerRef} className={styles.toastContainer}>
                    {/*<Toast title="Success" message="very long toast content ajkfwedjksdjkdjasasasasasasasaasasasaskf"*/}
                    {/*       style={TOAST_VARIANTS.success}/>*/}
                    {/*<Toast title="Info" message="abc" style={TOAST_VARIANTS.info}/>*/}
                    {/*<Toast title="Warning" message="abc" style={TOAST_VARIANTS.warning}/>*/}
                    {/*<Toast title="Error" message="abc" style={TOAST_VARIANTS.error}/>*/}
                </div>
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