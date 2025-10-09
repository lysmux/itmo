function setupIframeResize(iframe) {
    const resize = () => {
        const body = iframe.contentWindow.document.body;
        const html = iframe.contentWindow.document.documentElement;

        iframe.style.height = Math.max(body.scrollHeight, html.scrollHeight) + "px";
    };

    const observer = new MutationObserver(resize);
    observer.observe(iframe.contentWindow.document.body, {
        childList: true,
        subtree: true,
        attributes: true,
        characterData: true
    });
    window.addEventListener('resize', resize);
    resize()
}