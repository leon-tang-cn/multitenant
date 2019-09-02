package com.leon.solid.multitenant.utils;

import java.util.Arrays;

/**
 *
 * Utility class that consolidates general array operations.
 * <p>
 * The {@code  indexOf} methods inside this class is all safe on {@code null}.
 */
public final class ArrayUtils {

    /** Array {@code boolean} contains 0 element. */
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    /** Array {@code byte} contains 0 element. */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    /** Array {@code char} contains 0 element. */
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    /** Array {@code double} contains 0 element. */
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    /** Array {@code float} contains 0 element. */
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    /** Array {@code int} contains 0 element. */
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    /** Array {@code long} contains 0 element. */
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    /** Array {@code short} contains 0 element. */
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    /** Array {@code Object} contains 0 element. */
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private static final int NOT_FOUND = -1;

    private ArrayUtils() {
    }

    /**
     * Check if the specified array is {@code null} or emplty (size 0).
     *
     * @param array Array
     * @return if the array is null or empty, return {@code true}
     */
    public static boolean isEmpty(final boolean[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check if the specified array is {@code null} or emplty (size 0).
     *
     * @param array Array
     * @return if the array is null or empty, return {@code true}
     */
    public static boolean isEmpty(final byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check if the specified array is {@code null} or emplty (size 0).
     *
     * @param array Array
     * @return if the array is null or empty, return {@code true}
     */
    public static boolean isEmpty(final char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check if the specified array is {@code null} or emplty (size 0).
     *
     * @param array Array
     * @return if the array is null or empty, return {@code true}
     */
    public static boolean isEmpty(final double[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check if the specified array is {@code null} or emplty (size 0).
     *
     * @param array Array
     * @return if the array is null or empty, return {@code true}
     */
    public static boolean isEmpty(final float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check if the specified array is {@code null} or emplty (size 0).
     *
     * @param array Array
     * @return if the array is null or empty, return {@code true}
     */
    public static boolean isEmpty(final int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check if the specified array is {@code null} or emplty (size 0).
     *
     * @param array Array
     * @return if the array is null or empty, return {@code true}
     */
    public static boolean isEmpty(final long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check if the specified array is {@code null} or emplty (size 0).
     *
     * @param array Array
     * @return if the array is null or empty, return {@code true}
     */
    public static boolean isEmpty(final short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check if the specified array is {@code null} or emplty (size 0).
     *
     * @param array Array
     * @return if the array is null or empty, return {@code true}
     */
    public static boolean isEmpty(final Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check if the specified array has more than 1 elemnt. 
     *
     * @param array Array
     * @return if the array is not null, and size is bigger than 1, return {@code true}
     */
    public static boolean isNotEmpty(final boolean[] array) {
        return !isEmpty(array);
    }

    /**
     * Check if the specified array has more than 1 elemnt. 
     *
     * @param array Array
     * @return if the array is not null, and size is bigger than 1, return {@code true}
     */
    public static boolean isNotEmpty(final byte[] array) {
        return !isEmpty(array);
    }

    /**
     * Check if the specified array has more than 1 elemnt. 
     *
     * @param array Array
     * @return if the array is not null, and size is bigger than 1, return {@code true}
     */
    public static boolean isNotEmpty(final char[] array) {
        return !isEmpty(array);
    }

    /**
     * Check if the specified array has more than 1 elemnt. 
     *
     * @param array Array
     * @return if the array is not null, and size is bigger than 1, return {@code true}
     */
    public static boolean isNotEmpty(final double[] array) {
        return !isEmpty(array);
    }

    /**
     * Check if the specified array has more than 1 elemnt. 
     *
     * @param array Array
     * @return if the array is not null, and size is bigger than 1, return {@code true}
     */
    public static boolean isNotEmpty(final float[] array) {
        return !isEmpty(array);
    }

    /**
    * Check if the specified array has more than 1 elemnt. 
    *
    * @param array Array
    * @return if the array is not null, and size is bigger than 1, return {@code true}
    */
    public static boolean isNotEmpty(final int[] array) {
        return !isEmpty(array);
    }

    /**
     * Check if the specified array has more than 1 elemnt. 
     *
     * @param array Array
     * @return if the array is not null, and size is bigger than 1, return {@code true}
     */
    public static boolean isNotEmpty(final long[] array) {
        return !isEmpty(array);
    }

    /**
     * Check if the specified array has more than 1 elemnt. 
     *
     * @param array Array
     * @return if the array is not null, and size is bigger than 1, return {@code true}
     */
    public static boolean isNotEmpty(final short[] array) {
        return !isEmpty(array);
    }

    /**
    * Check if the specified array has more than 1 elemnt. 
    *
    * @param array Array
    * @return if the array is not null, and size is bigger than 1, return {@code true}
    */
    public static boolean isNotEmpty(final Object[] array) {
        return !isEmpty(array);
    }

    /**
     * If the specified array is {@code null}, return a fixed array of size 0, otherwise return the specified array as is.
     *
     * @param array Array
     * @return if the array is {@code null}, return a fixed array of size 0. Otherwise return array itself.
     */
    public static boolean[] orDefault(final boolean[] array) {
        return array == null ? EMPTY_BOOLEAN_ARRAY : array;
    }

    /**
     * If the specified array is {@code null}, return a fixed array of size 0, otherwise return the specified array as is.
     *
     * @param array Array
     * @return if the array is {@code null}, return a fixed array of size 0. Otherwise return array itself.
     */
    public static byte[] orDefault(final byte[] array) {
        return array == null ? EMPTY_BYTE_ARRAY : array;
    }

    /**
     * If the specified array is {@code null}, return a fixed array of size 0, otherwise return the specified array as is.
     *
     * @param array Array
     * @return if the array is {@code null}, return a fixed array of size 0. Otherwise return array itself.
     */
    public static char[] orDefault(final char[] array) {
        return array == null ? EMPTY_CHAR_ARRAY : array;
    }

    /**
     * If the specified array is {@code null}, return a fixed array of size 0, otherwise return the specified array as is.
     *
     * @param array Array
     * @return if the array is {@code null}, return a fixed array of size 0. Otherwise return array itself.
     */
    public static double[] orDefault(final double[] array) {
        return array == null ? EMPTY_DOUBLE_ARRAY : array;
    }

    /**
     * If the specified array is {@code null}, return a fixed array of size 0, otherwise return the specified array as is.
     *
     * @param array Array
     * @return if the array is {@code null}, return a fixed array of size 0. Otherwise return array itself.
     */
    public static float[] orDefault(final float[] array) {
        return array == null ? EMPTY_FLOAT_ARRAY : array;
    }

    /**
     * If the specified array is {@code null}, return a fixed array of size 0, otherwise return the specified array as is.
     *
     * @param array Array
     * @return if the array is {@code null}, return a fixed array of size 0. Otherwise return array itself.
     */
    public static int[] orDefault(final int[] array) {
        return array == null ? EMPTY_INT_ARRAY : array;
    }

    /**
     * If the specified array is {@code null}, return a fixed array of size 0, otherwise return the specified array as is.
     *
     * @param array Array
     * @return if the array is {@code null}, return a fixed array of size 0. Otherwise return array itself.
     */
    public static long[] orDefault(final long[] array) {
        return array == null ? EMPTY_LONG_ARRAY : array;
    }

    /**
     * If the specified array is {@code null}, return a fixed array of size 0, otherwise return the specified array as is.
     *
     * @param array Array
     * @return if the array is {@code null}, return a fixed array of size 0. Otherwise return array itself.
     */
    public static short[] orDefault(final short[] array) {
        return array == null ? EMPTY_SHORT_ARRAY : array;
    }

    /**
     * Finds the specified element in the specified array and returns its index number.
     *
     * @param array Array
     * @param value Value to search
     * @return if the value exists, return its index. If the array is {@code null}, or the value doesn't exist, return -1.
     */
    public static int indexOf(final boolean[] array, final boolean value) {
        if (array == null) {
            return NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Finds the specified element in the specified array and returns its index number.
     *
     * @param array Array
     * @param value Value to search
     * @return if the value exists, return its index. If the array is {@code null}, or the value doesn't exist, return -1.
     */
    public static int indexOf(final byte[] array, final byte value) {
        if (array == null) {
            return NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Finds the specified element in the specified array and returns its index number.
     *
     * @param array Array
     * @param value Value to search
     * @return if the value exists, return its index. If the array is {@code null}, or the value doesn't exist, return -1.
     */
    public static int indexOf(final char[] array, final char value) {
        if (array == null) {
            return NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Finds the specified element in the specified array and returns its index number.
     *
     * @param array Array
     * @param value Value to search
     * @return if the value exists, return its index. If the array is {@code null}, or the value doesn't exist, return -1.
     */
    public static int indexOf(final double[] array, final double value) {
        if (array == null) {
            return NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Finds the specified element in the specified array and returns its index number.
     *
     * @param array Array
     * @param value Value to search
     * @return if the value exists, return its index. If the array is {@code null}, or the value doesn't exist, return -1.
     */
    public static int indexOf(final float[] array, final float value) {
        if (array == null) {
            return NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Finds the specified element in the specified array and returns its index number.
     *
     * @param array Array
     * @param value Value to search
     * @return if the value exists, return its index. If the array is {@code null}, or the value doesn't exist, return -1.
     */
    public static int indexOf(final int[] array, final int value) {
        if (array == null) {
            return NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Finds the specified element in the specified array and returns its index number.
     *
     * @param array Array
     * @param value Value to search
     * @return if the value exists, return its index. If the array is {@code null}, or the value doesn't exist, return -1.
     */
    public static int indexOf(final long[] array, final long value) {
        if (array == null) {
            return NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Finds the specified element in the specified array and returns its index number.
     *
     * @param array Array
     * @param value Value to search
     * @return if the value exists, return its index. If the array is {@code null}, or the value doesn't exist, return -1.
     */
    public static int indexOf(final short[] array, final short value) {
        if (array == null) {
            return NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Returns an array of {@code int} containing all of the specified characters.
     *
     * @param characters Characters
     * @return An array of {@code int} containing the specified characters in order (at least one character must be specified)
     */
    public static int[] toIntArray(final char... characters) {
        final int[] is = new int[characters.length];
        for (int i = 0; i < characters.length; i++) {
            is[i] = characters[i];
        }
        return is;
    }

    /**
     * Duplicates an array of specified objects.
     *
     * @param <T> Class of objects in the array
     * @param original Array to be copied
     * @return Copied result. Return {@code null} when original array is {@code null}.
     */
    public static <T> T[] copy(final T[] original) {
        return original == null ? null : Arrays.copyOf(original, original.length);
    }

    /**
     * Duplicates the specified array of integer values.
     *
     * @param original Array to be copied
     * @return Copied result. Return {@code null} when original array is {@code null}.
     */
    public static int[] copy(final int[] original) {
        return original == null ? null : Arrays.copyOf(original, original.length);
    }

    /**
     * Returns a new array that eliminates all duplicate elements within the specified array. The array needs to be sorted in advance.
     * <p>
     * This method changes the array specified in the argument destructively.
     *
     * @param input Sorted array
     * @return New array which eliminates all duplicate elements. Return {@code null} when original array is {@code null}.
     */
    public static int[] removeDuplicates(final int[] input) {
        if (input == null || input.length <= 1) {
            return input;
        }

        int j = 0;
        int i = 1;
        while (i < input.length) {
            if (input[i] != input[j]) {
                input[++j] = input[i];
            }
            i++;
        }
        return Arrays.copyOf(input, j + 1);
    }

    /**
     * Combine 2 arrays into a new array.
     *
     * @param base The array to be placed before in the array after combining
     * @param appendage The array to be placed behind in the array after combining
     * @return New array after combining. The length of the array is the sum of the length of {@code base} and the length of {@code appendage}. If {@code base} is {@code null} then
      * A copy of {@code appendage} and a copy of {@code base} are returned if {@code appendage} is {@code null}.
     */
    public static int[] concat(final int[] base, final int[] appendage) {
        if (base == null) {
            return copy(appendage);
        }
        if (appendage == null) {
            return copy(base);
        }

        final int baseLen = base.length;
        final int appendageLen = appendage.length;
        final int[] result = new int[baseLen + appendageLen];
        System.arraycopy(base, 0, result, 0, baseLen);
        System.arraycopy(appendage, 0, result, baseLen, appendageLen);
        return result;
    }

    /**
     * Returns the first element of the specified array.
     *
     * @param <T> Array element type
     * @param array Array
     * @return The first element of the specified array. Return {@code null} when the original array is {@code null}.
     */
    public static <T> T head(final T[] array) {
        return isEmpty(array) ? null : array[0];
    }

    /**
     * Creates a new array with the objects specified in the first argument placed at the beginning of the array specified in the second argument.
     * 
     * @param o Object
     * @param appendage Array to be added
     * @return A new Array with the objects specified in the first argument placed at the beginning of the array specified in the second argument. If the second argument is {@code null}, return an array with only the first argument.
     */
    public static Object[] join(final Object o, final Object[] appendage) {
        if (appendage == null) {
            return new Object[] { o };
        }
        final int appendageLen = appendage.length;
        final Object[] result = new Object[appendageLen + 1];
        System.arraycopy(appendage, 0, result, 1, appendageLen);
        result[0] = o;
        return result;
    }
}
