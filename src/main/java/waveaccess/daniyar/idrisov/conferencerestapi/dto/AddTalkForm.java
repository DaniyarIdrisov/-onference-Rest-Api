package waveaccess.daniyar.idrisov.conferencerestapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddTalkForm {

    @NotNull
    private String topic;

    @NotNull
    private String description;

    private List<Long> usersId;

}
