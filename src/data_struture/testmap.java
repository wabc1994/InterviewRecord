package data_struture;
import java.util.Map;
import java.util.*;
public class testmap
    {
        public static void main(String[] args)
        {
            Map<String , Double> map = new TreeMap<String , Double>();
            map.put("ccc" , 89.0);
            map.put("aaa" , 80.0);
            map.put("zzz" , 80.0);
            map.put("bbb" , 89.0);
            System.out.println(map);
        }

    }

