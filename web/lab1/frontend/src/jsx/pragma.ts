import {Observer, ArrayObserver} from "../observer";
import {HTMLAttributes} from "./types";

type Child =
    | Node
    | ((properties: Properties) => string | Node)
    | string

type Properties = HTMLAttributes

type JSXElement =
    | string
    | ((properties: Properties, children: Child[]) => Node)

function parseChildren(children: Child[]) {
    return children.map(child => {
        if (typeof child === 'string') {
            return document.createTextNode(child);
        }
        return child;
    })
}


function parseNode(tag: string, properties: Properties, children: Child[]): Node {
    const el = document.createElement(tag);

    if (properties.model !== undefined) {
        if (typeof properties.model === 'string') {
            console.log(properties[properties.model])
            el.innerText = properties[properties.model];
        } else {
            const model: Observer<any> = properties.model;
            model.onChange((value) => {
                if (el instanceof HTMLInputElement) {
                    el.value = value;
                } else el.innerText = value;
            })
        }
    }

    if (properties.ref !== undefined) {
        properties.ref.value = el
    }

    if (properties.innerHTML !== undefined) {
        el.innerHTML = properties.innerHTML;
    }

    for (const key in properties) {
        const value = properties[key];

        // if (typeof value === "function") {
        //     el[key] = (event: Event) => {
        //         // Определяем сигнатуру функции и вызываем accordingly
        //         const functionLength = value.length;
        //
        //         if (value.length === 1) {
        //             return value({
        //                 event: event,
        //                 item: properties['data-item'],
        //             });
        //         }
        //
        //         return value()
        //     };
        // } else
        el[key] = properties[key];

    }

    parseChildren(children).forEach(child => {
        if (typeof child === 'function') {
            const child2 = child(properties);

            if (typeof child2 === 'string') {
                el.appendChild(document.createTextNode(child2))
            } else el.appendChild(child2);
        } else {
            el.appendChild(child);
        }
    });
    return el;
}

function jsx(element: JSXElement, properties?: Properties, ...children: Child[]): Node {
    properties = properties || {};

    if (typeof element === 'function') {
        return element(properties, children);
    }

    if (properties.forIt !== undefined) {
        const iter = properties.forIt;
        const key = properties.forKey || "item"
        const fragment = document.createDocumentFragment()

        if (iter instanceof ArrayObserver) {
            let nodes: Node[] = [];

            const rerender = (items: any[]) => {
                let parent : ParentNode;

                nodes.forEach((child: Node) => {
                    parent = child.parentNode;
                    parent.removeChild(child);
                })
                nodes = []

                items.forEach(item => {
                    properties[key] = item
                    nodes.push(parseNode(element, properties, children.flat()))
                })

                nodes.forEach(node => {
                    if (parent) parent.appendChild(node)
                    else fragment.appendChild(node)
                })
            }

            iter.onChange((value) => {
                rerender(iter.value)
            })

        } else {
            Array.from(iter)
                .forEach(el => {
                    properties[key] = el
                    fragment.appendChild(parseNode(element, properties, children.flat()))
                })
        }

        return fragment
    }

    return parseNode(element, properties, children.flat());
}

export default jsx;