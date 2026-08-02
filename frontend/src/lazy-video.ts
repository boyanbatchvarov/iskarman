function loadVideo(video: HTMLVideoElement): void {
  const src = video.dataset.src;
  if (!src || video.dataset.loaded === "true") return;

  video.src = src;
  video.load();
  video.dataset.loaded = "true";
}

function initLazyVideos(): void {
  const videos = document.querySelectorAll<HTMLVideoElement>("video.media-video[data-src]");
  if (videos.length === 0) return;

  if (!("IntersectionObserver" in window)) {
    videos.forEach(loadVideo);
    return;
  }

  const observer = new IntersectionObserver(
    (entries) => {
      for (const entry of entries) {
        if (entry.isIntersecting) {
          loadVideo(entry.target as HTMLVideoElement);
          observer.unobserve(entry.target);
        }
      }
    },
    { rootMargin: "200px 0px" },
  );

  videos.forEach((video) => observer.observe(video));
}

initLazyVideos();
