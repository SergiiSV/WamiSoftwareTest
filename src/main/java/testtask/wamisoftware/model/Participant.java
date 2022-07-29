package testtask.wamisoftware.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Participant implements Comparable<Participant> {
    private String tag;
    private LocalDateTime start;
    private LocalDateTime finish;
    private Long duration;

    @Override
    public int compareTo(Participant participant) {
        return duration.compareTo(participant.duration);
    }
}
