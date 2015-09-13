package com.crackdeveloperz.flymath;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.crackdeveloperz.flymath.Translator.Translate;
import com.microblink.activity.BlinkOCRActivity;
import com.microblink.ocr.ScanConfiguration;
import com.microblink.recognizers.ocr.blinkocr.parser.generic.RawParserSettings;

public class MainActivity extends AppCompatActivity
{

    private static final String LICENSE_KEY="QEAZF5XF-6M6OTULX-F4OBUP5Q-ZFJOILKJ-3HOOVHGW-WDEVFZBN-JHM5Z2U4-FDIW5EYJ";
    private static final String MATCH_TAG="Match";
    private static final int MY_REQUEST_CODE=1;
    private EditText scannedText;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result=(TextView)findViewById(R.id.calculated_result);
        scannedText=(EditText)findViewById(R.id.scanned_text);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE)
        {
            if (resultCode == BlinkOCRActivity.RESULT_OK && data != null) {
                // perform processing of the data here

                // for example, obtain parcelable recognition result
                Bundle extras = data.getExtras();
                Bundle results = extras.getBundle(BlinkOCRActivity.EXTRAS_SCAN_RESULTS);

                // results bundle contains result strings in keys defined
                // by scan configuration name
                // for example, if set up as in step 1, then you can obtain
                // e-mail address with following line
                //                             2x=4
                String raw="";
                if(results!=null)
                {
                    raw = results.getString(MATCH_TAG, "");
                }

                raw=raw.replaceAll("\\s+", "");
                raw=raw.replaceAll("A", "^");
                scannedText.setText(raw);

            }
        }
    }



    public void onClick(View view)
    {
        Intent intent=new Intent(this, BlinkOCRActivity.class);
        intent.putExtra(BlinkOCRActivity.EXTRAS_LICENSE_KEY, LICENSE_KEY);

        // setup array of scan configurations. Each scan configuration
// contains 4 elements: resource ID for title displayed
// in BlinkOCRActivity activity, resource ID for text
// displayed in activity, name of the scan element (used
// for obtaining results) and parser setting defining
// how the data will be extracted.
// For more information about parser setting, check the
// chapter "Scanning segments with BlinkOCR recognizer"

        ScanConfiguration[] confArray = new ScanConfiguration[]
                {
                new ScanConfiguration(R.string.math_title, R.string.math_msg, MATCH_TAG, new RawParserSettings())
        };
        intent.putExtra(BlinkOCRActivity.EXTRAS_SCAN_CONFIGURATION, confArray);

        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    public void calculate(View v)
    {
        String finalResult= Translate.sort(scannedText.getText().toString());
        result.setText(finalResult);

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
