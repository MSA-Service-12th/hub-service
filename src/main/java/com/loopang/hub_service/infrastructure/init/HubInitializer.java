package com.loopang.hub_service.infrastructure.init;

import com.loopang.hub_service.domain.hub.entity.Hub;
import com.loopang.hub_service.domain.hub.repository.HubRepository;
import com.loopang.hub_service.domain.hub.vo.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubInitializer implements ApplicationRunner {

    private final HubRepository hubRepository;

    private static final Object[][] HUBS = {
            {"서울특별시 센터", "서울특별시", "송파구", "송파대로 55", "서울특별시 송파구 송파대로 55", 37.5139, 127.1058, (short) 10},
            {"경기 북부 센터", "경기도", "고양시 덕양구", "권율대로 570", "경기도 고양시 덕양구 권율대로 570", 37.6584, 126.8320, (short) 10},
            {"경기 남부 센터", "경기도", "이천시", "덕평로 257-21", "경기도 이천시 덕평로 257-21", 37.1783, 127.4343, (short) 10},
            {"부산광역시 센터", "부산", "동구", "중앙대로 206", "부산 동구 중앙대로 206", 35.1143, 129.0404, (short) 10},
            {"대구광역시 센터", "대구", "북구", "태평로 161", "대구 북구 태평로 161", 35.8758, 128.5963, (short) 10},
            {"인천광역시 센터", "인천", "남동구", "정각로 29", "인천 남동구 정각로 29", 37.4563, 126.7052, (short) 10},
            {"광주광역시 센터", "광주", "서구", "내방로 111", "광주 서구 내방로 111", 35.1595, 126.8526, (short) 10},
            {"대전광역시 센터", "대전", "서구", "둔산로 100", "대전 서구 둔산로 100", 36.3504, 127.3845, (short) 10},
            {"울산광역시 센터", "울산", "남구", "중앙로 201", "울산 남구 중앙로 201", 35.5384, 129.3114, (short) 10},
            {"세종특별자치시 센터", "세종특별자치시", "", "한누리대로 2130", "세종특별자치시 한누리대로 2130", 36.4800, 127.2590, (short) 10},
            {"강원특별자치도 센터", "강원특별자치도", "춘천시", "중앙로 1", "강원특별자치도 춘천시 중앙로 1", 37.8813, 127.7298, (short) 10},
            {"충청북도 센터", "충북", "청주시 상당구", "상당로 82", "충북 청주시 상당구 상당로 82", 36.6358, 127.4913, (short) 10},
            {"충청남도 센터", "충남", "홍성군 홍북읍", "충남대로 21", "충남 홍성군 홍북읍 충남대로 21", 36.6588, 126.6728, (short) 10},
            {"전북특별자치도 센터", "전북특별자치도", "전주시 완산구", "효자로 225", "전북특별자치도 전주시 완산구 효자로 225", 35.8218, 127.1480, (short) 10},
            {"전라남도 센터", "전남", "무안군 삼향읍", "오룡길 1", "전남 무안군 삼향읍 오룡길 1", 34.8161, 126.4629, (short) 10},
            {"경상북도 센터", "경북", "안동시 풍천면", "도청대로 455", "경북 안동시 풍천면 도청대로 455", 36.5684, 128.7294, (short) 10},
            {"경상남도 센터", "경남", "창원시 의창구", "중앙대로 300", "경남 창원시 의창구 중앙대로 300", 35.2321, 128.6812, (short) 10},
    };

    @Override
    public void run(ApplicationArguments args) {
        if (hubRepository.findAll(org.springframework.data.domain.PageRequest.of(0, 1)).getTotalElements() > 0) {
            log.info("[HubInitializer] 이미 허브 데이터 존재 — 스킵");
            return;
        }

        log.info("[HubInitializer] 17개 허브 초기 데이터 등록 시작");

        for (Object[] hub : HUBS) {
            Address address = Address.builder()
                    .cityDo((String) hub[1])
                    .guGun((String) hub[2])
                    .dongDoro((String) hub[3])
                    .fullAddress((String) hub[4])
                    .latitude((Double) hub[5])
                    .longitude((Double) hub[6])
                    .build();

            Hub entity = Hub.builder()
                    .name((String) hub[0])
                    .capacity((Short) hub[7])
                    .address(address)
                    .build();

            hubRepository.save(entity);
        }

        log.info("[HubInitializer] 17개 허브 등록 완료");
    }
}
