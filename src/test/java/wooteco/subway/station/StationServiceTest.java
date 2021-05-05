package wooteco.subway.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class StationServiceTest {

    @Autowired
    private StationService stationService;

    @DisplayName("역 추가 기능")
    @Test
    void createStation() {
        final String name = "잠실역";
        stationService.createStation(new StationRequest(name));

        final Station station = stationService.findByName(name);
        assertThat(station.getName()).isEqualTo(name);
    }

    @DisplayName("중복된 이름의 지하철역 추가할 때 실패하는지 확인")
    @Test
    void checkDuplicatedStationName() {
        final String name = "잠실역";
        stationService.createStation(new StationRequest(name));

        assertThatThrownBy(() -> stationService.createStation(new StationRequest(name)))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("중복된 이름의 지하철역입니다.");
    }

    @DisplayName("역 조회 기능이 실패하는지 확인")
    @Test
    void findStationsFail() {
        final String name = "잠실역";
        assertThatThrownBy(() -> stationService.findByName(name))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("해당 이름의 지하철역이 없습니다.");
    }

    @DisplayName("역 제거 기능이 성공하는지 확인")
    @Test
    void deleteStation() {
        final String name = "잠실역";
        stationService.createStation(new StationRequest(name));
        final Station station = stationService.findByName(name);

        stationService.deleteStation(station.getId());
        assertThatThrownBy(() -> stationService.findById(station.getId()))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("해당 Id의 지하철역이 없습니다.");
    }

    @DisplayName("역 제거 기능이 실패하는지 확인")
    @Test
    void deleteStationFail() {
        assertThatThrownBy(() -> stationService.deleteStation(1L))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("해당 Id의 지하철역이 없습니다.");
    }
}
