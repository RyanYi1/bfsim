package com.wordpress.chalkboardco.bravefrontiersummonsimulator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.BitmapDrawable;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class UnitShowActivity extends Activity {

    private TextView mRarity;
    private TextView mElement;
    private ImageView mUnitImage;
    private TextView mQuote;
    private Button mReturnSummon;

    //The unit info to display. This was the summoned unit.
    //final BFUnit unit = UnitGenerator.generate();
    private String rarity;
    private String element;
    private boolean isCustom;
    private String path;
    private Bitmap bm;
    private String quote;
    private String toString;
    private File image;

    //Static variable to help bundle the data in.
    public static final String TAG = "unit";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_show);

        getFile();
        getUnitValues();

        //Establish all of the TextViews and the button. Also set the values of the TextViews.
        mRarity = (TextView) findViewById(R.id.RarityTextView);
        mRarity.setText(rarity);

        mElement = (TextView) findViewById(R.id.ElementTextView);
        mElement.setText(element);

        mUnitImage = (ImageView) findViewById(R.id.UnitView);
        if(!isCustom)
        {
            //Uri uPath = Uri.parse(path);
            //mUnitImage.setImageURI(uPath);
            mUnitImage.setImageBitmap(bm);
        }
        else
        {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            mUnitImage.setImageBitmap(bitmap);
        }

        mQuote = (TextView) findViewById(R.id.QuoteTextView);
        mQuote.setText(quote);

        mReturnSummon = (Button) findViewById(R.id.ReturnButton);
        mReturnSummon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent data = getIntent();
                data.putExtra(TAG, toString);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }

    private void getUnitValues()
    {
        Bundle info = getIntent().getBundleExtra("unitInfo");
        rarity = info.getString("UNIT_RARITY");
        element = info.getString("UNIT_ELEMENT");
        isCustom = info.getBoolean("UNIT_CUSTOM");
        path = info.getString("UNIT_PATH");
        quote = info.getString("UNIT_QUOTE");
        toString = info.getString("UNIT_TOSTRING");

        try
        {
            bm = BitmapFactory.decodeFile(image.getPath());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getFile()
    {
        try
        {
            image = new File(getApplicationContext().getFilesDir(), "showImage.png");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mUnitImage.setImageBitmap(null);

    }




}
