package com.example.gipsy;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener, OnClickListener{

	//filds 
	private LocationManager locationManager;
	TextView tvSzAktual, tvDlgAktual, tvStaraSz, tvStaraDlg;
	Button btn1;
	String szAktual, dlgAktual, szStara, dlgStara;
	Location lct;
	WakeLock wakeLock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		tvSzAktual = (TextView)findViewById(R.id.textView3);
		tvDlgAktual = (TextView)findViewById(R.id.textView44);
		tvStaraSz = (TextView)findViewById(R.id.textView7);
		tvStaraDlg = (TextView)findViewById(R.id.textView9);
		
		btn1 = (Button)findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                6000, 1, this);
		lct = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
	}//end of onCreate

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		double szerokosc = location.getLatitude();
		double dlugosc = location.getLongitude();
		
		szAktual = String.valueOf(szerokosc);
		tvSzAktual.setText(" "+szAktual);
		
		dlgAktual = String.valueOf(dlugosc); 
		tvDlgAktual.setText(" "+dlgAktual);
		
	}
	
	public void ustaw(Location loc, TextView tekst1, TextView tekst2){
		if(loc!=null){
		double dl = loc.getLongitude();
		String dlg = String.valueOf(dl);
		double sz = loc.getLatitude();
		String szr = String.valueOf(sz);
		tekst1.setText(" "+szr);
		tekst2.setText(" "+dlg);
		}else{
			Toast.makeText(getBaseContext(), "Dupa - nie ma ostatniej pozycji", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//String string = "Latitude: "+Location.getLatitude()+"Longitude: "+location.getLongitude();
		ustaw(lct, tvStaraSz, tvStaraDlg);
	}
	
	/* Request updates at startup */
	  @Override
	  protected void onResume() {
		  wakeLock.acquire();
	    super.onResume();
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 5, this);
	  }

	  /* Remove the locationlistener updates when Activity is paused */
	  @Override
	  protected void onPause() {
		  wakeLock.release();
	    super.onPause();
	    locationManager.removeUpdates(this);
	  }
	  

}
