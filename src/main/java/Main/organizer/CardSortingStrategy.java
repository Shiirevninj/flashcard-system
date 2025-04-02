package Main.organizer;

import java.util.List;

import Main.Card.Flashcard;

public interface CardSortingStrategy {

    List<Flashcard> organize(List<Flashcard> cards);
}