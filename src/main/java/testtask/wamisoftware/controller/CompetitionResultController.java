package testtask.wamisoftware.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.wamisoftware.dto.response.ParticipantResponseDto;
import testtask.wamisoftware.service.ReadInformationService;
import testtask.wamisoftware.service.mapper.ParticipantMapper;

@RestController
@RequestMapping("/results")
public class CompetitionResultController {

    private final ReadInformationService readInformationService;
    private final ParticipantMapper mapper;

    public CompetitionResultController(ReadInformationService readInformationService,
                                       ParticipantMapper mapper) {
        this.readInformationService = readInformationService;
        this.mapper = mapper;
    }

    @GetMapping("/theBestTen")
    public List<ParticipantResponseDto> getTenParticipantLeastTime() {
        return readInformationService.getParticipantData().stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }
}
