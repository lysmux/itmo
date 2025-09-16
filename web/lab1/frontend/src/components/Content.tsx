import jsx from "../jsx/pragma";
import Toast, {TOAST_VARIANTS} from "./toast/Toast";
import Plot from "./plot/Plot";
import styles from "./Content.module.scss"
import Form from "./form/Form";
import Table from "./table/Table";
import {ArrayObserver} from "../jsx/observer";

export default function Content() {
    const tableValuesObserver = new ArrayObserver<Record<string, any>>([
        {
            "Время": "14:01:12",
            "X": "1",
            "Y": "2",
            "R": "3",
            "Попадание": "Да",
            "Время выполнения": "10с",
        },
        {
            "Время": "14:01:12",
            "X": "1",
            "Y": "2",
            "R": "3",
            "Попадание": "Да",
            "Время выполнения": "10с",
        },

    ])

    return <table className={styles.content}>
        <tbody>
        <tr>
            <td>
                <div className={styles.plotBlock}><Plot/></div>
            </td>
            <td><div className={styles.formBlock}><Form/></div></td>
        </tr>
        <tr>
            <td>
                <div className={styles.toastContainer}>
                    <Toast title="Success" message="very long toast content ajkfwedjksdjkdjasasasasasasasaasasasaskf"
                           style={TOAST_VARIANTS.success}/>
                    <Toast title="Info" message="abc" style={TOAST_VARIANTS.info}/>
                    <Toast title="Warning" message="abc" style={TOAST_VARIANTS.warning}/>
                    <Toast title="Error" message="abc" style={TOAST_VARIANTS.error}/>
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