import useObserver from "./observer";
import {Result} from "./types";

export const R_OBSERVER = useObserver(getR());
export const RESULTS_OBSERVER = useObserver<Result>([])

function getR() {
    const el = $('[name$=":rVal"]');
    if (el.val() === "") {
        return null;
    }

    return Number(el.val());
}