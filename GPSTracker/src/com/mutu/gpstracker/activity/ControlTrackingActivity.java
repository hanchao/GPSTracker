/*------------------------------------------------------------------------------
 **     Ident: Delivery Center Java
 **    Author: rene
 ** Copyright: (c) Nov 17, 2012 Sogeti Nederland B.V. All Rights Reserved.
 **------------------------------------------------------------------------------
 ** Sogeti Nederland B.V.            |  No part of this file may be reproduced  
 ** Distributed Software Engineering |  or transmitted in any form or by any        
 ** Lange Dreef 17                   |  means, electronic or mechanical, for the      
 ** 4131 NJ Vianen                   |  purpose, without the express written    
 ** The Netherlands                  |  permission of the copyright holder.
 *------------------------------------------------------------------------------
 */
package com.mutu.gpstracker.activity;

import com.mutu.gpstracker.actions.NameTrack;
import com.mutu.gpstracker.db.GPStracking.Tracks;
import com.mutu.gpstracker.fragment.ControlTrackingFragment;
import com.mutu.gpstracker.fragment.ControlTrackingFragment.ControlTrackingListener;
import com.mutu.gpstracker.logger.GPSLoggerServiceManager;

import com.mutu.gpstracker.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * ????
 * 
 * @version $Id:$
 * @author rene (c) Nov 17, 2012, Sogeti B.V.
 */
public class ControlTrackingActivity extends Activity implements ControlTrackingListener
{

   private GPSLoggerServiceManager mLoggerServiceManager;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_trackingcontrol);

      findViewById(R.id.activity_trackingcontrol_main).setOnClickListener(new OnClickListener()
         {
            @Override
            public void onClick(View v)
            {
               dismiss();
            }
         });
      mLoggerServiceManager = new GPSLoggerServiceManager(this);
   }

   @Override
   protected void onResume()
   {
      super.onResume();
      mLoggerServiceManager.startup(this, new Runnable()
         {
            @Override
            public void run()
            {
               ControlTrackingFragment details = ControlTrackingFragment.newInstance(mLoggerServiceManager.getLoggingState());
               getFragmentManager().beginTransaction().add(R.id.activity_trackingcontrol_centerframe, details).commit();
            }
         });
   }

   @Override
   protected void onPause()
   {
      super.onPause();
      mLoggerServiceManager.shutdown(this);
   }

   @Override
   public void start()
   {
      Intent intent = new Intent();
      long loggerTrackId = mLoggerServiceManager.startGPSLogging(null);

      // Start a naming of the track
      Intent namingIntent = new Intent(this, NameTrack.class);
      namingIntent.setData(ContentUris.withAppendedId(Tracks.CONTENT_URI, loggerTrackId));
      startActivity(namingIntent);

      // Create data for the caller that a new track has been started
      ComponentName caller = this.getCallingActivity();
      if (caller != null)
      {
         intent.setData(ContentUris.withAppendedId(Tracks.CONTENT_URI, loggerTrackId));
         this.setResult(Activity.RESULT_OK, intent);
      }
   }

   @Override
   public void pause()
   {
      Intent intent = new Intent();
      mLoggerServiceManager.pauseGPSLogging();
      this.setResult(Activity.RESULT_OK, intent);
   }

   @Override
   public void resume()
   {
      Intent intent = new Intent();
      mLoggerServiceManager.resumeGPSLogging();
      this.setResult(Activity.RESULT_OK, intent);
   }

   @Override
   public void stop()
   {
      Intent intent = new Intent();
      mLoggerServiceManager.stopGPSLogging();
      this.setResult(Activity.RESULT_OK, intent);
   }

   @Override
   public void dismiss()
   {
      finish();
   }

}
