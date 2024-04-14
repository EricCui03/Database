package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class Lineup {
    private static final String URL = "jdbc:postgresql://localhost:5432/Project1";
    private static final String USER = "postgres";
    private static final String PASS = "admin";

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        File dir = new File("C:\\Users\\golde\\open-data\\data\\lineups");

        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        int i=0;
        if (files != null) {
            for (File file : files) {
                try {
                    System.out.println(i++);
                    Team[] teams = mapper.readValue(file, Team[].class);
                    insertData(teams);
                } catch (IOException e) {
                    System.err.println("Error processing file " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }


    private static void insertData(Team[] teams) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);

            // Prepare statements for each table
            PreparedStatement stmtTeam = conn.prepareStatement("INSERT INTO teams(team_id, team_name) VALUES (?, ?) ON CONFLICT (team_id) DO NOTHING;");
            PreparedStatement stmtPlayer = conn.prepareStatement("INSERT INTO player(player_id, team_id, player_name, player_nickname, jersey_number, country_id) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (player_id) DO NOTHING;");
            PreparedStatement stmtCountry = conn.prepareStatement("INSERT INTO country(id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING;");
            PreparedStatement stmtCard = conn.prepareStatement("INSERT INTO cards(player_id, time, card_type, reason, period) VALUES (?, ?, ?, ?, ?);");

            PreparedStatement stmtPosition = conn.prepareStatement("INSERT INTO positions(position_id, player_id, position, from_time, to_time, from_period, to_period, start_reason, end_reason) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (position_id, player_id) DO NOTHING;");

            for (Team team : teams) {
                // Insert team
                stmtTeam.setInt(1, team.team_id);
                stmtTeam.setString(2, team.team_name);
                stmtTeam.executeUpdate();

                for (Player player : team.lineup) {
                    try{
                    // Insert countries
                    if (player.country != null) {
                        stmtCountry.setInt(1, player.country.id);
                        stmtCountry.setString(2, player.country.name);
                        stmtCountry.executeUpdate();
                    }

                    // Insert players
                    stmtPlayer.setInt(1, player.player_id);
                    stmtPlayer.setInt(2, team.team_id);
                    stmtPlayer.setString(3, player.player_name);
                    setStringOrNull(stmtPlayer, 4, player.player_nickname); // Handles potential nulls in nickname
                    stmtPlayer.setInt(5, player.jersey_number);
                    stmtPlayer.setInt(6, player.country != null ? player.country.id : null); // Conditionally sets country ID
                    stmtPlayer.executeUpdate();

                    for (Card card : player.cards) {
                        // Insert cards
                        stmtCard.setInt(1, player.player_id);
                        setStringOrNull(stmtCard, 2, card.time);
                        setStringOrNull(stmtCard, 3, card.card_type);
                        setStringOrNull(stmtCard, 4, card.reason);
                        stmtCard.setInt(5, card.period);
                        stmtCard.executeUpdate();
                    }

                    for (Position position : player.positions) {
                        // Insert positions
                        stmtPosition.setInt(1, position.position_id);
                        stmtPosition.setInt(2, player.player_id);
                        setStringOrNull(stmtPosition, 3, position.position);
                        setStringOrNull(stmtPosition, 4, position.from);
                        setStringOrNull(stmtPosition, 5, position.to);
                        stmtPosition.setInt(6, position.from_period);
                        setIntOrNull(stmtPosition, 7, position.to_period); // Handles potential nulls in to_period
                        setStringOrNull(stmtPosition, 8, position.start_reason);
                        setStringOrNull(stmtPosition, 9, position.end_reason);
                        stmtPosition.executeUpdate();
                        }
                    }catch (NullPointerException e){

                    }
                }
            }

            conn.commit();  // Commit all changes
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setStringOrNull(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.VARCHAR);
        } else {
            stmt.setString(index, value);
        }
    }

    private static void setIntOrNull(PreparedStatement stmt, int index, Integer value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.INTEGER);
        } else {
            stmt.setInt(index, value);
        }
    }

}