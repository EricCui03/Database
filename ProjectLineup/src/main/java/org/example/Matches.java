package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class Matches {
    private static final String URL = "jdbc:postgresql://localhost:5432/Project1";
    private static final String USER = "postgres";
    private static final String PASS = "admin";

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        File dir = new File("C:\\Users\\golde\\open-data\\data\\matches");

        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        int i=0;
        if (files != null) {
            for (File file : files) {
                try {
                    System.out.println(i++);
                    MatchData[] matches = mapper.readValue(file, MatchData[].class);
                    insertData(matches);
                } catch (IOException e) {
                    System.err.println("Error processing file " + file.getName());
                    e.printStackTrace();
                }
            }
        }

    }

    private static void insertData(MatchData[] matches) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);

            PreparedStatement stmtCompetition = conn.prepareStatement("INSERT INTO competitions(competition_id, country_name, competition_name) VALUES (?, ?, ?) ON CONFLICT (competition_id) DO NOTHING;");
            PreparedStatement stmtCompetitionStage = conn.prepareStatement("INSERT INTO competition_stage(id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING;");
            PreparedStatement stmtStadium = conn.prepareStatement("INSERT INTO stadium(id, name, country_id) VALUES (?, ?, ?) ON CONFLICT (id) DO NOTHING;");
            PreparedStatement stmtReferee = conn.prepareStatement("INSERT INTO referee(id, name, country_id) VALUES (?, ?, ?) ON CONFLICT (id) DO NOTHING;");
            PreparedStatement stmtSeason = conn.prepareStatement("INSERT INTO seasons(season_id, season_name) VALUES (?, ?) ON CONFLICT (season_id) DO NOTHING;");
            PreparedStatement stmtMatch = conn.prepareStatement("INSERT INTO matches(match_id, match_date, kick_off, home_team_id, away_team_id, home_score, away_score, match_status, match_status_360, last_updated, last_updated_360, match_week, competition_stage_id, stadium_id, referee_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            PreparedStatement stmtManager = conn.prepareStatement("INSERT INTO manager(id, name, nickname, dob, country_id, team_id) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO NOTHING;");
            PreparedStatement stmtTeam = conn.prepareStatement("INSERT INTO teams(team_id, team_name, team_gender, team_group) VALUES (?, ?, ?, ?) ON CONFLICT (team_id) DO UPDATE SET team_gender = EXCLUDED.team_gender, team_group = EXCLUDED.team_group;;");

            for (MatchData match : matches) {
                try {
                // Insert competitions
                stmtCompetitionStage.setInt(1, match.competition_stage.id);
                stmtCompetitionStage.setString(2, match.competition_stage.name);
                stmtCompetitionStage.executeUpdate();

                stmtCompetition.setInt(1, match.competition.competition_id);
                stmtCompetition.setString(2, match.competition.country_name);
                stmtCompetition.setString(3, match.competition.competition_name);
                stmtCompetition.executeUpdate();

                stmtStadium.setInt(1, match.stadium.id);
                stmtStadium.setString(2, match.stadium.name);
                stmtStadium.setInt(3, match.stadium.country.id);
                stmtStadium.executeUpdate();


                    stmtReferee.setInt(1, match.referee.id);
                    stmtReferee.setString(2, match.referee.name);
                    stmtReferee.setInt(3, match.referee.country.id);
                    stmtReferee.executeUpdate();


                // Insert seasons
                stmtSeason.setInt(1, match.season.season_id);
                stmtSeason.setString(2, match.season.season_name);
                stmtSeason.executeUpdate();

                // Insert matches
                stmtMatch.setInt(1, match.match_id);
                stmtMatch.setDate(2, Date.valueOf(match.match_date));
                stmtMatch.setString(3,  match.kick_off.toString());
                stmtMatch.setInt(4, match.home_team.home_team_id);
                stmtMatch.setInt(5, match.away_team.away_team_id);
                stmtMatch.setInt(6, match.home_score);
                stmtMatch.setInt(7, match.away_score);
                stmtMatch.setString(8, match.match_status);
                stmtMatch.setString(9, match.match_status_360);
                stmtMatch.setString(10, match.last_updated);
                stmtMatch.setString(11, match.last_updated_360);
                stmtMatch.setInt(12, match.match_week);
                stmtMatch.setInt(13, match.competition_stage.id);
                stmtMatch.setInt(14, match.stadium.id);
                stmtMatch.setInt(15, match.referee != null ? match.referee.id : null);
                stmtMatch.executeUpdate();


                stmtTeam.setInt(1, match.home_team.home_team_id);
                stmtTeam.setString(2, match.home_team.home_team_name);
                stmtTeam.setString(3, match.home_team.home_team_gender);
                stmtTeam.setNull(4,0);
                stmtTeam.executeUpdate();

                stmtManager.setInt(1, match.home_team.manager.get(0).id);
                stmtManager.setString(2, match.home_team.manager.get(0).name);
                stmtManager.setString(3, match.home_team.manager.get(0).nickname);
                stmtManager.setDate(4,  new java.sql.Date(match.home_team.manager.get(0).dob.getTime()));
                stmtManager.setInt(5, match.home_team.manager.get(0).country.id);
                stmtManager.setInt(6, match.home_team.home_team_id);
                stmtManager.executeUpdate();


                stmtTeam.setInt(1, match.away_team.away_team_id);
                stmtTeam.setString(2, match.away_team.away_team_name);
                stmtTeam.setString(3, match.away_team.away_team_gender);
                stmtTeam.setNull(4,0);
                stmtTeam.executeUpdate();

                stmtManager.setInt(1, match.away_team.manager.get(0).id);
                stmtManager.setString(2, match.away_team.manager.get(0).name);
                stmtManager.setString(3, match.away_team.manager.get(0).nickname);
                stmtManager.setDate(4, new java.sql.Date(match.away_team.manager.get(0).dob.getTime()));
                stmtManager.setInt(5, match.away_team.manager.get(0).country.id);
                stmtManager.setInt(6, match.away_team.away_team_id);
                stmtManager.executeUpdate();
                }catch (NullPointerException e){

                }

            }

            conn.commit();  // Commit all changes
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
