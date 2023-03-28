package org.example.repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.JdbcUtils;
import org.example.domain.Agentie;
import org.example.repository.IRepository;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

public class AgentieRepository implements IRepository<Integer, Agentie> {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public AgentieRepository(Properties props) {
        logger.info("Initializing AgentieRepository with properties {} ", props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Optional<Agentie> findOne(Integer id) {
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM agentii WHERE id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String user = resultSet.getString("username");
                String password = resultSet.getString("password");
                Agentie agent = new Agentie(name, user, password);
                logger.traceExit(agent);
                return Optional.of(agent);
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Agentie> findAll() {
        ArrayList<Agentie> agentii = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM agentii")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String user = resultSet.getString("username");
                String password = resultSet.getString("password");
                agentii.add(new Agentie(name, user, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.traceExit(agentii);
        return agentii;
    }

    @Override
    public void save(Agentie entity) {
        logger.traceEntry("saving agency {} ", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO agentii (name, username, password) VALUES (?, ?, ?)")) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getUser());
            statement.setString(3, entity.getPassword());
            int result = statement.executeUpdate();
            logger.trace("saved {} instances " ,result );
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleted agency with ID:{} ", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM agentii WHERE id = ?")) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            logger.trace("Delete {} :", result);
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer id, Agentie entity) {
        logger.traceEntry("updating agency with id {} for {}", id, entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE agentii SET name=?, username=?, password=? WHERE id = ?")) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getUser());
            statement.setString(3, entity.getPassword());
            statement.setInt(4, id);
            int result = statement.executeUpdate();
            logger.trace("updated {} rows", result);
        } catch (SQLException ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
        logger.traceExit();
    }
}