import {Observer} from "../observer";

export type Ref<T extends HTMLElement> = Observer<T>

export default function ref<T extends HTMLElement>(): Ref<T> {
    return new Observer<T>(null);
}