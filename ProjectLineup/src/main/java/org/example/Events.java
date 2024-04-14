package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;
public class Events {
    private static final String URL = "jdbc:postgresql://localhost:5432/Project1";
    private static final String USER = "postgres";
    private static final String PASS = "admin";
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Read JSON file
            Event[] events = mapper.readValue(new File("D:\\Homework\\COMP 3005 Data Management System\\Assignments\\Final Project\\Data\\event\\15946.json"), Event[].class);

            // Database operation
            insertData(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void insertData(Event[] events) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);

            PreparedStatement stmtType = conn.prepareStatement("INSERT INTO type(id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING;");
            PreparedStatement stmtPossessionTeam = conn.prepareStatement("INSERT INTO team(id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING;");
            PreparedStatement stmtPlayPattern= conn.prepareStatement("INSERT INTO play_pattern(id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING;");
            PreparedStatement stmtTeam = conn.prepareStatement("INSERT INTO team(id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING;");


            PreparedStatement stmtEvent = conn.prepareStatement("INSERT INTO events(event_id, index, period, timestamp, minute, second, type_id, possession, possession_team_id, play_pattern_id, team_id, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (event_id) DO NOTHING;");
            PreparedStatement stmtTactics = conn.prepareStatement("INSERT INTO tactics(event_id, formation) VALUES (?, ?) ON CONFLICT (event_id) DO NOTHING;");
            PreparedStatement stmtLineup = conn.prepareStatement("INSERT INTO lineup(player_id, event_id) VALUES (?, ?) ON CONFLICT (player_id) DO NOTHING;");
            PreparedStatement stmtPosition = conn.prepareStatement("INSERT INTO position(player_id, event_id) VALUES (?, ?) ON CONFLICT (player_id) DO NOTHING;");

            for (Event event : events) {
                stmtType.setObject(1,event.type.id);
                stmtType.setObject(2,event.type.name);
                stmtType.executeUpdate();

                stmtPossessionTeam.setObject(1,event.possession_team.team_id);
                stmtPossessionTeam.setObject(2,event.possession_team.team_name);
                stmtPossessionTeam.executeUpdate();

                stmtPlayPattern.setObject(1,event.play_pattern.id);
                stmtPlayPattern.setObject(2,event.play_pattern.name);
                stmtPlayPattern.executeUpdate();


                // Insert events
                stmtEvent.setObject(1, event.id);
                stmtEvent.setInt(2, event.index);
                stmtEvent.setInt(3, event.period);
                stmtEvent.setTime(4, Time.valueOf(event.timestamp));
                stmtEvent.setInt(5, event.minute);
                stmtEvent.setInt(6, event.second);
                stmtEvent.setInt(7, event.type.id);
                stmtEvent.setInt(8, event.possession);
                stmtEvent.setInt(9, event.possession_team.team_id);
                stmtEvent.setInt(10, event.play_pattern.id);
                stmtEvent.setInt(11, event.team.team_id);
                stmtEvent.setDouble(12, event.duration);
                stmtEvent.executeUpdate();

                // Insert tactics
                stmtTactics.setObject(1, event.id);
                stmtTactics.setInt(2, event.tactics.formation);
                stmtTactics.executeUpdate();

                // Insert lineup
                for (Lineups lineups : event.tactics.lineups) {
                    stmtLineup.setInt(1, lineups.player.player_id);
                    stmtLineup.setObject(2, event.id);
                    stmtLineup.executeUpdate();
                }
            }

            conn.commit(); // Commit all changes
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
