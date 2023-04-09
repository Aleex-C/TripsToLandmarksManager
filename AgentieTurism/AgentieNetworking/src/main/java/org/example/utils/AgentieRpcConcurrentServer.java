package org.example.utils;

import org.example.AgentieClientRpcReflectionWorker;
import org.example.IServices;

import java.net.Socket;

public class AgentieRpcConcurrentServer extends AbstractConcurrentServer{

    private IServices agentieServices;

    public AgentieRpcConcurrentServer(int port, IServices agentieServices) {
        super(port);
        this.agentieServices = agentieServices;
        System.out.println("---AGENTIE - CONCURRENT SERVER--");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AgentieClientRpcReflectionWorker worker = new AgentieClientRpcReflectionWorker(agentieServices, client);
        return new Thread(worker);
    }
    @Override
    public void stop(){
        System.out.println("Stopping services ... ");
    }
}
