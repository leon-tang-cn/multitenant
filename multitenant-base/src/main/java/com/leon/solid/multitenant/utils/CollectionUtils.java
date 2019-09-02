package com.leon.solid.multitenant.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

/**
 * <p>
 * A utility that consolidates operations on a collection. It provides {@code null} safe operations
 * on collections, and easy generation of collections.
 * </p>
 */
public final class CollectionUtils {

  private static final Function<Object, String> DEFAULT_CONVERTER =
      (e) -> e == null ? StringUtils.EMPTY : e.toString();

  private CollectionUtils() {}

  /**
   * Check if the specified collection is {@code null} or empty.
   *
   * @param collection Collection to be checked
   * @return If the collection is {@code null} or empty, returns {@code true}
   */
  public static boolean isEmpty(final Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  /**
   * Check if the specified map is {@code null} or empty.
   *
   * @param map Map to be checked
   * @return If the map is {@code null} or empty, returns {@code true}
   */
  public static boolean isEmpty(final Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  /**
   * Check whether at least one element is contained in the specified collection.
   *
   * @param collection Map to be checked
   * @return If the collection is not {@code null} and it contains one or more elements, returns
   *         {@code true}
   */
  public static boolean isNotEmpty(final Collection<?> collection) {
    return !isEmpty(collection);
  }

  /**
   * Check whether at least one element is contained in the specified map.
   *
   * @param map Map to be checked
   * @return If the map is not {@code null} and it contains one or more elements, returns
   *         {@code true}
   */
  public static boolean isNotEmpty(final Map<?, ?> map) {
    return !isEmpty(map);
  }

  /**
   * Check if the specified collection contains the specified element ({@code null} safe version of
   * {@link Collection#contains (Object)}).
   *
   * @param collection Collection to be checked
   * @param o An element whose presence in this collection is to be checked
   * @return If the specified collection contains the specified element, returns {@code true}.
   *         Always returns {@code false} if the collection is {@code null}.
   */
  public static boolean contains(final Collection<?> collection, final Object o) {
    return collection != null && collection.contains(o);
  }

  /**
   * Returns the number of elements of the specified collection ({@code null} safe version of
   * {@link Collection#size ()}).
   *
   * @param collection Collection
   * @return Returns {@Code 0} if the collection is {@code null}, otherwise return the number of
   *         elements in the collection
   */
  public static int size(final Collection<?> collection) {
    return collection == null ? 0 : collection.size();
  }

  /**
   * Returns the number of key/value mappings in the specified map ({@code null} safe version of
   * {@link Map#size ()}).
   *
   * @param map Map
   * @return Returns {@Code 0} if the map is {@code null}, otherwise returns the number of key/value
   *         mappings in the map
   */
  public static int size(final Map<?, ?> map) {
    return map == null ? 0 : map.size();
  }

  /**
   * Generates a list containing all of the specified values. The result of this method is
   * modifiable.
   *
   * @param <T> The type of the specified value
   * @param args Values to include in the new list
   * @return New list。If no value is specified, returns an empty list.
   */
  @SafeVarargs
  public static <T> List<T> asList(final T... args) {
    final List<T> list = new ArrayList<T>();
    Collections.addAll(list, args);
    return list;
  }

  /**
   * Generates a list containing all of the specified values. The result of this method is
   * modifiable.
   *
   * @param <T> The type of the specified element
   * @param elements Elements to include in the new list
   * @return New list. If the argument is {@code null}, returns an empty list
   */
  public static <T> List<T> asList(final Iterable<? extends T> elements) {
    return elements == null ? new ArrayList<>()
        : elements instanceof Collection ? new ArrayList<T>((Collection<? extends T>) elements)
            : asList(elements.iterator());
  }

  /**
   * Generates a list containing all of the specified values. The result of this method is
   * modifiable.
   *
   * @param <T> The type of the specified element
   * @param elements Elements to include in the new list
   * @param elements Elements to include in the new list
   * @return New list. If the argument is {@code null}, returns an empty list
   */
  public static <T> List<T> asList(final Iterator<? extends T> elements) {
    final List<T> list = new ArrayList<>();
    if (elements == null) {
      return list;
    }
    while (elements.hasNext()) {
      list.add(elements.next());
    }
    return list;
  }

  /**
   * Creates a set containing all of the specified values. The result of this method is modifiable.
   *
   * @param <T> The type of the specified element
   * @param args Elements to include in the new set
   * @return New set
   */
  @SafeVarargs
  public static <T> Set<T> asSet(final T... args) {
    final Set<T> set = new HashSet<>();
    Collections.addAll(set, args);
    return set;
  }

  /**
   * Generate {@link SortedSet} containing all the specified values. The result of this method is
   * modifiable.
   *
   * @param <T> The type of the specified element
   * @param args Elements to include in the new set
   * @return New {@link SortedSet}
   */
  @SafeVarargs
  public static <T> SortedSet<T> asSortedSet(final T... args) {
    final SortedSet<T> set = new TreeSet<>();
    Collections.addAll(set, args);
    return set;
  }

  /**
   * Generates an unmodifiable list containing all of the specified values.
   *
   * @param <T> The type of the specified element
   * @param args Values to include in the new list
   * @return New list
   */
  @SafeVarargs
  public static <T> List<T> asUnmodifiableList(final T... args) {
    if (ArrayUtils.isEmpty(args)) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(asList(args));
  }

  /**
   * Generates an unmodifiable set that contains all the specified values.
   *
   * @param <T> The type of the specified element
   * @param args Values to include in the new set
   * @return New set
   */
  @SafeVarargs
  public static <T> Set<T> asUnmodifiableSet(final T... args) {
    if (ArrayUtils.isEmpty(args)) {
      return Collections.emptySet();
    }
    return Collections.unmodifiableSet(asSet(args));
  }

  /**
   * Generates an unmodifiable {@link SortedSet} that contains all the specified values.
   *
   * @param <T> The type of the specified element
   * @param args Values to include in the new set
   * @return New {@link SortedSet}
   */
  @SafeVarargs
  public static <T> SortedSet<T> asUnmodifiableSortedSet(final T... args) {
    if (ArrayUtils.isEmpty(args)) {
      return Collections.emptySortedSet();
    }
    return Collections.unmodifiableSortedSet(asSortedSet(args));
  }

  /**
   * Stringifies the specified collection. This method applies {@code converter} to each element in
   * the collection, converts the elements into strings, and concatenates the results with the
   * string specified by {@code delimiter}.
   *
   * @param target Collection to be stringified
   * @param converter Stringify each element of the collection {@link Function}. If {@code null} is
   *        passed, it must be implemented so that appropriate results can be stringified.
   * @param delimiter Value used as a delimiter for each element
   * @return The result of stringifying the collection. If the collection is {@code null} or empty
   *         it will return an empty string.
   */
  public static String toString(final Collection<?> target,
      final Function<Object, String> converter, @Nonnull final String delimiter) {
    if (isEmpty(target)) {
      return StringUtils.EMPTY;
    }
    final int dlen = delimiter.length();
    final StringBuilder sb = new StringBuilder();
    for (final Object e : target) {
      sb.append(delimiter).append(converter.apply(e));
    }
    return sb.substring(dlen);
  }

  /**
   * Stringifies the specified collection. This method applies {@code converter} to each element in
   * the collection to stringify the elements, and returns the result concatenated with commas.
   *
   * @param target Collection to be stringified
   * @param converter Stringify each element of the collection {@link Function}. If {@code null} is
   *        passed, it must be implemented so that appropriate results can be stringified.
   * @return The result of stringifying the collection. If the collection is {@code null} or empty
   *         it will return an empty string.
   */
  public static String toString(final Collection<?> target,
      final Function<Object, String> converter) {
    return toString(target, converter, ",");
  }

  /**
   * Stringifies the specified collection. This method {@link Object#toString ()} takes each element
   * in the collection and returns the result concatenated with a comma. If the element is
   * {@code null}, an empty string is used.
   *
   * @param target Collection to be stringified
   * @return The result of stringifying the collection. If the collection is {@code null} or empty
   *         it will return an empty string.
   */
  public static String toString(final Collection<?> target) {
    return toString(target, (e) -> e == null ? StringUtils.EMPTY : e.toString());
  }

  /**
   * Strings the specified collection. This method will {@link Object # toString ()} each element in
   * the collection and return the result concatenated with the string specified by
   * {@code delimiter}. If the element is {@code null}, an empty string is used.
   *
   * @param target Collection to be converted to a string
   * @param delimiter Value used as a delimiter for each element
   * @return The result of stringizing the collection. If the collection is {@code null} or empty,
   *         an empty string is returned.
   */
  public static String toString(final Collection<?> target, final String delimiter) {
    return toString(target, DEFAULT_CONVERTER, delimiter);
  }

  /**
   * After clearing all contents of the map specified as the target, add all the values of the
   * specified map.
   * <p>
   * This method is effectively equivalent to copying {@code values} to {@code target}.
   *
   * @param <K> Target key type
   * @param <V> Target value type
   * @param target Map where all content is replaced
   * @param values A map holding the value to be set to {@code target}. In the case of {@code null},
   *        just clear {@code target} and do not add a value.
   */
  public static <K, V> void substitute(final @Nonnull Map<K, V> target,
      final Map<? extends K, ? extends V> values) {
    target.clear();
    putAll(target, values);
  }

  /**
   * A map holding the value to be set to {@code target}. In the case of {@code null}, just clear
   * {@code target} and do not add a value.
   * <p>
   * This method is effectively equivalent to copying {@code values} to {@code target}.
   *
   * @param <E> Collection value type
   * @param target Collection where all content is replaced
   * @param values A collection holding the values to be set to {@code target}. In the case of
   *        {@code null}, just clear {@code target} and do not add a value.
   */
  public static <E> void substitute(final @Nonnull Collection<E> target,
      final Collection<? extends E> values) {
    target.clear();
    if (values == null) {
      return;
    }
    target.addAll(values);
  }

  /**
   * Copy all mappings of the map specified as {@code value} to the map specified as {@code target}
   * ({@code null} safe version of {@link Map # putAll (Map)}).
   *
   * @param <K> Target key type
   * @param <V> Target value type
   * @param target Map where values are stored
   * @param values Map holding values to be stored in {@code target} (ignored for {@code null})
   */
  private static <K, V> void putAll(final @Nonnull Map<K, V> target,
      final Map<? extends K, ? extends V> values) {
    if (values != null) {
      target.putAll(values);
    }
  }

  /**
   * Creates a new map holding all the contents of the specified map. This method performs "shallow
   * copy", and the map key / value stored in the copy destination is the same as the copy source
   * key / value.
   *
   * @param <K> Target key type
   * @param <V> Target value type
   * @param values Copy source map
   * @return Copied map. If the copy source map is {@code null}, an empty map that can be changed is
   *         returned.
   */
  public static <K, V> Map<K, V> copy(final Map<K, V> values) {
    final Map<K, V> target = new HashMap<K, V>();
    putAll(target, values);
    return target;
  }

  /**
   * If the specified {@link List} is {@code null}, it returns an empty {@link List}, otherwise it
   * returns the specified {@link List} itself.
   *
   * @param <E> Element type of {@link List}
   * @param list Target {@link List}
   * @return An unmodifiable empty {@link List} if the specified {@link List} is {@code null}.
   *         Otherwise returns the specified {@link List} itself.
   */
  public static <E> List<E> orDefault(final List<E> list) {
    return list == null ? Collections.emptyList() : list;
  }

  /**
   * Returns an empty {@link Set} if the specified {@link Set} is {@code null}, otherwise returns
   * the specified {@link Set} itself.
   *
   * @param <E> Element type of {@link Set}
   * @param set Target {@link Set}
   * @return An unmodifiable empty {@link Set} if the specified {@link Set} is {@code null}.
   *         Otherwise returns the specified {@link Set} itself.
   */
  public static <E> Set<E> orDefault(final Set<E> set) {
    return set == null ? Collections.emptySet() : set;
  }

  /**
   * Returns an empty {@link SortedSet} if the specified {@link SortedSet} is {@code null},
   * otherwise returns the specified {@link Set} itself.
   *
   * @param <E> Element type of {@link SortedSet}
   * @param set Target {@link SortedSet}
   * @return An unmodifiable empty {@link SortedSet} if the specified {@link SortedSet} is
   *         {@code null}. Otherwise returns the specified {@link SortedSet} itself.
   */
  public static <E> SortedSet<E> orDefault(final SortedSet<E> set) {
    return set == null ? Collections.emptySortedSet() : set;
  }

  /**
   * Returns an empty {@link Map} if the specified {@link Map} is {@code null}, otherwise returns
   * the specified {@link Set} itself.
   *
   * @param <K> Key element type of {@link Map}
   * @param <V> Value element type of {@link Map}
   * @param set Target {@link Map}
   * @return An unmodifiable empty {@link Map} if the specified {@link Map} is {@code null}.
   *         Otherwise returns the specified {@link Map} itself.
   */
  public static <K, V> Map<K, V> orDefault(final Map<K, V> map) {
    return map == null ? Collections.emptyMap() : map;
  }

  /**
   * Creates a new list with the new element added to the beginning of the specified existing list
   * and returns it.
   *
   * @param <T> The elements stored in the list
   * @param element Element to add at the beginning
   * @param values Existing list
   * @return New list
   */
  public static <T> List<T> prepend(final T element, final List<? extends T> values) {
    final List<T> list = new ArrayList<>();
    list.add(element);
    list.addAll(values);
    return list;
  }

  /**
   * Clones the specified {@link Hashtable} as a {@link Hashtable} keyed by a string.
   * <p>
   * The implementation of this method assumes that the given {@link Hashtable} 's keys are all
   * strings.
   *
   * @param table Replication source {@link Hashtable}
   * @return Replicated {@link Hashtable}. If the argument is {@code null}, an empty
   *         {@link Hashtable} is newly generated and returned.
   */
  public static Hashtable<String, Object> copyToKeytable(final Hashtable<String, ?> table) {
    final Hashtable<String, Object> newTable = new Hashtable<>();
    if (table == null || table.isEmpty()) {
      return newTable;
    }
    for (final Entry<String, ?> e : table.entrySet()) {
      newTable.put(e.getKey(), e.getValue());
    }
    return newTable;
  }

  /**
   * Gets the first element of the specified list.
   *
   * @param <E> Type of object stored in list
   * @param list List
   * @return First element of the list. If the list is {@code null} or empty {@code null}.
   */
  public static <E> E head(final List<E> list) {
    return isEmpty(list) ? null : list.get(0);
  }

  /**
   * Returns a list containing the results of applying {@code mapper} to each element of the
   * specified collection.
   *
   * @param <T> Type of original element
   * @param <R> Type of element after conversion
   * @param collection Collection to be processed
   * @param mapper Function to convert each element of collection
   * @return List after converting each element. If the collection is {@code null}, return an empty
   *         list.
   */
  public static <T, R> List<R> map(final Collection<T> collection,
      final Function<? super T, ? extends R> mapper) {
    return isEmpty(collection) ? Collections.emptyList()
        : collection.stream().map(mapper).collect(Collectors.toList());
  }
}
