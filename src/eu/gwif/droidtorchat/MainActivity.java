package eu.gwif.droidtorchat;

import info.guardianproject.onionkit.ui.OrbotHelper;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private OrbotHelper orbotHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		orbotHelper = new OrbotHelper(getBaseContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
    public void onIsRunning(View hello) {
    	TextView text = (TextView)findViewById(R.id.txtIsRunning);
    	text.setText(String.format(
    			"Installed: %s, Running: %s",
    			orbotHelper.isOrbotInstalled(),
    			orbotHelper.isOrbotRunning()));
    	//Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
    }
}
