import jsx from "../../jsx/pragma";
import Input from "./input/Input";
import Radio from "./input/Radio";
import Checkbox from "./input/Checkbox";
import styles from "./Form.module.scss"
import {ObjectObserver} from "../../jsx/observer";

interface formProps {
    x: number;
    y: number;
    r: number;
}

export default function Form() {
    const formData = {}

    return <form style="color: white">
        <table className={styles.table}>
            <tbody>
            <tr>
                <td><label>Input</label></td>
                <td><Input /></td>
            </tr>
            <tr>
                <td><label>Input</label></td>
                <td><Radio /></td>
            </tr>
            <tr>
                <td><label>Input</label></td>
                <td><Checkbox /></td>
            </tr>
            <tr>
                <td colSpan="2">
                    <button type="submit">Проверить</button>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
}