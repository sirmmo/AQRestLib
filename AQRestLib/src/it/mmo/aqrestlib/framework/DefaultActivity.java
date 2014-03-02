package it.mmo.aqrestlib.framework;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;

public class DefaultActivity extends Activity {

   public AQuery aq;



   protected DefaultActivity inner_this;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      inner_this = this;
      aq = new AQuery(this);

   }
  
   
}
