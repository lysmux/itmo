import superagent from "superagent";
import {CheckResponse} from "./types";
import {TABLE_DATA_OBSERVER} from "./global";
import $ from "jquery"

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
            const result = res.body as CheckResponse;
            TABLE_DATA_OBSERVER.value.push(result)

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
            // toast
        })
        .finally(() => {
            window.clearTimeout(loaderVisibleTimeout)
            setLoaderVisible(false);
        })
}

export function clearPoints() {
    superagent.post("/clear")
        .then(() => {
            TABLE_DATA_OBSERVER.value.length = 0
        })
        .catch(error => {

    })
}