package danliu.winr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

    private static final String[] SRC = new String[13];

    static {
        SRC[0] = "A";
        SRC[10] = "J";
        SRC[11] = "Q";
        SRC[12] = "K";
        for (int i = 1; i < 10; i++) {
            SRC[i] = String.valueOf(i + 1);
        }
    }

    public static void main(String[] args) {
        printAllSorted();
    }

    private static void printAllSorted() {
        List<Card> allCards = new ArrayList<>(52);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < SRC.length; j++) {
                allCards.add(new Card(Card.Type.valueOf(i + 1), SRC[j]));
            }
        }
        final int size = (int) RateHelper.C_Long(52, 3);
        List<Hand> allHands = new ArrayList<>(size);
        for (int i = 0; i < allCards.size() - 2; i++) {
            for (int j = i + 1; j < allCards.size() - 1; j++) {
                for (int k = j + 1; k < allCards.size(); k ++) {
                    allHands.add(new Hand(allCards.get(i), allCards.get(j), allCards.get(k)));
                }
            }
        }
        Collections.sort(allHands);
        for (int i = 0; i < size; i++) {
            System.out.println(allHands.get(i));
        }
    }


    private static void testRandom() {
        List<Hand> hands = new ArrayList<>();
        hands.add(new Hand(new Card(Card.Type.CLUB, "A"), new Card(Card.Type.CLUB, "2"),
                new Card(Card.Type.CLUB, "3")));
        hands.add(new Hand(new Card(Card.Type.SPADE, "A"), new Card(Card.Type.SPADE, "2"),
                new Card(Card.Type.SPADE, "3")));
        hands.add(new Hand(new Card(Card.Type.HEARD, "A"), new Card(Card.Type.HEARD, "2"),
                new Card(Card.Type.HEARD, "3")));
        hands.add(new Hand(new Card(Card.Type.DIAMOND, "A"), new Card(Card.Type.DIAMOND, "2"),
                new Card(Card.Type.DIAMOND, "3")));
        hands.add(new Hand(new Card(Card.Type.CLUB, "A"), new Card(Card.Type.DIAMOND, "2"),
                new Card(Card.Type.CLUB, "3")));
        hands.add(new Hand(new Card(Card.Type.CLUB, "A"), new Card(Card.Type.DIAMOND, "2"),
                new Card(Card.Type.SPADE, "3")));
        hands.add(new Hand(new Card(Card.Type.CLUB, "A"), new Card(Card.Type.DIAMOND, "2"),
                new Card(Card.Type.DIAMOND, "3")));
        hands.add(new Hand(new Card(Card.Type.CLUB, "A"), new Card(Card.Type.DIAMOND, "2"),
                new Card(Card.Type.HEARD, "3")));
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            hands.add(new Hand(new Card(Card.Type.valueOf(random.nextInt(4) + 1),
                    SRC[random.nextInt(SRC.length)]),
                    new Card(Card.Type.valueOf(random.nextInt(4) + 1),
                            SRC[random.nextInt(SRC.length)]),
                    new Card(Card.Type.valueOf(random.nextInt(4) + 1),
                            SRC[random.nextInt(SRC.length)])));
        }
        Collections.sort(hands);
        for (int i = 0; i < hands.size(); i++) {
            System.out.println(hands.get(i));
        }
    }

    private static void testRate() {

    }

    private static void printNPlayer(int n) {
        long xx = RateHelper.C_Long(52, 3 * n);
        for (int i = 0; i < n; i++) {
            xx *= RateHelper.C_Long(i * 3 + 3, 3);
        }
        System.out.println("n:" + 5 + " result: " + xx);
    }

    private static void testSort() {
        Random random = new Random();
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cards.add(new Card(Card.Type.valueOf(random.nextInt(4) + 1), SRC[random.nextInt(13)]));
            //            cards.add(new Card(Card.Type.CLUB, SRC[random.nextInt(13)]));
            //            cards.add(new Card(Card.Type.valueOf(random.nextInt(4) + 1), "A"));
        }
        Collections.sort(cards);
        for (int i = 0; i < cards.size(); i++) {
            System.out.println(cards.get(i));
        }
    }


}
