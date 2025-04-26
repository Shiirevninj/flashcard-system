package Main.organizer;

import java.util.ArrayList;
import java.util.List;

import Main.Card.Flashcard;

public class MistakesFirstSorter implements CardSortingStrategy {

    @Override
    public List<Flashcard> organize(List<Flashcard> cards) {
        List<Flashcard> mistakenCards = new ArrayList<>();
        List<Flashcard> correctCards = new ArrayList<>();

        for (Flashcard card : cards) {
            if (card.getMistakeCounter() > 0) {
                mistakenCards.add(card);
            }
            else{
                correctCards.add(card);
            }

        }
        List<Flashcard> reversedList = new ArrayList<>();
        for (int i = mistakenCards.size() - 1; i >= 0; i--) {
            reversedList.add(mistakenCards.get(i));
        }
        reversedList.addAll(correctCards);
        return reversedList;
    }

}