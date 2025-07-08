package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.example.model.ParticipantType;

import java.util.UUID;

public class MatchParticipantStats {
    
    @NotNull
    @JsonProperty("participantId")
    private UUID participantId;
    
    @NotNull
    @JsonProperty("participantType")
    private ParticipantType participantType;
    
    @NotNull
    @JsonProperty("points")
    private Integer points;
    
    @NotNull
    @JsonProperty("isWinner")
    private Boolean isWinner;
    
    @JsonProperty("goals")
    private Integer goals;
    
    @JsonProperty("assists")
    private Integer assists;
    
    @JsonProperty("fouls")
    private Integer fouls;
    
    @JsonProperty("yellowCards")
    private Integer yellowCards;
    
    @JsonProperty("redCards")
    private Integer redCards;
    
    @JsonProperty("knockdowns")
    private Integer knockdowns;
    
    @JsonProperty("submissions")
    private Integer submissions;
    
    @JsonProperty("setsWon")
    private Integer setsWon;
    
    @JsonProperty("timePlayed")
    private Float timePlayed;
    
    // Constructors
    public MatchParticipantStats() {}
    
    public MatchParticipantStats(UUID participantId, ParticipantType participantType, Integer points, Boolean isWinner) {
        this.participantId = participantId;
        this.participantType = participantType;
        this.points = points;
        this.isWinner = isWinner;
    }
    
    // Getters and Setters
    public UUID getParticipantId() {
        return participantId;
    }
    
    public void setParticipantId(UUID participantId) {
        this.participantId = participantId;
    }
    
    public ParticipantType getParticipantType() {
        return participantType;
    }
    
    public void setParticipantType(ParticipantType participantType) {
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
    
    public Integer getGoals() {
        return goals;
    }
    
    public void setGoals(Integer goals) {
        this.goals = goals;
    }
    
    public Integer getAssists() {
        return assists;
    }
    
    public void setAssists(Integer assists) {
        this.assists = assists;
    }
    
    public Integer getFouls() {
        return fouls;
    }
    
    public void setFouls(Integer fouls) {
        this.fouls = fouls;
    }
    
    public Integer getYellowCards() {
        return yellowCards;
    }
    
    public void setYellowCards(Integer yellowCards) {
        this.yellowCards = yellowCards;
    }
    
    public Integer getRedCards() {
        return redCards;
    }
    
    public void setRedCards(Integer redCards) {
        this.redCards = redCards;
    }
    
    public Integer getKnockdowns() {
        return knockdowns;
    }
    
    public void setKnockdowns(Integer knockdowns) {
        this.knockdowns = knockdowns;
    }
    
    public Integer getSubmissions() {
        return submissions;
    }
    
    public void setSubmissions(Integer submissions) {
        this.submissions = submissions;
    }
    
    public Integer getSetsWon() {
        return setsWon;
    }
    
    public void setSetsWon(Integer setsWon) {
        this.setsWon = setsWon;
    }
    
    public Float getTimePlayed() {
        return timePlayed;
    }
    
    public void setTimePlayed(Float timePlayed) {
        this.timePlayed = timePlayed;
    }
} 