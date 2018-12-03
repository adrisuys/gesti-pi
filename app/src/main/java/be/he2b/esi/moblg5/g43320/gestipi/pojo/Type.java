package be.he2b.esi.moblg5.g43320.gestipi.pojo;

import java.util.HashMap;
import java.util.Map;

public enum Type {

    REUNION("1", "REUNION"),
    HIKE("2", "HIKE"),
    BAR_PI("1", "BAR PI"),
    BAR_PI_SPECIAL("2", "BAR PI SPECIAL"),
    OPE_BOUFFE("3", "OPE BOUFFE"),
    BAL_PI("3", "BAL PI"),
    SOIREE("3", "SOIREE"),
    REUNION_CAMP("2", "REUNION POUR LE CAMP"),
    CAMP("3", "CAMP"),
    AUTRES("0", "AUTRES");

    private String importance;
    private String libelle;
    private static final Map<String, Type> MY_MAP = new HashMap<String, Type>();
    static {
        for (Type type : values()) {
            MY_MAP.put(type.toString(), type);
        }
    }

    private Type(String importance, String libelle){
        this.importance = importance;
        this.libelle = libelle;
    }

    public String getImportance(){
        return importance;
    }

    public String toString(){
        return libelle;
    }

    public static Type getTypeByLabel(String libelle){
        return MY_MAP.get(libelle);
    }


}
