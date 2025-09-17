import jsx from "../../../jsx/pragma";
import styles from "./Input.module.scss"

interface InputProps {
    type: string,
    placeholder?: string;
    onInput?: (value: string) => void;
    value?: string
}

export default function Input(props: InputProps) {
    return <input
        type={props.type}
        value={props.value}
        placeholder={props.placeholder}
        oninput={(event: InputEvent) => props.onInput((event.target as HTMLInputElement).value)}
        className={styles.input}
    />
}