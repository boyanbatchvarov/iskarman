function initScrollHint(): void {
  const hint = document.getElementById("scroll-hint");
  if (!hint) return;

  const hide = (): void => {
    hint.classList.add("hidden");
    window.removeEventListener("scroll", onScroll);
  };

  const onScroll = (): void => {
    if (window.scrollY > 20) hide();
  };

  if (window.scrollY > 20) {
    hide();
    return;
  }

  window.addEventListener("scroll", onScroll, { passive: true });
}

initScrollHint();
