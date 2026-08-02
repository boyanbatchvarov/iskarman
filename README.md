# iskarman.com

Bilingual landing page for the Iskarman 10 km open-water swimming challenge (12 September 2026, Europe/Sofia).

## Static site (GitHub Pages)

The deployable site lives in [`docs/`](docs/):

```
docs/
├── index.html          # English
├── bg/index.html       # Bulgarian
└── static/             # CSS, JS, images
```

### Preview locally

```bash
cd frontend && npm install && npm run build
cd docs && python3 -m http.server 8080
```

Open [http://localhost:8080](http://localhost:8080) (Bulgarian: [http://localhost:8080/bg/](http://localhost:8080/bg/)).

### Deploy to GitHub Pages

1. Push this repo to GitHub (public repo — Pages is free for public repositories).
2. Go to **Settings → Pages**.
3. Under **Build and deployment**, set **Source** to **GitHub Actions**.
4. Push to `main` — the [pages workflow](.github/workflows/pages.yml) deploys `docs/` automatically.

Your site will be at `https://<username>.github.io/<repo>/` until you add a custom domain.

### Custom domain (later)

When you register `iskarman.com`:

1. In **Settings → Pages**, enter `iskarman.com` as the custom domain.
2. At your registrar, add DNS records pointing to GitHub Pages:
   - **A** records for `@`: `185.199.108.153`, `185.199.109.153`, `185.199.110.153`, `185.199.111.153`
   - **CNAME** for `www`: `<username>.github.io`
3. Enable **Enforce HTTPS** once DNS has propagated.

### Background image

Add `docs/static/iskarman.png` for the animated swim-route background. The page works without it (solid dark background).

## Ktor dev server (optional)

The original Kotlin/Ktor server is still in `src/` for local development with hot reload:

```bash
./dev.sh
```

Open [http://localhost:8080](http://localhost:8080).

## Stack

- Static HTML (EN + BG)
- Vanilla TypeScript (`frontend/src/`) compiled to `countdown.js` and `path-follow.js`
- CSS in `docs/static/`
- GitHub Pages for hosting
