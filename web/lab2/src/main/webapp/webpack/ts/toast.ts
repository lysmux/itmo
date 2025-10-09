import $ from "jquery";

export type ToastVariant = 'success' | 'error' | 'info' | 'warning';
export type ToastVariantsConfig = Record<ToastVariant, ToastStyle>

export const TOAST_VARIANTS: ToastVariantsConfig = {
    success: {
        className: "success",
        svgIcon: "/assets/success.svg"
    },
    error: {
        className: "error",
        svgIcon: "/assets/error.svg"
    },
    info: {
        className: "info",
        svgIcon: "/assets/info.svg"
    },
    warning: {
        className: "warning",
        svgIcon: "/assets/warning.svg"
    }
}

export interface ToastStyle {
    className?: string;
    svgIcon?: string
}

interface ToastProps {
    title: string;
    message: string;
    style: ToastStyle
}

export function addToast({title, message, style}: ToastProps) {
    const toast = $(`
        <div class="toast ${style.className}">
            <div class="toast-wrapper">
                <div class="icon-block"><img src="${style.svgIcon}" alt="" class="icon" /></div>
                <div class="content">
                    <h1 class="title">${title}</h1>
                    <p class="message">${message}</p>
                </div>
                <div class="btn-block">
                    <button class="close-btn"><img class="icon" src="/assets/close.svg"></button>
                </div>
            </div>
            <div class="progress"></div>
        </div>
    `)

    toast.on("animationend", (e) => {
        const animationEvent = e.originalEvent as AnimationEvent;
        if (animationEvent.animationName === "disappear") toast.remove();
    })

    toast.find(".progress").on("animationend", (e) => {
        toast.addClass("disappear-animation")
    })

    toast.find(".close-btn").on("click", (e) => {
        toast.addClass("disappear-animation")
    })

    $("#toast-container").append(toast)
}