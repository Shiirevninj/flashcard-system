package Main.organizer;

import java.util.List;

import Main.Card.Flashcard;

public class WorstFirstSorter implements CardSortingStrategy{
    @Override
    public List<Flashcard> organize(List<Flashcard> cards){
        for (int i = 0; i < cards.size() - 1; i++) {
            for (int j = i + 1; j < cards.size(); j++) {
                if (cards.get(i).getMistakeCounter() <= cards.get(j).getMistakeCounter()) {
                    Flashcard temp = cards.get(i);
                    cards.set(i, cards.get(j));
                    cards.set(j, temp);
                }
            }
        }
        return cards;
    }
}
