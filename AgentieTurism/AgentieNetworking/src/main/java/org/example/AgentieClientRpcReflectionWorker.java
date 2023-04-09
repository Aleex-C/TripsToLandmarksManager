package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.Agentie;
import org.example.domain.Excursie;
import org.example.domain.Info;
import org.example.domain.Rezervare;
import org.example.dto.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AgentieClientRpcReflectionWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    private final static Logger logger = LogManager.getLogger();

    public AgentieClientRpcReflectionWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    @Override
    public void update(List<Excursie> forSelector, List<Excursie> forMainPage) {
        UpdateDTO transferUpdate = DTOUtils.getUpdateDTO(forSelector, forMainPage);
        Response response = new Response.Builder().type(ResponseType.RESERVATION_ADDED).data(transferUpdate).build();
        try{
            sendResponse(response);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private static final Response okResponse = new Response.Builder().type(ResponseType.OK).build();
    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + (request).type();
        System.out.println("HandlerName " + handlerName);
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return response;

    }
    private void sendResponse(Response response) throws IOException {
        System.out.println("Sending response " + response);
        output.writeObject(response);
        output.flush();
    }
    private Response handleLOGIN(Request request) {
        System.out.println("Login request..." + request.type());
        AgentieDTO agentieDTO = (AgentieDTO) request.data();
        Agentie agentie = DTOUtils.getFromDTO(agentieDTO);
        try{
            server.login(agentie, this);
            return new Response.Builder().type(ResponseType.OK).data(agentieDTO).build();
        }catch (AgentieException e){
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleLOGOUT(Request request){
        System.out.println("Logout request...");
        AgentieDTO udto=(AgentieDTO) request.data();
        Agentie user=DTOUtils.getFromDTO(udto);
        try {
            server.logout(user, this);
            connected=false;
            return okResponse;

        } catch (AgentieException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleGET_LANDMARKS(Request request){
        try{
            List<String> landmarks = server.findAllLandmarks();
            return new Response.Builder().type(ResponseType.OK).data(landmarks).build();
        } catch (AgentieException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleGET_EXCURSIES(Request request){
        try{
            List<Excursie> excursies = (List<Excursie>) server.getAllExcursies();
            return new Response.Builder().type(ResponseType.OK).data(excursies).build();
        } catch (AgentieException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleGET_LANDMARK_TIME(Request request){
        InfoDTO infoDTO = (InfoDTO) request.data();
        Info info = DTOUtils.InfoGetFromDTO(infoDTO);
        try{
            List<Excursie> excursiesFiltered = server.findByLandmarkAndInterval(info.getLandmark(), info.getT1(), info.getT2());
            return new Response.Builder().type(ResponseType.OK).data(excursiesFiltered).build();
        } catch (AgentieException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleADD_RESERVATION(Request request){
        ReservationDTO infoDTO = (ReservationDTO) request.data();
        Rezervare info = DTOUtils.GetReservationFromDTO(infoDTO);
        try{
            server.addReservation(info.getName(), info.getPhone_number(), info.getNumber_of_tickets(), infoDTO.getExc(), infoDTO.getT1(), infoDTO.getT2());
            return new Response.Builder().type(ResponseType.OK).data(info).build();
        } catch (AgentieException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
}
