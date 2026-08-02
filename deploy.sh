#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT"

WORKFLOW="Deploy GitHub Pages"

usage() {
  cat <<EOF
Usage: $(basename "$0") [options]

Build frontend assets and deploy the static site in docs/ to GitHub Pages.

Options:
  --build-only    Build frontend only (default)
  --deploy        Build, push to origin/main, and wait for the Pages workflow
  --commit MSG    Commit all changes with MSG, then deploy (implies --deploy)
  --dispatch      Build and trigger the Pages workflow without pushing
  -h, --help      Show this help

Examples:
  ./deploy.sh
  ./deploy.sh --deploy
  ./deploy.sh --commit "Update copy" --deploy
  ./deploy.sh --dispatch
EOF
}

build() {
  echo "==> Building frontend"
  (cd frontend && npm ci && npm run build)
}

require_main_branch() {
  local branch
  branch="$(git rev-parse --abbrev-ref HEAD)"
  if [[ "$branch" != "main" ]]; then
    echo "error: deploy requires branch 'main' (currently on '$branch')" >&2
    exit 1
  fi
}

require_clean_tree() {
  if [[ -n "$(git status --porcelain)" ]]; then
    echo "error: working tree has uncommitted changes; commit them or use --commit MSG" >&2
    exit 1
  fi
}

commit_changes() {
  local message="$1"
  if [[ -z "$(git status --porcelain)" ]]; then
    echo "==> No changes to commit"
    return
  fi
  echo "==> Committing changes"
  git add -A
  git commit -m "$message"
}

deploy_push() {
  require_main_branch
  require_clean_tree

  echo "==> Pushing to origin/main"
  git push origin main

  wait_for_workflow
}

deploy_dispatch() {
  echo "==> Triggering GitHub Actions workflow: $WORKFLOW"
  gh workflow run "$WORKFLOW"
  sleep 3
  wait_for_workflow
}

wait_for_workflow() {
  local run_id
  run_id="$(gh run list --workflow="$WORKFLOW" --limit 1 --json databaseId -q '.[0].databaseId')"
  if [[ -z "$run_id" ]]; then
    echo "error: could not find a workflow run for $WORKFLOW" >&2
    exit 1
  fi
  echo "==> Waiting for workflow run $run_id"
  gh run watch "$run_id" --exit-status
  gh run view "$run_id" --json url -q '.url'
}

do_build=true
do_deploy=false
do_dispatch=false
commit_message=""

while [[ $# -gt 0 ]]; do
  case "$1" in
    --build-only)
      do_deploy=false
      do_dispatch=false
      ;;
    --deploy)
      do_deploy=true
      ;;
    --dispatch)
      do_dispatch=true
      ;;
    --commit)
      shift
      if [[ $# -eq 0 ]]; then
        echo "error: --commit requires a message" >&2
        exit 1
      fi
      commit_message="$1"
      do_deploy=true
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "error: unknown option: $1" >&2
      usage >&2
      exit 1
      ;;
  esac
  shift
done

if $do_build; then
  build
fi

if [[ -n "$commit_message" ]]; then
  require_main_branch
  commit_changes "$commit_message"
fi

if $do_dispatch; then
  deploy_dispatch
elif $do_deploy; then
  deploy_push
elif $do_build; then
  echo "==> Build complete. Run ./deploy.sh --deploy to publish."
fi
