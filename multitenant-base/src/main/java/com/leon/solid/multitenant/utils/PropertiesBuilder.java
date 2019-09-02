package com.leon.solid.multitenant.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.springframework.core.io.Resource;

/**
 * The utility class that load the properties context from file and create you own {@link Properties} instance on manual.
 */
public final class PropertiesBuilder {

    /**
     * the suffix of properties file name
     */
    public static final String PROPERTIES_EXTENTION = ".properties";

    private Properties prop;

    /**
     * create a new instance
     */
    public PropertiesBuilder() {
        init();
    }

    /**
     * Get a {@link Properties} instance by loading {@code stream}
     *
     * @param stream the content contains properties
     * @return the new instance of {@link Properties}
     * @throws IOException exception when it's loading failure
     * @see Properties#load(InputStream)
     */
    public static Properties load(final InputStream stream) throws IOException {
        final Properties prop = new Properties();
        prop.load(stream);
        return prop;
    }

    /**
     * Get a {@link Properties} instance by loading content from a {@link Resource}
     *
     * @param resource the location of file storing
     * @return  the new instance of {@link Properties}
     * @throws IOException exception when it's loading failure
     */
    public static Properties load(final Resource resource) throws IOException {
        final Properties prop = new Properties();
        try (InputStream is = resource.getInputStream()) {
            prop.load(is);
        }
        return prop;
    }

    /**
     *  Read the properties file when the specified {@link Resource resource} exists, otherwise empty {@link Properties} instance.
     *
     * @param resource the location of file storing
     * @return  the new instance of {@link Properties}. When {@code resource } does'nt exist, it returns empty {@link Properties} instance.
     * @throws IOException exception when it's loading failure
     */
    public static Properties loadIfExists(final Resource resource) throws IOException {
        return resource.exists() ? load(resource) : new Properties();
    }

    /**
     *  Read the properties file and convert to key-and-value-customize {@link Map} instance
     *
     * @param <K> the type of key in {@link Map}
     * @param <V> the type of value in {@link Map}
     * @param resource the location of file storing
     * @param keyConverter the converter that converts the key
     * @param valueConverter the converter that converts the value
     * @return the converted {@link Map}
     * @throws IOException exception when it's loading failure
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V>  loadIfExistsAndToMap(final Resource resource, @Nonnull Function<String, K> keyConverter, @Nonnull Function<String, V> valueConverter) throws IOException {
        return resource.exists() ? toMap(load(resource), keyConverter, valueConverter) : Collections.EMPTY_MAP;
    }

    /**
     * Merge two {@link Properties} instances into new  {@link Properties} instance.
     * <p>
     * The new instance bases on first {@code base}. Only if the key in {@code additional} exists in {@code base} and
     * the value in {@code additional} is not empty, the contained item will be remove.
     * Otherwise add item.
     *
     * @param base the {@link Properties} instance to base on
     * @param additional  the {@link Properties} instance to be merged
     * @return a new instance that these two {@link Properties} instances merge into
     */
    public static Properties merge(final Properties base, final Properties additional) {
        final Properties result = new Properties();
        result.putAll(base);

        additional.entrySet().stream().forEach(e -> {
            final String key = (String) e.getKey();
            final String value = (String) e.getValue();
            if (base.containsKey(key) && StringUtils.isEmpty(value)) {
                result.remove(key);
            } else {
                result.setProperty(key, value);
            }
        });
        return result;
    }

    /**
     * Add a internal {@link Properties} instance with specified {@code key}. If the {@code key} is {@code null} or empty, it will be skipped.
     *
     * @param key the key of new {@link Properties} instance
     * @param value the value of new {@link Properties} instance. If it's  {@code null} or empty, it will be skipped
     * @return the self handler
     */
    public PropertiesBuilder put(final String key, final Object value) {
        putInternal(key, value);
        return this;
    }

    /**
     *  Add the specified {@link Map} into {@link Properties}
     *
     * @param values the added map
     * @return the self handler
     */
    public PropertiesBuilder putAll(final Map<String, ?> values) {
        for (final Entry<String, ?> e : values.entrySet()) {
            putInternal(e.getKey(), e.getValue());
        }
        return this;
    }

    /**
     * Get a new {@link Properties} instance contains items by previous operations
     * @return  the new {@link Properties} instance
     */
    public Properties build() {
        final Properties result = prop;
        init();
        return result;
    }

    private static <K, V> Map<K, V> toMap(@Nonnull final Properties prop, @Nonnull Function<String, K> keyConverter, @Nonnull Function<String, V> valueConverter) {
        final Map<K, V> map = new HashMap<>();
        for (final Entry<Object, Object> e : prop.entrySet()) {
            map.put(keyConverter.apply((String) e.getKey()), valueConverter.apply((String) e.getValue()));
        }
        return map;
    }

    private void putInternal(final String key, final Object value) {
        if (value == null) {
            return;
        }
        final String stringValue = StringUtils.asString(value);
        if (StringUtils.hasText(key) && StringUtils.hasText(stringValue)) {
            prop.put(key, stringValue);
        }
    }

    private void init() {
        prop = new Properties();
    }
}
