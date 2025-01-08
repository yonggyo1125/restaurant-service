package org.koreait.datatransfer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class Transfer {

    @Test
    void process() {
        CsvMapper mapper = new CsvMapper();

        try (BufferedReader br = new BufferedReader(new FileReader("C:/data/data.csv"))) {
            String line;
            while((line = br.readLine()) != null) {
                List<String> item = mapper.readValue(line, new TypeReference<>(){});
            }

        } catch (IOException e) {}
    }
}
