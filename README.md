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

## API 엔드포인트

### Hub

| 메서드 | URL | 설명 |
|---|---|---|
| POST | `/api/hubs` | 허브 생성 |
| GET | `/api/hubs/{hubId}` | 허브 단건 조회 |
| GET | `/api/hubs` | 허브 목록 조회 (페이징 10/30/50) |
| PUT | `/api/hubs/{hubId}` | 허브 수정 |
| DELETE | `/api/hubs/{hubId}` | 허브 삭제 (소프트) |

### HubInventory (구현 예정)

| 메서드 | URL | 설명 |
|---|---|---|
| POST | `/api/hubs/{hubId}/inventories` | 재고 등록 |
| GET | `/api/hubs/{hubId}/inventories` | 재고 목록 조회 |
| PUT | `/api/hubs/{hubId}/inventories/{inventoryId}` | 재고 수정 |
| DELETE | `/api/hubs/{hubId}/inventories/{inventoryId}` | 재고 삭제 |

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
| version | INT | | 낙관적 락 (@Version) |
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

## 클린 아키텍처

의존성 방향: `presentation → application → domain ← infrastructure`

```text
┌─────────────────────────────────────────────┐
│  presentation (Controller, DTO)             │  ← 외부 요청 처리
│    ↓                                        │
│  application (Service, 유스케이스)            │  ← 비즈니스 흐름 조율
│    ↓                                        │
│  domain (Entity, Repository 인터페이스, VO)   │  ← 순수 비즈니스 규칙
│    ↑                                        │
│  infrastructure (JPA 구현, Kafka, Config)    │  ← 기술 구현체
└─────────────────────────────────────────────┘
```

| 레이어 | 역할 | 포함 내용 |
|---|---|---|
| **presentation** | 외부 요청/응답 처리 | Controller, Request/Response DTO |
| **application** | 유스케이스 조율 | Service (트랜잭션, 이벤트 발행, 도메인 조합) |
| **domain** | 순수 비즈니스 규칙 | Entity, VO, Repository 인터페이스, 도메인 예외 |
| **infrastructure** | 기술 구현체 | JPA Repository 구현, Kafka 이벤트, 외부 API, Config |

## 패키지 구조

```text
com.loopang.hub_service
├── application/
│   ├── hub/
│   │   └── HubService.java
│   └── inventory/
│       └── HubInventoryService.java
├── domain/
│   ├── hub/
│   │   ├── entity/Hub.java
│   │   ├── repository/HubRepository.java
│   │   ├── exception/
│   │   │   ├── HubErrorCode.java
│   │   │   ├── HubNotFoundException.java
│   │   │   ├── HubNameDuplicateException.java
│   │   │   └── MasterOnlyException.java
│   │   └── vo/
│   │       ├── Address.java
│   │       └── HubStatus.java
│   └── inventory/
│       ├── entity/HubInventory.java
│       ├── repository/HubInventoryRepository.java
│       ├── exception/
│       └── vo/
├── infrastructure/
│   ├── persistence/JpaHubRepository.java
│   ├── event/
│   └── config/
└── presentation/
    ├── hub/
    │   ├── HubController.java
    │   └── dto/
    │       ├── request/HubCreateRequest.java
    │       ├── request/HubUpdateRequest.java
    │       └── response/HubResponse.java
    └── inventory/
        ├── HubInventoryController.java
        └── dto/request, response
```

## 엔티티 특징

- `@SQLRestriction("deleted_at IS NULL")` — 소프트 삭제된 데이터 조회 시 자동 제외
- `@Version` — 낙관적 락 (동시 수정 방지)
- `BaseUserEntity` 상속 — createdAt/By, updatedAt/By, deletedAt/By 자동 관리
- `Address` VO — @Embeddable로 주소 정보 캡슐화
- `HubStatus` VO — DB 저장 안 함, capacity/currentLoad 비율로 런타임 계산
  - **NORMAL** (정상): 70% 미만
  - **BUSY** (혼잡): 70% 이상
  - **FULL** (만원): 100%
- `capacity` = 허브 수용 인원수, `currentLoad` = 현재 근무 인원수

## 예외 처리

| 클래스 | 상위 예외 | 설명 |
|---|---|---|
| `HubErrorCode` | ErrorCodeSpec | 에러 코드 enum (CustomException과 함께 사용) |
| `HubNotFoundException` | NotFoundException (404) | 허브를 찾을 수 없습니다. Hub ID: {id} |
| `HubNameDuplicateException` | BadRequestException (400) | 이미 존재하는 허브 이름입니다: {name} |
| `MasterOnlyException` | ForbiddenException (403) | 마스터 관리자만 수행할 수 있는 작업입니다. |

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

## 구현 현황

### 완료
- [x] 프로젝트 설정 (build.gradle, application.yaml, Eureka/Config 연동)
- [x] 클린 아키텍처 패키지 구조
- [x] Hub 엔티티 (@SQLRestriction, @Version, Address VO, HubStatus)
- [x] 예외 분리 (HubErrorCode + HubNotFoundException, HubNameDuplicateException, MasterOnlyException)
- [x] Hub CRUD API (생성, 조회, 목록, 수정, 삭제)
- [x] HubStatus — 런타임 상태 계산 (NORMAL/BUSY/FULL)

### 진행 예정
- [ ] HubInventory CRUD API
- [ ] 도메인 이벤트 (Hub 변경 시 Kafka 발행)
- [ ] UserType enum

### TODO (다른 서비스 연동 시)
- [ ] RoleCheck — 권한 체크 (유저 서비스 헤더 규격 확정 후)
- [ ] AddressResolver — 카카오 API 주소 → 좌표 변환
- [ ] Swagger/OpenAPI 설정
- [ ] 검색/필터 API (QueryDSL)
- [ ] Dockerfile + Docker 배포
- [ ] GitHub Actions CI/CD
