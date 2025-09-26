import jsx from "../../jsx/pragma";
import styles from "./Plot.module.scss"
import ref from "../../jsx/ref";
import useObserver, {Observer} from "../../observer";
import * as THREE from "three";
import {Sky} from "three/examples/jsm/objects/Sky";
import {OrbitControls} from "three/examples/jsm/controls/OrbitControls";
import {Group, Tween, Easing} from '@tweenjs/tween.js'
import {GLTFLoader} from "three/examples/jsm/loaders/GLTFLoader";
import {Line2} from "three/examples/jsm/lines/Line2.js";
import {LineMaterial} from "three/examples/jsm/lines/LineMaterial.js";
import {LineGeometry} from "three/examples/jsm/lines/LineGeometry";
import {TextGeometry} from "three/examples/jsm/geometries/TextGeometry";
import {FontLoader} from "three/examples/jsm/loaders/FontLoader";

const SCALE = 2
const SHOW_HELICOPTER_PATH = false

const LINE_COLOR = "#007e00";
const PAINT_COLOR = "#007e00";
const EXTRUDE_COLOR = "#0000ff"
const EXTRUDE_EDGE_COLOR = "#ff0000"
const LABEL_COLOR = "#ff6a00"

const LABEL_FONT_URL = "fonts/helvetiker_regular.typeface.json"

const LINE_POINTS_COUNT = 500

const HELICOPTER_OFFSET_X = 0.8
const HELICOPTER_OFFSET_Z = 0.8

const LABEL_POSITIONS = [
    new THREE.Vector3(1, 0),
    new THREE.Vector3(-1, 0),
    new THREE.Vector3(0, 1),
    new THREE.Vector3(0, -1)
]

interface PlotProps {
    radiusObs?: Observer<number>;
}

export default function Plot({radiusObs = useObserver(2)}: PlotProps) {
    const canvasRef = ref<HTMLCanvasElement>();
    const containerRef = ref<HTMLDivElement>();
    const rendererObs = useObserver<THREE.WebGLRenderer>()
    const controlsObs = useObserver<OrbitControls>()

    const animationGroup = new Group()
    const animationMixers: THREE.AnimationMixer[] = []

    // materials
    const extrudeMaterial = new THREE.MeshBasicMaterial({color: EXTRUDE_COLOR})
    const paintMaterial = new THREE.MeshBasicMaterial({color: PAINT_COLOR})
    const lineMaterial = new THREE.MeshBasicMaterial({color: LINE_COLOR});
    const helicopterPathMaterial = new THREE.LineBasicMaterial({color: "red"});
    const edgeMaterial = new LineMaterial({
        color: EXTRUDE_EDGE_COLOR,
        linewidth: 3,
        worldUnits: false,
    });
    const labelMaterial = new THREE.MeshPhongMaterial({
        color: LABEL_COLOR,
        shininess: 100
    });
    // materials

    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(75);
    camera.position.z = 8
    camera.position.x = 3

    scene.fog = new THREE.Fog(0xcccccc, 10, 12);

    // Light
    const directionalLight = new THREE.DirectionalLight(0xffffff, 5);
    directionalLight.position.set(5, 10, 7.5);
    directionalLight.castShadow = true;
    scene.add(directionalLight);
    // Light

    // Sky
    const sky = new Sky();
    sky.scale.setScalar(450000);

    const phi = THREE.MathUtils.degToRad(90);
    const theta = THREE.MathUtils.degToRad(180);
    sky.material.uniforms.sunPosition.value = new THREE.Vector3().setFromSphericalCoords(1, phi, theta);

    scene.add(sky);
    // Sky

    // Coords helper
    const gridHelperX = new THREE.GridHelper(10, 20, "red");
    gridHelperX.rotation.x = Math.PI / 2;

    const gridHelperY = new THREE.GridHelper(10, 20, "green");
    gridHelperY.rotation.y = Math.PI / 2;

    const gridHelperZ = new THREE.GridHelper(10, 20, "orange");
    gridHelperZ.rotation.z = Math.PI / 2;

    scene.add(gridHelperX, gridHelperY, gridHelperZ);
    // Coords helper

    // Text

    const labelsGroup = new THREE.Group();
    const fontLoader = new FontLoader();
    fontLoader.load(LABEL_FONT_URL, function (font) {
        radiusObs.onChange(radius => {
            labelsGroup.clear()

            const text = radius ? radius.toString() : "R";
            const textGeometry = new TextGeometry(text, {
                font: font,
                size: 0.3,
                depth: 0.2,
                curveSegments: 12,
                bevelEnabled: false
            });

            textGeometry.computeBoundingBox();
            const textWidth = textGeometry.boundingBox.max.x - textGeometry.boundingBox.min.x;
            textGeometry.translate(-textWidth / 2, 0, 0);

            LABEL_POSITIONS.forEach(pos => {
                const textMesh = new THREE.Mesh(textGeometry, labelMaterial);
                textMesh.position.copy(pos.clone().multiplyScalar(SCALE))
                textMesh.position.x *= 1.1 // смещаем, чтобы не находилось внутри фигуры

                labelsGroup.add(textMesh);
            })
        })
    });
    scene.add(labelsGroup)
    // Text

    // Shape
    const shape = new THREE.Shape();
    shape.moveTo(0, 0);
    shape.lineTo(-1, 0);
    shape.lineTo(-1, 1);
    shape.lineTo(0, 1);
    shape.absarc(0, 0, 1, Math.PI / 2, 0, true);
    shape.lineTo(0.5, 0);
    shape.lineTo(0.5, -0.5);
    shape.lineTo(0, 0);

    const linePoints = shape.getSpacedPoints(LINE_POINTS_COUNT)

    let tube: THREE.Mesh = null;

    function createTube(progress: number) {
        if (tube) scene.remove(tube);

        const curPoints = linePoints.slice(0, Math.floor(progress * LINE_POINTS_COUNT))
        const curPoints3D = curPoints.map(point => new THREE.Vector3(point.x, point.y, 0));

        const subCurve = new THREE.CatmullRomCurve3(curPoints3D);
        const geometry = new THREE.TubeGeometry(subCurve, 100, 0.02, 12, false);
        tube = new THREE.Mesh(geometry, lineMaterial);
        tube.scale.set(SCALE, SCALE, SCALE)
        scene.add(tube);
    }

    const extrudeGeometry = new THREE.ExtrudeGeometry(shape, {bevelEnabled: false})
    const extrude = new THREE.Mesh(extrudeGeometry, extrudeMaterial)
    extrude.visible = false

    const edgesGeometry = new THREE.EdgesGeometry(extrudeGeometry, 15);
    const positions = edgesGeometry.attributes.position.array;

    const lineGeometry = new LineGeometry();
    lineGeometry.setPositions(new Float32Array(positions));

    const edges = new Line2(lineGeometry, edgeMaterial);
    extrude.add(edges);
    scene.add(extrude);

    const lineExtrudeAnimation = new Tween({progress: 0})
        .to({progress: 1}, 2000)
        .easing(Easing.Quadratic.Out)
        .onStart(() => {
            extrude.visible = true;
        })
        .onUpdate(({progress}) => {
            extrude.scale.set(SCALE, SCALE, progress * SCALE)
        })
        .group(animationGroup)

    new Tween({progress: 0})
        .to({progress: 1}, 2000)
        .onUpdate(({progress}) => {
            createTube(progress)
        })
        .onComplete(() => {
            if (tube) tube.removeFromParent()
        })
        .chain(lineExtrudeAnimation)
        .group(animationGroup)
        .start()
    // Shape

    // Helicopter path
    const curve = new THREE.CatmullRomCurve3([
        new THREE.Vector3(-10, SCALE + 1, 10),
        new THREE.Vector3(-SCALE / 2, SCALE + 1, SCALE / 2),
        new THREE.Vector3(5, SCALE + 1, -10),
    ]);

    if (SHOW_HELICOPTER_PATH) {
        const points = curve.getPoints(50);
        const geometry = new THREE.BufferGeometry().setFromPoints(points);
        const curveObject = new THREE.Line(geometry, helicopterPathMaterial);
        scene.add(curveObject);
    }
    // Helicopter path

    // Paint
    const paintGeometry = new THREE.SphereGeometry(0.1, 32, 16);
    const paint = new THREE.Mesh(paintGeometry, paintMaterial);
    const paintPosition = {x: 0, y: SCALE + 1, z: 0};
    const paintAnimation = new Tween(paintPosition)
        .to({y: SCALE}, 500)
        .onStart(() => {
            paint.position.set(paintPosition.x, paintPosition.y, paintPosition.z);
            scene.add(paint);
        })
        .onUpdate(({y}) => {
            paint.position.y = y;
        })
        .onComplete(() => {
            paint.removeFromParent()

            const newColor = new THREE.Color(PAINT_COLOR)
            new Tween({
                r: extrudeMaterial.color.r,
                g: extrudeMaterial.color.g,
                b: extrudeMaterial.color.b,
            })
                .to({
                    r: newColor.r,
                    g: newColor.g,
                    b: newColor.b,
                }, 300)
                .easing(Easing.Quadratic.In)
                .onUpdate((color) => {
                    extrudeMaterial.color.setRGB(color.r, color.g, color.b)
                })
                .group(animationGroup)
                .start()
        })
        .group(animationGroup)

    // Helicopter
    const loader = new GLTFLoader();
    loader.load("/models/helicopter/scene.gltf", function (gltf) {
        gltf.scene.position.y = 100
        gltf.scene.position.z = 100
        gltf.scene.scale.set(0.5, 0.5, 0.5)

        const animMixer = new THREE.AnimationMixer(gltf.scene)
        animMixer.clipAction(gltf.animations[0]).play()

        animationMixers.push(animMixer)
        scene.add(gltf.scene);

        new Tween({progress: 0})
            .to({progress: 1}, 8000)
            .onUpdate(({progress}) => {
                const point = curve.getPointAt(progress);
                const tangent = curve.getTangent(progress);

                if (point.z <= SCALE / 2 + HELICOPTER_OFFSET_Z && point.z >= SCALE / 2 && !paintAnimation.isPlaying()) {
                    paintPosition.x = point.x + HELICOPTER_OFFSET_X;
                    paintPosition.z = point.z - HELICOPTER_OFFSET_Z;
                    paintAnimation.start()
                }

                gltf.scene.position.copy(point);
                gltf.scene.lookAt(point.clone().add(tangent));
            })
            .onComplete(() => {
                gltf.scene.removeFromParent()
            })
            .start()
            .group(animationGroup)

    }, undefined, function (error) {
        console.error("Failed load helicopter model " + error);
    });

    const clock = new THREE.Clock();
    const tick = () => {
        const delta = clock.getDelta();
        const elapsed = clock.getElapsedTime();

        animationGroup.update()
        animationMixers.forEach(mixer => mixer.update(delta));

        controlsObs.value.update(delta)
        rendererObs.value.render(scene, camera);
    }

    canvasRef.onChange((canvas) => {
        if (canvas === null) return;
        const renderer = new THREE.WebGLRenderer({canvas});

        controlsObs.value = new OrbitControls(camera, canvasRef.value);
        setupControls(controlsObs.value, {
            minX: -5,
            maxX: 5,
            minY: -5,
            maxY: 5,
            minZ: -5,
            maxZ: 5,
        })

        rendererObs.value = renderer
        renderer.setAnimationLoop(tick)
    })

    function resizeCanvas() {
        const {width, height} = containerRef.value.getBoundingClientRect();

        camera.aspect = width / height;
        camera.updateProjectionMatrix();
        rendererObs.value.setSize(width, height);
    }

    window.addEventListener("resize", () => resizeCanvas());
    window.addEventListener("load", () => resizeCanvas());

    return <div ref={containerRef} className={styles.container}>
        <canvas ref={canvasRef} className={styles.plot}></canvas>
    </div>
}

interface CameraBounds {
    minX: number;
    minY: number;
    minZ: number;
    maxX: number;
    maxY: number;
    maxZ: number;
}

function setupControls(controls: OrbitControls, cameraBounds?: CameraBounds) {
    controls.enableDamping = true;
    controls.dampingFactor = 0.05;

    controls.minDistance = SCALE * 2;
    controls.maxDistance = 10;

    controls.enablePan = true;
    controls.panSpeed = 0.5;

    controls.rotateSpeed = 0.5

    if (cameraBounds) setCameraBounds(controls, cameraBounds);
}

function setCameraBounds(controls: OrbitControls, bounds: CameraBounds) {
    controls.addEventListener("change", () => {
        controls.target.x = Math.max(bounds.minX, Math.min(bounds.maxX, controls.target.x));
        controls.target.y = Math.max(bounds.minY, Math.min(bounds.maxY, controls.target.y));
        controls.target.z = Math.max(bounds.minZ, Math.min(bounds.maxZ, controls.target.z));
    });
}
