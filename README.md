# Loopang Hub Service

loopang MSA 프로젝트의 허브 + 허브재고 도메인 서비스.

## 기술 스택

- Java 21, Spring Boot 3.5.13, Spring Cloud 2025.0.1
- Spring MVC (Servlet 기반)
- 공통 모듈: `com.loopang:common:0.0.4-SNAPSHOT`
- PostgreSQL 17, JPA + QueryDSL
- Kafka (Outbox/Inbox 패턴)
- Eureka Client + Config Server
- Lombok, Gradle
- server.port = 18100

## 도메인 범위

- **허브 관리** (CRUD) — `p_hub`
- **허브재고 관리** (CRUD, 입출고) — `p_hub_inventory`
- 라우트(`p_hub_route`)는 route-service 담당

## 테이블 구조

### p_hub (허브)

| 컬럼 | 타입 | 제약 | 설명 |
|---|---|---|---|
| hub_id | UUID | PK | 허브ID |
| name | VARCHAR(50) | NOT NULL | 허브이름 |
| capacity | SMALLINT | NOT NULL | 용적 |
| current_load | SMALLINT | NOT NULL | 현재부하 |
| city_do | VARCHAR(50) | NULL | 시/도 |
| gu_gun | VARCHAR(50) | NULL | 구/군 |
| dong_doro | VARCHAR(50) | NULL | 동/도로명 |
| detail_address | VARCHAR(100) | NULL | 상세주소 |
| full_address | VARCHAR(300) | NOT NULL | 전체주소 |
| latitude | Double | NOT NULL | 위도 |
| longitude | Double | NOT NULL | 경도 |
| + BaseUserEntity (createdAt/By, updatedAt/By, deletedAt/By) |

### p_hub_inventory (허브재고)

| 컬럼 | 타입 | 제약 | 설명 |
|---|---|---|---|
| hub_inventory_id | UUID | PK | 허브재고ID |
| hub_id | UUID | NOT NULL | 허브ID |
| hub_name | VARCHAR(100) | NULL | 허브이름 |
| item_id | UUID | NOT NULL | 아이템ID |
| item_name | VARCHAR(255) | NULL | 아이템명 |
| quantity | INT | NOT NULL | 개수 |
| company_id | UUID | NOT NULL | 업체ID |
| company_name | VARCHAR(100) | NULL | 업체명 |
| + BaseUserEntity |

## 패키지 구조

```
com.loopang.hub_service
├── domain/
│   ├── hub/
│   │   ├── controller/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── service/
│   │   └── vo/
│   └── inventory/
│       ├── controller/
│       ├── dto/
│       │   ├── request/
│       │   └── response/
│       ├── entity/
│       ├── exception/
│       ├── repository/
│       ├── service/
│       └── vo/
└── infrastructure/
    └── config/
```

## 로컬 실행

### 사전 조건
- Eureka Server (18761)
- Config Server (18888)
- PostgreSQL Docker (5435)

### 실행 순서

```bash
# 1. PostgreSQL
docker start postgres-hub

# 2. Eureka → Config → hub-service (IntelliJ에서 순서대로 실행)
```

### 환경변수 (IntelliJ Run Configuration)

```
DB_URL=localhost:5435/hub;DB_USERNAME=postgres;DB_PASSWORD=본인비밀번호
```
