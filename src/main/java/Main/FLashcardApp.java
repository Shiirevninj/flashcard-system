package Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Main.Card.Flashcard;
import Main.organizer.CardSortingStrategy;
import Main.organizer.RandomCardSorter;
import Main.organizer.WorstFirstSorter;


public class FLashcardApp {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    private static final String HELP_MESSAGE = ANSI_YELLOW + """
            Usage: <cards-file> [options]
            Options:
              --help                      Show help message
              --order <order>             Set card order (random, worst-first, recent-mistakes-first)
              --repetitions <num>         Number of repetitions per card
              --invertCards               Invert question and answer
            """ + ANSI_RESET;
    private static final String START_MESSAGE = """
            If you need help with the settings type --help""";

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {
        Scanner mainScanner = new Scanner(System.in);

        String cardsFile = "cards.txt";
        String order = "random";
        int repetitions = 1;
        boolean invertCards = false;

        List<Flashcard> cards = loadCards(cardsFile);
            if (cards == null) {
                return;
            }

        while (true) {
            System.out.println(START_MESSAGE);
            System.out.println("Default is --order " + order + " --repetitions " + repetitions + " --invertCards " + invertCards);
            System.out.println("Urgeljluuleh bolon tohirgoog uurchluhiig husvel enter darna uu:");
            String input = mainScanner.nextLine();
            String[] settings = input.split(" ");

            boolean showHelp = false;

            

            for (int i = 0; i < settings.length; i++) {
                switch (settings[i]) {
                    case "--help" -> {
                        System.out.println(HELP_MESSAGE);
                        showHelp = true;
                        break;
                    }
                    case "" -> {
                        continue;
                    }
                    case "--order" -> {
                        if (i + 1 <= settings.length) {
                            order = settings[++i];
                        } else {
                            System.err.println("Error: Missing argument for --order");
                            return;
                        }
                    }
                    case "--repetitions" -> {
                        if (i + 1 < settings.length) {
                            try {
                                repetitions = Integer.parseInt(settings[++i]);
                            } catch (NumberFormatException e) {
                                System.err.println("Error: WRONG NUMBER FOOL");
                                return;
                            }
                        }
                    }
                    case "--invertCards" -> {
                        invertCards = true;

                    }
                    default -> {
                        System.err.println("Error: " + settings[i]);
                        System.out.println("Type --help to see the options");
                    }

                }
            }

            if (showHelp) {
                continue;
            }


            if (invertCards) {
                for (Flashcard card : cards) {
                    String temp = card.getQuestion();
                    card = new Flashcard(card.getAnswer(), temp);
                }
            }

            CardSortingStrategy organizer = new RandomCardSorter();
            switch (order) {
                case "random" -> {
                    organizer = new RandomCardSorter();
                }
                case "worst-first" -> {
                    organizer = new WorstFirstSorter();
                }
                default -> {
                    System.err.println("Error: Iim daraalal baihgui");
                }
            }
            System.out.println("\nflashcard iig ehluuleh bol 'start' garah bol 'quit' darna uu");
            String command = mainScanner.nextLine().trim().toLowerCase();

            if (command.equals("quit")) {
                System.out.print("garch baina...");
                mainScanner.close();
                break;
            }
            if (!command.equals("start")) {
                System.out.print("Command buruu baina");
                continue;
            }

            cards = organizer.organize(cards);

            startFlashCard(cards, repetitions, mainScanner);
        }

        mainScanner.close();
    }

    /**
     * Asuultuudiig file aas unshina.
     */
    private static List<Flashcard> loadCards(String filePath) {
        List<Flashcard> cards = new ArrayList<>();

        if (!filePath.startsWith("/")) {
            filePath = "/" + filePath;
        }

        try (InputStream inputStream = FLashcardApp.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.err.println("Error: Could not find resource file: " + filePath);
                return null;
            }

            try (Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        cards.add(new Flashcard(parts[0].trim(), parts[1].trim()));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }

        return cards;

    }

    /**
     * Togloom ehluulne.
     */
    @SuppressWarnings("ConvertToTryWithResources")
private static void startFlashCard(List<Flashcard> cards, int repetitions, Scanner scanner) {
    int correct = 0;
    int incorrect = 0;
    
    // Amjiltiin heseg
    int totalCorrectAnswers = 0;
    int repeatCounter = 0;
    int confidentCounter = 0;

    for (Flashcard card : cards) {
        int cardCorrect = 0;
        int cardIncorrect = 0;
        for (int i = 0; i < repetitions; i++) {
            System.out.println("Asuult: " + card.getQuestion());
            System.out.print("hariult:");
            String answer = scanner.nextLine();
            System.out.print(" Aldaa: " + card.getMistakeCounter());
            
            if (answer.equalsIgnoreCase(card.getAnswer())) {
                System.out.println(ANSI_GREEN + "Zuv" + ANSI_RESET);
                correct++;
                cardCorrect++;  // hariult suv bol nemne
            } else {
                System.out.println(ANSI_RED + "Buruu. Zuv hariult: " + ANSI_YELLOW + card.getAnswer() + ANSI_RESET);
                card.mistakeAdder();
                incorrect++;
                cardIncorrect++;  // hariult buruu boll nemne
            }
        }
        
        // Amjiltiig shalgah heseg
        if (cardCorrect == repetitions) {  // CONFIDENT: tuhain kartad zuv hariulsan bol
            confidentCounter++;
        }

        if (cardIncorrect >= 5) {  // REPEAT: 5 udaa buruu hariulsan bol
            repeatCounter++;
        }

        if (cardCorrect == repetitions) {  // CORRECT:tuhain kartiin buh asuultad zuv hariulsan bol
            totalCorrectAnswers++;
        }
    }

    int sum = correct + incorrect;
    float percent = ((float) correct / (float) sum) * 100;
    System.out.println("Zuv: " + correct + " Buruu: " + incorrect);
    if (percent < 85) {
        System.out.println(percent + "% hangalttai bish baina. Dahin oroldono uu");
    } else {
        System.out.println("Wow " + percent + "% Sain baina");
    }

    // Amjilt shalgah
    if (totalCorrectAnswers == cards.size()) {
        System.out.println("CORRECT: Buh kartand zuv hariulsan!");
    }
    if (repeatCounter > 0) {
        System.out.println("REPEAT: kartand 5-aas deesh buruu hariulsam");
    }
    if (confidentCounter > 0) {
        System.out.println("CONFIDENT: kartand 3 udaa zuv hariulsan");
    }
}
}