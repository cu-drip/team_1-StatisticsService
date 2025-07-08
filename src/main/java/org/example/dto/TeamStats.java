package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class TeamStats {
    
    @JsonProperty("teamId")
    private UUID teamId;
    
    @JsonProperty("totalGames")
    private Integer totalGames;
    
    @JsonProperty("totalPoints")
    private Integer totalPoints;
    
    @JsonProperty("averagePoints")
    private Float averagePoints;
    
    @JsonProperty("totalWins")
    private Integer totalWins;
    
    // Constructors
    public TeamStats() {}
    
    public TeamStats(UUID teamId, Integer totalGames, Integer totalPoints, Float averagePoints, Integer totalWins) {
        this.teamId = teamId;
        this.totalGames = totalGames;
        this.totalPoints = totalPoints;
        this.averagePoints = averagePoints;
        this.totalWins = totalWins;
    }
    
    // Getters and Setters
    public UUID getTeamId() {
        return teamId;
    }
    
    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }
    
    public Integer getTotalGames() {
        return totalGames;
    }
    
    public void setTotalGames(Integer totalGames) {
        this.totalGames = totalGames;
    }
    
    public Integer getTotalPoints() {
        return totalPoints;
    }
    
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }
    
    public Float getAveragePoints() {
        return averagePoints;
    }
    
    public void setAveragePoints(Float averagePoints) {
        this.averagePoints = averagePoints;
    }
    
    public Integer getTotalWins() {
        return totalWins;
    }
    
    public void setTotalWins(Integer totalWins) {
        this.totalWins = totalWins;
    }
} 