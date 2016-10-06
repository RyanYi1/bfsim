package com.wordpress.chalkboardco.bravefrontiersummonsimulator;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;



public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the Summon Button and set what it's suppose to do.
        final Button SummonButton = (Button) findViewById(R.id.sumButton);
        SummonButton.setOnClickListener((
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //Simply start a new activity.
                        Intent intent = new Intent(MainActivity.this, SummonActivity.class);
                        startActivity(intent);

                    }
                }));
        createUnits();
        readCustomFile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mItem)
    {
        //Handles presses on action bar items
        switch(mItem.getItemId())
        {
            case R.id.about:
                Intent about = new Intent(MainActivity.this, AboutInfoActivity.class);
                startActivity(about);
                break;

            case R.id.addunit:
                Intent addunit = new Intent(MainActivity.this, AddUnitActivity.class);
                startActivityForResult(addunit, RESULT_OK);
                break;

        }

        return true;
    }

    //Create the filereader to read all the information of the units, and put them in the static
    //array. This will be used to determine what unit is summoned.
    public void createUnits()
    {
        //Create the readers to help read from the file.
        InputStream ifile = getResources().openRawResource(R.raw.networkunitinfo);
        BufferedReader reader = new BufferedReader(new InputStreamReader(ifile));


        //Set the loop to read through all of the units and their info
        boolean loop = true;
        while(loop)
        {

            //Set the read info to variables. If unable, probably no more lines to read.
            try {
                String name = reader.readLine();
                String rarity = reader.readLine();
                String element = reader.readLine();
                String quote = reader.readLine();
                String path = reader.readLine();
                reader.readLine();

                //Checks to see if the info is valid. If so, add it to the static unit arraylist.
                if (name != null)
                {
                    UnitGenerator.addUnit(new BFUnit(name, rarity, element, quote, path, false));
                }
                else
                {
                    //Nothing more to read. Stop loop.
                    loop = false;
                    reader.close();
                }
            }
            catch (Exception ex)
            {
                System.err.println("File does not exist.");
            }
        }
    }

    //Helper method to read units from the custom file.
    private void readCustomFile()
    {
        try
        {
            File file = new File(getApplicationContext().getFilesDir(), "customUnits.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            //Set the loop to read through all of the units and their info
            boolean loop = true;
            while(loop)
            {

                //Set the read info to variables. If unable, probably no more lines to read.

                    String name = br.readLine();
                    String rarity = br.readLine();
                    String element = br.readLine();
                    String quote = br.readLine();
                    String path = br.readLine();

                    //Checks to see if the info is valid. If so, add it to the static unit arraylist.
                    if (name != null)
                    {
                        UnitGenerator.addUnit(new BFUnit(name, rarity, element, quote, path, true));
                    }
                    else
                    {
                        //Nothing more to read. Stop loop.
                        loop = false;
                        br.close();
                    }
            }
        }
        catch(Exception e)
        {
            System.err.println("File does not exist.");
        }
    }
}
