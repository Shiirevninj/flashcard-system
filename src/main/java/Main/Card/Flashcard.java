package Main.Card;
public class Flashcard {

    private final String question;
    private final String answer;
    private int mistakeCounter;

    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
        mistakeCounter = 0;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getMistakeCounter() {
        return mistakeCounter;
    }

    public void mistakeAdder() {
        mistakeCounter++;
    }
}