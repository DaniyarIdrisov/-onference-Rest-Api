package waveaccess.daniyar.idrisov.conferencerestapi.services;


import waveaccess.daniyar.idrisov.conferencerestapi.dto.AddTalkForm;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.TalkDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;

import java.util.List;

public interface TalksService {

    List<TalkDto> getTalksByUserId(Long userId);

    List<TalkDto> getAllTalks();

    TalkDto addTalk(AddTalkForm addTalkForm, UserDto userDto);

    TalkDto getTalkById(Long talkId);

    TalkDto updateTalkById(Long talkId, TalkDto talkDto, UserDto userDto);

    TalkDto deleteTalkById(Long talkId, UserDto userDto);

    TalkDto addSpeakerToTalk(Long talkId, Long userId, UserDto userDto);

    TalkDto deleteSpeakerIntoTalk(Long talkId, Long userId, UserDto userDto);

    TalkDto getTalkByScheduleId(Long scheduleId);

}
