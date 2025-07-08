package org.example.entity;

import jakarta.persistence.*;
import org.example.model.ParticipantType;

import java.util.UUID;


@Entity
@Table(name = "match_participants",
       indexes = {
           @Index(name = "idx_participants_participant_id", columnList = "participant_id"),
           @Index(name = "idx_participants_type", columnList = "participant_type")
       })
public class MatchParticipant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
    
    @Column(name = "participant_id", nullable = false)
    private UUID participantId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "participant_type", nullable = false)
    private ParticipantType participantType;
    
    @Column(name = "points", nullable = false)
    private Integer points;
    
    @Column(name = "is_winner", nullable = false)
    private Boolean isWinner;
    
    @Column(name = "goals")
    private Integer goals;
    
    @Column(name = "assists")
    private Integer assists;
    
    @Column(name = "fouls")
    private Integer fouls;
    
    @Column(name = "yellow_cards")
    private Integer yellowCards;
    
    @Column(name = "red_cards")
    private Integer redCards;
    
    @Column(name = "knockdowns")
    private Integer knockdowns;
    
    @Column(name = "submissions")
    private Integer submissions;
    
    @Column(name = "sets_won")
    private Integer setsWon;
    
    @Column(name = "time_played")
    private Float timePlayed;
    
    // Constructors
    public MatchParticipant() {}
    
    public MatchParticipant(Match match, UUID participantId, ParticipantType participantType, 
                           Integer points, Boolean isWinner) {
        this.match = match;
        this.participantId = participantId;
        this.participantType = participantType;
        this.points = points;
        this.isWinner = isWinner;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Match getMatch() {
        return match;
    }
    
    public void setMatch(Match match) {
        this.match = match;
    }
    
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
