import jsx from "../../jsx/pragma";
import styles from "./Toast.module.scss"
import CloseIcon from "../../assets/icons/close.svg?source"
import ErrorIcon from "../../assets/icons/error.svg?source"
import SuccessIcon from "../../assets/icons/success.svg?source"
import InfoIcon from "../../assets/icons/info.svg?source"
import WarningIcon from "../../assets/icons/warning.svg?source"
import ref from "../../jsx/ref";
import Svg from "../../jsx/Svg";

type ToastVariant = 'success' | 'error' | 'info' | 'warning';
type ToastVariantsConfig = Record<ToastVariant, ToastStyle>

export const TOAST_VARIANTS: ToastVariantsConfig = {
    success: {
        className: styles.success,
        svgIcon: SuccessIcon
    },
    error: {
        className: styles.error,
        svgIcon: ErrorIcon
    },
    info: {
        className: styles.info,
        svgIcon: InfoIcon
    },
    warning: {
        className: styles.warning,
        svgIcon: WarningIcon
    }
}

interface ToastStyle {
    className?: string;
    svgIcon?: string
}

interface ToastProps {
    title: string;
    message: string;
    style?: ToastStyle
}

export default function Toast({title, message, style}: ToastProps) {
    const progressRef = ref<HTMLTableCellElement>()
    const toastRef = ref<HTMLTableCellElement>()

    toastRef.onChange((value) => {
        if (value === null) return
        value.addEventListener("animationend", (e) => {
            if (e.animationName === styles.disappear) toastRef.value.remove();
        })
    })

    progressRef.onChange((value) => {
        if (value === null) return
        value.addEventListener("animationend", () => toastRef.value.classList.add(styles.disappearAnimation));
    })

    return <table className={`${styles.container} ${style.className}`} ref={toastRef}>
        <tbody>
        <tr>
            <td>
                <table className={styles.content}>
                    <tbody>
                    <tr>
                        <td style="width: 30px">
                            <Svg icon={style.svgIcon} className={styles.icon}/>
                        </td>
                        <td style="width: 100%;vertical-align: middle">
                            <h1 className={styles.title}>{title}</h1>
                        </td>
                        <td style="width: 30px">
                            <button
                                className={styles.closeBtn}
                                onclick={
                                    () => toastRef.value.classList.add(styles.disappearAnimation)
                                }
                            >
                                <Svg icon={CloseIcon} className={styles.icon}/>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td colSpan="2">
                            <p className={styles.message}>{message}</p>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr>
            <td className={styles.progress} ref={progressRef}></td>
        </tr>
        </tbody>
    </table>;
};