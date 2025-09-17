import jsx from "../../jsx/pragma";
import Input from "./input/Input";
import Checkbox from "./input/Checkbox";
import styles from "./Form.module.scss"
import {createRange} from "../../utils/range";
import useObserver from "../../observer";
import {MaxConstraint, MinConstraint, RequiredConstraint} from "../../validator/constrains";
import Validator from "../../validator/validator";
import {CheckResponse, Coordinates} from "../types";
import ref from "../../jsx/ref";
import ApiClient from "../../api/api";
import {getVar} from "../../utils/context";
import {TOAST_VARIANTS, ToastStyle} from "../toast/Toast";


export default function Form() {
    const submitBtnRef = ref<HTMLButtonElement>();

    const formData = useObserver<Partial<Coordinates>>({
        x: new Set(),
        y: null,
        r: new Set()
    })

    const rules = {
        x: [
            new RequiredConstraint(),
        ],
        y: [
            new RequiredConstraint(),
            new MinConstraint(-3),
            new MaxConstraint(5),
        ],
        r: [
            new RequiredConstraint(),
        ],
    }
    const validator = new Validator(formData, rules)

    submitBtnRef.onChange(btn => {
        if (btn === null) return;
        validator.isValid.onChange(valid => btn.disabled = !valid)
    })

    function submit(event: SubmitEvent) {
        event.preventDefault();

        new ApiClient("http://localhost:8000/fcgi-bin/app.jar")
            .post<CheckResponse>("/check", {
                x: Array.from(formData.value.x)[0],
                y: formData.value.y,
                r: Array.from(formData.value.r)[0]
            })
            .then((response) => {
                const addResult = getVar<({x, y, r}: {x: number, y: number, r: number}, result: CheckResponse) => void>("addResult")
                addResult({x: Array.from(formData.value.x)[0],
                    y: formData.value.y,
                    r: Array.from(formData.value.r)[0]}, response)
            })
            .catch((error) => {
                const addToast = getVar<(options: {
                    title: string;
                    message: string;
                    style: ToastStyle;
                }) => void>("addToast")
                addToast({
                    title: `API ERROR | ${error.status}`,
                    message: error.response.text,
                    style: TOAST_VARIANTS.error
                })
            })
    }

    return <form className={styles.form}>
        <table>
            <tbody>
            <tr>
                <td>
                    <div className={styles.block}>
                        <h1>X</h1>
                        <div className={styles.inputGroup}>
                            {
                                createRange(-4, 5).map(x => {
                                    return <Checkbox group="x" value={x.toString()} onChange={(x, checked) => {
                                        const xValues = formData.value.x;
                                        if (checked) xValues.add(Number(x));
                                        else xValues.delete(Number(x));
                                        formData.value.x = new Set(xValues)
                                    }}/>
                                })
                            }
                        </div>
                    </div>
                </td>
                <td>
                    <div className={styles.block}>
                        <h1>R</h1>
                        <div className={styles.inputGroup}>
                            {
                                createRange(1, 6).map(r => {
                                    return <Checkbox group="r" value={r.toString()} onChange={(r, checked) => {
                                        const rValues = formData.value.r;
                                        if (checked) rValues.add(Number(r));
                                        else rValues.delete(Number(r));
                                        formData.value.r = new Set(rValues)
                                    }}/>
                                })
                            }
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td colSpan="2">
                    <div className={styles.block}>
                        <h1>Y</h1>
                        <Input type="number" placeholder="Введите Y" onInput={
                            value => {
                                formData.value.y = value === '' ? null : Number(value);
                            }
                        }/>
                    </div>
                </td>
            </tr>
            <tr>
                <td colSpan="2">
                    <button ref={submitBtnRef} type="submit" onclick={submit} className={styles.btn}>Проверить</button>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
}