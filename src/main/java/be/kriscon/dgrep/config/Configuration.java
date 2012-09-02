package be.kriscon.dgrep.config;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Kristof Coninx <kristof.coninx@student.kuleuven.be>
 * @credit Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public interface Configuration {

    public double getDouble( String key, double defaultValue );

    public float getFloat( String key, float defaultValue );

    public long getLong( String key, long defaultValue );

    public int getInt( String key, int defaultValue );

    public short getShort( String key, short defaultValue );

    public boolean getBoolean( String key, boolean defaultValue );

    public String getString( String key, String defaultValue );

    public Iterator<String> getKeys();

    public List<?> getList( String key );

    public <T> List<T> getList( String key, List<T> defaultValue );

    public Configuration subset( String prefix );

    public Map<Object,Object> asMap();

}