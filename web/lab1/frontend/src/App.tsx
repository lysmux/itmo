import jsx from "./jsx/pragma"
import Header from "./components/header/Header";
import Content from "./components/Content";
import styles from "./App.module.scss"

const App = () => {
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