package eu.gwif.droidtorchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import info.guardianproject.onionkit.ui.OrbotHelper;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int TORCHAT_PORT = 11109;
	
	private OrbotHelper orbotHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		orbotHelper = new OrbotHelper(getBaseContext());
		new Thread(new SocketListener()).start();
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
    
    public void onRegister(View view) {
    	orbotHelper.requestHiddenServiceOnPort(this, TORCHAT_PORT);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == OrbotHelper.HS_REQUEST_CODE) {
	    	TextView text = (TextView)findViewById(R.id.txtRegister);
			if (resultCode != RESULT_OK) {
				text.setText("Failed");
				return;
			}
			
			String host = data.getExtras().getString("hs_host");
			text.setText(host);
		}
	}
	
	private void showLine(final String line) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
		    	TextView text = (TextView)findViewById(R.id.txtLine);
		    	text.setText(line);
			}
		});		
	}
	
	private class SocketListener implements Runnable {
		@Override
		public void run() {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(TORCHAT_PORT);
				while (true) {
					Socket socket = serverSocket.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String line;
					while (null != (line = in.readLine())) {
						showLine(line);
					}
				}
			} catch (IOException e) {
				// Kaputt
				showLine(e.getMessage());
			} finally {
				try {
					if (serverSocket!=null) {
						serverSocket.close();
					}
				} catch (IOException e) {
					// well...
				}
			}
		}
		
	}
}
