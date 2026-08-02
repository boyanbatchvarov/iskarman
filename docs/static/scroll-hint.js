"use strict";
function initScrollHint() {
    const hint = document.getElementById("scroll-hint");
    if (!hint)
        return;
    const hide = () => {
        hint.classList.add("hidden");
        window.removeEventListener("scroll", onScroll);
    };
    const onScroll = () => {
        if (window.scrollY > 20)
            hide();
    };
    if (window.scrollY > 20) {
        hide();
        return;
    }
    window.addEventListener("scroll", onScroll, { passive: true });
}
initScrollHint();
