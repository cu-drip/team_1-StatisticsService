package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.model.Sport;
import org.example.model.TournamentType;

import java.util.List;
import java.util.UUID;

public class TournamentMatchStatsRequest {
    
    @NotNull
    @JsonProperty("tournamentId")
    private UUID tournamentId;
    
    @NotNull
    @JsonProperty("sport")
    private Sport sport;
    
    @NotNull
    @JsonProperty("tournamentType")
    private TournamentType tournamentType;
    
    @NotEmpty
    @Valid
    @JsonProperty("matches")
    private List<MatchWithParticipants> matches;
    
    // Constructors
    public TournamentMatchStatsRequest() {}
    
    public TournamentMatchStatsRequest(UUID tournamentId, Sport sport, TournamentType tournamentType, List<MatchWithParticipants> matches) {
        this.tournamentId = tournamentId;
        this.sport = sport;
        this.tournamentType = tournamentType;
        this.matches = matches;
    }
    
    // Getters and Setters
    public UUID getTournamentId() {
        return tournamentId;
    }
    
    public void setTournamentId(UUID tournamentId) {
        this.tournamentId = tournamentId;
    }
    
    public Sport getSport() {
        return sport;
    }
    
    public void setSport(Sport sport) {
        this.sport = sport;
    }
    
    public TournamentType getTournamentType() {
        return tournamentType;
    }
    
    public void setTournamentType(TournamentType tournamentType) {
        this.tournamentType = tournamentType;
    }
    
    public List<MatchWithParticipants> getMatches() {
        return matches;
    }
    
    public void setMatches(List<MatchWithParticipants> matches) {
        this.matches = matches;
    }
} 
