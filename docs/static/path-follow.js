"use strict";
const PATH = [
    [53.4, 40.14], [54.21, 40.06], [55.02, 39.98], [55.84, 39.91], [56.65, 39.83],
    [57.46, 39.75], [58.27, 39.67], [59.42, 39.41], [60.56, 39.15], [61.7, 38.89],
    [62.85, 38.64], [64.0, 38.38], [65.14, 38.12], [66.43, 37.71], [67.72, 37.31],
    [69.02, 36.9], [70.31, 36.49], [71.6, 36.09], [72.89, 35.68], [72.45, 37.09],
    [72.01, 38.5], [71.58, 39.91], [71.14, 41.31], [70.7, 42.72], [70.26, 44.13],
    [69.38, 45.1], [68.5, 46.08], [67.62, 47.05], [66.75, 48.02], [65.87, 49.0],
    [64.99, 49.97], [64.49, 51.3], [63.98, 52.63], [63.48, 53.96], [62.98, 55.29],
    [62.47, 56.62], [61.97, 57.95], [61.42, 58.7], [60.88, 59.46], [60.34, 60.22],
    [59.79, 60.97], [59.25, 61.72], [58.7, 62.48], [57.79, 62.37], [56.89, 62.26],
    [55.98, 62.15], [55.07, 62.04], [54.17, 61.93], [53.26, 61.82], [52.38, 62.23],
    [51.5, 62.63], [50.62, 63.04], [49.75, 63.45], [48.87, 63.85], [47.99, 64.26],
    [46.99, 63.55], [45.99, 62.83], [44.98, 62.12], [43.98, 61.41], [42.98, 60.69],
    [41.98, 59.98], [41.5, 59.41], [41.02, 58.85], [40.54, 58.28], [40.06, 57.71],
    [39.58, 57.15], [39.1, 56.58], [39.25, 55.48], [39.4, 54.38], [39.55, 53.27],
    [39.69, 52.17], [39.84, 51.07], [39.99, 49.97], [40.32, 49.14], [40.65, 48.3],
    [40.98, 47.47], [41.32, 46.64], [41.65, 45.8], [41.98, 44.97], [42.39, 44.92],
    [42.81, 44.87], [43.22, 44.82], [43.64, 44.77], [44.05, 44.72], [44.47, 44.67],
    [45.06, 43.89], [45.64, 43.1], [46.23, 42.31], [46.82, 41.53], [47.4, 40.75],
    [47.99, 39.96], [48.59, 39.45], [49.19, 38.93], [49.78, 38.41], [50.38, 37.9],
    [50.98, 37.38], [51.58, 36.87], [52.16, 36.47], [52.74, 36.07], [53.33, 35.67],
    [53.91, 35.28], [54.49, 34.88], [55.07, 34.48], [55.35, 35.43], [55.63, 36.39],
    [55.91, 37.34], [56.18, 38.29], [56.46, 39.25], [56.74, 40.2], [56.18, 40.19],
    [55.63, 40.18], [55.07, 40.17], [54.51, 40.16], [53.96, 40.15], [53.4, 40.14],
];
const LAP_MS = 55000;
const ZOOM = 135;
const PAN = 0.72;
function sample(t) {
    const segments = PATH.length - 1;
    const pos = t * segments;
    const index = Math.min(Math.floor(pos), segments - 1);
    const frac = pos - index;
    const a = PATH[index];
    const b = PATH[index + 1];
    return {
        x: a[0] + (b[0] - a[0]) * frac,
        y: a[1] + (b[1] - a[1]) * frac,
        heading: Math.atan2(b[1] - a[1], b[0] - a[0]) * (180 / Math.PI),
    };
}
function initPathFollow(pathBg, pathMarker) {
    const reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    function render(time) {
        const t = (time % LAP_MS) / LAP_MS;
        const point = sample(t);
        pathBg.style.backgroundPosition =
            (50 + (50 - point.x) * PAN) + "% " + (50 + (50 - point.y) * PAN) + "%";
        pathBg.style.backgroundSize = ZOOM + "%";
        pathMarker.style.left = "50%";
        pathMarker.style.top = "50%";
        pathMarker.style.transform = "translate(-50%, -50%) rotate(" + point.heading + "deg)";
        if (!reducedMotion) {
            requestAnimationFrame(render);
        }
    }
    if (reducedMotion) {
        const start = sample(0);
        pathBg.style.backgroundPosition =
            (50 + (50 - start.x) * PAN) + "% " + (50 + (50 - start.y) * PAN) + "%";
        pathBg.style.backgroundSize = ZOOM + "%";
        pathMarker.style.left = "50%";
        pathMarker.style.top = "50%";
    }
    else {
        requestAnimationFrame(render);
    }
}
const pathBg = document.querySelector(".path-bg");
const pathMarker = document.querySelector(".path-marker");
if (pathBg && pathMarker) {
    initPathFollow(pathBg, pathMarker);
}
