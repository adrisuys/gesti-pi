package be.he2b.esi.moblg5.g43320.gestipi.model;

public enum Type {

    REUNION(1),
    HIKE(2),
    BAR_PI(1),
    BAR_PI_SPECIAL(2),
    OPE_BOUFFE(3),
    BAL_PI(3),
    SOIREE(3),
    REUNION_CAMP(2),
    CAMP(3),
    AUTRES(0);

    private int importance;

    private Type(int importance){
        this.importance = importance;
    }

    public int getImportance(){
        return importance;
    }


}
