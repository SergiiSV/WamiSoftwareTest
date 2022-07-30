package testtask.wamisoftware.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import testtask.wamisoftware.model.Participant;
import testtask.wamisoftware.service.ReadInformationService;

@Service
public class ReadInformationFromLogs implements ReadInformationService {
    private static final String START_DATA_FILE_NAME =
            "src/main/resources/dataFiles/tag_read_start.log";
    private static final String FINISH_DATA_FILE_NAME =
            "src/main/resources/dataFiles/tag_reads_finish.log";
    private static final String FINISH_TIME_ZONE = "Europe/Kiev";
    private static final int TAG_STARTS = 4;
    private static final int TAG_ENDS = 16;
    private static final int TIMESTAMP_STARTS = 20;
    private static final int TIMESTAMP_ENDS = 32;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public List<Participant> getParticipantData() {
        Map<String, LocalDateTime> startData = getStatisticFromStartData(START_DATA_FILE_NAME);
        Map<String, LocalDateTime> finishData = getStatisticFromFinishData(FINISH_DATA_FILE_NAME);
        return finishData.entrySet().stream()
                .filter(e -> startData.containsKey(e.getKey()))
                .map(e -> {
                    String key = e.getKey();
                    LocalDateTime startTime = startData.get(key);
                    LocalDateTime finishTime = e.getValue();
                    return new Participant(key, startTime, finishTime,
                            Duration.between(startTime, finishTime).toSeconds()
                                    - finishTime
                                    .atZone(TimeZone.getTimeZone(FINISH_TIME_ZONE).toZoneId())
                                    .getOffset().getTotalSeconds());
                })
                .filter(p -> p.getDuration() > 0)
                .sorted()
                .limit(10)
                .collect(Collectors.toList());
    }

    private Map<String, LocalDateTime> getStatisticFromStartData(String fromStartFileName) {
        Map<String, LocalDateTime> startData = new HashMap<>();
        String data = getDataFromFile(fromStartFileName);
        String[] dividedData = data.split(System.lineSeparator());
        for (String participantData : dividedData) {
            String tagName = participantData.substring(TAG_STARTS, TAG_ENDS);
            if (!startData.containsKey(tagName)) {
                startData.put(tagName,
                        parseDate(participantData.substring(TIMESTAMP_STARTS, TIMESTAMP_ENDS)));
            }
        }
        return startData;
    }

    private Map<String, LocalDateTime> getStatisticFromFinishData(String fromFinishFileName) {
        Map<String, LocalDateTime> finishData = new HashMap<>();
        String data = getDataFromFile(fromFinishFileName);
        String[] dividedData = data.split(System.lineSeparator());
        for (String participantData : dividedData) {
            finishData.put(participantData.substring(TAG_STARTS, TAG_ENDS),
                    parseDate(participantData.substring(TIMESTAMP_STARTS, TIMESTAMP_ENDS)));
        }
        return finishData;
    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date, formatter);
    }

    private String getDataFromFile(String fileName) {
        File file = new File(fileName);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String value = reader.readLine();
            while (value != null) {
                stringBuilder.append(value).append(System.lineSeparator());
                value = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read file " + fileName, e);
        }
        return stringBuilder.toString();
    }
}
