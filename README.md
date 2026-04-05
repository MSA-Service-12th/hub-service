# Loopang Hub Service

loopang MSA 프로젝트의 허브 + 허브재고 도메인 서비스.

## 기술 스택

- Java 21, Spring Boot 3.5.13, Spring Cloud 2025.0.1
- Spring MVC (Servlet 기반)
- 공통 모듈: `com.loopang:common:0.0.5-SNAPSHOT`
- PostgreSQL 17, JPA + QueryDSL
- Kafka (Outbox/Inbox 패턴 — common 제공)
- OpenFeign (item-service, company-service 연동)
- Eureka Client + Config Server
- Lombok, Gradle
- server.port = 18100

## 도메인 범위

- **허브 관리** (CRUD) — `p_hub`
- **허브재고 관리** (CRUD + Kafka 이벤트) — `p_hub_inventory`
- 라우트(`p_hub_route`)는 route-service 담당

## API 엔드포인트

### Hub

| 메서드 | URL | 설명 | 권한 | 상태 |
|---|---|---|---|---|
| POST | `/api/hubs` | 허브 등록 | MASTER, HUB | ✅ |
| GET | `/api/hubs/{hubId}` | 허브 단건 조회 | 전체 | ✅ |
| GET | `/api/hubs` | 허브 목록 조회 (페이징) | 전체 | ✅ |
| PUT | `/api/hubs/{hubId}` | 허브 수정 | MASTER, HUB | ✅ |
| DELETE | `/api/hubs/{hubId}` | 허브 삭제 (소프트) | MASTER, HUB | ✅ |

### Hub Inventory

| 메서드 | URL | 설명 | 권한 | 상태 |
|---|---|---|---|---|
| POST | `/api/hub-inventories` | 재고 등록 | MASTER | ✅ |
| GET | `/api/hub-inventories/{id}` | 재고 상세 조회 | 전체 | ✅ |
| GET | `/api/hub-inventories` | 재고 목록 조회 (페이징) | MASTER | ✅ |
| PATCH | `/api/hub-inventories/{id}` | 재고 수정 (quantity) | MASTER | ✅ |
| DELETE | `/api/hub-inventories/{id}` | 재고 삭제 | MASTER | ✅ |

## Kafka 이벤트

### 수신 (Order → Hub)
- 토픽: `prod-order-pending`
- 처리: 재고 차감 → 결과 발행
- 리스너: `OrderPendingListener`

### 발행 (Hub → Order)
- 토픽: `prod-hub-stock-updated`
- 내용: 재고 차감 성공/실패 결과 (orderId, balance, success)
- 발행: `HubEventsImpl` (Outbox 패턴)

### 토픽명 (Config Server에서 관리)
- `prod-order-pending` — 주문 대기 (주문 → 허브)
- `prod-hub-stock-updated` — 재고 차감 결과 (허브 → 주문)

## 테이블 구조

### p_hub (허브)

| 컬럼 | 타입 | 제약 | 설명 |
|---|---|---|---|
| hub_id | UUID | PK | 허브ID |
| name | VARCHAR(50) | NOT NULL | 허브이름 |
| capacity | SMALLINT | NOT NULL | 수용 인원수 |
| current_load | SMALLINT | NOT NULL | 현재 근무 인원수 |
| city_do | VARCHAR(50) | NULL | 시/도 |
| gu_gun | VARCHAR(50) | NULL | 구/군 |
| dong_doro | VARCHAR(50) | NULL | 동/도로명 |
| detail_address | VARCHAR(100) | NULL | 상세주소 |
| full_address | VARCHAR(300) | NOT NULL | 전체주소 |
| latitude | Double | NOT NULL | 위도 |
| longitude | Double | NOT NULL | 경도 |
| version | INT | | 낙관적 락 |
| + BaseUserEntity |

### p_hub_inventory (허브재고)

| 컬럼 | 타입 | 제약 | 설명 |
|---|---|---|---|
| hub_inventory_id | UUID | PK | 허브재고ID |
| hub_id | UUID | NOT NULL | 허브ID |
| hub_name | VARCHAR(100) | NULL | 허브이름 |
| item_id | UUID | NOT NULL | 아이템ID |
| item_name | VARCHAR(255) | NULL | 아이템명 |
| quantity | INT | NOT NULL | 재고 수량 |
| reserved_quantity | INT | NOT NULL | 예약 재고 (기본 0) |
| company_id | UUID | NOT NULL | 업체ID |
| company_name | VARCHAR(100) | NULL | 업체명 |
| version | INT | | 낙관적 락 |
| + BaseUserEntity |

## 예외 처리

| 클래스 | HTTP | 설명 |
|---|---|---|
| HubNotFoundException | 404 | 허브를 찾을 수 없습니다 |
| HubNameDuplicateException | 400 | 이미 존재하는 허브 이름 |
| MasterOnlyException | 403 | 마스터 관리자만 수행 가능 |
| HubInventoryNotFoundException | 404 | 허브재고를 찾을 수 없습니다 |

## Feign Client

| Client | 대상 서비스 | API | 용도 |
|---|---|---|---|
| ItemFeignClient | item-service | GET /api/items/{id} | 아이템 이름, 업체 정보 조회 |
| CompanyFeignClient | company-service | GET /api/companies/{id} | 업체 이름 조회 |

## 패키지 구조

```text
com.loopang.hub_service
├── application/
│   ├── hub/HubService.java
│   └── inventory/HubInventoryService.java
├── domain/
│   ├── hub/
│   │   ├── entity/Hub.java
│   │   ├── repository/HubRepository.java
│   │   ├── exception/
│   │   └── vo/Address.java, HubStatus.java
│   ├── inventory/
│   │   ├── entity/HubInventory.java
│   │   ├── repository/HubInventoryRepository.java
│   │   ├── exception/
│   │   └── service/ItemProvider.java, CompanyProvider.java, dto/
│   └── event/
│       ├── HubEvents.java
│       ├── OrderPendingPayload.java
│       ├── HubStockUpdatedPayload.java
│       └── HubChangedPayload.java
├── infrastructure/
│   ├── persistence/JpaHubRepository.java, JpaHubInventoryRepository.java
│   ├── client/ItemFeignClient.java, CompanyFeignClient.java, *ProviderImpl.java
│   ├── event/HubEventsImpl.java
│   └── kafka/
│       ├── HubTopicProperties.java
│       └── listener/OrderPendingListener.java
└── presentation/
    ├── hub/HubController.java + dto/
    └── inventory/HubInventoryController.java + dto/
```

## 로컬 실행

### 사전 조건
- Eureka Server (18761)
- Config Server (18888)
- PostgreSQL Docker (5435)
- Kafka (GCP 34.64.141.183:9092 등)

### 환경변수 (IntelliJ Run Configuration)
```text
DB_URL=localhost:5435/hub;DB_USERNAME=postgres;DB_PASSWORD=본인비밀번호;KAFKA_BOOTSTRAP_SERVERS=34.64.141.183:9092,34.64.150.98:9092,34.64.82.185:9092
```

## 구현 현황

### 완료
- [x] Hub CRUD + 권한 체크 (MASTER, HUB)
- [x] HubInventory CRUD + MASTER 권한 체크
- [x] Feign Client (ItemProvider, CompanyProvider — 재고 등록 시 이름 조회)
- [x] Kafka 이벤트 (OrderPendingListener → 재고 차감 → 결과 발행)
- [x] HubEvents (Outbox 패턴 발행)
- [x] API 스펙 맞춤 (reservedQuantity, 삭제 응답 ID)
- [x] Kafka/토픽 설정 Config Server로 이동

### TODO
- [ ] 검색/필터 (QueryDSL — 목록 조회 필터)
- [ ] Hub update() 분리 (changeName, changeCapacity, changeAddress)
- [ ] SecurityUtil 전환
- [ ] AddressResolver (카카오 API)
- [ ] Swagger/OpenAPI
- [ ] Dockerfile + Docker 배포 + CI/CD
