import "../scss/style.scss"
import "./header";
import "./form";
import "./plot/plot";
import "./api";
import "./toast"
import {loadPointsHistory} from "./api";

import $ from "jquery";
import {RESULTS_OBSERVER} from "./global";

$(() => {
    loadPointsHistory()
})

RESULTS_OBSERVER.onChange(_ => {
    const iframe = $("#results-iframe")[0] as HTMLIFrameElement;
    iframe.contentWindow.location.reload();
})