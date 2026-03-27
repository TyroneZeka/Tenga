# Tenga — Local Development Guide

How to start the stack locally for development and testing.

---

## Prerequisites

| Tool | Min version | Install |
|------|-------------|---------|
| Docker Desktop | 4.x | https://www.docker.com/products/docker-desktop |
| Java (JDK) | 21 | `winget install EclipseAdoptium.Temurin.21.JDK` |
| Node.js | 18+ | https://nodejs.org |
| Flutter SDK | 3.22+ | https://docs.flutter.dev/get-started/install |
| Git | any | https://git-scm.com |

---

## 1. Infrastructure (start this first)

All services run in Docker. From the repo root:

```bash
cd infra
docker compose up -d
```

Wait ~30 seconds for all health checks to pass, then verify:

```bash
docker compose ps        # all services should show "healthy" or "running"
```

### Service ports

| Service | Port | URL / notes |
|---------|------|-------------|
| PostgreSQL | 5432 | `tenga_dev` DB, user `tenga`, password `tenga` |
| Redis | 6379 | No auth in dev |
| Elasticsearch | 9200 | `http://localhost:9200` |
| Kafka | 9092 | Broker `localhost:9092` |
| MinIO (S3) | 9000 | API endpoint |
| MinIO Console | 9001 | `http://localhost:9001` — user `minioadmin` / `minioadmin` |
| Mailpit (SMTP) | 1025 | Catches all outbound email |
| Mailpit UI | 8025 | `http://localhost:8025` — view OTP codes here |

To stop everything:

```bash
docker compose down          # keep volumes (data persists)
docker compose down -v       # nuke volumes (fresh start)
```

---

## 2. Backend (Spring Boot)

```bash
cd backend
./gradlew bootRun --args='--spring.profiles.active=dev'
```

The backend starts on **http://localhost:8080**.

On first run with a clean DB, it automatically seeds:
- **1 fixed test user** — login with `+263771234567` / `Test1234!` (phone pre-verified)
- **50 random users** — all with password `Test1234!`, phones like `+263771234567`
- **1 000 realistic listings** across all categories and cities

Other useful commands:

```bash
./gradlew test                  # unit tests
./gradlew check                 # lint + test (what CI runs)
./gradlew spotlessApply         # auto-fix Java formatting before committing
```

API docs (Swagger UI) once running: **http://localhost:8080/api/docs**

> **Tip:** OTP codes for phone verification are sent via Mailpit. Open
> `http://localhost:8025` to read them. In dev mode, SMS is mocked — the
> code is also printed directly in the backend log:
> ```
> INFO  MockSmsService - OTP sent to +263771234567: 123456
> ```

---

## 3. Web Frontend (React / Vite)

```bash
cd web
npm install          # first time only
npm run dev
```

Opens on **http://localhost:5173** with hot reload.

Other commands:

```bash
npm run lint         # ESLint
npm run type-check   # TypeScript check (no emit)
npm run test -- --run  # Vitest (single run, CI mode)
npm run build        # production build → dist/
```

---

## 4. Mobile App (Flutter)

```bash
cd mobile
flutter pub get      # first time only
```

### Run in browser (recommended on Windows)

Due to a known Flutter/Chrome DDS issue on Windows, always pass `--no-dds`:

```bash
flutter run -d chrome --no-dds
```

Hot reload (`r`), hot restart (`R`), and Flutter DevTools all work normally.

Alternatively, use `web-server` mode and open the URL yourself:

```bash
flutter run -d web-server --web-port 8090
# Then open http://localhost:8090 in any browser
```

### Run on Windows desktop

```bash
flutter run -d windows
```

### Run on Android emulator

1. Open Android Studio → Device Manager → start an emulator
2. Then:
```bash
flutter run           # auto-detects the running emulator
```

The mobile app connects to the backend at `http://10.0.2.2:8080` (Android
emulator's alias for localhost). For web-server/Windows it uses `http://localhost:8080`.

To point at a different backend:

```bash
flutter run -d web-server --web-port 8090 --dart-define=BASE_URL=http://localhost:8080/api/v1
```

Other commands:

```bash
flutter analyze      # static analysis (must be 0 issues before committing)
flutter test         # unit tests
dart run build_runner build --delete-conflicting-outputs  # regenerate freezed models after model changes
```

---

## 5. Test Credentials

| Account | Phone | Password | Notes |
|---------|-------|----------|-------|
| Test user | `+263771234567` | `Test1234!` | Pre-verified, pre-seeded on first run |
| 50 seeded users | random `+263...` | `Test1234!` | See backend logs on startup for their phones |

---

## 6. Full Stack Start Order

```
1. docker compose up -d          (infra)
2. ./gradlew bootRun              (backend — wait for "Started TengaApplication")
3. npm run dev                    (web)
4. flutter run -d chrome --no-dds              (mobile — separate terminal)
```

Each process needs its own terminal. Everything is hot-reload / hot-restart capable.

---

## 7. Resetting Dev Data

```bash
# Wipe DB and all volumes, then restart
cd infra && docker compose down -v && docker compose up -d

# Then restart the backend — it will re-seed automatically
cd ../backend && ./gradlew bootRun --args='--spring.profiles.active=dev'
```

---

## 8. Environment Variables (dev defaults)

The dev profile (`application-dev.yml`) has sensible defaults for everything.
No `.env` file is needed locally. The only values you might override:

```bash
# Optional overrides via shell environment
export SERVER_PORT=8081           # if 8080 is taken
export JWT_SECRET=your-secret     # custom JWT secret
```

For staging/production, all variables listed in `CLAUDE.local.md §12.2` must
be set via CI secrets or a vault.
