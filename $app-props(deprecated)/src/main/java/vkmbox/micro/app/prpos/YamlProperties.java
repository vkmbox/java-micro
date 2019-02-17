package vkmbox.micro.app.prpos;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;
import lombok.experimental.Accessors;
  
import java.util.Map;
import java.util.Queue;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.io.ByteArrayInputStream;

public class YamlProperties
{

  public Map<String, String> transform(byte[] encode) throws IOException
  {
    Yaml yaml = new Yaml();  
    try( InputStream in = new ByteArrayInputStream( encode ) ) {
      Map<String, Object> tree = yaml.load( in );
      return flattenTree(tree);
    }
  }

  private Map<String, String> flattenTree(Map<String, Object> tree)
  {
    Map<String, String> result = new HashMap<>();
    Queue<Pair> queue = new LinkedList<>();
//    iterateMap(tree, result, queue);
    for ( Map.Entry<String, Object> entry : tree.entrySet() ) {
      processKeyValue(entry.getKey(), entry.getValue(), result, queue);
    }
    
    /*for ( Map.Entry<String, Object> entry : tree.entrySet() ) {
      if ( entry.getValue() instanceof String ) {
        result.put(entry.getKey(), entry.getValue().toString());
      } else if ( entry.getValue() instanceof Map ) {
        processMapValue(entry.getKey(), (Map<String, Object>)entry.getValue(), queue);
      }
    }*/
    Pair pair;
    while ( (pair = queue.poll()) != null ) {
      processKeyValue(pair.getKey(), pair.getValue(), result, queue);
    }
    return result;
  }
  
  /*private void iterateMap( Map<String, Object> map, Map<String, String> result, Queue<Pair> queue) {
    for ( Map.Entry<String, Object> entry : map.entrySet() ) {
      processKeyValue(entry.getKey(), entry.getValue(), result, queue);
    }
  }*/

  private void processKeyValue( String key, Object value, Map<String, String> result, Queue<Pair> queue) {
    if ( value instanceof String ) {
      result.put(key, value.toString());
    } else if ( value instanceof Map ) {
      processMapValue(key, (Map<String, Object>)value, queue);
    }
  }
  
  private void processMapValue( String key, Map<String, Object> value, Queue<Pair> queue) {
    for ( Map.Entry<String, Object> entry : value.entrySet() ) {
      if ( entry.getValue() != null ) { //StringUtils.isEmpty(key) ? entry.getKey(): 
        queue.add( new Pair( key + "." + entry.getKey(), entry.getValue() ) );
      } 
    }
  }
  
  @Getter @Setter
  @Accessors(chain = true)
  private static class Pair {
    private String key;
    private Object value;
    
    public Pair( String key, Object value ) {
      this.key = key;
      this.value = value;
    }
  }
}
