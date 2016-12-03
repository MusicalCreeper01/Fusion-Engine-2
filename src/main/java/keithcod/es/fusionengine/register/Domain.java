package keithcod.es.fusionengine.register;

import java.util.HashMap;
import java.util.Map;

public class Domain {

    public static Domain DEFAULT = new Domain("default");

    public static Map<String, Domain> domains = new HashMap<>();

    public String name = "";

    public Domain (String name){
        this.name = name;
    }

    public static Domain createDomain(String s){
        if(domains.containsKey(s)){
            System.out.println("Domain \"" + s + "\" is already registered, returning instead of creating...");
            return getDomain(s);
        }
        Domain d = new Domain(s);
        domains.put(s, d);
        return d;
    }

    public static Domain getDomain (String s){
        return domains.containsKey(s) ? domains.get(s) : null;
    }

}
