import jsx from "../../jsx/pragma"
import styles from "./Header.module.scss"

export default function Header() {
    return <header className={styles.header}>
        <table>
            <tbody>
            <tr><td className={styles.title}>Разыграев Кирилл Сергеевич</td></tr>
            <tr><td className={styles.subtitle}>Группа: P3215</td></tr>
            <tr><td className={styles.subtitle}>Вариант: 467213</td></tr>
            </tbody>
        </table>
    </header>
}