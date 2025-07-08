package org.example.service;

import org.example.dto.*;
import org.example.entity.Match;
import org.example.entity.MatchParticipant;
import org.example.model.ParticipantType;
import org.example.model.Sport;
import org.example.repository.MatchParticipantRepository;
import org.example.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.exception.ResourceNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatisticsService {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private MatchParticipantRepository matchParticipantRepository;
    
    public void updateTournamentStats(TournamentMatchStatsRequest request) {
        for (MatchWithParticipants matchData : request.getMatches()) {
            // Проверяем, есть ли уже матч с таким matchId
            Match match = matchRepository.findByMatchId(matchData.getMatchId())
                .orElseGet(() -> matchRepository.save(new Match(matchData.getMatchId(), request.getTournamentId(), request.getSport())));

            // Для каждого участника проверяем уникальность по matchId + participantId
            for (MatchParticipantStats participantData : matchData.getParticipants()) {
                Optional<MatchParticipant> existingParticipant = matchParticipantRepository
                    .findByMatch_MatchIdAndParticipantId(match.getMatchId(), participantData.getParticipantId());
                if (existingParticipant.isPresent()) {
                    // Обновляем статистику участника
                    MatchParticipant participant = existingParticipant.get();
                    participant.setParticipantType(participantData.getParticipantType());
                    participant.setPoints(participantData.getPoints());
                    participant.setIsWinner(participantData.getIsWinner());
                    participant.setGoals(participantData.getGoals());
                    participant.setAssists(participantData.getAssists());
                    participant.setFouls(participantData.getFouls());
                    participant.setYellowCards(participantData.getYellowCards());
                    participant.setRedCards(participantData.getRedCards());
                    participant.setKnockdowns(participantData.getKnockdowns());
                    participant.setSubmissions(participantData.getSubmissions());
                    participant.setSetsWon(participantData.getSetsWon());
                    participant.setTimePlayed(participantData.getTimePlayed());
                    matchParticipantRepository.save(participant);
                } else {
                    MatchParticipant participant = new MatchParticipant(
                        match,
                        participantData.getParticipantId(),
                        participantData.getParticipantType(),
                        participantData.getPoints(),
                        participantData.getIsWinner()
                    );
                    participant.setGoals(participantData.getGoals());
                    participant.setAssists(participantData.getAssists());
                    participant.setFouls(participantData.getFouls());
                    participant.setYellowCards(participantData.getYellowCards());
                    participant.setRedCards(participantData.getRedCards());
                    participant.setKnockdowns(participantData.getKnockdowns());
                    participant.setSubmissions(participantData.getSubmissions());
                    participant.setSetsWon(participantData.getSetsWon());
                    participant.setTimePlayed(participantData.getTimePlayed());
                    matchParticipantRepository.save(participant);
                }
            }
        }
    }
    
    public PlayerStats getPlayerStats(UUID userId, Sport sport) {
        List<MatchParticipant> playerMatches = matchParticipantRepository
            .findByParticipantIdAndTypeAndSport(userId, ParticipantType.player, sport);
        if (playerMatches.isEmpty()) {
            throw new ResourceNotFoundException("Игрок с id " + userId + " не найден для спорта " + sport);
        }
        int totalGames = playerMatches.size();
        int totalPoints = playerMatches.stream().mapToInt(MatchParticipant::getPoints).sum();
        float averagePoints = (float) totalPoints / totalGames;
        int totalWins = (int) playerMatches.stream().filter(MatchParticipant::getIsWinner).count();
        return new PlayerStats(userId, totalGames, totalPoints, averagePoints, totalWins);
    }
    
    public TeamStats getTeamStats(UUID teamId, Sport sport) {
        List<MatchParticipant> teamMatches = matchParticipantRepository
            .findByParticipantIdAndTypeAndSport(teamId, ParticipantType.team, sport);
        if (teamMatches.isEmpty()) {
            throw new ResourceNotFoundException("Команда с id " + teamId + " не найдена для спорта " + sport);
        }
        int totalGames = teamMatches.size();
        int totalPoints = teamMatches.stream().mapToInt(MatchParticipant::getPoints).sum();
        float averagePoints = (float) totalPoints / totalGames;
        int totalWins = (int) teamMatches.stream().filter(MatchParticipant::getIsWinner).count();
        return new TeamStats(teamId, totalGames, totalPoints, averagePoints, totalWins);
    }
    
    public List<PlayerStats> getTopPlayers(Sport sport) {
        List<MatchParticipant> topParticipants = matchParticipantRepository.findTopPlayersBySport(sport);
        
        // Группируем по участнику и суммируем статистику
        Map<UUID, List<MatchParticipant>> playerGroups = topParticipants.stream()
            .collect(Collectors.groupingBy(MatchParticipant::getParticipantId));
        
        List<PlayerStats> topPlayers = new ArrayList<>();
        
        for (Map.Entry<UUID, List<MatchParticipant>> entry : playerGroups.entrySet()) {
            UUID userId = entry.getKey();
            List<MatchParticipant> matches = entry.getValue();
            
            int totalGames = matches.size();
            int totalPoints = matches.stream().mapToInt(MatchParticipant::getPoints).sum();
            float averagePoints = (float) totalPoints / totalGames;
            int totalWins = (int) matches.stream().filter(MatchParticipant::getIsWinner).count();
            
            topPlayers.add(new PlayerStats(userId, totalGames, totalPoints, averagePoints, totalWins));
        }
        
        // Сортируем по общему количеству очков, затем по количеству побед и берем топ-5
        return topPlayers.stream()
            .sorted(Comparator
                .comparingInt(PlayerStats::getTotalPoints).reversed()
                .thenComparingInt(PlayerStats::getTotalWins).reversed()
            )
            .limit(5)
            .collect(Collectors.toList());
    }
    
    public List<MatchRecord> getMatchHistory(Sport sport, UUID participantId) {
        List<Match> matches;
        
        if (participantId != null) {
            matches = matchRepository.findBySportAndParticipantId(sport, participantId);
        } else {
            matches = matchRepository.findBySport(sport);
        }
        
        List<MatchRecord> matchRecords = new ArrayList<>();
        
        for (Match match : matches) {
            List<MatchParticipant> participants = matchParticipantRepository
                .findByMatchId(match.getMatchId());
            
            for (MatchParticipant participant : participants) {
                matchRecords.add(new MatchRecord(
                    match.getMatchId(),
                    participant.getParticipantId(),
                    participant.getParticipantType().toString(),
                    participant.getPoints(),
                    participant.getIsWinner()
                ));
            }
        }
        
        return matchRecords;
    }
} 