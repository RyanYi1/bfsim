package com.wordpress.chalkboardco.bravefrontiersummonsimulator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;


public class SummonActivity extends Activity {

    static private final int UNIT_CODE = 1;
    public static final String TAG = "Summon";
    public TextView LastUnit;

    BFUnit unit;
    Bundle unitInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon);

        //Gate Button.
        final ImageButton GateButton = (ImageButton) findViewById(R.id.gateButton);
        GateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Will start another activity that will show the unit, then return back here.
                Intent data = new Intent(SummonActivity.this, UnitShowActivity.class);
                data.putExtra("unitInfo", unitInfo);
                startActivityForResult(data, UNIT_CODE);
            }
        });
        //TextView will contain the information of the last unit.
        LastUnit = (TextView) findViewById(R.id.lastsummon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == UNIT_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                LastUnit.setText(data.getStringExtra(UnitShowActivity.TAG));
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        createBundle();
    }

    private void createBundle()
    {
        System.out.println("In creating bundle method.");
        unit = UnitGenerator.generate();
        if(!unit.isCustom())
        {
            System.out.println("Not a custom unit.");
            new LoadFromNet().execute(unit.getPath());
        }

        unitInfo = new Bundle();
        System.out.println("Bundling!");
        unitInfo.putString("UNIT_RARITY", unit.getRarity());
        unitInfo.putString("UNIT_ELEMENT", unit.getElement());
        unitInfo.putString("UNIT_PATH", unit.getPath());
        unitInfo.putString("UNIT_QUOTE", unit.getQuote());
        unitInfo.putBoolean("UNIT_CUSTOM", unit.isCustom());
        unitInfo.putString("UNIT_TOSTRING", unit.toString());
    }

    private class LoadFromNet extends AsyncTask<String, Void, Bitmap>
    {
        Bitmap bitmap;

        @Override
        protected Bitmap doInBackground(String... params)
        {
            try
            {
                System.out.println("Attempting to get image");
                URL address = new URL(params[0]);
                InputStream is = address.openStream();
                bitmap = BitmapFactory.decodeStream(is);

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            try
            {
                FileOutputStream fo = openFileOutput("showImage.png", Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fo);
                fo.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}
