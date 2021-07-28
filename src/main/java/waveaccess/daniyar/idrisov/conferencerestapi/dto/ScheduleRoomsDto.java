package waveaccess.daniyar.idrisov.conferencerestapi.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRoomsDto {

    private RoomDto roomDto;

    private List<ScheduleDto> scheduleDtoList;

}
