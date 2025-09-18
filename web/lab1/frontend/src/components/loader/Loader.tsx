import jsx from "../../jsx/pragma";
import {Observer} from "../../observer";
import styles from "./Loader.module.scss"
import ref from "../../jsx/ref";

interface LoaderProps {
    isVisible: Observer<boolean>
}

export default function Loader({isVisible}: LoaderProps) {
    const loaderRef = ref<HTMLDivElement>()
    loaderRef.onChange(loader => {
        if (loader === null) return

        isVisible.onChange(visible => {
            if (visible) loader.classList.remove(styles.hidden)
            else loader.classList.add(styles.hidden)
            console.log(visible)
        })
    })

    return <div ref={loaderRef} className={styles.block}>
        <div className={styles.loader}></div>
    </div>
}