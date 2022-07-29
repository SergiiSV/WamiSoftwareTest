package testtask.wamisoftware.service.mapper;

import org.springframework.stereotype.Component;
import testtask.wamisoftware.dto.response.ParticipantResponseDto;
import testtask.wamisoftware.model.Participant;

@Component
public class ParticipantMapper {
    public ParticipantResponseDto mapToDto(Participant participant) {
        ParticipantResponseDto responseDto = new ParticipantResponseDto();
        responseDto.setTag(participant.getTag());
        responseDto.setSeconds(participant.getDuration());
        return responseDto;
    }
}
