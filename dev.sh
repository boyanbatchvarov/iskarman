#!/usr/bin/env bash
set -euo pipefail

cleanup() {
  kill "$WATCH_PID" 2>/dev/null || true
}
trap cleanup EXIT INT TERM

(cd frontend && npm run build)

./gradlew -t classes processResources &
WATCH_PID=$!

./gradlew run
