# CQRS Architecture

This project follows the Command Query Responsibility Segregation (CQRS) pattern.

## Overview

```
                    ┌─────────────────────┐
                    │  Mobile / Web       │
                    │  Client             │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │  API Gateway :8080  │
                    │  Auth, routing      │
                    └──────────┬──────────┘
                               │
              ┌────────────────┼────────────────┐
              │                │                │
              ▼                ▼                ▼
    ┌─────────────┐   ┌─────────────┐   ┌─────────────┐
    │ OAuth :9191 │   │ Write :9091 │   │ Read :9092  │
    │ Token       │   │ Commands    │   │ Queries     │
    └─────────────┘   └──────┬──────┘   └──────┬──────┘
                             │                 │
                    ┌────────┴────────┐ ┌──────┴──────┐
                    │ RDS Write       │ │ Redis       │
                    │ Primary         │ │ Cache       │
                    │ RDS primary    │ └──────┬──────┘
                    └────────────────┘        │ miss
                                              ▼
                                    ┌─────────────┐
                                    │ RDS Read    │
                                    │ Replica     │
                                    └─────────────┘
```

## Modules

| Module | Port | Responsibility |
|--------|------|----------------|
| **ProjectGateway** | 8080 | API Gateway - routes by method (POST/PUT/DELETE → Write, GET → Read, /oauth → OAuth) |
| **ProjectWrite** | 9091 | Commands - insert/update/delete user_lifestyle_daily |
| **ProjectRead** | 9092 | Queries - cache-first (Redis), fallback to RDS read replica |
| **ProjectOauth** | 9191 | OAuth2 token issuance |
| **ProjectApi** | 9090 | Legacy/monolith (optional) |

## Routing

- **Write service**: `POST`, `PUT`, `DELETE`, `PATCH` on `/api/**`
- **Read service**: `GET` on `/api/**`
- **OAuth**: `/oauth/**` → ProjectOauth

## Run Order

1. MySQL (e.g. docker-compose)
2. Redis (for Read cache)
3. ProjectOauth (9191)
4. ProjectWrite (9091)
5. ProjectRead (9092)
6. ProjectGateway (8080)

Client uses `http://localhost:8080` as base URL.

## Environment Variables

| Variable | Description |
|----------|-------------|
| `WRITE_SERVICE_URI` | Write service URL (default: http://localhost:9091) |
| `READ_SERVICE_URI` | Read service URL (default: http://localhost:9092) |
| `OAUTH_SERVICE_URI` | OAuth service URL (default: http://localhost:9191) |
| `AUTH_SERVER_URL` | Auth server for token validation |
| `REDIS_HOST`, `REDIS_PORT` | Redis connection |
| `READ_REPLICA_URL` | RDS read replica JDBC URL |
| `WRITE_DATASOURCE_URL` | RDS write primary JDBC URL |
