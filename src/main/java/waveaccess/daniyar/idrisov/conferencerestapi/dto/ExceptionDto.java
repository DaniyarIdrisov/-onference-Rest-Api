package waveaccess.daniyar.idrisov.conferencerestapi.dto;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDto {

    private String message;

    private Integer code;

}
