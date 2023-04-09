package org.example;

import org.example.domain.Agentie;
import org.example.domain.Excursie;
import org.example.domain.Info;
import org.example.domain.Rezervare;
import org.example.dto.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AgentieServicesRpcProxy implements IServices {

    private String host;
    private int port;
    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public AgentieServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }
    private void initializeConnection() throws AgentieException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }
    private void sendRequest(Request request) throws AgentieException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new AgentieException("Error sending object " + e);
        }
    }
    private Response readResponse() throws AgentieException {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void handleUpdate(Response response) throws AgentieException {
        if (response.type() == ResponseType.RESERVATION_ADDED) {
            UpdateDTO uto = (UpdateDTO) response.data();
            client.update(uto.getForSelector(), uto.getForMainPage());
        }
    }
    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.RESERVATION_ADDED;
    }
    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | AgentieException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

    @Override
    public Agentie login(Agentie agent, IObserver client) throws AgentieException {
        initializeConnection();
        AgentieDTO adtto = DTOUtils.getDTO(agent);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(adtto).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK){
            this.client = client;
            AgentieDTO adto = (AgentieDTO) response.data();
            return DTOUtils.getFromDTO(adto);
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new AgentieException(err);
        }
        return null;
    }

    @Override
    public void logout(Agentie agent, IObserver client) throws AgentieException {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(DTOUtils.getDTO(agent)).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AgentieException(err);
        }
    }

    @Override
    public List<String> findAllLandmarks() throws AgentieException {
        initializeConnection();
        Request request = new Request.Builder().type(RequestType.GET_LANDMARKS).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AgentieException(err);
        }
        return (List<String>) response.data();
    }

    @Override
    public List<Excursie> findByLandmarkAndInterval(String landmark, LocalTime t1, LocalTime t2) throws AgentieException {
        Request request = new Request.Builder().type(RequestType.GET_LANDMARK_TIME).data(DTOUtils.InfoGetDTO(new Info(landmark,t1,t2))).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AgentieException(err);
        }
        return (List<Excursie>) response.data();
    }

    @Override
    public void addReservation(String name, String phoneNo, Integer ticketsNo, Excursie exc, LocalTime t1, LocalTime t2) throws AgentieException {
        ReservationDTO rez = new ReservationDTO(name, phoneNo, ticketsNo, exc, t1, t2);
        Request request = new Request.Builder().type(RequestType.ADD_RESERVATION).data(rez).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AgentieException(err);
        }

    }

    @Override
    public List<Excursie> getAllExcursies() throws AgentieException {
        Request request = new Request.Builder().type(RequestType.GET_EXCURSIES).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AgentieException(err);
        }
        return (List<Excursie>) response.data();
    }
}
