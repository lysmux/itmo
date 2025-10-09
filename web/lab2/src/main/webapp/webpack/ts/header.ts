import $ from "jquery";

$("input[name=theme]").on("change", (e) => {
    const theme = (e.target as HTMLInputElement).value;
    changeTheme(theme)
})

function changeTheme(theme: string) {
    const isDark = theme === "dark"

    localStorage.setItem("theme", theme)
    $("#light-theme").prop("checked", !isDark)
    $("#dark-theme").prop("checked", isDark)

    document.documentElement.classList.forEach((class_) => {
        if (class_.startsWith("theme-")) document.documentElement.classList.remove(class_);
    })
    document.documentElement.classList.add(`theme-${theme}`);
}

function getDefaultTheme(): string {
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
        return "dark";
    }
    return "light"
}

$(() => {
    const theme = localStorage.getItem("theme") || getDefaultTheme()

    changeTheme(theme)
})