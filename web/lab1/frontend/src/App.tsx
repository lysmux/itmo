import jsx from "./jsx/pragma"
import Header from "./components/header/Header";
import Content from "./components/Content";
import styles from "./App.module.scss"
import {CheckResponse} from "./components/types";
import useObserver from "./observer";
import {setVar} from "./utils/context";


const App = () => {
    const resultsObs = useObserver<CheckResponse[]>(JSON.parse(localStorage.getItem("results")) || [])
    resultsObs.onChange(results => {
        localStorage.setItem("results", JSON.stringify(results));
    })
    setVar("resultsObs", resultsObs)

    return (
        <table className={styles.container}>
            <tbody>
            <tr><td><Header/></td></tr>
            <tr className={styles.content}><td><Content /></td></tr>
            </tbody>
        </table>
    )
}

export default App