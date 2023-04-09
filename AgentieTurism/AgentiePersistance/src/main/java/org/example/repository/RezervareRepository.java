package org.example.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.Rezervare;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

public class RezervareRepository implements IRepository<Integer, Rezervare> {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RezervareRepository(Properties props) {
        logger.info("Initializing AgentieRepository with properties {} ", props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Optional<Rezervare> findOne(Integer id) {
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM rezervari WHERE id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String phone_number = resultSet.getString("phone_number");
                Integer number_of_tickets = resultSet.getInt("number_of_tickets");
                Rezervare rez = new Rezervare(name, phone_number, number_of_tickets);
                logger.traceExit("Am gasit: {}", rez);
                return Optional.of(rez);
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Rezervare> findAll() {
        Connection connection = dbUtils.getConnection();
        ArrayList<Rezervare> rezervari = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM rezervari")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phone_number");
                int numberOfTickets = resultSet.getInt("number_of_tickets");
                rezervari.add(new Rezervare(name, phoneNumber, numberOfTickets));
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit("Am gasit: {}", rezervari);
        return rezervari;
    }

    @Override
    public void save(Rezervare entity) {
        logger.traceEntry("saving rezervare {} ", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO rezervari (name, phone_number, number_of_tickets) VALUES (?, ?, ?)")) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getPhone_number());
            statement.setInt(3, entity.getNumber_of_tickets());
            int result = statement.executeUpdate();
            logger.trace("saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("Deleting reservation with ID {}", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM rezervari WHERE id = ?")) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            logger.trace("Deleted {} items", result);
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer id, Rezervare entity) {
        logger.traceEntry("Updating reservation with ID {} with this one -> {}", id, entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE rezervari SET name = ?, phone_number = ?, number_of_tickets = ? WHERE id = ?")) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getPhone_number());
            statement.setInt(3, entity.getNumber_of_tickets());
            statement.setInt(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
    }
}
