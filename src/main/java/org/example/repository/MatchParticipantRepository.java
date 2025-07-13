package org.example.repository;

import org.example.entity.MatchParticipant;
import org.example.model.ParticipantType;
import org.example.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, UUID> {
    
    List<MatchParticipant> findByParticipantId(UUID participantId);
    
    List<MatchParticipant> findByParticipantIdAndParticipantType(UUID participantId, ParticipantType participantType);
    
    @Query("SELECT mp FROM MatchParticipant mp JOIN mp.match m WHERE mp.participantId = :participantId " +
           "AND mp.participantType = :participantType AND m.sport = :sport")
    List<MatchParticipant> findByParticipantIdAndTypeAndSport(@Param("participantId") UUID participantId,
                                                              @Param("participantType") ParticipantType participantType,
                                                              @Param("sport") Sport sport);
    
    @Query("SELECT mp FROM MatchParticipant mp JOIN mp.match m WHERE m.sport = :sport ORDER BY mp.points DESC")
    List<MatchParticipant> findTopPlayersBySport(@Param("sport") Sport sport);
    
    @Query("SELECT mp FROM MatchParticipant mp WHERE mp.match.matchId = :matchId")
    List<MatchParticipant> findByMatchId(@Param("matchId") UUID matchId);

    Optional<MatchParticipant> findByMatch_MatchIdAndParticipantId(UUID matchId, UUID participantId);
} 
