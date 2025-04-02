package Main.organizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Main.Card.Flashcard;

public class RandomCardSorter implements CardSortingStrategy {

    @Override
    public List<Flashcard> organize(List<Flashcard> cards) {
        List<Flashcard> shuffledCards = new ArrayList<>(cards);
        Collections.shuffle(shuffledCards);
        return shuffledCards;
    }
}