package danliu.winr;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by danliu on 16-8-4.
 */
public class Hand implements Comparable<Hand> {

    private List<Card> mCards;
    private int mWinLevel;

    public Hand(Card... cards) {
        this(Arrays.asList(cards));
    }

    public Hand(List<Card> card) {
        Collections.sort(card);
        mCards = card;
        calculateWinLevel(card);
    }

    @Override
    public String toString() {
        return mCards.get(0)
                .toString() + "_" + mCards.get(1)
                .toString() + "_" + mCards.get(2)
                .toString();
    }

    //需要测试A32
    @Override
    public int compareTo(Hand o) {
        if (mWinLevel > o.mWinLevel) {
            return -1;
        } else if (mWinLevel < o.mWinLevel) {
            return 1;
        } else {
            Card c1 = mCards.get(0);
            Card c2 = mCards.get(1);
            Card c3 = mCards.get(2);
            Card o1 = o.mCards.get(0);
            Card o2 = o.mCards.get(1);
            Card o3 = o.mCards.get(2);
            if (mWinLevel == 10) {
                return o1.getConvertedValue() - c1.getConvertedValue();
            } else if (mWinLevel == 9 || mWinLevel == 7) {
                if (o1.getValue() == c1.getValue()) {
                    if (o2.getValue() == c2.getValue()) {
                        if (o3.getValue() == c3.getValue()) {
                            if (isA32()) {
                                if (o2.getTypeValue() == c2.getTypeValue()) {
                                    if (o3.getTypeValue() == c3.getTypeValue()) {
                                        return o1.getTypeValue() - c1.getTypeValue();
                                    } else {
                                        return o3.getTypeValue() - c3.getTypeValue();
                                    }
                                } else {
                                    return o2.getTypeValue() - c2.getTypeValue();
                                }
                            } else {
                                if (c1.getTypeValue() == o1.getTypeValue()) {
                                    if (c2.getTypeValue() == o2.getTypeValue()) {
                                        return o3.getTypeValue() - c3.getTypeValue();
                                    } else {
                                        return o2.getTypeValue() - c2.getTypeValue();
                                    }
                                } else {
                                    return o1.getTypeValue() - c1.getTypeValue();
                                }
                            }
                        } else {
                            if (o.isA32()) {
                                return -1;
                            } else if (isA32()) {
                                return 1;
                            } else {
                                return o3.getConvertedValue() - c3.getConvertedValue();
                            }
                        }
                    } else {
                        if (o.isA32()) {
                            return -1;
                        } else if (isA32()) {
                            return 1;
                        } else {
                            return o2.getConvertedValue() - c2.getConvertedValue();
                        }
                    }
                } else {
                    if (o.isA32()) {
                        return -1;
                    } else if (isA32()) {
                        return 1;
                    } else {
                        return o1.getConvertedValue() - c1.getConvertedValue();
                    }
                }
            } else if (mWinLevel == 8 || mWinLevel < 6) {
                if (o1.getValue() == c1.getValue()) {
                    if (o2.getValue() == c2.getValue()) {
                        if (o3.getValue() == c3.getValue()) {
                            if (c1.getTypeValue() == o1.getTypeValue()) {
                                if (c2.getTypeValue() == o2.getTypeValue()) {
                                    return o3.getTypeValue() - c3.getTypeValue();
                                } else {
                                    return o2.getTypeValue() - c2.getTypeValue();
                                }
                            } else {
                                return o1.getTypeValue() - c1.getTypeValue();
                            }
                        } else {
                            return o3.getConvertedValue() - c3.getConvertedValue();
                        }
                    } else {
                        return o2.getConvertedValue() - c2.getConvertedValue();
                    }
                } else {
                    return o1.getConvertedValue() - c1.getConvertedValue();
                }
            } else {
                int[] od = findDouble(o.mCards);
                int[] cd = findDouble(mCards);
                if (mCards.get(cd[0])
                        .getValue() == o.mCards.get(od[0])
                        .getValue()) {
                    if (mCards.get(cd[1])
                            .getValue() == o.mCards.get(od[1])
                            .getValue()) {
                        return o.mCards.get(od[1])
                                .getTypeValue() - mCards.get(cd[1])
                                .getTypeValue();
                    } else {
                        return o.mCards.get(od[1])
                                .getConvertedValue() - mCards.get(cd[1])
                                .getConvertedValue();
                    }
                } else {
                    return o.mCards.get(od[0])
                            .getConvertedValue() - mCards.get(cd[0])
                            .getConvertedValue();
                }
            }
        }
    }

    private boolean isA32() {
        return mCards.get(0)
                .getValue() == 1 && mCards.get(1)
                .getValue() == 3;
    }

    private void calculateWinLevel(List<Card> card) {
        if (isAllSame(card)) {
            mWinLevel = 10;
        } else if (isRoyalFlush(card)) {
            mWinLevel = 9;
        } else if (isRoyal(card)) {
            mWinLevel = 8;
        } else if (isFlush(card)) {
            mWinLevel = 7;
        } else if (isDouble(card)) {
            mWinLevel = 6;
        } else {
            mWinLevel = 5;
        }
    }

    private static int[] findDouble(List<Card> cards) {
        if (cards.get(0)
                .getValue() == cards.get(1)
                .getValue()) {
            return new int[]{
                    0,
                    2
            };
        } else if (cards.get(0)
                .getValue() == cards.get(2)
                .getValue()) {
            return new int[]{
                    0,
                    1
            };
        } else {
            return new int[]{
                    1,
                    0
            };
        }
    }

    private static boolean isDouble(List<Card> cards) {
        return (cards.get(0)
                .getValue() == cards.get(1)
                .getValue() && cards.get(1)
                .getValue() != cards.get(2)
                .getValue()) || (cards.get(0)
                .getValue() == cards.get(2)
                .getValue() && cards.get(1)
                .getValue() != cards.get(2)
                .getValue()) || (cards.get(1)
                .getValue() == cards.get(2)
                .getValue() && cards.get(0)
                .getValue() != cards.get(2)
                .getValue());

    }

    private static boolean isAllSame(List<Card> cards) {
        return cards.get(0)
                .getValue() == cards.get(1)
                .getValue() && cards.get(0)
                .getValue() == cards.get(2)
                .getValue();
    }

    private static boolean isRoyalFlush(List<Card> cards) {
        return isRoyal(cards) && isFlush(cards);
    }

    private static boolean isRoyal(List<Card> cards) {
        return cards.get(0)
                .getType() == cards.get(1)
                .getType() && cards.get(0)
                .getType() == cards.get(2)
                .getType();
    }

    private static boolean isFlush(List<Card> cards) {
        return (cards.get(0)
                .getConvertedValue() - cards.get(1)
                .getConvertedValue() == 1 && cards.get(1)
                .getValue() - cards.get(2)
                .getValue() == 1) || (cards.get(0)
                .getValue() == 1 && cards.get(1)
                .getValue() == 3 && cards.get(2)
                .getValue() == 2);
    }
}
