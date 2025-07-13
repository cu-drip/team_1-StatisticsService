package org.example.repository;

import org.example.entity.Match;
import org.example.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
    
    List<Match> findBySport(Sport sport);
    
    List<Match> findByTournamentId(UUID tournamentId);
    
    @Query("SELECT m FROM Match m WHERE m.sport = :sport AND m.matchId IN " +
           "(SELECT mp.match.matchId FROM MatchParticipant mp WHERE mp.participantId = :participantId)")
    List<Match> findBySportAndParticipantId(@Param("sport") Sport sport, 
                                           @Param("participantId") UUID participantId);

    Optional<Match> findByMatchId(UUID matchId);
} 