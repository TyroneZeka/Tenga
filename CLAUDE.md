# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Tenga is a C2C second-hand marketplace for Zimbabwe. It is a monorepo with three platforms:
- `backend/` — Spring Boot 3.3+ / Java 21 API
- `web/` — React 18 / TypeScript 5 / Vite 5 PWA
- `mobile/` — Flutter 3.22+ / Dart 3.4+ (Android + iOS)
- `infra/` — Docker, Kubernetes, Nginx configs
- `docs/` — ADRs, API collections, changelogs

> Full coding standards, naming conventions, security checklist, and architecture decisions are in `CLAUDE.local.md`. Read it before making any non-trivial changes.

---

## Build, Lint & Test Commands

### Backend
```bash
./gradlew clean build          # Full build + tests
./gradlew test                 # Unit tests only
./gradlew test --tests "com.tenga.listing.service.ListingServiceTest"  # Single test class
./gradlew spotlessCheck        # Check formatting (Spotless / Google Java Format)
./gradlew spotlessApply        # Auto-fix formatting
./gradlew check                # Lint + test (what CI runs on PRs)
```

### Web
```bash
npm install                    # Install deps
npm run dev                    # Dev server (Vite)
npm run build                  # Production build
npm run lint                   # ESLint
npm run type-check             # tsc --noEmit
npm run test                   # Vitest (watch mode)
npm run test -- --run          # Vitest (CI / single run)
npm run test -- ListingCard    # Single test file
npm run test:e2e               # Playwright E2E
```

### Mobile
```bash
flutter pub get                # Install deps
flutter analyze                # Static analysis (runs on PRs)
flutter test                   # All unit tests
flutter test test/features/listing/listing_notifier_test.dart  # Single test
flutter test integration_test/ # Integration tests (needs emulator)
flutter build apk --release    # Android release build
flutter build ios --release    # iOS release build
```

### Infra / Dev Environment
```bash
docker compose up -d           # Start PostgreSQL, Redis, Kafka, Elasticsearch, MinIO
docker compose down            # Stop all services
docker compose logs -f backend # Tail backend logs
```

---

## Architecture

### Backend Module Structure
Every feature lives under `com.tenga.{module}` with this internal layout:
```
controller/   ← thin REST layer, delegates to service, returns DTOs only
service/      ← business logic (interfaces + impl), @Transactional here
repository/   ← Spring Data JPA repos
model/
  entity/     ← JPA entities extending BaseEntity (UUID PK, auditing, @Version)
  dto/        ← Java records for request/response
  mapper/     ← MapStruct mappers (entity ↔ DTO)
  enums/
event/        ← Kafka / Spring application events
config/       ← Module-specific Spring beans
exception/    ← RuntimeException subclasses, caught by global @ControllerAdvice
```

Modules: `auth`, `user`, `listing`, `chat`, `payment`, `location`, `review`, `notification`, `admin`, `common`.

Each module owns its own DB schema prefix: `auth_*`, `usr_*`, `lst_*`, `cht_*`, `pay_*`, `loc_*`, `rev_*`, `ntf_*`, `adm_*`.

### Web Feature Structure
```
web/src/
  features/{feature}/
    components/   ← feature-specific UI
    hooks/        ← feature-specific React hooks
    api/          ← TanStack Query hooks (all server state here)
    types/        ← feature-local TypeScript types
    index.ts      ← public API of the feature
  components/     ← shared/reusable UI primitives
  pages/          ← route-level components (default exports allowed here only)
  lib/            ← API client, shared utils, constants
  stores/         ← Zustand stores (client state only)
```

### Mobile Feature Structure (Clean Architecture)
```
mobile/lib/
  features/{feature}/
    data/           ← repos, data sources, JSON models
    domain/         ← entities, repo interfaces, use cases
    presentation/   ← screens, widgets, Riverpod notifiers
  core/             ← Dio client, go_router, theme, DI setup
  shared/           ← shared widgets, extensions, utils
```

---

## Key Conventions

- **All endpoints** under `/api/v1/`, kebab-case, plural nouns.
- **Flyway migrations** in `backend/src/main/resources/db/migration/`, named `V{yyyyMMdd_HHmm}__{description}.sql`. Never edit a committed migration — create a new one.
- **DTOs are Java records**. Entities never leave the service layer.
- **Constructor injection only** in Spring (no `@Autowired` on fields).
- **No `Optional` from repos going null** — always use `orElseThrow` with a typed exception.
- **Prices stored as `(BigDecimal amount, Currency currency)`** — `Currency` enum is `ZIG` or `USD`.
- **Zimbabwe phone validation**: `^\+263[0-9]{9}$`
- **PostGIS SRID 4326**, default radius 25km, max 100km.

## Branch & Commit Format
```
feature/TNG-{issue#}-{short-kebab-desc}   ← branch
feat(listing): add image compression       ← commit (Conventional Commits)
```
Scopes: `auth`, `user`, `listing`, `chat`, `payment`, `location`, `review`, `notification`, `admin`, `web`, `mobile`, `infra`, `deps`.
