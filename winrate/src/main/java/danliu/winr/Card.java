package danliu.winr;

/**
 * Created by danliu on 16-8-3.
 */
public class Card implements Comparable<Card>{

    public int getTypeValue() {
        return mType.value;
    }

    public enum Type {
        SPADE(4), HEARD(3),CLUB(2),DIAMOND(1) //黑桃，红桃，梅花，方块
        ;

        public final int value;

        Type(int i) {
            value = i;
        }

        public static Type valueOf(int i) {
            switch (i) {
                case 1:
                    return DIAMOND;
                case 2:
                    return CLUB;
                case 3:
                    return HEARD;
                case 4:
                    return SPADE;
                default:
                    return null;
            }
        }
    }

    private Type mType;
    private int mValue;
    private String mRawValue;

    public Card(Type type, String value) {
        this.mType = type;
        try {
            if ("A".equalsIgnoreCase(value)) {
                mValue = 1;
            } else if ("J".equalsIgnoreCase(value)) {
                mValue = 11;
            } else if ("Q".equalsIgnoreCase(value)) {
                mValue = 12;
            } else if ("K".equalsIgnoreCase(value)) {
                mValue = 13;
            } else {
                mValue = Integer.parseInt(value);
            }
        } catch (Exception e) {
            mValue = 0;
        }
        mRawValue = value;
    }

    public boolean isValid() {
        return mValue > 0 && mValue < 14 && mType != null;
    }

    public int getValue() {
        return mValue;
    }

    public String getRawValue() {
        return mRawValue;
    }

    public Type getType() {
        return mType;
    }


    public int getConvertedValue() {
        if (mValue == 1) {
            return 13;
        } else {
            return mValue - 1;
        }
    }

    @Override
    public int compareTo(Card o) {
        if (o == null) {
            return -1;
        }
        if (o.isValid() && isValid()) {
            if (o.mValue == mValue) {
                return o.mType.value - mType.value;
            } else {
                return convertValue(o.mValue) - convertValue(mValue);
            }
        } else {
            if (o.isValid()) {
                return  1;
            } else  {
                return  -1;
            }
        }
    }

    private static int convertValue(int value) {
        if (value == 1) {
            return 13;
        } else {
            return value - 1;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(mType) + "_" + mRawValue;
    }

    public int toNumber() {
        return (getType().value << 4) | (getValue());
    }
}
