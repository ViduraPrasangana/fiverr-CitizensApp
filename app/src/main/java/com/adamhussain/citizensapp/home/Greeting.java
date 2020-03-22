package com.adamhussain.citizensapp.home;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Greeting {
    private static List<String> greetings = Arrays.asList(
            "Hi there",
            "Howdy",
            "Greetings",
            "Hey, What’s up?",
            "What’s going on?",
            "How’s everything?",
            "How are things?",
            "Good to see you",
            "Great to see you",
            "Nice to see you",
            "Hey, boo"
    );

    static String getGreeting(){
        Collections.shuffle(greetings);
        return greetings.get(0);
    }
}
