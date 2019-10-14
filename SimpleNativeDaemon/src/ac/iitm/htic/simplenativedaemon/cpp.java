package ac.iitm.htic.simplenativedaemon;

public class cpp {

    public native  static Boolean b(Boolean p);

    public static Boolean bn(Boolean p){
         Boolean s= b(p);
         return s;
    }

    static{
        System.loadLibrary("myLib");
    }

}
