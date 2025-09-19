import jsx from "../../jsx/pragma"
import styles from "./Header.module.scss"
import {getVar} from "../../utils/context";
import {Observer} from "../../observer";
import Svg from "../Svg";
import Sun from "../../assets/icons/sun.svg?source"
import Moon from "../../assets/icons/moon.svg?source"
import ref from "../../jsx/ref";

export default function Header() {
    // const themeIndicatorRef = ref<HTMLDivElement>()
    //
    // const themeObs = getVar<Observer<string>>("themeObs")
    // function setTheme(theme: string) {
    //     themeObs.value = theme
    //
    //     switch (theme) {
    //         case "dark":
    //             themeIndicatorRef.value.classList.add(styles.dark)
    //             break
    //         case "light":
    //             themeIndicatorRef.value.classList.remove(styles.dark)
    //     }
    // }

    return <header className={styles.header}>
        <table>
            <tbody>
            <tr>
                <td className={styles.title}>Разыграев Кирилл Сергеевич</td>
            </tr>
            <tr>
                <td className={styles.subtitle}>Группа: P3215</td>
            </tr>
            <tr>
                <td className={styles.subtitle}>Вариант: 467213</td>
            </tr>
            </tbody>
        </table>
        {/*<div className={styles.themeSwitcher}>*/}
        {/*    <div ref={themeIndicatorRef} className={styles.themeIndicator}></div>*/}
        {/*    <div className={styles.themeRadio}>*/}
        {/*        <input type="radio" id="light-theme" name="theme" checked*/}
        {/*               onchange={() => setTheme("light")}/>*/}
        {/*        <label htmlFor="light-theme">*/}
        {/*            <Svg icon={Sun} className={styles.themeIcon}/>*/}
        {/*        </label>*/}
        {/*    </div>*/}
        {/*    <div className={styles.themeRadio}>*/}
        {/*        <input type="radio" id="dark-theme" name="theme" onchange={() => setTheme("dark")}/>*/}
        {/*        <label htmlFor="dark-theme">*/}
        {/*            <Svg icon={Moon} className={styles.themeIcon}/>*/}
        {/*        </label>*/}
        {/*    </div>*/}
        {/*</div>*/}
    </header>
}