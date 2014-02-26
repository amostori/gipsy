package pl.waw.bitmed.gipsy;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends Activity implements LocationListener, OnClickListener{

	//filds 
	private LocationManager locationManager;
	TextView tvSzAktual, tvDlgAktual;
	Button btn1;
	String szAktual, dlgAktual, szStara, dlgStara;
	Location lct;
	WakeLock wakeLock;
	private GoogleMap map;
	private LatLng pozycja;
	static final LatLng Wroclaw = new LatLng(51.110, 17.030);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		tvSzAktual = (TextView)findViewById(R.id.textView3);
		tvDlgAktual = (TextView)findViewById(R.id.textView44);
		
		
		btn1 = (Button)findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                6000, 1, this);
		Toast.makeText(getBaseContext(), "Trwa namierzanie", Toast.LENGTH_LONG).show();
		Toast.makeText(getBaseContext(), "To mo¿e potrwaæ kilkanaœcie minut", Toast.LENGTH_LONG).show();
		
	}//end of onCreate

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		double szerokosc = location.getLatitude();
		double dlugosc = location.getLongitude();
		
		//skrócenie liczby do 6 miejsc po przecinku
		double szerokosc6 = roundMe(szerokosc, 6);
		double dlugosc6 = roundMe(dlugosc, 6);
		
		pozycja = new LatLng(szerokosc, dlugosc);
		
		szAktual = String.valueOf(szerokosc6);
		tvSzAktual.setText(" "+szAktual);
		
		dlgAktual = String.valueOf(dlugosc6); 
		tvDlgAktual.setText(" "+dlgAktual);
		
	}
	
	
	@Override
	public void onProviderDisabled(String arg0) {
		
		///////////////////////////////
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
		startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
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
		if(pozycja != null){
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(pozycja, 15));
			map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
			
			Marker mrk = map.addMarker(new MarkerOptions().position(pozycja).title("Tu jestem").snippet("Snipeyt").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			
		}else{
			Toast.makeText(getBaseContext(), "Brak pomiaru - poczekaj jeszcze", Toast.LENGTH_LONG).show();
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Wroclaw, 16));
			map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
			
			Marker mrk = map.addMarker(new MarkerOptions().position(Wroclaw).title("Tu jestem").snippet("Snipeyt").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			
		}
		
		
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
	  
	  public double roundMe(double val, int places){
			BigDecimal bd = new BigDecimal(val);
			bd =  bd.setScale(places, BigDecimal.ROUND_HALF_UP);
			return bd.doubleValue();
		}
	  

}
