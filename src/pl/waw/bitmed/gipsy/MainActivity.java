package pl.waw.bitmed.gipsy;

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
	//static final LatLng Wroclaw = new LatLng(51.110, 17.030);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		//map.setMyLocationEnabled(true);
	//	map.moveCamera(CameraUpdateFactory.newLatLngZoom(Wroclaw, 15));
	//	map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		
		//pozycja = new LatLng(50.12, 19.45);
		
		tvSzAktual = (TextView)findViewById(R.id.textView3);
		tvDlgAktual = (TextView)findViewById(R.id.textView44);
		
		
		btn1 = (Button)findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                6000, 1, this);
		//lct = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
	}//end of onCreate

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		double szerokosc = location.getLatitude();
		double dlugosc = location.getLongitude();
		
		pozycja = new LatLng(szerokosc, dlugosc);
		//pozycja.latitude = szerokosc;
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
		if(pozycja != null){
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(pozycja, 16));
			map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
			
			Marker mrk = map.addMarker(new MarkerOptions().position(pozycja).title("Tu jestem").snippet("Snipeyt").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			
		}else{
			Toast.makeText(getBaseContext(), "Brak pomiaru - poczekaj jeszcze", Toast.LENGTH_LONG).show();
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
	  

}
