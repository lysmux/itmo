import jsx from "../../jsx/pragma";
import styles from "./Plot.module.scss"
import ref from "../../jsx/ref";
import PlotDrawer from "./plotDrawer";
import {Observer} from "../../observer";

interface PlotProps {
    drawer?: Observer<PlotDrawer>;
}

export default function Plot({drawer = new Observer()}: PlotProps) {
    const canvasRef = ref<HTMLCanvasElement>();
    const containerRef = ref<HTMLDivElement>();

    containerRef.onChange((container) => {
        if (container === null) return;
        const resizeObserver = new ResizeObserver(entries => {
            const {width, height} = container.getBoundingClientRect();

            canvasRef.value.width = width;
            canvasRef.value.height = height;
            drawer.value.update()
        });
        resizeObserver.observe(containerRef.value);
    })

    canvasRef.onChange((canvas) => {
        if (canvas === null) return;
        drawer.value = new PlotDrawer(canvas)
    })

    return <div ref={containerRef} className={styles.container}>
        <canvas ref={canvasRef} className={styles.plot}></canvas>
    </div>
}