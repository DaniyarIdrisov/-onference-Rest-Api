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
@Table(name = "users")
public class User {

    private static final long serialVersionUID = 7L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(name = "hash_password")
    private String hashPassword;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String organization;

    @Column(name = "confirm_code")
    private String confirmCode;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN, SPEAKER, LISTENER
    }

    @Enumerated(EnumType.STRING)
    private State state;

    public enum State {
        ACTIVE, BANNED
    }

    @Enumerated(EnumType.STRING)
    private Confirmation confirmation;

    public enum Confirmation {
        CONFIRMED, NOT_CONFIRMED
    }

    @ManyToMany(mappedBy = "speakers")
    private List<Talk> talks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Token> tokens;

    public boolean isActive() {
        return this.state == State.ACTIVE;
    }

    public boolean isBanned() {
        return this.state == State.BANNED;
    }

    public boolean isConfirmed() {
        return this.confirmation == Confirmation.CONFIRMED;
    }

    public  boolean isNotConfirmed() {
        return this.confirmation == Confirmation.NOT_CONFIRMED;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isSpeaker() {
        return this.role == Role.SPEAKER;
    }

    public boolean isListener() {
        return this.role == Role.LISTENER;
    }


}
