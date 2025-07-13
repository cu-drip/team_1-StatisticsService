package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class MatchRecord {
    
    @JsonProperty("matchId")
    private UUID matchId;
    
    @JsonProperty("participantId")
    private UUID participantId;
    
    @JsonProperty("participantType")
    private String participantType;
    
    @JsonProperty("points")
    private Integer points;
    
    @JsonProperty("isWinner")
    private Boolean isWinner;
    
    // Constructors
    public MatchRecord() {}
    
    public MatchRecord(UUID matchId, UUID participantId, String participantType, Integer points, Boolean isWinner) {
        this.matchId = matchId;
        this.participantId = participantId;
        this.participantType = participantType;
        this.points = points;
        this.isWinner = isWinner;
    }
    
    // Getters and Setters
    public UUID getMatchId() {
        return matchId;
    }
    
    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }
    
    public UUID getParticipantId() {
        return participantId;
    }
    
    public void setParticipantId(UUID participantId) {
        this.participantId = participantId;
    }
    
    public String getParticipantType() {
        return participantType;
    }
    
    public void setParticipantType(String participantType) {
        this.participantType = participantType;
    }
    
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
    
    public Boolean getIsWinner() {
        return isWinner;
    }
    
    public void setIsWinner(Boolean isWinner) {
        this.isWinner = isWinner;
    }
} 
