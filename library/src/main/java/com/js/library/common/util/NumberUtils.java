package com.js.library.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * <pre>
 *     author: blankj
 *     blog  : http://blankj.com
 *     time  : 2020/04/12
 *     desc  : utils about number
 * </pre>
 */
public final class NumberUtils {

    // 默认除法运算精度
    private static final Integer DEF_DIV_SCALE = 2;
    private static final ThreadLocal<DecimalFormat> DF_THREAD_LOCAL = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            return (DecimalFormat) NumberFormat.getInstance();
        }
    };

    public static DecimalFormat getSafeDecimalFormat() {
        return DF_THREAD_LOCAL.get();
    }

    private NumberUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Format the value.
     *
     * @param value          The value.
     * @param fractionDigits The number of digits allowed in the fraction portion of value.
     * @return the format value
     */
    public static String format(float value, int fractionDigits) {
        return format(value, false, 1, fractionDigits, true);
    }

    /**
     * Format the value.
     *
     * @param value          The value.
     * @param fractionDigits The number of digits allowed in the fraction portion of value.
     * @param isHalfUp       True to rounded towards the nearest neighbor.
     * @return the format value
     */
    public static String format(float value, int fractionDigits, boolean isHalfUp) {
        return format(value, false, 1, fractionDigits, isHalfUp);
    }

    /**
     * Format the value.
     *
     * @param value            The value.
     * @param minIntegerDigits The minimum number of digits allowed in the integer portion of value.
     * @param fractionDigits   The number of digits allowed in the fraction portion of value.
     * @param isHalfUp         True to rounded towards the nearest neighbor.
     * @return the format value
     */
    public static String format(float value, int minIntegerDigits, int fractionDigits, boolean isHalfUp) {
        return format(value, false, minIntegerDigits, fractionDigits, isHalfUp);
    }

    /**
     * Format the value.
     *
     * @param value          The value.
     * @param isGrouping     True to set grouping will be used in this format, false otherwise.
     * @param fractionDigits The number of digits allowed in the fraction portion of value.
     * @return the format value
     */
    public static String format(float value, boolean isGrouping, int fractionDigits) {
        return format(value, isGrouping, 1, fractionDigits, true);
    }

    /**
     * Format the value.
     *
     * @param value          The value.
     * @param isGrouping     True to set grouping will be used in this format, false otherwise.
     * @param fractionDigits The number of digits allowed in the fraction portion of value.
     * @param isHalfUp       True to rounded towards the nearest neighbor.
     * @return the format value
     */
    public static String format(float value, boolean isGrouping, int fractionDigits, boolean isHalfUp) {
        return format(value, isGrouping, 1, fractionDigits, isHalfUp);
    }

    /**
     * Format the value.
     *
     * @param value            The value.
     * @param isGrouping       True to set grouping will be used in this format, false otherwise.
     * @param minIntegerDigits The minimum number of digits allowed in the integer portion of value.
     * @param fractionDigits   The number of digits allowed in the fraction portion of value.
     * @param isHalfUp         True to rounded towards the nearest neighbor.
     * @return the format value
     */
    public static String format(float value, boolean isGrouping, int minIntegerDigits, int fractionDigits, boolean isHalfUp) {
        return format(float2Double(value), isGrouping, minIntegerDigits, fractionDigits, isHalfUp);
    }

    /**
     * Format the value.
     *
     * @param value          The value.
     * @param fractionDigits The number of digits allowed in the fraction portion of value.
     * @return the format value
     */
    public static String format(double value, int fractionDigits) {
        return format(value, false, 1, fractionDigits, true);
    }

    /**
     * Format the value.
     *
     * @param value          The value.
     * @param fractionDigits The number of digits allowed in the fraction portion of value.
     * @param isHalfUp       True to rounded towards the nearest neighbor.
     * @return the format value
     */
    public static String format(double value, int fractionDigits, boolean isHalfUp) {
        return format(value, false, 1, fractionDigits, isHalfUp);
    }

    /**
     * Format the value.
     *
     * @param value            The value.
     * @param minIntegerDigits The minimum number of digits allowed in the integer portion of value.
     * @param fractionDigits   The number of digits allowed in the fraction portion of value.
     * @param isHalfUp         True to rounded towards the nearest neighbor.
     * @return the format value
     */
    public static String format(double value, int minIntegerDigits, int fractionDigits, boolean isHalfUp) {
        return format(value, false, minIntegerDigits, fractionDigits, isHalfUp);
    }

    /**
     * Format the value.
     *
     * @param value          The value.
     * @param isGrouping     True to set grouping will be used in this format, false otherwise.
     * @param fractionDigits The number of digits allowed in the fraction portion of value.
     * @return the format value
     */
    public static String format(double value, boolean isGrouping, int fractionDigits) {
        return format(value, isGrouping, 1, fractionDigits, true);
    }

    /**
     * Format the value.
     *
     * @param value          The value.
     * @param isGrouping     True to set grouping will be used in this format, false otherwise.
     * @param fractionDigits The number of digits allowed in the fraction portion of value.
     * @param isHalfUp       True to rounded towards the nearest neighbor.
     * @return the format value
     */
    public static String format(double value, boolean isGrouping, int fractionDigits, boolean isHalfUp) {
        return format(value, isGrouping, 1, fractionDigits, isHalfUp);
    }

    /**
     * Format the value.
     *
     * @param value            The value.
     * @param isGrouping       True to set grouping will be used in this format, false otherwise.
     * @param minIntegerDigits The minimum number of digits allowed in the integer portion of value.
     * @param fractionDigits   The number of digits allowed in the fraction portion of value.
     * @param isHalfUp         True to rounded towards the nearest neighbor.
     * @return the format value
     */
    public static String format(double value, boolean isGrouping, int minIntegerDigits, int fractionDigits, boolean isHalfUp) {
        DecimalFormat nf = getSafeDecimalFormat();
        nf.setGroupingUsed(isGrouping);
        nf.setRoundingMode(isHalfUp ? RoundingMode.HALF_UP : RoundingMode.DOWN);
        nf.setMinimumIntegerDigits(minIntegerDigits);
        nf.setMinimumFractionDigits(fractionDigits);
        nf.setMaximumFractionDigits(fractionDigits);
        return nf.format(value);
    }

    /**
     * Float to double.
     *
     * @param value The value.
     * @return the number of double
     */
    public static double float2Double(float value) {
        return new BigDecimal(String.valueOf(value)).doubleValue();
    }

    /**
     * double转换成string 可以防止double显示成4.99958333E7
     *
     * @param value     待转换值
     * @param precision 需要保留的位数
     */
    public static String doubleToString(double value, int precision) {
        return doubleToString(value, precision, true);
    }

    /**
     * double转换成string 可以防止double显示成4.99958333E7 不四舍五入 不四舍五入
     *
     * @param value     待转换值
     * @param precision 需要保留的位数
     * @param isUpDown  是否需要四舍五入 false 直接截取
     */
    public static String doubleToString(double value, int precision,
                                        boolean isUpDown) {
        NumberFormat df = NumberFormat.getInstance();
        df.setMaximumFractionDigits(precision);
        df.setMinimumFractionDigits(precision);
        if (!isUpDown) {
            df.setRoundingMode(RoundingMode.DOWN);
        }
        return df.format(value).replace(",", "");
    }

    /**
     * 保留两位小数，之后的直接舍去
     *
     * @param money
     * @return
     */
    public static String getPriceTruncation(double money) {
        return "¥" + doubleToString(money, 2, false);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static Double add(Number value1, Number value2) {
        return add(value1.doubleValue(), value2.doubleValue());
    }

    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static Double add(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).doubleValue();
    }

    public static String add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).toString();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(Number value1, Number value2) {
        String s = Double.toString(value1.doubleValue());
        String s1 = Double.toString(value2.doubleValue());
        return sub(s, s1);
    }

    public static Double sub(Double value1, Double value2) {
        return sub(Double.toString(value1), Double.toString(value2));
    }

    public static Double sub(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static Double mul(Number value1, Number value2) {
        return mul(Double.toString(value1.doubleValue()), Double.toString(value2.doubleValue()));
    }

    public static Double mul(Double value1, Double value2) {
        return mul(Double.toString(value1), Double.toString(value2));
    }

    public static Double mul(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 两个参数的商
     */
    public static String div(String dividend, String divisor) {
        return div(dividend, divisor, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @param scale    表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static Double div(Double dividend, Double divisor, Integer scale) {
        if (scale < 0) {
            return 0.00;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String div(String dividend, String divisor, Integer scale) {
        if (scale < 0) {
            return "0";
        }
        BigDecimal b1 = new BigDecimal(dividend);
        BigDecimal b2 = new BigDecimal(divisor);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }
}
