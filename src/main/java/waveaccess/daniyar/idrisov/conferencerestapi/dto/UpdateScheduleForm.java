package waveaccess.daniyar.idrisov.conferencerestapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScheduleForm {

    @NotNull
    private String talkTime;

}
