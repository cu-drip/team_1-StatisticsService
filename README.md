# Statistics Service

Микросервис для сбора и анализа статистики турниров, матчей, игроков и команд.

## Возможности
- Добавление статистики матчей и турниров (POST /api/v1/stats/update)
- Получение статистики игрока и команды
- Получение топ-игроков по очкам и победам
- История матчей по спорту и участнику
- Корректная обработка ошибок (400, 404, 500)

## Технологии
- Java 17
- Spring Boot
- Maven
- PostgreSQL (или другая совместимая реляционная БД)
- Docker, Docker Compose

## Быстрый старт

### 1. Сборка и запуск

```bash
mvn clean package -DskipTests
docker-compose build
docker-compose up -d
```

### 2. API доступен по адресу
```
http://localhost:8081
```

## Структура API

### Добавить статистику турнира/матчей
**POST** `/api/v1/stats/update`

Пример тела запроса:
```json
{
  "tournamentId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
  "sport": "football",
  "tournamentType": "single_elimination",
  "matches": [
    {
      "matchId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
      "participants": [
        {
          "participantId": "cccccccc-cccc-cccc-cccc-cccccccccccc",
          "participantType": "player",
          "points": 7,
          "isWinner": true,
          "goals": 3,
          "assists": 2,
          "fouls": 1,
          "yellowCards": 1,
          "redCards": 0,
          "knockdowns": 0,
          "submissions": 0,
          "setsWon": 1,
          "timePlayed": 90.0
        }
      ]
    }
  ]
}
```

### Получить статистику игрока
**GET** `/api/v1/stats/player/{userId}?sport=football`

### Получить статистику команды
**GET** `/api/v1/stats/team/{teamId}?sport=football`

### Топ игроков
**GET** `/api/v1/stats/top/players?sport=football`

### История матчей
**GET** `/api/v1/stats/matches/history?sport=football`
**GET** `/api/v1/stats/matches/history?sport=football&participant_id={uuid}`

## Тестирование через Postman

1. Импортируйте коллекцию запросов или используйте примеры выше.
2. Для POST-запроса используйте Content-Type: application/json.
3. Для GET-запросов используйте валидные UUID и значения enum:
   - sport: football, boxing, basketball, chess, tennis, jiu_jitsu
   - tournamentType: round_robin, single_elimination, double_elimination, group_stage
   - participantType: player, team

## Обработка ошибок
- 400 Bad Request — невалидный JSON, неверные значения enum, ошибки валидации
- 404 Not Found — не найден игрок, команда или другой ресурс
- 500 Internal Server Error — внутренняя ошибка сервера

## Пример docker-compose.yml
```yaml
version: '3.8'
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: statsdb
      POSTGRES_USER: statsuser
      POSTGRES_PASSWORD: statspass
    ports:
      - "5432:5432"
  app:
    build: .
    ports:
      - "8081:8080"
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/statsdb
      SPRING_DATASOURCE_USERNAME: statsuser
      SPRING_DATASOURCE_PASSWORD: statspass
```

## Лицензия
MIT 