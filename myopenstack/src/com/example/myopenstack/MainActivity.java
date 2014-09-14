package com.example.myopenstack;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	public static List<ServerModel> servers;

	private void drawTableHeaders() {
		TableLayout tl = (TableLayout) findViewById(R.id.tableLayout1);
		tl.setBackgroundColor(Color.LTGRAY);
		TableRow tr1 = new TableRow(tl.getContext());
		tr1.setBackgroundColor(Color.BLUE);

		TextView textview = new TextView(tl.getContext());
		textview.setText("Name");
		textview.setTextColor(Color.BLACK);
		tr1.addView(textview);

		textview = new TextView(tl.getContext());
		textview.setText("Status");
		textview.setTextColor(Color.BLACK);
		tr1.addView(textview);

		textview = new TextView(tl.getContext());
		textview.setText("Action");
		textview.setTextColor(Color.BLACK);
		tr1.addView(textview);

		tl.addView(tr1);
	}

	private void handleButtons() {
		Button imagesButton = (Button) findViewById(R.id.ButtonImages);
		imagesButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Button b = (Button)findViewById(R.id.ButtonImages);
				Intent myIntent=new Intent(b.getContext(), ImageActivity.class);
		        startActivityForResult(myIntent,0);
			}
		});

		Button refreshButton = (Button) findViewById(R.id.ButtonRefresh);
		refreshButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new PopulateTableTask().execute("");
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawTableHeaders();
		new PopulateTableTask().execute("");
		handleButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class OpenStackTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			OSClient osc = OSEndpoint.get();
			if (params[0].equalsIgnoreCase("stop")) {
				int index = Integer.parseInt(params[1]);
				osc.stopServer(MainActivity.servers.get(index));
			}
			return "";
		}
	}

	private class PopulateTableTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			OSClient osc = OSEndpoint.get();
			servers = osc.getServerList();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			TableLayout tl = (TableLayout) findViewById(R.id.tableLayout1);
			tl.removeAllViewsInLayout();
			for (int i = 0; i < servers.size(); i++) {
				ServerModel sm = servers.get(i);
				// -----------------
				TableRow tr1 = new TableRow(tl.getContext());
				tr1.setBackgroundColor(Color.WHITE);

				TextView textview = new TextView(tl.getContext());
				textview.setText(sm.getName());
				textview.setTextColor(Color.BLACK);
				tr1.addView(textview);

				textview = new TextView(tl.getContext());
				textview.setText(sm.getStatus());
				textview.setTextColor(Color.BLACK);
				tr1.addView(textview);

				if (sm.isStoppable()) {
					Button b = new Button(tl.getContext());
					b.setText("Stop");
					b.setId(i);
					b.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							new OpenStackTask().execute("stop",
									Integer.toString(v.getId()));
						}
					});
					tr1.addView(b);
				} else {
					textview = new TextView(tl.getContext());
					textview.setText("n/a");
					textview.setTextColor(Color.BLACK);
					tr1.addView(textview);
				}

				tl.addView(tr1);
			}
		}

	}
}
