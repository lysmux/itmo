import jsx from "../../../jsx/pragma";
import styles from "./Input.module.scss"

interface InputProps {
    type: string,
    placeholder?: string;
    onInput?: (event: InputEvent) => void;
    value?: string,
    minLength?: number,
    maxLength?: number,
    min?: number;
    max?: number;
}

export default function Input(props: InputProps) {
    return <input
        type={props.type}
        value={props.value || ""}
        placeholder={props.placeholder}
        maxLength={props.maxLength}
        minLength={props.minLength}
        min={props.min}
        max={props.max}
        oninput={(event: InputEvent) => props.onInput(event)}
        className={styles.input}
    />
}