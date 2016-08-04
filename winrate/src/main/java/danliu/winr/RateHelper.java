package danliu.winr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by danliu on 16-8-3.
 */
public class RateHelper {


    public static double rate(int player, List<Card> cards) {
        if (cards == null || cards.isEmpty() || cards.size() != 3) {
            return 0;
        }
        for (int i = 0; i < cards.size(); i++) {
            if (!cards.get(i)
                    .isValid()) {
                return 0;
            }
        }
        final List<Card> sorted = new ArrayList<>(cards);
        Collections.sort(sorted);
        cards = sorted;
        int greaterCount = 0;
        final Card card0 = cards.get(0);
        final Card card1 = cards.get(1);
        final Card card2 = cards.get(2);
        if (isAllSame(cards)) {
            final int value = card0.getConvertedValue();
            greaterCount = 4 * (13 - value + 1);
        } else {
            final int deltaTypeValue = Card.Type.SPADE.value - card0.getType().value;
            if (isRoyalFlush(cards)) {
                greaterCount = 4 * 13 - 3 * 3;
                if (card0.getValue() == 1 && card1.getValue() == 3) {
                    greaterCount += deltaTypeValue + 4 * 11;
                } else {
                    greaterCount += deltaTypeValue + (13 - card0.getConvertedValue()) * 4;
                }
            } else {
                if (isRoyal(cards)) {
                    //FIXME : 没有考虑2的情况.
                    greaterCount = 13 * 4 - 3 * 3 + 48 - 2 * 3;
                    //相同数字的情况
                    greaterCount += deltaTypeValue;
                    //首位数字相同的情况
                    //先计算出第三位增大的情况
                    greaterCount += 3 * (card1.getConvertedValue() - card2.getConvertedValue() - 1);
                    //再计算第二位增大的情况
                    if (card0.getConvertedValue() - card1.getConvertedValue() > 1) {
                        greaterCount += 3 * (doubleRangeCount(card1.getConvertedValue(),
                                card0.getConvertedValue()) - 1);
                    } else {
                        //减去同花顺的情况
                        if (card1.getConvertedValue() - card2.getConvertedValue() > 1) {
                            greaterCount -= 3;
                        }
                    }
                    //首位数字大于牌面首位数字的情况
                    for (int i = card0.getConvertedValue() + 1; i < 14; i++) {
                        for (int j = 2; j < i; j++) {
                            greaterCount += 4 * (j - 1);
                            if (j >= card2.getConvertedValue()) {
                                greaterCount -= 1;
                            }
                            if (j >= card1.getConvertedValue()) {
                                greaterCount -= 1;
                            }
                        }
                        greaterCount -= 3;
                    }
                } else if (isFlush(cards)) {
                    greaterCount = 13 * 4 - 3 * 3 + 48 + 1096 -  3 * 144; //1157
                    if (card0.getValue() == 1 && card1.getValue() == 3) {
                        greaterCount += Card.Type.SPADE.value - card2.getType().value;
                    } else {
                        greaterCount += deltaTypeValue;
                    }
                    if (card0.getValue() == 1 && card1.getValue() == 3) {
                        greaterCount += (13 - card1.getConvertedValue()) * (64 - 4) - 4 - 16;
                    } else {
                        greaterCount += (13 - card0.getConvertedValue()) * (64 - 4);
                        if (card0.getConvertedValue() != 13) {
                            greaterCount -= 4 + 16;
                        }
                    }
                } else if (isDouble(cards)) {
                    greaterCount = 13 + 48 + 1096 + 720;
                    int[] indexes = findDouble(cards);
                    final Card doubleC = cards.get(indexes[0]);
                    final Card singleC = cards.get(indexes[1]);
                    greaterCount += Card.Type.SPADE.value - singleC.getType().value;
                    //计算double大的情况,这里先加上总数
                    greaterCount += 4 * 3 * (13 - doubleC.getConvertedValue()) * (13 - 1) * 4;
                    //计算double相同的情况
                    if (doubleC.getConvertedValue() > singleC.getConvertedValue()) {
                        greaterCount += 4 * (13 - singleC.getConvertedValue()) - 4;
                        //减去 xx单 和 xx双
                        greaterCount -= 4 * 3  + 4 * 3 * 2;
                    } else {
                        greaterCount += 4 * (13 - singleC.getConvertedValue());
                        //减去 单单x 和 单单双
                        greaterCount -= 3 * 2 * (13 - 2) * 4 + 3 * 2 * 2;
                    }
                } else {
                    greaterCount = 13 + 48 + 1096 + 720 + 7488; //9365

                    //相同数字的情况
                    greaterCount += deltaTypeValue * 3 * 3;
                    //扣除后两张花色在选取范围内的同花情况
                    if (card1.getType().value <= card0.getType().value && card2.getType().value <= card0.getType().value) {
                        greaterCount -= deltaTypeValue;
                    } else if (card1.getType().value <= card0.getType().value) {
                        greaterCount -=  deltaTypeValue - 1;
                    } else if (card2.getType().value <= card0.getType().value) {
                        greaterCount -= deltaTypeValue - 1;
                    } else if (card1.getType().value != card2.getType().value) {
                        greaterCount -=  deltaTypeValue - 2;
                    } else {
                        greaterCount -= deltaTypeValue - 1;
                    }
                    //首位数字相同的情况
                    //先计算出第三位增大的情况
                    //-1是减去同花的情况的粗略计算,,不精确
                    greaterCount += (3 * 3 * 4 - 1) * (card1.getConvertedValue() - card2.getConvertedValue() - 1);
                    //再计算第二位增大的情况
                    if (card0.getConvertedValue() - card1.getConvertedValue() > 1) {
                        greaterCount += (3 * 3 * 4 - 1) * (doubleRangeCount(card1.getConvertedValue(),
                                card0.getConvertedValue()) - 1);
                    } else {
//                        //减去同花顺的情况
//                        if (card1.getConvertedValue() - card2.getConvertedValue() > 1) {
//                            greaterCount -= 3;
//                        }
                    }
                    //首位数字大于牌面首位数字的情况, 依旧忽略了很多细节...
                    for (int i = card0.getConvertedValue() + 1; i < 14; i++) {
                        for (int j = 2; j < i; j++) {
                            greaterCount += 64 * (j - 1);
                            if (j >= card2.getConvertedValue()) {
                                greaterCount -= 16;
                            }
                            if (j >= card1.getConvertedValue()) {
                                greaterCount -= 16;
                            }
                        }
                        greaterCount -= 64;
                    }

                }
            }
        }
        return greaterCount;
    }

    public static double rate(int player, Card... cards) {
        if (cards == null) {
            return 0;
        }
        final List<Card> cs = Arrays.asList(cards);
        return rate(player, cs);
    }

    public static int A(int range, int index) {
        int result = 1;
        for (int i = 0; i < index; i++) {
            result *= (range - i);
        }
        return result;
    }


    private static int doubleRangeCount(int start, int end) {
        int result = 0;
        for (int i = start + 1; i < end; i++) {
            result += i - 1;
        }
        return result;
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


    public static long C_Long(int range, int index) {
        long result = 1;
        for (int i = 0; i < index; i++) {
            result *= range - i;
        }
        long divider = 1;
        for (int i = 1; i <= index; i++) {
            divider *= i;
        }
        return result / divider;
    }


}
