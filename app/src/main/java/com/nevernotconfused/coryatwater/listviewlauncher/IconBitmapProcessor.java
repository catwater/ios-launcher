package com.nevernotconfused.coryatwater.listviewlauncher;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coryatwater on 3/18/16.
 */
public class IconBitmapProcessor extends AsyncTask<Void,Void,Void> {
    public static final String TAG = "IconBitmapProcessor: ";
    ProgressDialog pd;
    PackageManager manager;
    ArrayList<AppDetails> apps;
    Context c;
    Resources resources;


    public IconBitmapProcessor(Context c,PackageManager manager,Resources resources){
        this.c = c;
        this.manager = manager;
        this.resources = resources;
        Log.w(TAG, "IconBitmapProcessor: did it get here?" );
    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.w(TAG, "doInBackground: HOW ABOUT HERE?" );
        loadApps();
        for(int i = 0; i < apps.size(); i++){
            Drawable d = apps.get(i).icon;
            BitmapDrawable drawableBitmap = (BitmapDrawable)d;
            Bitmap iconBitmap = drawableBitmap.getBitmap();

            Bitmap.Config conf = Bitmap.Config.RGB_565;
            Bitmap mutableBitmap = Bitmap.createBitmap(120, 120,conf);
            Canvas canvas = new Canvas(mutableBitmap);

            BitmapShader iconShader = new BitmapShader(iconBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            RectF sharpRectangle = new RectF(0,0,40,40);
            Paint paint = new Paint();
            //paint.setShader(iconShader);
            paint.setColor(Color.RED);
            canvas.drawRoundRect(sharpRectangle,0,0,paint);
            BitmapDrawable finalIcon = new BitmapDrawable(resources,mutableBitmap);

            Log.w(TAG, "doInBackground: AND HERE?" );
            apps.get(i).icon = finalIcon;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setMessage("Fetching and appling icons...");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pd.dismiss();
    }

    private void loadApps(){

        apps = new ArrayList<AppDetails>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){
            AppDetails app = new AppDetails();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;

            //edit and initialize the icon
            /*Drawable d = ri.activityInfo.loadIcon(manager);

            BitmapDrawable drawableBitmap = (BitmapDrawable)d;
            Bitmap iconBitmap = drawableBitmap.getBitmap();

            Bitmap.Config conf = Bitmap.Config.RGB_565;
            Bitmap mutableBitmap = Bitmap.createBitmap(120, 120,conf);
            Canvas canvas = new Canvas(mutableBitmap);

            BitmapShader iconShader = new BitmapShader(iconBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            RectF sharpRectangle = new RectF(0,0,40,40);
            Paint paint = new Paint();
            //paint.setShader(iconShader);
            paint.setColor(Color.RED);
            canvas.drawRoundRect(sharpRectangle,0,0,paint);
            BitmapDrawable finalIcon = new BitmapDrawable(resources,mutableBitmap);

            Log.w(TAG, "doInBackground: AND HERE?" );*/
            Drawable finalIcon = resources.getDrawable( R.drawable.anicon );
            //set app icon
            app.icon = finalIcon;



            apps.add(app);
        }
    }
}
