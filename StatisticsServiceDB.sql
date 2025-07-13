-- Создание таблицы matches
CREATE TABLE matches (
    id UUID PRIMARY KEY,
    match_id UUID NOT NULL,
    tournament_id UUID NOT NULL,
    sport VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы match_participants
CREATE TABLE match_participants (
    id UUID PRIMARY KEY,
    match_id UUID NOT NULL,
    participant_id UUID NOT NULL,
    participant_type VARCHAR(20) NOT NULL,
    points INTEGER DEFAULT 0,
    is_winner BOOLEAN DEFAULT FALSE,
    goals INTEGER DEFAULT 0,
    assists INTEGER DEFAULT 0,
    fouls INTEGER DEFAULT 0,
    yellow_cards INTEGER DEFAULT 0,
    red_cards INTEGER DEFAULT 0,
    time_played INTEGER DEFAULT 0,
    sets_won INTEGER DEFAULT 0,
    submissions INTEGER DEFAULT 0,
    knockdowns INTEGER DEFAULT 0,
    FOREIGN KEY (match_id) REFERENCES matches(id)
);

-- Создание индексов для производительности
CREATE INDEX idx_matches_sport ON matches(sport);
CREATE INDEX idx_matches_tournament ON matches(tournament_id);
CREATE INDEX idx_participants_participant_id ON match_participants(participant_id);
CREATE INDEX idx_participants_type ON match_participants(participant_type);
