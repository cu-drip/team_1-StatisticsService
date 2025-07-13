package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.example.model.Sport;

import java.util.UUID;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"teamId", "sport", "totalGames", "totalPoints", "averagePoints", "totalWins", "totalDraws", "totalLosses", "assists", "fouls", "yellowCards", "redCards", "setsWon", "submissions", "knockdowns", "timePlayed"})
public class TeamStats {
    
    @JsonProperty("teamId")
    private UUID teamId;
    
    @JsonProperty("sport")
    private Sport sport;
    
    @JsonProperty("totalGames")
    private Integer totalGames;
    
    @JsonProperty("totalPoints")
    private Integer totalPoints;
    
    @JsonProperty("averagePoints")
    private Float averagePoints;
    
    @JsonProperty("totalWins")
    private Integer totalWins;
    
    @JsonProperty("totalDraws")
    private int totalDraws;
    @JsonProperty("totalLosses")
    private int totalLosses;
    
    private Integer assists;
    private Integer fouls;
    private Integer yellowCards;
    private Integer redCards;
    private Integer setsWon;
    private Integer submissions;
    private Integer knockdowns;
    private Float timePlayed;
    @JsonInclude(Include.NON_NULL)
    private Integer winRate;
    
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

    public int getTotalDraws() {
        return totalDraws;
    }
    public void setTotalDraws(int totalDraws) {
        this.totalDraws = totalDraws;
    }
    public int getTotalLosses() {
        return totalLosses;
    }
    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    public Sport getSport() {
        return sport;
    }
    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Integer getAssists() { return assists; }
    public void setAssists(Integer assists) { this.assists = assists; }
    public Integer getFouls() { return fouls; }
    public void setFouls(Integer fouls) { this.fouls = fouls; }
    public Integer getYellowCards() { return yellowCards; }
    public void setYellowCards(Integer yellowCards) { this.yellowCards = yellowCards; }
    public Integer getRedCards() { return redCards; }
    public void setRedCards(Integer redCards) { this.redCards = redCards; }
    public Integer getSetsWon() { return setsWon; }
    public void setSetsWon(Integer setsWon) { this.setsWon = setsWon; }
    public Integer getSubmissions() { return submissions; }
    public void setSubmissions(Integer submissions) { this.submissions = submissions; }
    public Integer getKnockdowns() { return knockdowns; }
    public void setKnockdowns(Integer knockdowns) { this.knockdowns = knockdowns; }
    public Float getTimePlayed() { return timePlayed; }
    public void setTimePlayed(Float timePlayed) { this.timePlayed = timePlayed; }
    public Integer getWinRate() { return winRate; }
    public void setWinRate(Integer winRate) { this.winRate = winRate; }
} 
