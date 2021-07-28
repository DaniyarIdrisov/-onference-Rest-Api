package waveaccess.daniyar.idrisov.conferencerestapi.dto;

import lombok.*;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Schedule;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {

    private Long id;

    private String talkTime;

    public static ScheduleDto from(Schedule schedule) {
        if (schedule == null) {
            return null;
        }
        return ScheduleDto.builder()
                .id(schedule.getId())
                .talkTime(schedule.getTalkTime())
                .build();
    }

    public static List<ScheduleDto> from(List<Schedule> schedule) {
        return schedule.stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

}
