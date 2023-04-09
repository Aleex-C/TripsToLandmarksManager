package org.example.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.Excursie;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ExcursieRepository implements IRepository<Integer, Excursie> {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public ExcursieRepository(Properties props) {
        logger.info("Initializing AgentieRepository with properties {} ", props);
        dbUtils=new JdbcUtils(props);
    }
    public List<Excursie> findByLandmarkAndTimeInterval(String landmark_find, LocalTime t1, LocalTime t2){
        Connection connection = dbUtils.getConnection();
        List<Excursie> excursii_filtrate = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM excursii WHERE landmark = ? AND departure_time > ? AND departure_time < ?")) {
            statement.setString(1, landmark_find);
            statement.setTime(2, Time.valueOf(t1));
            statement.setTime(3, Time.valueOf(t2));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String landmark = resultSet.getString("landmark");
                String transportCompany = resultSet.getString("transport_company");
                LocalTime departureTime = resultSet.getTime("departure_time").toLocalTime();
                int availableTickets = resultSet.getInt("available_tickets");
                float price = resultSet.getFloat("price");
                Excursie exc = new Excursie(landmark, transportCompany, departureTime, availableTickets, price);
                exc.setId(id);
                excursii_filtrate.add(exc);
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit("Returned {}", excursii_filtrate);
        return excursii_filtrate;
    }

    @Override
    public Optional<Excursie> findOne(Integer id) {
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM excursii WHERE id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String landmark = resultSet.getString("landmark");
                String transportCompany = resultSet.getString("transport_company");
                LocalTime departureTime = resultSet.getTime("departure_time").toLocalTime();
                int availableTickets = resultSet.getInt("available_tickets");
                float price = resultSet.getFloat("price");
                Excursie exc = new Excursie(landmark, transportCompany, departureTime, availableTickets, price);
                logger.traceExit("Found the excurise["+id+"]" + " = " + exc);
                return Optional.of(exc);
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Excursie> findAll() {
        Connection connection = dbUtils.getConnection();
        ArrayList<Excursie> excursii = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM excursii")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String landmark = resultSet.getString("landmark");
                String transportCompany = resultSet.getString("transport_company");
                LocalTime departureTime = resultSet.getTime("departure_time").toLocalTime();
                int availableTickets = resultSet.getInt("available_tickets");
                float price = resultSet.getFloat("price");
                excursii.add(new Excursie(landmark, transportCompany, departureTime, availableTickets, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.traceExit("Found all {} ", excursii);
        return excursii;
    }

    public List<String> findAllLandmarks() {
        Connection connection = dbUtils.getConnection();
        ArrayList<String> landmarks = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT landmark FROM excursii")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String landmark = resultSet.getString("landmark");
                landmarks.add(landmark);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.traceExit("Found all {} ", landmarks);
        return landmarks;
    }

    @Override
    public void save(Excursie entity) {
        Connection connection = dbUtils.getConnection();
        logger.traceEntry("saving excursie {} ", entity);
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO excursii (landmark, transport_company, departure_time, available_tickets, price) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, entity.getLandmark());
            statement.setString(2, entity.getTransport_company());
            statement.setTime(3, Time.valueOf(entity.getDeparture_time()));
            statement.setInt(4, entity.getAvailable_tickets());
            statement.setFloat(5, entity.getPrice());
            int result = statement.executeUpdate();
            logger.trace("saved {} instances ", result);
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deletin excursie with id {} ", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM excursii WHERE id = ?")) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            logger.trace("delete {} insatnce", result);
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer id, Excursie entity) {
        logger.traceEntry("Updating {} with {}", id, entity);
        Connection connection = dbUtils.getConnection();
            try (PreparedStatement statement = connection.prepareStatement("UPDATE excursii SET landmark = ?, transport_company = ?, departure_time = ?, available_tickets = ?, price = ? WHERE id = ?")) {
                statement.setString(1, entity.getLandmark());
                statement.setString(2, entity.getTransport_company());
                statement.setTime(3, Time.valueOf(entity.getDeparture_time()));
                statement.setInt(4, entity.getAvailable_tickets());
                statement.setFloat(5, entity.getPrice());
                statement.setInt(6, id);
                int result = statement.executeUpdate();
                logger.trace("{} instances ", result);
            } catch (SQLException ex) {
                logger.error(ex);
                ex.printStackTrace();
            }
        logger.traceExit();
    }
}