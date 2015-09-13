package com.crackdeveloperz.flymath;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crackdeveloperz.flymath.StringPreProcessing.PreProcess;
import com.crackdeveloperz.flymath.Translator.Translate;
import com.microblink.activity.BlinkOCRActivity;
import com.microblink.ocr.ScanConfiguration;
import com.microblink.recognizers.ocr.blinkocr.parser.generic.RawParserSettings;

public class MainActivity extends AppCompatActivity
{

    private static final String LICENSE_KEY="QEAZF5XF-6M6OTULX-F4OBUP5Q-ZFJOILKJ-3HOOVHGW-WDEVFZBN-JHM5Z2U4-FDIW5EYJ";
    private static final String RAW_STRING="+(-3( (4*5+4x)-7-8x)/(22-4)+x)8";
    private static final String MATCH_TAG="Match";
    private static final int MY_REQUEST_CODE=1;
    private EditText scannedText;
    private TextView result, scanText, calculateButton, results;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init()
    {
//        result=(TextView)findViewById(R.id.calculated_result);
        results=(TextView)findViewById(R.id.id_of_result);
        scannedText=(EditText)findViewById(R.id.scanned_text);
        calculateButton=(TextView)findViewById(R.id.calculate_button);
        scanText=(TextView)findViewById(R.id.scan_text);
        scrollView=(ScrollView)findViewById(R.id.scrollView);

        Typeface typeface=Typeface.createFromAsset(getAssets(),"icomoon.ttf");
        scanText.setTypeface(typeface);
        calculateButton.setTypeface(typeface);

        calculateButton.setText(getResources().getString(R.string.calculate3));
        scanText.setText(getResources().getString(R.string.scan1));

        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE)
        {
            if (resultCode == BlinkOCRActivity.RESULT_OK && data != null)
            {
                Bundle extras = data.getExtras();
                Bundle results = extras.getBundle(BlinkOCRActivity.EXTRAS_SCAN_RESULTS);
                String raw="";
                if(results!=null)
                {
                    raw = results.getString(MATCH_TAG, "");
                }

                scannedText.setText(raw);
                scrollView.scrollTo(0, scrollView.getBottom());


            }
        }
    }



    public void onClick(View view)
    {

        Intent intent=new Intent(this, BlinkOCRActivity.class);
        intent.putExtra(BlinkOCRActivity.EXTRAS_LICENSE_KEY, LICENSE_KEY);
        ScanConfiguration[] confArray = new ScanConfiguration[]
        {
                new ScanConfiguration(R.string.math_title, R.string.math_msg, MATCH_TAG, new RawParserSettings())
        };
        intent.putExtra(BlinkOCRActivity.EXTRAS_SCAN_CONFIGURATION, confArray);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }


    public void calculate(View v)
    {
        String originalString=scannedText.getText().toString();
        PreProcess preProcess=new PreProcess(originalString);
        originalString=preProcess.removeUnnecesarySymbols();
        scannedText.setText(originalString);
        preProcess.splitAndCheck(originalString);
        ///THIS IF CONDITION IS FOR BRACKETS VALIDATIONS
        if(preProcess.bracketValidation())
        {
            if(preProcess.checkOperators(originalString))
            {
                if(preProcess.checkBracketsAndOperators(originalString))
                {
                    if(preProcess.checkVariableNames(originalString))
                    {
                        if(preProcess.decideTypeNSplitCheck(originalString))
                        {
                            String generalEQ=preProcess.generalizeEquation();
                            Log.i("FILTERD String", generalEQ);
                            String finalResult= Translate.sort(generalEQ);
                            results.setText(finalResult);
                        }
                        else
                        {
                            Toast.makeText(this, "INAVLID EXPRESSION", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(this, "INVALID VARIABLE NAMES", Toast.LENGTH_SHORT).show();
                    }


                }
                else
                {
                    Toast.makeText(this, "BRACKETS AND OPERATORS ON CONFLICT", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(this, "FATAL USE OF OPERATORS", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "BRACKETS MISMATCHED", Toast.LENGTH_SHORT).show();
        }


        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
