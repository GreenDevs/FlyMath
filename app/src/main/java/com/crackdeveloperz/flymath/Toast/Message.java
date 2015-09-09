package com.crackdeveloperz.flymath.Toast;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by script on 9/9/15.
 */
public class Message
{
    public static void message(Context context,String message)
    {
        Toast.makeText(context, "" + message, android.widget.Toast.LENGTH_SHORT).show();
    }
}
