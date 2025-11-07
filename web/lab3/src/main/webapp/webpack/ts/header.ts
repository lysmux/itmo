import $ from "jquery";

function changeTheme(theme: string) {
    const isDark = theme === "dark"
    localStorage.setItem("theme", theme)

    $("#light-theme").prop("checked", !isDark)
    $("#dark-theme").prop("checked", isDark)

    const setThemeToElement = (element: Element) => {
        element.className = element.className.replace(/\btheme-\w+/g, '') + ` theme-${theme}`;
    };

    setThemeToElement(document.documentElement);
    document.querySelectorAll("iframe").forEach(iframe => {
            iframe.addEventListener('load', () => {
                setThemeToElement(iframe.contentWindow.document.documentElement);
            });
            setThemeToElement(iframe.contentWindow.document.documentElement);
        }
    );
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

$("input[name=theme]").on("change", (e) => {
    const theme = (e.target as HTMLInputElement).value;
    changeTheme(theme)
})
