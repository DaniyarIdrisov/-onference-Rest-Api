package waveaccess.daniyar.idrisov.conferencerestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.AddTalkForm;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.TalkDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.AddEntityException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.DeleteEntityException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.UsageException;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Talk;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.TalksRepository;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;

import static waveaccess.daniyar.idrisov.conferencerestapi.dto.TalkDto.from;

@Service
public class TalksServiceImpl implements TalksService {

    @Autowired
    private TalksRepository talksRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public List<TalkDto> getTalksByUserId(Long userId) {
        User user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        return from(talksRepository.getAllBySpeakersContains(user));
    }

    @Override
    public List<TalkDto> getAllTalks() {
        return from(talksRepository.findAll());
    }

    @Override
    public TalkDto addTalk(AddTalkForm addTalkForm, UserDto userDto) {
        List<Long> usersId = addTalkForm.getUsersId();
        User currentUser = usersRepository.findById(userDto.getId()).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        List<User> users = new ArrayList<>();
        users.add(currentUser);
        if (usersId != null) {
            for (Long userId: usersId) {
                User user = usersRepository.findById(userId).orElse(null);
                if (user != null && user.getRole().equals(User.Role.SPEAKER)) {
                    users.add(user);
                }
            }
        }
        Talk talk = Talk.builder()
                .topic(addTalkForm.getTopic())
                .description(addTalkForm.getDescription())
                .speakers(users)
                .build();
        talksRepository.save(talk);
        return from(talk);
    }

    @Override
    public TalkDto getTalkById(Long talkId) {
        return from(talksRepository.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!")));
    }

    private boolean isYourTalk(Long talkId, UserDto userDto) {
        User user = usersRepository.findById(userDto.getId()).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        Talk talk = talksRepository.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        List<Talk> talks = user.getTalks();
        for (Talk talk1 : talks) {
            if (talk.getId().equals(talk1.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public TalkDto updateTalkById(Long talkId, TalkDto talkDto, UserDto userDto) {
        if (!isYourTalk(talkId, userDto)) throw new UsageException("This is not your talk!");
        Talk talk = talksRepository.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        if (talkDto.getTopic() != null) {
            talk.setTopic(talkDto.getTopic());
        }
        if (talkDto.getDescription() != null) {
            talk.setDescription(talkDto.getDescription());
        }
        talksRepository.save(talk);
        return from(talk);
    }

    private boolean hasChildren(Long talkId) {
        Talk talk = talksRepository.findById(talkId).orElse(null);
        assert talk != null;
        return !(talk.getSchedule() == null) || !(talk.getSpeakers().isEmpty());
    }

    @Override
    public TalkDto deleteTalkById(Long talkId, UserDto userDto) {
        if (!isYourTalk(talkId, userDto)) throw new UsageException("This is not your talk!");
        if (hasChildren(talkId)) throw new DeleteEntityException("You cannot delete an entity as it has children!");
        Talk talk = talksRepository.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        talksRepository.deleteById(talkId);
        return from(talk);
    }

    @Override
    public TalkDto addSpeakerToTalk(Long talkId, Long userId, UserDto userDto) {
        if (!isYourTalk(talkId, userDto)) throw new UsageException("This is not your talk!");
        if (isAlreadyInTalk(talkId, userId)) throw new AddEntityException("This user is already in the talk!");
        Talk talk = talksRepository.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        if (!isSpeaker(userId)) throw new AddEntityException("This user is not a speaker!");
        User user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        List<User> speakers = talk.getSpeakers();
        speakers.add(user);
        talk.setSpeakers(speakers);
        talksRepository.save(talk);
        return from(talk);
    }

    @Override
    public TalkDto deleteSpeakerIntoTalk(Long talkId, Long userId, UserDto userDto) {
        if (!isYourTalk(talkId, userDto)) throw new UsageException("This is not your talk!");
        if (userId.equals(userDto.getId())) throw new DeleteEntityException("You cannot remove yourself from the talk!");
        if (!isSpeaker(userId)) throw new AddEntityException("This user is not a speaker!");
        Talk talk = talksRepository.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        User user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        List<User> speakers = talk.getSpeakers();
        speakers.remove(user);
        talk.setSpeakers(speakers);
        talksRepository.save(talk);
        return from(talk);
    }

    private boolean isAlreadyInTalk(Long talkId, Long userId) {
        Talk talk = talksRepository.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        User user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        List<User> speakers = talk.getSpeakers();
        for (User speaker : speakers) {
            if (speaker.equals(user)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSpeaker(Long userId) {
        User user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        if (user.getRole().equals(User.Role.SPEAKER)) {
            return true;
        }
        return false;
    }

    @Override
    public TalkDto getTalkByScheduleId(Long scheduleId) {
        return from(talksRepository.getTalkBySchedule_Id(scheduleId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!")));
    }

}
