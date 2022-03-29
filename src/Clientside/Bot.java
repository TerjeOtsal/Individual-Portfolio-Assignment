package Clientside;

import java.util.Random;
import java.util.ArrayList;

class List {
    // one list for availble bots and one for the bots that are picked
    public ArrayList<String> availableBots = new ArrayList<>();
    public ArrayList<String> usedBots = new ArrayList<>();

    public void addBots() {
        availableBots.add("Karl");
        availableBots.add("Jenny");
        availableBots.add("Timmy");
        availableBots.add("Marte");
    }

    public ArrayList<String> getAvailableBots() {
        return availableBots;
    }
    // takes the used bot name out of the list that you can choose from
    public void BotUsed(String botName) {
        usedBots.add(botName);
        availableBots.remove(botName);

    }
}


public class Bot {

    private String[] keyWords;

    {
        keyWords = new String[]{"dance", "drink", "walk", "run", "travel"};
    }

    private String botAnswer;
    private String foundKey;
    private boolean foundKeyBoolean = false;

    private Random randomGen = new Random();
    private int randomNumber = randomGen.nextInt();

    public Bot(String stringClient, String botName) {

        String[] greet = {"hey", "hello", "hi", "what's up"};
        for (String s : greet) {
            if (stringClient.contains(s)) {
                setAnswer("Hello there!");
            }
        }

        if (botAnswer == null) {
            KeywordFinder(stringClient);
            startBot(botName);
        }
    }

    public String answerBot() {
        return botAnswer;
    }

    public void setAnswer(String answerBot) {
        this.botAnswer = answerBot;
    }
    // based on the name you choose
    public void startBot(String botName) {

        if ("Karl".equals(botName)) {
            karlBot();
        } else if ("Jenny".equals(botName)) {
            jennyBot();
        } else if ("Timmy".equals(botName)) {
            timmyBot();
        } else if ("Marte".equals(botName)) {
            marteBot();
        } else {
            throw new IllegalStateException("Unexpected value: " + botName);
        }


    }
    // each bot has their own sets of replies
    private void marteBot() {

        if (foundKeyBoolean) {
            String[] array = {
                    foundKey + "ing might be my favorite thing to do!",
                    "Yes let's do " + foundKey, "I really want to "+foundKey+" right now!",
                    "I really don't like " + foundKey + ", let's do something else please",
                    "I would love to go " + foundKey +"ing" + " with you!"
            };
            setAnswer(array[randomNumber]);
        } else {
            setAnswer("tell me something else please, i don't understand!");
        }
    }

    private void timmyBot() {

        if (foundKeyBoolean) {
            String[] array = {
                    foundKey + "ing " + "is the funniest thing i know!",
                    "Yes let's do " + foundKey +" right now!!",
                    "Awww, I can't" + foundKey + ", but i really want to "+foundKey+
                            "with you tomorrow!",
                    "YEEES!!I want to do " + foundKey +"ing" + " with you."
            };
            setAnswer(array[randomNumber]);
        } else {
            setAnswer("What do you want? I dont understand!");
        }

    }

    private void jennyBot() {

        if (foundKeyBoolean) {
            String[] array = {
                    foundKey + "ing " + "is not cool enough!",
                    "I have better plans than " + foundKey+"ing",
                    "I don't want to " + foundKey + ", let's do something cool",
                    "I really don't want to do " + foundKey +"ing" + " with you."
            };
            setAnswer(array[randomNumber]);
        } else {
            setAnswer("Sorry didn't catch that, you were trying to say?");
        }

    }

    private void karlBot() {

        if (foundKeyBoolean) {
            String[] array = {
                    "ooo, yes let's do " + foundKey,
                    foundKey + "ing " + "is amazing!",
                    "I can't " + foundKey + ", let's do something else",
                    "I want to do " + foundKey +"ing but not alone!"
            };
            setAnswer(array[randomNumber]);
        } else {
            setAnswer("That doesn't make any sense");
        }
    }

    public void KeywordFinder(String stringFrClient) {
        String[] array = stringFrClient.split("[ ,?]");

        for (String key : keyWords) {
            for (String s : array) {
                if (s.equalsIgnoreCase(key)) {
                    foundKey = s;
                    foundKeyBoolean = true;
                }
            }
        }
    }
}