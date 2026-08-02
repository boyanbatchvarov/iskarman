import { copyFileSync, mkdirSync } from "node:fs";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";

const root = join(dirname(fileURLToPath(import.meta.url)), "..");
const dist = join(root, "frontend", "dist");
const targets = [
  join(root, "docs", "static"),
  join(root, "src", "main", "resources", "static"),
];
const files = ["countdown.js", "path-follow.js", "lazy-video.js"];

for (const dir of targets) {
  mkdirSync(dir, { recursive: true });
  for (const file of files) {
    copyFileSync(join(dist, file), join(dir, file));
  }
}
