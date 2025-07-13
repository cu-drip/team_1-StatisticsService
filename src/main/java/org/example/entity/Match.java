package org.example.entity;

import jakarta.persistence.*;
import org.example.model.Sport;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "matches",
       indexes = {
           @Index(name = "idx_matches_sport", columnList = "sport"),
           @Index(name = "idx_matches_tournament", columnList = "tournament_id")
       })
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "match_id", nullable = false, unique = true)
    private UUID matchId;
    
    @Column(name = "tournament_id", nullable = false)
    private UUID tournamentId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "sport", nullable = false)
    private Sport sport;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public Match() {}
    
    public Match(UUID matchId, UUID tournamentId, Sport sport) {
        this.matchId = matchId;
        this.tournamentId = tournamentId;
        this.sport = sport;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getMatchId() {
        return matchId;
    }
    
    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }
    
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 
