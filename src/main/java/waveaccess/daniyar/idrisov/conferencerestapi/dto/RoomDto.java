package waveaccess.daniyar.idrisov.conferencerestapi.dto;

import lombok.*;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Room;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private Long id;

    private String roomNumber;

    public static RoomDto from(Room room) {
        if (room == null) {
            return null;
        }
        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .build();
    }

    public static List<RoomDto> from(List<Room> rooms) {
        return rooms.stream()
                .map(RoomDto::from)
                .collect(Collectors.toList());
    }

}
