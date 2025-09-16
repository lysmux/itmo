import jsx from "../../jsx/pragma";
import styles from "./Plot.module.scss"
import ref from "../../jsx/ref";
import PlotDrawer from "./plotDrawer";
import {Observer} from "../../jsx/observer";

export default function Plot() {
    const canvasRef = ref<HTMLCanvasElement>();
    const containerRef = ref<HTMLDivElement>();

    const drawerRef = new Observer<PlotDrawer>(null);
    const rObserver = new Observer<number>(4);

    containerRef.onChange((container) => {
        if (container === null) return;

        const resizeObserver = new ResizeObserver(entries => {
            const { width, height } = container.getBoundingClientRect();
            console.log(width, height);

            canvasRef.value.width = width;
            canvasRef.value.height = height;
            drawerRef.value.update()
        });
        resizeObserver.observe(containerRef.value);
    })

    canvasRef.onChange((canvas) => {
        if (canvas === null) return;
        drawerRef.value = new PlotDrawer(canvas, rObserver)
    })

    return <div ref={containerRef} className={styles.container}>
        <canvas ref={canvasRef} className={styles.plot}></canvas>
    </div>
}