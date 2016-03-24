package com.nevernotconfused.coryatwater.listviewlauncher;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coryatwater on 3/17/16.
 */
public class AppListActivity extends AppCompatActivity {
    public static final String TAG = "AppListActivity: ";
    private PackageManager manager;
    private List<AppDetails> apps;
    private List<ImageView> appImages;
    private ScreenOffReceiver sor;

    private GridView list;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sor = new ScreenOffReceiver();
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(sor,filter);


        setContentView(R.layout.activity_app_grid);

        //IconBitmapProcessor icbm = new IconBitmapProcessor(this.getApplicationContext(),getPackageManager(),getResources());
        //icbm.execute();
        loadApps();
        loadListView();
        addClickListener();
    }
    private void loadApps(){
        manager = getPackageManager();
        apps = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){
            AppDetails app = new AppDetails();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = iosifyIcons(ri);
            apps.add(app);
        }
    }
    private void loadListView(){
        list = (GridView)findViewById(R.id.apps_list);

        ArrayAdapter<AppDetails> adapter = new ArrayAdapter<AppDetails>(this,
                R.layout.list_item,
                apps) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ImageView appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).icon);

                String appLabel;
                appLabel = (String)apps.get(position).label;

                String appName;
                appName = (String)apps.get(position).name;

                return convertView;
            }
        };

        list.setAdapter(adapter);
    }
    private void addClickListener(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {


                Intent i = manager.getLaunchIntentForPackage(apps.get(pos).name.toString());
                AppListActivity.this.startActivity(i);
            }
        });
    }

    private Drawable iosifyIcons(ResolveInfo ri){
        Drawable d = ri.activityInfo.loadIcon(manager);

        BitmapDrawable drawableBitmap = (BitmapDrawable)d;
        Bitmap iconBitmap = drawableBitmap.getBitmap();

        iconBitmap = iconBitmap.createScaledBitmap(iconBitmap,140,140,false);

        Bitmap.Config conf = Bitmap.Config.ARGB_4444;
        Bitmap mutableBitmap = Bitmap.createBitmap(200, 200,conf);
        Canvas canvas = new Canvas(mutableBitmap);

        RectF sharpRectangle = new RectF(0,0,190,190);

            //find average color to use as the background
            //build array of pixels
            int[] pix = new int[iconBitmap.getHeight() * iconBitmap.getWidth()];
            iconBitmap.getPixels(pix,0,iconBitmap.getWidth(),0,0,iconBitmap.getWidth(),iconBitmap.getHeight());

            long red = 1;
            long blue = 1;
            long green = 1;
            for(int i = 0; i < pix.length; i++){
                //if(Color.red(pix[i]) != Color.blue(pix[i]) && Color.green(pix[i]) != Color.blue(pix[i])){
                    red += Color.red(pix[i]);
                    blue += Color.blue(pix[i]);
                    green += Color.green(pix[i]);
                //}
            }
            int red2 = (int)red/(pix.length)+70;
            int blue2 = (int)blue/(pix.length)+70;
            int green2 = (int)green/(pix.length)+70;

        Paint bgColorPaint = new Paint();
        bgColorPaint.setColor(Color.rgb(red2,green2,blue2));

        canvas.drawRoundRect(sharpRectangle,40,40,bgColorPaint);
        canvas.drawBitmap(iconBitmap,25,24,null);
        BitmapDrawable finalIcon = new BitmapDrawable(getResources(),mutableBitmap);

        return finalIcon;
    }

    @Override
    public void onStop(){
        super.onStop();
        sor.onReceive(this,);
    }

    @Override
    public void onRestart(){
        super.onRestart();
    }
}
