package waveaccess.daniyar.idrisov.conferencerestapi.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "talks")
public class Talk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private String description;

    @ManyToMany
    @JoinTable(name = "talk_speakers",
            joinColumns = {@JoinColumn(name = "talk_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "speaker_id", referencedColumnName = "id")})
    private List<User> speakers;

    @OneToOne(mappedBy = "talk")
    private Schedule schedule;

}
