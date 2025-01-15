package org.koreait.datatransfer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.junit.jupiter.api.Test;
import org.koreait.restaurant.entities.Restaurant;
import org.koreait.restaurant.repositories.RestaurantRepository;
import org.locationtech.proj4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

@SpringBootTest
public class Transfer {

    @Autowired
    private RestaurantRepository repository;

    @Test
    void process() {
        CsvMapper mapper = new CsvMapper();

        try (BufferedReader br = new BufferedReader(new FileReader("D:/data.csv"))) {
            String line;
            while((line = br.readLine()) != null) {
                try {
                    List<String> item = mapper.readValue(line, new TypeReference<>() {
                    });
                    if (item.get(8).equals("폐업")) continue;


                    double[] location = transformTMToWGS84(Double.parseDouble(item.get(26)), Double.parseDouble(item.get(27)));
                    Restaurant rItem = Restaurant.builder()
                            .seq(Long.parseLong(item.get(0)))
                            .name(item.get(21))
                            .category(item.get(25))
                            .address(item.get(19) + " " + item.get(20))
                            .latitude(location[1])
                            .longitude(location[0])
                            .build();
                    System.out.println(rItem);
                    repository.saveAndFlush(rItem);

                } catch (Exception e) {}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double[] transformTMToWGS84(double lon, double lat) {
        CRSFactory crsFactory = new CRSFactory();

        // WGS84 좌표계 (EPSG:4326)
        CoordinateReferenceSystem crsWGS84 = crsFactory.createFromName("EPSG:4326");

        // TM 중부원점 좌표계 (EPSG:2097)
        CoordinateReferenceSystem crsTM = crsFactory.createFromParameters("EPSG:2097", "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43");

        // CoordinateTransformFactory 생성
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();

        // 좌표 변환 객체 생성
        //CoordinateTransform transform = ctFactory.createTransform(crsWGS84, crsTM);
        CoordinateTransform transform = ctFactory.createTransform(crsTM, crsWGS84);
        // 변환할 좌표 설정
        ProjCoordinate sourceCoordinate = new ProjCoordinate(lon, lat);
        ProjCoordinate targetCoordinate = new ProjCoordinate();

        // 좌표 변환 수행
        transform.transform(sourceCoordinate, targetCoordinate);

        return new double[]{targetCoordinate.x, targetCoordinate.y};
    }
}
