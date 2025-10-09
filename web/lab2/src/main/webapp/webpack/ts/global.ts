import useObserver from "./observer";
import {CheckResponse} from "./types";

export const R_OBSERVER = useObserver(2);
export const TABLE_DATA_OBSERVER = useObserver<CheckResponse[]>([])