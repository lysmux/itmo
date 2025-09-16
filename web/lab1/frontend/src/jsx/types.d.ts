import {Observer} from "./observer";
import {Ref} from "./ref";

declare namespace JSX {
    interface IntrinsicElements {
        [elemName: string]: HTMLAttributes;

        div: HTMLAttributes,
        button: ButtonAttributes
    }
}

interface HTMLAttributes {
    [key: string]: any;

    className?: string;
    id?: string;

    for?: Iterable<any>;
    forKey?: string;
    model?: Observer<any> | string
    ref?: Ref<any>
    innerHTML?: string
}

interface EventContext<T = any> {
    event: Event,
    item: T
}

interface ButtonAttributes extends HTMLAttributes {
    onclick?: (context: EventContext) => void;
}