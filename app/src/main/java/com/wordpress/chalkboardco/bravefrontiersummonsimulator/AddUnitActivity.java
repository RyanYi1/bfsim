package com.wordpress.chalkboardco.bravefrontiersummonsimulator;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;


public class AddUnitActivity extends Activity {
    //Tag and code
    static final String TAG = "AddUnit";
    static final int PICK_IMAGE = 20;

    //Set all of the views that will be used by this activity.
    public TextView mRarityTitle;
    public ToggleButtonGroupTableLayout mRarityGroup;
    public TextView mElementTextView;
    public ToggleButtonGroupTableLayout mElementGroup;
    public TextView mNameTitle;
    public EditText mNameEditText;
    public TextView mImageTitle;
    public Button mImagePathButton;
    public TextView mImagePath;
    public TextView mQuoteTitle;
    public EditText mQuoteEditText;
    public Button mSubmit;
    public Button mCancel;

    //instance variables used to help send information back to previous activity.
    public String rarity;
    public String element;
    public String uName;
    public String imagePath;
    public String uQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);

        //Initiate the views
        mRarityTitle = (TextView) findViewById(R.id.RarityTitleLabel);
        mRarityGroup = (ToggleButtonGroupTableLayout) findViewById(R.id.RarityGroup);
        mElementTextView = (TextView) findViewById(R.id.ElementTitleLabel);
        mElementGroup = (ToggleButtonGroupTableLayout) findViewById(R.id.ElementGroup);
        mNameTitle = (TextView) findViewById(R.id.NameTitleLabel);
        mNameEditText = (EditText) findViewById(R.id.NameEditText);
        mImageTitle = (TextView) findViewById(R.id.ImageNameTitleLabel);
        mImagePathButton = (Button) findViewById(R.id.ImagePathButton);
        mImagePath = (TextView) findViewById(R.id.ImagePathDisplay);
        mQuoteTitle = (TextView) findViewById(R.id.QuoteTitleLabel);
        mQuoteEditText = (EditText) findViewById(R.id.QuoteEditText);
        mSubmit = (Button) findViewById(R.id.SubmitButton);
        mCancel = (Button) findViewById(R.id.CancelButton);

        mImagePathButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent imageFind = new Intent(Intent.ACTION_PICK,
                                              MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(imageFind, PICK_IMAGE);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rarity = checkRarity();
                element = checkElement();
                uName = mNameEditText.getText().toString();
                imagePath = (String) mImagePath.getText();
                System.out.println(imagePath);
                uQuote = mQuoteEditText.getText().toString();
                if(!hasError())
                {
                    BFUnit customUnit = new BFUnit(uName, rarity, element, uQuote, imagePath, true);
                    UnitGenerator.addUnit(customUnit);
                    addToFile();
                    Intent data = getIntent();
                    data.putExtra(TAG, customUnit.toString());
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Goes back to previous activity with no result.
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    //Overridden method to decide what to do with returned data intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            Uri imagegot = data.getData();
            String actualPath = getRealPathFromURI(imagegot);
            mImagePath.setText(actualPath);
        }
    }

    //Helper method to get the real path of the selected image Uri.
    //This code was written by dextor on SO (StackOverflow)
    //dextor updated PercyPercy's code that is old, also on SO
    //URL for the code is
    //http://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore
    private String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null,
                                               null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //Helper method used to make sure the user didn't forget any important information.
    private boolean hasError()
    {
        boolean result = false;
        String errorString;
        if(rarity == null)
        {
            errorString = "Please select a Rarity";
            result = setToast(errorString);
        }
        else
        {
            if(element == null)
            {
                errorString = "Please select an element";
                result = setToast(errorString);
            }
            else
            {
                if(uName == null || uName.equals(""))
                {
                    errorString = "Please select a proper name.";
                    result = setToast(errorString);
                }
                else
                {
                    if(imagePath.equals("Image Path"))
                    {
                        errorString = "Please sleect a proper image path.";
                        result = setToast(errorString);
                    }
                    else
                    {
                        if(uQuote == null || uQuote.equals(""))
                        {
                            errorString = "Please select a proper quote.";
                            result = setToast(errorString);
                        }
                    }
                }
            }
        }
        return result;
    }

    //Helper method used to help set the toast errormsg.
    private boolean setToast(String error)
    {
        Toast errormsg = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
        errormsg.show();
        return true;
    }
    //Helper method used to help check the rarity that the user selected.
    private String checkRarity()
    {
        String rare;
        switch(mRarityGroup.getCheckedRadioButtonId())
        {
            case R.id.TwoStarButton:
                rare = "2 Star";
                break;

            case R.id.ThreeStarButton:
                rare = "3 Star";
                break;

            case R.id.FourStarButton:
                rare = "4 Star";
                break;

            case R.id.FiveStarButton:
                rare = "5 Star";
                break;

            case R.id.SixStarButton:
                rare = "6 Star";
                break;

            case R.id.SevenStarButton:
                rare = "7 Star";
                break;

            default:
                rare = null;
                break;

        }

        return rare;
    }

    //Helper method used to check what element the user selected.
    private String checkElement()
    {
        String ele;
        switch(mElementGroup.getCheckedRadioButtonId())
        {
            case R.id.FireButton:
                ele = "Fire";
                break;

            case R.id.WaterButton:
                ele = "Water";
                break;

            case R.id.ThunderButton:
                ele = "Thunder";
                break;

            case R.id.EarthButton:
                ele = "Earth";
                break;

            case R.id.LightButton:
                ele = "Light";
                break;

            case R.id.DarkButton:
                ele = "Dark";
                break;

            default:
                ele = null;
                break;

        }

        return ele;
    }

    //Helper method to create a file (or add to it if it already exists) to help record the
    //information of all of the custom units.
    private void addToFile()
    {
        FileOutputStream outputStream;
        try
        {
            outputStream = openFileOutput("customUnits.txt", Context.MODE_PRIVATE | Context.MODE_APPEND);
            outputStream.write(uName.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(rarity.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(element.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(uQuote.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(imagePath.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

