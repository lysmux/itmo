import superagent from "superagent";
import {CheckResponse} from "./types";
import {RESULTS_OBSERVER} from "./global";
import $ from "jquery"
import {addToast, TOAST_VARIANTS} from "./toast";

const loader = $(".loader")

function setLoaderVisible(visible: boolean) {
    if (visible) loader.removeClass("hidden")
    else loader.addClass("hidden")
}

export function processPoints(x: number[], y: number, r: number[]) {
    const loaderVisibleTimeout = setTimeout(() => {
        setLoaderVisible(true);
    }, 200)

    superagent
        .get("?")
        .query({
            x: x,
            y: y,
            r: r,
        })
        .then(res => {
            const result = res.body as CheckResponse[];
            RESULTS_OBSERVER.value.push(...result)

            // if (result.checks.length == 1) {
            //     let sound: HTMLAudioElement;
            //
            //     if (result.checks[0].contains) {
            //         sound = new Audio('/assets/sounds/yes.mp3')
            //     } else {
            //         sound = new Audio('/assets/sounds/no.mp3')
            //     }
            //
            //     sound.play()
            // }
        })
        .catch(error => {
            addToast({
                title: error.status ? `Ошибка API | ${error.status}` : "Ошибка API",
                message: error.responce ? error.response.text : error.message,
                style: TOAST_VARIANTS.error
            })
        })
        .finally(() => {
            window.clearTimeout(loaderVisibleTimeout)
            setLoaderVisible(false);
        })
}

export function clearPoints() {
    superagent.post("/clear")
        .then(() => {
            RESULTS_OBSERVER.value.length = 0
        })
        .catch(error => {
            addToast({
                title: error.status ? `Ошибка API | ${error.status}` : "Ошибка API",
                message: error.responce ? error.response.text : error.message,
                style: TOAST_VARIANTS.error
            })
    })
}

export function loadPointsHistory() {
    superagent.get("/history")
        .then(res => {
            RESULTS_OBSERVER.value = res.body as CheckResponse[]
        })
        .catch(error => {
            addToast({
                title: error.status ? `Ошибка API | ${error.status}` : "Ошибка API",
                message: error.responce ? error.response.text : error.message,
                style: TOAST_VARIANTS.error
            })
        })
}