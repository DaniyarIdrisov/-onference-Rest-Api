package waveaccess.daniyar.idrisov.conferencerestapi.dto;

import lombok.*;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Talk;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TalkDto {

    private Long id;

    private String topic;

    private String description;

    public static TalkDto from(Talk talk) {
        if (talk == null) {
            return null;
        }
        return TalkDto.builder()
                .id(talk.getId())
                .topic(talk.getTopic())
                .description(talk.getDescription())
                .build();
    }

    public static List<TalkDto> from(List<Talk> talks) {
        return talks.stream()
                .map(TalkDto::from)
                .collect(Collectors.toList());
    }

}
