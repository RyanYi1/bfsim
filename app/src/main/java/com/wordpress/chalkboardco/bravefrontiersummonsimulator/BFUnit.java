package com.wordpress.chalkboardco.bravefrontiersummonsimulator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ryan on 7/1/15.
 * This class will contain the typical information a Brave Frontier Unit has.
 */
public class BFUnit
{
    private String name;
    private String rarity;
    private String element;
    private String quote;
    private String path;
    private boolean custom;


    //Constructor to make a new unit. Will contain name, rarity, and quote.
    public BFUnit(String name, String rarity, String element, String quote, String path, boolean custom)
    {
        this.name = name;
        this.rarity = rarity;
        this.element = element;
        this.quote = quote;
        this.path = path;
        this.custom = custom;
    }

    public BFUnit()
    {
        super();
    }

    //The getters to help get the values for a BF unit.
    public String getName() { return this.name;}

    public String getRarity() {return this.rarity;}

    public String getElement() {return this.element;}

    public String getQuote()
    {
        return this.quote;
    }

    public String getPath() { return this.path;}

    public boolean isCustom() { return this.custom;}

    //Overridden method of toString to display the unit information.
    @Override
    public String toString()
    {
        return "Name: " + getName() + "\nRarity: " + getRarity() +
                "\nElement: " + getElement() + "\nQuote: " + getQuote();
    }

}
