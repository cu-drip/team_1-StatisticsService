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
        return ResponseEntity.status(201).body("Данные успешно приняты");
    }
    
    @GetMapping("/player/{userId}")
    public ResponseEntity<PlayerStats> getPlayerStats(
            @PathVariable UUID userId,
            @RequestParam Sport sport) {
        PlayerStats stats = statisticsService.getPlayerStats(userId, sport);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/team/{teamId}")
    public ResponseEntity<TeamStats> getTeamStats(
            @PathVariable UUID teamId,
            @RequestParam Sport sport) {
        TeamStats stats = statisticsService.getTeamStats(teamId, sport);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/top/players")
    public ResponseEntity<List<PlayerStats>> getTopPlayers(@RequestParam Sport sport) {
        List<PlayerStats> topPlayers = statisticsService.getTopPlayers(sport);
        return ResponseEntity.ok(topPlayers);
    }
    
    @GetMapping("/matches/history")
    public ResponseEntity<List<MatchRecord>> getMatchHistory(
            @RequestParam Sport sport,
            @RequestParam(required = false) UUID participant_id) {
        List<MatchRecord> history = statisticsService.getMatchHistory(sport, participant_id);
        return ResponseEntity.ok(history);
    }
} 