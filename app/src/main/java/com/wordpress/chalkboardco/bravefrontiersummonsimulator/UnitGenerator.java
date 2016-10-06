package com.wordpress.chalkboardco.bravefrontiersummonsimulator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Created by ryan on 7/1/15.
 * Will be used to generate a unit.
 */
public class UnitGenerator {
    static private Random random;
    public static List<BFUnit> unitList = new ArrayList<BFUnit>();
    public static List<BFUnit> customList = new ArrayList<>();

    //Pretty much only method is to make a number and use that number to generate the correct unit.
    public static BFUnit generate() {
        BFUnit result;
        random = new Random();
        int num = random.nextInt(unitList.size() + customList.size());
        try {
            result = unitList.get(num);
        }
        catch (Exception e)
        {
            result = customList.get(num - unitList.size());
        }
        return result;
    }

    //Allows the adding of units to the unitList arraylist.
    public static void addUnit(BFUnit unit) {
        if (!unit.isCustom())
        {
            unitList.add(unit);
        } else
        {
            customList.add(unit);
        }
    }
}