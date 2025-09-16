declare module "*.module.scss" {
    interface IClassNames {
        [className: string]: string;
    }

    const classNames: IClassNames;
    export = classNames;
}

declare module '*.svg' {
    const content: string;
    export default content;
}

declare module "*?source" {
    const content: string;
    export default content;
}