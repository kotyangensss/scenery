package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        Map<String, Actor> actors = new HashMap<>();
        actors.put("Chandler", new Actor("Chandler"));
        actors.put("Joey", new Actor("Joey"));
        actors.put("Monica", new Actor("Monica"));
        actors.put("Phoebe", new Actor("Phoebe"));
        actors.put("Rachel", new Actor("Rachel"));
        actors.put("Ross", new Actor("Ross"));

        try {
            InputStream inputStream = Main.class.getResourceAsStream("/scenery.txt");
            assert inputStream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                String character = parts[0];
                String dialogue = parts[1];
                Actor actor = actors.get(character);
                if (actor != null) {
                    new Thread(() -> actor.say(dialogue)).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Actor {
        private final String name;

        public Actor(String name) {
            this.name = name;
        }

        public void say(String dialogue) {
            lock.lock();
            try {
                System.out.println(name + ": " + dialogue);
            } finally {
                lock.unlock();
            }
        }
    }
}