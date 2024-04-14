package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

class Team {
    @JsonProperty("team_id")
    int team_id;
    @JsonProperty("team_name")
    String team_name;
    @JsonProperty("team_gender")
    String team_gender;
    @JsonProperty("team_group")
    String team_group;
    @JsonProperty("lineup")
    List<Player> lineup;
    @JsonProperty("country")
    Country country;
    @JsonProperty("managers")
    List<Manager> manager;

    @JsonProperty("home_team_id")
    int home_team_id;
    @JsonProperty("away_team_id")
    int away_team_id;
    @JsonProperty("home_team_name")
    String home_team_name;
    @JsonProperty("away_team_name")
    String away_team_name;
    @JsonProperty("home_team_gender")
    String home_team_gender;
    @JsonProperty("home_team_group")
    Boolean home_team_group;
    @JsonProperty("away_team_gender")
    String away_team_gender;
    @JsonProperty("away_team_group")
    Boolean away_team_group;

    @JsonProperty("id")
    void setId(int id){
        this.team_id = id;
    }

    @JsonProperty("name")
    void setName(String name){
        this.team_name = name;
    }

}

class Player {
    @JsonProperty("player_id")
    int player_id;
    @JsonProperty("player_name")
    String player_name;
    @JsonProperty("player_nickname")
    String player_nickname;
    @JsonProperty("jersey_number")
    int jersey_number;
    @JsonProperty("country")
    Country country;
    @JsonProperty("cards")
    List<Card> cards;
    @JsonProperty("positions")
    List<Position> positions;

    @JsonProperty("id")
    void setId(int id){
        this.player_id = id;
    }

    @JsonProperty("name")
    void setName(String name){
        this.player_name = name;
    }
}

class Country {
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
}

class Card {
    @JsonProperty("time")
    String time;
    @JsonProperty("card_type")
    String card_type;
    @JsonProperty("reason")
    String reason;
    @JsonProperty("period")
    int period;
}

class Position {
    @JsonProperty("position_id")
    int position_id;
    @JsonProperty("position")
    String position;
    @JsonProperty("from")
    String from;
    @JsonProperty("to")
    String to;
    @JsonProperty("from_period")
    int from_period;
    @JsonProperty("to_period")
    int to_period;
    @JsonProperty("start_reason")
    String start_reason;
    @JsonProperty("end_reason")
    String end_reason;
}

class MatchData {
    @JsonProperty("match_id")
    int match_id;
    @JsonProperty("match_date")
    String match_date;
    @JsonProperty("kick_off")
    String kick_off;
    @JsonProperty("competition")
    Competition competition;
    @JsonProperty("season")
    Season season;
    @JsonProperty("home_team")
    Team home_team;
    @JsonProperty("away_team")
    Team away_team;
    @JsonProperty("home_score")
    int home_score;
    @JsonProperty("away_score")
    int away_score;
    @JsonProperty("match_status")
    String match_status;
    @JsonProperty("match_status_360")
    String match_status_360;
    @JsonProperty("last_updated")
    String last_updated;
    @JsonProperty("last_updated_360")
    String last_updated_360;
    @JsonProperty("metadata")
    Metadata metadata;
    @JsonProperty("match_week")
    int match_week;
    @JsonProperty("competition_stage")
    CompetitionStage competition_stage;
    @JsonProperty("stadium")
    Stadium stadium;
    @JsonProperty("referee")
    Referee referee;
}

class Competition {
    @JsonProperty("competition_id")
    int competition_id;
    @JsonProperty("country_name")
    String country_name;
    @JsonProperty("competition_name")
    String competition_name;
}

class Season {
    @JsonProperty("season_id")
    int season_id;
    @JsonProperty("season_name")
    String season_name;
}

class Metadata {
    @JsonProperty("data_version")
    String data_version;
    @JsonProperty("shot_fidelity_version")
    String shot_fidelity_version;
    @JsonProperty("xy_fidelity_version")
    String xy_fidelity_version;
}

class CompetitionStage {
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
}

class Stadium {
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
    @JsonProperty("country")
    Country country;
}

class Referee {
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
    @JsonProperty("country")
    Country country;
}

class Manager {
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
    @JsonProperty("nickname")
    String nickname;
    @JsonProperty("dob")
    Date dob;
    @JsonProperty("country")
    Country country;
    @JsonProperty("team_id")
    int team_id;
}
class Event {
    @JsonProperty("id")
    String id;
    @JsonProperty("index")
    int index;
    @JsonProperty("period")
    int period;
    @JsonProperty("timestamp")
    String timestamp;
    @JsonProperty("minute")
    int minute;
    @JsonProperty("second")
    int second;
    @JsonProperty("type")
    EventType type;
    @JsonProperty("possession")
    int possession;
    @JsonProperty("possession_team")
    Team possession_team;
    @JsonProperty("play_pattern")
    PlayPattern play_pattern;
    @JsonProperty("team")
    Team team;
    @JsonProperty("duration")
    double duration;
    @JsonProperty("tactics")
    Tactics tactics;
}

class EventType {
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
}

class PlayPattern {
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
}

class Tactics {
    @JsonProperty("formation")
    int formation;
    @JsonProperty("lineup")
    List<Lineups> lineups;

    @JsonProperty("position")
    Position position;
}

class Lineups{
    @JsonProperty("tactics")
    Tactics tactics;
    @JsonProperty("player")
    Player player;
}




