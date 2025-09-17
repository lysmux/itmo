import jsx from "../../../jsx/pragma";
import styles from "./Checkbox.module.scss"

interface CheckboxProps {
    group: string;
    value: string;
    onChange?: (value: string, checked: boolean) => void;
}

export default function Checkbox({group, value, onChange}: CheckboxProps) {
    const id = `checkbox-${group}-${value}`;

    return <div className={styles.checkbox}>
        <input
            id={id}
            type="checkbox"
            name={group}
            value={value}
            onchange={
                (event: InputEvent) => {
                    onChange(value, (event.target as HTMLInputElement).checked)
                }
            }
        />
        <label htmlFor={id}>{value}</label>
    </div>
}