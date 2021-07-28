package waveaccess.daniyar.idrisov.conferencerestapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddScheduleForm {

    @NotNull
    private String talkTime;

}
