/*------------------------------------------------------------------------------
 **     Ident: Delivery Center Java
 **    Author: rene
 ** Copyright: (c) Feb 25, 2012 Sogeti Nederland B.V. All Rights Reserved.
 **------------------------------------------------------------------------------
 ** Sogeti Nederland B.V.            |  No part of this file may be reproduced  
 ** Distributed Software Engineering |  or transmitted in any form or by any        
 ** Lange Dreef 17                   |  means, electronic or mechanical, for the      
 ** 4131 NJ Vianen                   |  purpose, without the express written    
 ** The Netherlands                  |  permission of the copyright holder.
 *------------------------------------------------------------------------------
 */
package com.mutu.gpstracker.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.mutu.gpstracker.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

//import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * ????
 * 
 * @version $Id:$
 * @author rene (c) Feb 25, 2012, Sogeti B.V.
 */
public class About extends Activity
{

   private static final String TAG = "OGT.About";

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.about);
      fillContentFields();

   }

   private void fillContentFields()
   {
      TextView version = (TextView) findViewById(R.id.version);
      try
      {
         version.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
      }
      catch (NameNotFoundException e)
      {
         version.setText("");
      }
      WebView license = (WebView) findViewById(R.id.notices);
      license.loadUrl("file:///android_asset/notices.html");

      WebView apacheLicense = (WebView) findViewById(R.id.apachelong_body);
      apacheLicense.loadUrl("file:///android_asset/apache_license_version_20.html");

//      TextView playservices = (TextView) findViewById(R.id.playservices);
//      playservices.setText(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));
   }

   public static String readRawTextFile(Context ctx, int resId)
   {
      InputStream inputStream = ctx.getResources().openRawResource(resId);

      InputStreamReader inputreader = new InputStreamReader(inputStream);
      BufferedReader buffreader = new BufferedReader(inputreader);
      String line;
      StringBuilder text = new StringBuilder();

      try
      {
         while ((line = buffreader.readLine()) != null)
         {
            text.append(line);
            text.append('\n');
         }
      }
      catch (IOException e)
      {
         Log.e(TAG, "Failed to read raw text resource", e);
      }
      return text.toString();
   }

}