# WTOrder System (Wireless Table Order System)

## 프로젝트 개요
WTOrder System은 음식점, 카페, 소규모 매장에서 사용할 수 있는 무선 테이블 오더 기반 통합 주문 관리 시스템입니다.
- **백엔드**: Spring Boot, Kotlin, JPA (Hibernate), WebSocket, H2 Database (개발용 메모리 DB)
- **앱(클라이언트)**: Android, Kotlin, Jetpack Compose, Retrofit, MVVM

## 폴더 구조
- `wtorder-server/`: Spring Boot 백엔드 서버 프로젝트
- `wtorder-app/`: Android 네이티브 앱 프로젝트

---

## 1. 서버 실행 방법 (wtorder-server)
1. IntelliJ IDEA (또는 Eclipse)에서 `wtorder-server` 폴더를 Open(또는 Import) 합니다.
2. Gradle이 자동으로 의존성을 다운로드합니다.
3. `src/main/kotlin/com/wtorder/WTOrderApplication.kt` 파일을 찾아 실행(Run)합니다.
4. 서버는 `http://localhost:8080` 에서 실행되며, H2 인메모리 DB가 활성화되어 샘플 데이터(메뉴 15개, 테이블 10개)가 자동 삽입됩니다.

---

## 2. 앱 빌드 및 실행 방법 (wtorder-app)
1. Android Studio에서 `wtorder-app` 폴더를 Open 합니다.
2. Gradle Sync가 완료될 때까지 기다립니다.
3. **에뮬레이터 실행 시 주의사항**: 
   - 앱 내의 `RetrofitClient.kt`와 `WebSocketManager.kt`의 서버 주소는 `10.0.2.2` (에뮬레이터용 로컬 루프백)로 설정되어 있습니다.
   - 서버가 켜져있는 PC에서 Android 에뮬레이터로 앱을 실행하면 정상 연동됩니다.
4. **실제 스마트폰 실행 시 주의사항 (APK 제출 시)**:
   - 스마트폰과 PC가 같은 Wi-Fi에 연결되어 있어야 합니다.
   - PC의 내부 IP 주소(예: `192.168.0.x`)를 확인합니다.
   - `RetrofitClient.kt`의 `BASE_URL`과 `WebSocketManager.kt`의 `WS_URL` 주소를 해당 PC의 IP로 변경한 후 빌드해야 합니다.
5. **APK 추출**:
   - 상단 메뉴에서 `Build -> Build Bundle(s) / APK(s) -> Build APK(s)`를 클릭합니다.
   - 완료 후 생성된 `app-debug.apk`를 과제용 배포 파일로 제출합니다.

---

## 앱 사용 시나리오 (역할별)
하나의 앱을 실행하면 **역할 선택 화면**이 나옵니다. 각각의 기능을 다음과 같이 테스트해보세요.

1. **Customer (고객)**: 테이블 번호를 선택하고 진입. 메뉴를 장바구니에 담고 주문하기를 누르면 주방과 매장으로 주문이 전송됩니다.
2. **Kitchen (주방)**: 고객 주문이 실시간 전표 카드로 나타납니다. 카드를 누르면 상세 화면에서 '조리 완료' 처리를 할 수 있습니다.
3. **Store (매장)**: 매장 메뉴의 가격/품절 상태를 수정하고, 새 메뉴를 추가할 수 있습니다. 상단 모니터링 아이콘을 누르면 현재 주문 및 직원 호출 상태를 실시간으로 확인합니다.
4. **PC Manager (카운터)**: 전체 테이블의 상태(빈 자리, 주문 중, 호출)를 색상으로 확인합니다. 테이블을 눌러 수동으로 주문을 추가하거나 삭제할 수 있습니다.
5. **Admin (최고 관리자)**: 매장 기본 정보를 수정하고, 총 누적 매출 및 테이블 가동률 등을 실시간 대시보드로 확인합니다.
