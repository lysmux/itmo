import jsx from "../jsx/pragma";
import styles from "./Toast.module.scss"

interface ToastProps {
    title: string;
    message: string;
}

export default function Toast({title, message}: ToastProps) {
    return <div className={styles.container}>
        <h1 className={styles.title}>{title}</h1>
        <p className={styles.message}>{message}</p>
    </div>;
};