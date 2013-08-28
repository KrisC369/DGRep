package be.kriscon.dgrep.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Format and API a configuration object should adhere to.
 *
 * @author Kristof Coninx <kristof.coninx@student.kuleuven.be>
 * @credit Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public interface Configuration {

    double getDouble(String key, double defaultValue);

    float getFloat(String key, float defaultValue);

    long getLong(String key, long defaultValue);

    int getInt(String key, int defaultValue);

    short getShort(String key, short defaultValue);

    boolean getBoolean(String key, boolean defaultValue);

    String getString(String key, String defaultValue);

    Iterator<String> getKeys();

    List<?> getList(String key);

    <T> List<T> getList(String key, List<T> defaultValue);

    Configuration subset(String prefix);

    Map asMap();
}