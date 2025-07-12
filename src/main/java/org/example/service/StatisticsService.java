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
        List<UUID> matchIds = request.getMatches().stream()
            .map(MatchWithParticipants::getMatchId)
            .collect(Collectors.toList());
        // Проверка: есть ли хотя бы один matchId уже в базе
        List<UUID> existingMatchIds = matchIds.stream()
            .filter(id -> matchRepository.findByMatchId(id).isPresent())
            .collect(Collectors.toList());
        if (!existingMatchIds.isEmpty()) {
            throw new IllegalArgumentException("В базе уже существуют матчи с matchId: " + existingMatchIds);
        }
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
    
    public PlayerStats getPlayerStats(UUID userId, Sport sport, Integer lastMatches, Integer lastTournaments, UUID tournamentId, UUID matchId) {
        List<MatchParticipant> playerMatches = matchParticipantRepository.findByParticipantIdAndTypeAndSport(userId, ParticipantType.player, sport);
        playerMatches = filterMatches(playerMatches, lastMatches, lastTournaments, tournamentId, matchId);
        if (playerMatches.isEmpty()) {
            return null;
        }
        int totalGames = playerMatches.size();
        int totalPoints = playerMatches.stream().mapToInt(MatchParticipant::getPoints).sum();
        float averagePoints = (float) totalPoints / totalGames;
        int totalWins = (int) playerMatches.stream().filter(MatchParticipant::getIsWinner).count();
        int totalDraws = (int) playerMatches.stream().filter(mp -> isDraw(mp)).count();
        int totalLosses = totalGames - totalWins - totalDraws;
        PlayerStats stats = new PlayerStats(userId, totalGames, totalPoints, averagePoints, totalWins);
        stats.setTotalDraws(totalDraws);
        stats.setTotalLosses(totalLosses);
        if (sport != null) {
            stats.setSport(sport);
            switch (sport) {
                case football -> {
                    stats.setAssists(playerMatches.stream().map(MatchParticipant::getAssists).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setFouls(playerMatches.stream().map(MatchParticipant::getFouls).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setYellowCards(playerMatches.stream().map(MatchParticipant::getYellowCards).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setRedCards(playerMatches.stream().map(MatchParticipant::getRedCards).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(playerMatches));
                }
                case basketball -> {
                    stats.setAssists(playerMatches.stream().map(MatchParticipant::getAssists).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setFouls(playerMatches.stream().map(MatchParticipant::getFouls).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(playerMatches));
                }
                case tennis -> {
                    stats.setSetsWon(playerMatches.stream().map(MatchParticipant::getSetsWon).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(playerMatches));
                }
                case jiu_jitsu -> {
                    stats.setSubmissions(playerMatches.stream().map(MatchParticipant::getSubmissions).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(playerMatches));
                }
                case chess -> stats.setTimePlayed(sumTimePlayed(playerMatches));
                case boxing -> {
                    stats.setKnockdowns(playerMatches.stream().map(MatchParticipant::getKnockdowns).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(playerMatches));
                }
            }
        }
        Integer winRate = (totalGames - totalDraws) > 0 ? Math.round(((float) totalWins / (totalGames - totalDraws)) * 100f) : null;
        stats.setWinRate(winRate);
        return stats;
    }
    
    public TeamStats getTeamStats(UUID teamId, Sport sport, Integer lastMatches, Integer lastTournaments, UUID tournamentId, UUID matchId) {
        List<MatchParticipant> teamMatches = matchParticipantRepository.findByParticipantIdAndTypeAndSport(teamId, ParticipantType.team, sport);
        teamMatches = filterMatches(teamMatches, lastMatches, lastTournaments, tournamentId, matchId);
        if (teamMatches.isEmpty()) {
            return null;
        }
        int totalGames = teamMatches.size();
        int totalPoints = teamMatches.stream().mapToInt(MatchParticipant::getPoints).sum();
        float averagePoints = (float) totalPoints / totalGames;
        int totalWins = (int) teamMatches.stream().filter(MatchParticipant::getIsWinner).count();
        int totalDraws = (int) teamMatches.stream().filter(mp -> isDraw(mp)).count();
        int totalLosses = totalGames - totalWins - totalDraws;
        TeamStats stats = new TeamStats(teamId, totalGames, totalPoints, averagePoints, totalWins);
        stats.setTotalDraws(totalDraws);
        stats.setTotalLosses(totalLosses);
        if (sport != null) {
            stats.setSport(sport);
            switch (sport) {
                case football -> {
                    stats.setAssists(teamMatches.stream().map(MatchParticipant::getAssists).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setFouls(teamMatches.stream().map(MatchParticipant::getFouls).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setYellowCards(teamMatches.stream().map(MatchParticipant::getYellowCards).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setRedCards(teamMatches.stream().map(MatchParticipant::getRedCards).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(teamMatches));
                }
                case basketball -> {
                    stats.setAssists(teamMatches.stream().map(MatchParticipant::getAssists).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setFouls(teamMatches.stream().map(MatchParticipant::getFouls).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(teamMatches));
                }
                case tennis -> {
                    stats.setSetsWon(teamMatches.stream().map(MatchParticipant::getSetsWon).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(teamMatches));
                }
                case jiu_jitsu -> {
                    stats.setSubmissions(teamMatches.stream().map(MatchParticipant::getSubmissions).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(teamMatches));
                }
                case chess -> stats.setTimePlayed(sumTimePlayed(teamMatches));
                case boxing -> {
                    stats.setKnockdowns(teamMatches.stream().map(MatchParticipant::getKnockdowns).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(teamMatches));
                }
            }
        }
        Integer winRate = (totalGames - totalDraws) > 0 ? Math.round(((float) totalWins / (totalGames - totalDraws)) * 100f) : null;
        stats.setWinRate(winRate);
        return stats;
    }
    
    public List<PlayerStats> getTopPlayers(Sport sport, int limit, UUID tournamentId) {
        List<MatchParticipant> topParticipants = matchParticipantRepository.findTopPlayersBySport(sport);
        if (tournamentId != null) {
            topParticipants = topParticipants.stream()
                .filter(mp -> mp.getMatch().getTournamentId().equals(tournamentId))
                .toList();
        }
        Map<UUID, List<MatchParticipant>> playerGroups = topParticipants.stream().collect(Collectors.groupingBy(MatchParticipant::getParticipantId));
        List<PlayerStats> topPlayers = new ArrayList<>();
        for (Map.Entry<UUID, List<MatchParticipant>> entry : playerGroups.entrySet()) {
            UUID userId = entry.getKey();
            List<MatchParticipant> matches = entry.getValue();
            int totalGames = matches.size();
            int totalPoints = matches.stream().mapToInt(MatchParticipant::getPoints).sum();
            float averagePoints = (float) totalPoints / totalGames;
            int totalWins = (int) matches.stream().filter(MatchParticipant::getIsWinner).count();
            int totalDraws = (int) matches.stream().filter(mp -> isDraw(mp)).count();
            int totalLosses = totalGames - totalWins - totalDraws;
            PlayerStats stats = new PlayerStats(userId, totalGames, totalPoints, averagePoints, totalWins);
            stats.setTotalDraws(totalDraws);
            stats.setTotalLosses(totalLosses);
            stats.setSport(sport);
            switch (sport) {
                case football -> {
                    stats.setAssists(matches.stream().map(MatchParticipant::getAssists).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setFouls(matches.stream().map(MatchParticipant::getFouls).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setYellowCards(matches.stream().map(MatchParticipant::getYellowCards).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setRedCards(matches.stream().map(MatchParticipant::getRedCards).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case basketball -> {
                    stats.setAssists(matches.stream().map(MatchParticipant::getAssists).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setFouls(matches.stream().map(MatchParticipant::getFouls).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case tennis -> {
                    stats.setSetsWon(matches.stream().map(MatchParticipant::getSetsWon).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case jiu_jitsu -> {
                    stats.setSubmissions(matches.stream().map(MatchParticipant::getSubmissions).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case chess -> stats.setTimePlayed(sumTimePlayed(matches));
                case boxing -> {
                    stats.setKnockdowns(matches.stream().map(MatchParticipant::getKnockdowns).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
            }
            Integer winRate = (totalGames - totalDraws) > 0 ? Math.round(((float) totalWins / (totalGames - totalDraws)) * 100f) : null;
            stats.setWinRate(winRate);
            topPlayers.add(stats);
        }
        List<PlayerStats> sortedTop = topPlayers.stream()
            .sorted(
                Comparator.comparingInt(PlayerStats::getTotalPoints).reversed()
                    .thenComparing(Comparator.comparingInt(PlayerStats::getTotalWins).reversed())
            )
            .limit(limit)
            .collect(Collectors.toList());
        return sortedTop;
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

    public List<PlayerStats> getPlayerStatsByAllSports(UUID userId, Integer lastMatches, Integer lastTournaments, UUID tournamentId, UUID matchId) {
        List<MatchParticipant> playerMatches = matchParticipantRepository.findByParticipantIdAndParticipantType(userId, ParticipantType.player);
        playerMatches = filterMatches(playerMatches, lastMatches, lastTournaments, tournamentId, matchId);
        if (playerMatches.isEmpty()) return List.of();
        Map<Sport, List<MatchParticipant>> bySport = playerMatches.stream().collect(Collectors.groupingBy(mp -> mp.getMatch().getSport()));
        List<PlayerStats> result = new ArrayList<>();
        for (Map.Entry<Sport, List<MatchParticipant>> entry : bySport.entrySet()) {
            List<MatchParticipant> matches = entry.getValue();
            int totalGames = matches.size();
            int totalPoints = matches.stream().mapToInt(MatchParticipant::getPoints).sum();
            float averagePoints = (float) totalPoints / totalGames;
            int totalWins = (int) matches.stream().filter(MatchParticipant::getIsWinner).count();
            int totalDraws = (int) matches.stream().filter(mp -> isDraw(mp)).count();
            int totalLosses = totalGames - totalWins - totalDraws;
            PlayerStats stats = new PlayerStats(userId, totalGames, totalPoints, averagePoints, totalWins);
            stats.setTotalDraws(totalDraws);
            stats.setTotalLosses(totalLosses);
            stats.setSport(entry.getKey());
            switch (entry.getKey()) {
                case football -> {
                    stats.setAssists(matches.stream().map(MatchParticipant::getAssists).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setFouls(matches.stream().map(MatchParticipant::getFouls).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setYellowCards(matches.stream().map(MatchParticipant::getYellowCards).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setRedCards(matches.stream().map(MatchParticipant::getRedCards).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case basketball -> {
                    stats.setAssists(matches.stream().map(MatchParticipant::getAssists).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setFouls(matches.stream().map(MatchParticipant::getFouls).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case tennis -> {
                    stats.setSetsWon(matches.stream().map(MatchParticipant::getSetsWon).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case jiu_jitsu -> {
                    stats.setSubmissions(matches.stream().map(MatchParticipant::getSubmissions).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case chess -> stats.setTimePlayed(sumTimePlayed(matches));
                case boxing -> {
                    stats.setKnockdowns(matches.stream().map(MatchParticipant::getKnockdowns).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
            }
            Integer winRate = (totalGames - totalDraws) > 0 ? Math.round(((float) totalWins / (totalGames - totalDraws)) * 100f) : null;
            stats.setWinRate(winRate);
            result.add(stats);
        }
        return result;
    }

    public List<TeamStats> getTeamStatsByAllSports(UUID teamId, Integer lastMatches, Integer lastTournaments, UUID tournamentId, UUID matchId) {
        List<MatchParticipant> teamMatches = matchParticipantRepository.findByParticipantIdAndParticipantType(teamId, ParticipantType.team);
        teamMatches = filterMatches(teamMatches, lastMatches, lastTournaments, tournamentId, matchId);
        if (teamMatches.isEmpty()) return List.of();
        Map<Sport, List<MatchParticipant>> bySport = teamMatches.stream().collect(Collectors.groupingBy(mp -> mp.getMatch().getSport()));
        List<TeamStats> result = new ArrayList<>();
        for (Map.Entry<Sport, List<MatchParticipant>> entry : bySport.entrySet()) {
            List<MatchParticipant> matches = entry.getValue();
            int totalGames = matches.size();
            int totalPoints = matches.stream().mapToInt(MatchParticipant::getPoints).sum();
            float averagePoints = (float) totalPoints / totalGames;
            int totalWins = (int) matches.stream().filter(MatchParticipant::getIsWinner).count();
            int totalDraws = (int) matches.stream().filter(mp -> isDraw(mp)).count();
            int totalLosses = totalGames - totalWins - totalDraws;
            TeamStats stats = new TeamStats(teamId, totalGames, totalPoints, averagePoints, totalWins);
            stats.setTotalDraws(totalDraws);
            stats.setTotalLosses(totalLosses);
            stats.setSport(entry.getKey());
            switch (entry.getKey()) {
                case football -> {
                    stats.setAssists(matches.stream().map(MatchParticipant::getAssists).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setFouls(matches.stream().map(MatchParticipant::getFouls).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setYellowCards(matches.stream().map(MatchParticipant::getYellowCards).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setRedCards(matches.stream().map(MatchParticipant::getRedCards).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case basketball -> {
                    stats.setAssists(matches.stream().map(MatchParticipant::getAssists).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setFouls(matches.stream().map(MatchParticipant::getFouls).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case tennis -> {
                    stats.setSetsWon(matches.stream().map(MatchParticipant::getSetsWon).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case jiu_jitsu -> {
                    stats.setSubmissions(matches.stream().map(MatchParticipant::getSubmissions).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
                case chess -> stats.setTimePlayed(sumTimePlayed(matches));
                case boxing -> {
                    stats.setKnockdowns(matches.stream().map(MatchParticipant::getKnockdowns).filter(Objects::nonNull).mapToInt(Integer::intValue).sum());
                    stats.setTimePlayed(sumTimePlayed(matches));
                }
            }
            Integer winRate = (totalGames - totalDraws) > 0 ? Math.round(((float) totalWins / (totalGames - totalDraws)) * 100f) : null;
            stats.setWinRate(winRate);
            result.add(stats);
        }
        return result;
    }

    public List<MatchWithParticipants> getMatchesByTournament(UUID tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId);
        List<MatchWithParticipants> result = new ArrayList<>();
        for (Match match : matches) {
            List<MatchParticipant> participants = matchParticipantRepository.findByMatchId(match.getMatchId());
            List<MatchParticipantStats> participantStats = participants.stream().map(mp -> {
                MatchParticipantStats stats = new MatchParticipantStats(
                    mp.getParticipantId(),
                    mp.getParticipantType(),
                    mp.getPoints(),
                    mp.getIsWinner()
                );
                stats.setGoals(mp.getGoals());
                stats.setAssists(mp.getAssists());
                stats.setFouls(mp.getFouls());
                stats.setYellowCards(mp.getYellowCards());
                stats.setRedCards(mp.getRedCards());
                stats.setKnockdowns(mp.getKnockdowns());
                stats.setSubmissions(mp.getSubmissions());
                stats.setSetsWon(mp.getSetsWon());
                stats.setTimePlayed(mp.getTimePlayed());
                return stats;
            }).toList();
            result.add(new MatchWithParticipants(match.getMatchId(), participantStats));
        }
        return result;
    }

    public MatchWithParticipants getMatchWithParticipants(UUID matchId) {
        List<MatchParticipant> participants = matchParticipantRepository.findByMatchId(matchId);
        if (participants.isEmpty()) return null;
        List<MatchParticipantStats> participantStats = participants.stream().map(mp -> {
            MatchParticipantStats stats = new MatchParticipantStats(
                mp.getParticipantId(),
                mp.getParticipantType(),
                mp.getPoints(),
                mp.getIsWinner()
            );
            stats.setGoals(mp.getGoals());
            stats.setAssists(mp.getAssists());
            stats.setFouls(mp.getFouls());
            stats.setYellowCards(mp.getYellowCards());
            stats.setRedCards(mp.getRedCards());
            stats.setKnockdowns(mp.getKnockdowns());
            stats.setSubmissions(mp.getSubmissions());
            stats.setSetsWon(mp.getSetsWon());
            stats.setTimePlayed(mp.getTimePlayed());
            return stats;
        }).toList();
        return new MatchWithParticipants(matchId, participantStats);
    }

    private boolean isDraw(MatchParticipant mp) {
        // Реализовать: если оба участника матча isWinner == false, то это ничья
        // Для этого нужно получить всех участников матча и проверить их isWinner
        List<MatchParticipant> participants = matchParticipantRepository.findByMatchId(mp.getMatch().getMatchId());
        long notWinners = participants.stream().filter(p -> !p.getIsWinner()).count();
        return notWinners == participants.size();
    }

    private List<MatchParticipant> filterMatches(List<MatchParticipant> matches, Integer lastMatches, Integer lastTournaments, UUID tournamentId, UUID matchId) {
        // Фильтрация по matchId
        if (matchId != null) {
            matches = matches.stream().filter(mp -> mp.getMatch().getMatchId().equals(matchId)).toList();
        }
        // Фильтрация по tournamentId
        if (tournamentId != null) {
            matches = matches.stream().filter(mp -> mp.getMatch().getTournamentId().equals(tournamentId)).toList();
        }
        // Фильтрация по lastTournaments
        if (lastTournaments != null) {
            // Получаем последние N уникальных турниров по дате любого матча турнира
            List<UUID> lastTournamentIds = matches.stream()
                .collect(Collectors.groupingBy(mp -> mp.getMatch().getTournamentId(),
                        Collectors.maxBy(Comparator.comparing(mp -> mp.getMatch().getCreatedAt()))))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().get().getMatch().getCreatedAt().compareTo(e1.getValue().get().getMatch().getCreatedAt()))
                .limit(lastTournaments)
                .map(Map.Entry::getKey)
                .toList();
            matches = matches.stream().filter(mp -> lastTournamentIds.contains(mp.getMatch().getTournamentId())).toList();
        }
        // Фильтрация по lastMatches
        if (lastMatches != null) {
            matches = matches.stream()
                .sorted((a, b) -> b.getMatch().getCreatedAt().compareTo(a.getMatch().getCreatedAt()))
                .limit(lastMatches)
                .toList();
        }
        return matches;
    }

    private Float sumTimePlayed(List<MatchParticipant> matches) {
        double sum = matches.stream().map(MatchParticipant::getTimePlayed).filter(Objects::nonNull).mapToDouble(Float::doubleValue).sum();
        return matches.isEmpty() ? null : (float) sum;
    }
} 
