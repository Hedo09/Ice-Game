package ItemClasses;

/**
 * State of an Item
 * - frozen
 * - inhand
 * - thrownDon
 */
public enum ItemState {
    frozen,inHand,thrownDown;

    public String getShortName(ItemState is){
        if(is.equals(frozen))
            return "F";
        else
            return "";
    }
}
