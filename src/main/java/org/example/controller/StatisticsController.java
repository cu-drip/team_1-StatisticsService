package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.*;
import org.example.model.Sport;
import org.example.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stats")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @PostMapping("/update")
    public ResponseEntity<String> updateStats(@Valid @RequestBody TournamentMatchStatsRequest request) {
        statisticsService.updateTournamentStats(request);
        return ResponseEntity.ok("Данные успешно приняты");
    }
    
    @GetMapping("/player/{userId}")
    public ResponseEntity<?> getPlayerStats(
            @PathVariable UUID userId,
            @RequestParam(required = false) Sport sport,
            @RequestParam(required = false) Integer last_matches,
            @RequestParam(required = false) Integer last_tournaments,
            @RequestParam(required = false) UUID tournament_id,
            @RequestParam(required = false) UUID match_id) {
        if (sport == null) {
            List<PlayerStats> statsList = statisticsService.getPlayerStatsByAllSports(userId, last_matches, last_tournaments, tournament_id, match_id);
            return ResponseEntity.ok(statsList == null ? List.of() : statsList);
        } else {
            PlayerStats stats = statisticsService.getPlayerStats(userId, sport, last_matches, last_tournaments, tournament_id, match_id);
            return ResponseEntity.ok(stats == null ? new PlayerStats() : stats);
        }
    }
    
    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getTeamStats(
            @PathVariable UUID teamId,
            @RequestParam(required = false) Sport sport,
            @RequestParam(required = false) Integer last_matches,
            @RequestParam(required = false) Integer last_tournaments,
            @RequestParam(required = false) UUID tournament_id,
            @RequestParam(required = false) UUID match_id) {
        if (sport == null) {
            List<TeamStats> statsList = statisticsService.getTeamStatsByAllSports(teamId, last_matches, last_tournaments, tournament_id, match_id);
            return ResponseEntity.ok(statsList == null ? List.of() : statsList);
        } else {
            TeamStats stats = statisticsService.getTeamStats(teamId, sport, last_matches, last_tournaments, tournament_id, match_id);
            return ResponseEntity.ok(stats == null ? new TeamStats() : stats);
        }
    }
    
    @GetMapping("/top/players")
    public ResponseEntity<List<PlayerStats>> getTopPlayers(@RequestParam Sport sport, @RequestParam(required = false, defaultValue = "5") Integer limit, @RequestParam(required = false) UUID tournament_id) {
        List<PlayerStats> topPlayers = statisticsService.getTopPlayers(sport, limit, tournament_id);
        return ResponseEntity.ok(topPlayers == null ? List.of() : topPlayers);
    }

    @GetMapping("/tournament/{tournamentId}/matches")
    public ResponseEntity<List<MatchWithParticipants>> getMatchesByTournament(@PathVariable UUID tournamentId) {
        List<MatchWithParticipants> matches = statisticsService.getMatchesByTournament(tournamentId);
        return ResponseEntity.ok(matches == null ? List.of() : matches);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<MatchWithParticipants> getMatchStats(@PathVariable UUID matchId) {
        MatchWithParticipants match = statisticsService.getMatchWithParticipants(matchId);
        return ResponseEntity.ok(match == null ? new MatchWithParticipants() : match);
    }
} 
