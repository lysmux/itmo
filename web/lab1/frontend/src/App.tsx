import jsx from "./jsx/pragma"
import Header from "./components/header/Header";
import Content from "./components/Content";
import styles from "./App.module.scss"
import {CheckResponse} from "./components/types";
import useObserver from "./observer";
import {setVar} from "./utils/context";

function getDefaultTheme(): string {
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches){
        return "dark";
    }
    return "light"
}

export default function App() {
    const resultsObs = useObserver<CheckResponse[]>(JSON.parse(localStorage.getItem("results")) || [])
    resultsObs.onChange(results => {
        localStorage.setItem("results", JSON.stringify(results));
    })
    setVar("resultsObs", resultsObs)

    const themeObs = useObserver<string>(localStorage.getItem("theme") || getDefaultTheme())
    themeObs.onChange(theme => {
        localStorage.setItem("theme", theme)
        document.documentElement.classList.forEach((class_) => {
            if (class_.startsWith("theme-")) document.documentElement.classList.remove(class_);
        })
        document.documentElement.classList.add(`theme-${theme}`);
    })
    setVar("themeObs", themeObs)


    return (
        <table className={styles.container}>
            <tbody>
            <tr>
                <td><Header/></td>
            </tr>
            <tr className={styles.content}>
                <td><Content/></td>
            </tr>
            </tbody>
        </table>
    )
}
