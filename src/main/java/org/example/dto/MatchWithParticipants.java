package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class MatchWithParticipants {
    
    @NotNull
    @JsonProperty("matchId")
    private UUID matchId;
    
    @NotEmpty
    @Valid
    @JsonProperty("participants")
    private List<MatchParticipantStats> participants;
    
    // Constructors
    public MatchWithParticipants() {}
    
    public MatchWithParticipants(UUID matchId, List<MatchParticipantStats> participants) {
        this.matchId = matchId;
        this.participants = participants;
    }
    
    // Getters and Setters
    public UUID getMatchId() {
        return matchId;
    }
    
    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }
    
    public List<MatchParticipantStats> getParticipants() {
        return participants;
    }
    
    public void setParticipants(List<MatchParticipantStats> participants) {
        this.participants = participants;
    }
} 