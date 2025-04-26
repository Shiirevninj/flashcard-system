package Main.organizer;

import java.util.List;

import Main.Card.Flashcard;

public interface CardSortingStrategy {
//todorhoi zarchmaar zohion baiguulah interface
    List<Flashcard> organize(List<Flashcard> cards);
}