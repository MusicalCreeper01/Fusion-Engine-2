package keithcod.es.fusionengine.core;

public class Utils {
    public static String safeName(String name){
        return name.replace(' ', '_').replace("/\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|\\)|\\`|\\~|\\[|\\]|\\{|\\}|\\;|\\'|\\,|\\/|\\:|\\\"|\\<|\\>|\\\\|\\=|\\+|\\?|\\|/g", "").toLowerCase();
    }
}
