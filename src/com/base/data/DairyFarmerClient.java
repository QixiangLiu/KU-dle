package com.base.data;

import com.base.data.interfaces.Events;
import com.base.data.interfaces.Users;
import com.base.data.models.Event;
import com.base.data.models.User;
import com.base.util.Time;
import com.google.gson.Gson;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DairyFarmerClient {
    private Gson gson;
    private FileWriter writer;
    private BufferedReader reader;

    private HashMap<LocalDate, List<Event>> events;
    private HashMap<String, User> users;

    /**
     * Initializes gson object
     */
    public DairyFarmerClient() {
        gson = new Gson();
    }

    /**
     * Creates a hashmap of users
     *
     * @throws IOException
     */
    public void initUsers() throws IOException {
        File userFile = new File("res/users.txt");
        if (!userFile.exists()) {
            users = new HashMap<>();
            return;
        }

        reader = new BufferedReader(new FileReader("res/users.txt"));

        String json = reader.readLine();
        Users tempUsers = gson.fromJson(json, Users.class);

        users = tempUsers.getUsers();
    }

    /**
     * Creates a hashmap of events
     *
     * @throws IOException
     */
    public void initEvents() throws IOException {
        //TODO: maybe change path to a constant?
        File eventsFile = new File("res/events.txt");
        if (!eventsFile.exists()) {
            events = new HashMap<>();
            return;
        }

        reader = new BufferedReader(new FileReader("res/events.txt"));

        String json = reader.readLine();
        Events tempEvents = gson.fromJson(json, Events.class);

        events = tempEvents.getEvents();
    }

    /**
     * Converts events to json and saves to file
     *
     * @throws IOException
     */
    public void saveEvents() throws IOException {
        String eventJson = gson.toJson(events);

        createFile(eventJson, "res/events.txt");
    }

    /**
     * Converts user data to json and save to file
     *
     * @throws IOException
     */
    public void saveUsers() throws IOException {
        String userJson = gson.toJson(users);

        createFile(userJson, "res/users.txt");
    }

    /**
     * Writes json data to file
     *
     * @param json String representing json data
     * @param fileName String representing file name
     * @throws IOException
     */
    public void createFile(String json, String fileName) throws IOException {
        writer = new FileWriter(fileName);
        writer.write(json);
        writer.close();
    }

    /**
     * Returns list of events for specific date
     *
     * @param date LocalDate to access events from specific day
     * @return list of events for the provided date
     * @throws IOException
     * @see LocalDate
     */
    public List<Event> getEvents(LocalDate date) throws IOException {
        //TODO: not sure if we need to check containsKey if event list does not exist

        return events.get(date);
    }

    /**
     * Creates an event with specified information
     *
     * @param eventName String representing event name
     * @param creatorName String representing creator name
     * @param date LocalDate representing day of event
     * @param times List of times for event
     * @throws IOException
     * @see LocalDate
     * @see Time
     * @see List
     */
    public void createEvent(String eventName, String creatorName, LocalDate date, List<Time> times) throws IOException {
        //TODO: maybe have list of times with their own list of attendees (that way we know who is available at what time)?
        //TODO: maybe add to list -> have list of events for each day and get specific by creator
        //TODO: only if date exists in hashmap add event -> else add date and list with event to hashmap
        Event event = new Event(eventName, creatorName, date, times);

        if (events.containsKey(date)) {
            events.get(date).add(event);
        } else {
            List<Event> tempEvents = new ArrayList<>();
            tempEvents.add(event);

            events.put(date, tempEvents);
        }
    }

    /**
     * Deletes and event if it exists
     *
     * @param date used to look up event list
     * @param eventName String to find specific event
     * @throws IOException
     * @see LocalDate
     */
    public void deleteEvent(LocalDate date, String eventName) throws IOException {
        List<Event> events = getEvents(date);
        for(Event event : events){
            if(event.getEventName().equals(eventName)){
                events.remove(event);
            }
        }

    }

    /**
     * Creates a user with specified information
     *
     * @param username String representing username
     * @param password String representing password
     * @param isAdmin boolean representing admin status of user
     */
    public boolean createUser(String username, String password, boolean isAdmin)
    {
        if(!users.containsKey(username))
        {
          User newUser = new User(username, password, isAdmin);
          users.put(username, newUser);
          return(true);
        }
        else
        {
          return(false); 
        }

    }

    /**
     * Deletes user with specified username
     *
     * @param username String representing username
     */
    public void deleteUser(String username)
    {
        //TODO make sure this doesnt break

    }

    /**
     * Returns a list of users
     *
     * @return users
     * @see List
     */
    public List<User> getUsers() {
        return null;
    }
}
