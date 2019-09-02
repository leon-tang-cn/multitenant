package com.leon.solid.multitenant.utils;

import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.function.IntUnaryOperator;

import javax.annotation.Nonnull;


/**
 * A utility that consolidates methods for string manipulation.
 */
public final class StringUtils {

    /** A constant that represents an empty string. */
    public static final String EMPTY = "";

    /** A constant representing an ASCII space character ({@code U + 0020}). */
    public static final char SPACE = '\u0020';

    /** Line feed code used in the current operating environment. The value of {@link System # lineSeparator ()} is held. */
    public static final String LINE_SEPARATOR;

    /** This constant represents a line feed code in UNIX-like OS. */
    public static final String LINE_SEPARATOR_UNIX = "\n";

    /** A constant that represents the line feed code on Windows. */
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";

    private static final String DEFAULT_JOIN_DELIMITER = ",";

    private static final String SHORTENED_SYMBOL = "...";

    static {
        LINE_SEPARATOR = System.lineSeparator();
    }

    private StringUtils() {
    }

    /**
     * Checks if the specified string is {@code null} or an empty string.
     *
     * @param str Character string to be checked
     * @return {@code true} if the string is {@code null} or an empty string
     */
    public static boolean isEmpty(final CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * Checks if the specified string is not the empty string <b> </ b>.
     *
     * @param str Character string to be checked
     * @return {@code true} if the string is not {@code null} and contains only one character
     */
    public static boolean isNotEmpty(final CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * Checks if the specified string contains any non-blank characters. The determination of whitespace is done by {@link Character # isWhitespace (char)}.
     *
     * @param str Character string to be checked
     * @return {@code true} if the string is not {@code null} and contains only one character
     */
    public static boolean hasText(final CharSequence str) {
        if (str == null) {
            return false;
        }
        return str.chars().anyMatch(c -> !Character.isWhitespace(c));
    }

    /**
     * Checks if the specified string is {@code null}, an empty string, or a string consisting of all whitespace characters. Whether it is blank or not is checked by {@link Character # isWhitespace (char)}.
     *
     * @param str Character string to be checked
     * @return If the string is {@code null}, an empty string, or all characters are blank characters {@code true}
     */
    public static boolean isBlankText(final CharSequence str) {
        return !hasText(str);
    }

    /**
     * If the specified object is {@link String}, return the object itself. Otherwise returns {@link Object#toString()} for that object. This method is mainly provided to convert {@link CharSequence} to {@link String}.
     *
     * @param o Object
     * @return The result of {@link Object # toString ()} on the specified object. If the value specified in the argument is {@link String}, the value is directly returned. If {@code null}, {@code null} is returned.
     */
    public static String asString(final Object o) {
        return asString(o, null);
    }

    /**
     * If the specified object is {@link String}, return the object itself. Otherwise returns {@link Object#toString()} for that object.
     *
     * @param o Object
     * @param defaultValue Default string returned if {@code o} is {@code null}
     * @return The result of {@link Object#toString()} on the specified object. If the value specified in the argument is {@link String}, the value is directly returned. If {@code null}, {@code defaultValue} is returned.
     */
    public static String asString(final Object o, final String defaultValue) {
        return o == null ? defaultValue : o.toString();
    }

    /**
     * If the specified string is {@code null}, an empty string is returned, otherwise the specified string is returned unchanged.
     *
     * @param value String
     * @return Empty string if {@code value} is {@code null}, otherwise {@code value}.
     */
    @Nonnull
    public static String orDefault(final CharSequence value) {
        return value == null ? EMPTY : asString(value);
    }

    /**
     * If the string specified in the first argument is {@code null} or an empty string, the string specified in the second argument will be returned, otherwise the string specified in the first argument will be returned unchanged.
     *
     * @param value String
     * @param defaultValue Default string returned if {@code value} is {@code null} or an empty string
     * @return {@code value} is {@code null} or an empty string, returns {@code defaultValue}, otherwise returns {@code value}. However, if {@code defaultValue} is {@code null}, returns an empty string.
     */
    @Nonnull
    public static String orDefault(final CharSequence value, final CharSequence defaultValue) {
        return orDefault(isEmpty(value) ? defaultValue : value);
    }

    /**
     * If the specified string is {@code null} or an empty string {@code null} is returned, otherwise the specified string is returned unchanged.
     *
     * @param <T> Type of string given by argument
     * @param value String
     * @return {@Code null} if the specified value is an empty string, otherwise the specified value
     */
    public static <T extends CharSequence> T toNullIfEmpty(final T value) {
        return StringUtils.isEmpty(value) ? null : value;
    }

    /**
     * If the specified string is {@code null}, an empty string, or a string consisting of all whitespace characters, return {@code null}. Otherwise return the specified string itself.
     *
     * @param <T> Type of string given by argument
     * @param value String
     * @return {@Code null} if the specified value is a null string or a string consisting of all whitespace characters, otherwise the specified value itself
     */
    public static <T extends CharSequence> T toNullIfBlank(final T value) {
        return StringUtils.isBlankText(value) ? null : value;
    }

    /**
     * Returns a new string combining all of the given elements, using the specified delimiters.
     * <p>
     * This method is different from {@link String#join(CharSequence, Iterable)}. It would not join if the join target is {@code null}.
     *
     * @param delimiter Delimiter to separate each element
     * @param elements The element to combine If the element is not {@link CharSequence}, convert it with {@link Object#toString()}.
     * @return New joined string with delimiters. If no element is specified, returns an empty string.
     */
    @Nonnull
    public static String join(@Nonnull final CharSequence delimiter, final Iterable<?> elements) {
        if (elements == null) {
            return EMPTY;
        }

        final StringJoiner joiner = new StringJoiner(delimiter);
        elements.forEach(e -> {
            if (e != null) {
                joiner.add(e.toString());
            }
        });
        return joiner.toString();
    }

    /**
     * Returns a new string combining all of the given elements, using the specified delimiters.
     * <p>
     * This method is different from {@link String#join(CharSequence, CharSequence...)}. It would not join if the join target is {@code null}.
     *
     * @param delimiter Delimiter to separate each element
     * @param elements The element to combine If the element is not {@link CharSequence}, convert it with {@link Object # toString ()}.
     * @return New joined string with delimiters. If no element is specified, an empty string is returned.
     */
    @Nonnull
    public static String join(@Nonnull final CharSequence delimiter, final Object... elements) {
        return join(delimiter, Arrays.asList(elements));
    }

    /**
     * Returns a new string combining all given elements with commas ({@code ,}).
     * <p>
     * This method is different from {@link String#join(CharSequence, Iterable)}. It would not join if the join target is {@code null}.
     *
     * @param elements The element to combine If the element is not {@link CharSequence}, convert it with {@link Object # toString ()}.
     * @return New joined string with delimiters. If no element is specified, an empty string is returned.
     */
    @Nonnull
    public static String join(final Iterable<?> elements) {
        return join(DEFAULT_JOIN_DELIMITER, elements);
    }

    /**
     * Returns a new string combining all given elements with commas ({@code,}).
     * <p>
     * This method is different from {@link String#join(CharSequence, CharSequence ...)}, and it would not join if the join target is {@code null}.
     *
     * @param elements The element to combine. If the element is not {@link CharSequence}, convert it with {@link Object # toString ()} .
     * @return New joined string using delimiters. If no element is specified, an empty string is returned.
     */
    @Nonnull
    public static String join(final Object... elements) {
        return join(DEFAULT_JOIN_DELIMITER, Arrays.asList(elements));
    }

    /**
     * Converts the given object to a string.
     * <p>
     * This method usually simply calls {@link Object # toString ()} of the specified object. However, when the object is an array, it calls {@link Arrays # toString ()} and similar methods.
     *
     * @param value Target Object
     * @return Result of conversion to string. If the specified value is {@code null}, an empty string is returned.
     */
    public static String toString(final Object value) {
        if (value == null) {
            return EMPTY;
        }

        final Class<?> clz = value.getClass();
        if (!clz.isArray()) {
            return value.toString();
        }

        final Class<?> clazz = clz.getComponentType();
        if (clazz == String.class) {
            return Arrays.toString((Object[]) value);
        }
        if (clazz == boolean.class) {
            return Arrays.toString((boolean[]) value);
        }
        if (clazz == char.class) {
            return Arrays.toString((char[]) value);
        }
        if (clazz == int.class) {
            return Arrays.toString((int[]) value);
        }
        if (clazz == long.class) {
            return Arrays.toString((long[]) value);
        }
        if (clazz == byte.class) {
            return Arrays.toString((byte[]) value);
        }
        if (clazz == short.class) {
            return Arrays.toString((short[]) value);
        }
        if (clazz == float.class) {
            return Arrays.toString((float[]) value);
        }
        if (clazz == double.class) {
            return Arrays.toString((double[]) value);
        }
        return Arrays.toString((Object[]) value);
    }

    /**
     * Removes the space from the right of a string.
     *
     * @param str Target string
     * @return Converted string
     */
    public static String trimRight(final String str) {
        return removeCharRight(str, ' ');
    }

    /**
     * Removes the specified character from the string to the right.
     *
     * @param str Target string
     * @param removal The string to remove
     * @return Converted string
     */
    public static String removeCharRight(final String str, final char removal) {

        if (isEmpty(str)) {
            return str;
        }

        int idx = str.length();
        for (int i = str.length() - 1; i >= 0; i--) {
            final char c = str.charAt(i);
            if (c != removal) {
                break;
            }
            idx = i;
        }

        return str.substring(0, idx);
    }

    /**
     * Padding 0 into a string from the beginning to the specified number of digits
     *
     * @param target A string representing an integer
     * @param digit Number of digits
     * @return 0 padded digit string
     * @throws NumberFormatException If the string can not be interpreted as a number
     */
    public static String zeroPadding(final String target, final int digit) {

        // If it is longer than the specified number of digits, 0 padding unnecessary
        if (target.length() > digit) {
            return target;
        }

        // Quantify the passed number as a string
        final int number = Integer.parseInt(target);

        // Prepare a format according to the number of digits
        final StringBuilder zero = new StringBuilder();
        for (int i = 0; i < digit; i++) {
            zero.append("0");
        }
        // Return the 0 padded result
        return new DecimalFormat(zero.toString()).format(number);
    }

    /**
     * Removes leading zeros at the beginning of a string
     *
     * @param numberStr A string representing an integer
     * @return String with leading zeros removed
     */
    public static String zeroSuppress(final String numberStr) {
        return numberStr.replaceFirst("^0*(\\d*)$", "$1");
    }

    /**
     * Returns a string in which the first character of the specified string has been converted to upper case. Other characters are not converted.
     *
     * @param str String to convert
     * @return The result of converting the first character to upper case. If the argument is {@code null}, returns {@code null}.
     */
    public static String capitalize(final String str) {
        return changeFirstCharactersCase(str, true);
    }

    /**
     * Returns a string in which the first character of the specified string has been converted to lowercase. Other characters are not converted.
     *
     * @param str String to convert
     * @return The result of converting the first letter to lower case. If the argument is {@code null}, return {@code null}.
     */
    public static String uncapitalize(final String str) {
        return changeFirstCharactersCase(str, false);
    }

    /**
     * <p>
     * Returns a substring within a string based on the "length" of the character. The substring begins with the specified {@code beginIndex} and extends to the character at index {@code endIndex - 1}.
     * </p>
     * <p>
     * This method calculates the position of the cut out of the character based on the "length" of the character returned by the specified counter. If the character is split at the position of the cut, the character is not the target of the cut.
     *  For example, if you specify a counter that counts hiragana length as {@code 2}, the target character string is "{@code あいうえお}", the extraction start position is {@code 1}, and the extraction end position is {@code For 9},
     * </p>
     * <ul>
     * <li>The start position is corrected to "{@code あ}" because the start position is located at a place where "{@code い}" is divided.</li>
     * <li>The end position is corrected to "{@code お}}" because the end position is located where the "{@code え}}" is divided.</li>
     * </ul>
     * <p>
     * As a result, "{@code いうえ}} will be cut out.
     * </p>
     *
     * @param in String
     * @param beginIndex Start position
     * @param endIndex End position
     * @param counter Counter to calculate the "length" of the character
     * @return A string that has been cut out. An empty string if the string is {@code null} or {@code endIndex} is less than 0.
     */
    @Nonnull
    public static String substringBySize(final String in, final int beginIndex, final int endIndex,
            final IntUnaryOperator counter) {
        if (StringUtils.isEmpty(in) || endIndex < 0) {
            return StringUtils.EMPTY;
        }

        int pos = 0;
        char c = 0;

        // Skip to beginIndex: When splitting a full-width character, skip to a position beyond the full-width split character
        final StringCharacterIterator sci = new StringCharacterIterator(in);
        for (c = sci.first(); c != CharacterIterator.DONE && beginIndex > pos; c = sci.next()) {
            pos += counter.applyAsInt(c);
        }

        // Cut out from the position skipped to the endIndex
        final StringBuilder sb = new StringBuilder();
        for (; c != CharacterIterator.DONE; c = sci.next()) {
            pos += counter.applyAsInt(c);
            if (pos <= endIndex) {
                sb.append(c);
            }
            if (pos >= endIndex) {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * Returns the "length" of the string, using a counter to calculate the "width" of each character.
     * <p>
     * The "length" returned by this method depends on the implementation of the counter.
     * For example, using {@link CharsetUtils # TRADITIONAL_SIZE_COUNTER} or {@link HeuristicSizeCounter} as a counter, "width" of character is the length, while using {@link ByteSizeCounter} as a counter, "width" is the number of bytes in string.
     *
     * @param str String
     * @param counter Counter to calculate the "width" of characters
     * @return String "length". Returns {@code 0} if the string is {@code null} or an empty string.
     */
    public static int calculateSize(final CharSequence str, final IntUnaryOperator counter) {
        return str == null ? 0 : str.codePoints().map(c -> counter.applyAsInt(c)).sum();
    }

    private static String changeFirstCharactersCase(final String str, final boolean capitalize) {
        return isEmpty(str) ? str
                : prepareFirstCharacterAsSpecifiedCase(str, capitalize).append(str.substring(1)).toString();
    }

    private static StringBuilder prepareFirstCharacterAsSpecifiedCase(
            @Nonnull final CharSequence str, final boolean capitalize) {
        final char c = str.charAt(0);
        return new StringBuilder(str.length()).append(capitalize ? Character.toUpperCase(c) : Character.toLowerCase(c));
    }

    /**
     * Truncates a string below the specified maximum length and appends a default ellipsis to the end. This method is prepared for the case of giving the contents of long string argument to exception string briefly.
     *
     * @param s String
     * @param limit Maximum number of characters allowed
     * @return String truncated if necessary
     */
    public static String chop(final CharSequence s, final int limit) {
        if (isEmpty(s)) {
            return EMPTY;
        }
        final int length = s.length();
        final boolean tooLong = length > limit;
        return tooLong ? s.subSequence(0, limit).toString() + SHORTENED_SYMBOL : asString(s);
    }
}
