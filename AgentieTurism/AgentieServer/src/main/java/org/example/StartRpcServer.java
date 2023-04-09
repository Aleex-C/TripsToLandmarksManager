package org.example;

import org.example.repository.AgentieRepository;
import org.example.repository.ExcursieRepository;
import org.example.repository.RezervareRepository;
import org.example.server.AgentServiceImpl;
import org.example.utils.AbstractServer;
import org.example.utils.AgentieRpcConcurrentServer;
import org.example.utils.ServerException;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/agentserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }
        AgentieRepository agentieRepository=new AgentieRepository(serverProps);
        ExcursieRepository excursieRepository=new ExcursieRepository(serverProps);
        RezervareRepository rezervareRepository = new RezervareRepository(serverProps);
        IServices agentServerImpl=new AgentServiceImpl(agentieRepository, excursieRepository, rezervareRepository);
        int agentServerPort=defaultPort;
        try {
            agentServerPort = Integer.parseInt(serverProps.getProperty("agent.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+agentServerPort);
        AbstractServer server = new AgentieRpcConcurrentServer(agentServerPort, agentServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
