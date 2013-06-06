package tim.draksler;

import android.content.DialogInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.ToggleButton;

public class Metronome extends Activity {

	boolean mTece = false;
	Button mZacetekKonecGumb;
	PowerManager.WakeLock mWakeLock;
	SeekBar mTrak;
	TextView mTakt;
	TickPlayer tp;
	TextView VrednostTempa;
	Button mTaktGumbi[];
	Button mPlus;
	Button mMinus;
	String naslov;
	private TextView m_TempoIme;
	Button next;
	RadioButton mTaktRadio[];
	ToggleButton m_btnStartStop;
	private final static int MIN_TEMPO = 30;
	private final static int MAX_TEMPO = 260;
	private final static int[] s_arBpm = new int[] { 1, 40, 60, 66, 76, 108,
			120, 168, 200 };
	private final static String[] hitrostiMetronomaLabela = new String[] {
			"Larghissimo", "Largo", "Larghetto", "Adagio", "Andante",
			"Moderato", "Allegro", "Presto", "Prestissimo" };

	private static final int osnovni_tempo = 75;
	private static final int osnovni_takt = 4;
	private int mTempo = osnovni_tempo;
	private int mPerioda = osnovni_takt;
	
	private static final int steviloPeriodvTaktu = 4;
	private static final int najvecjaHitrost = 260;
	private static final String KLJUCNI_TEMPO = "METRONOME_TEMPO";
	private static final String KLJUCNA_PERIODA = "METRONOME_PERIOD";


	public void kontaktnastranOdpri() {// kontaktna stran
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.facebook.com/tim.draksler"));
		startActivity(browserIntent);

	}

	public void poklici() {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:040750420"));
			startActivity(callIntent);
		} catch (ActivityNotFoundException e) {
			Log.e("Poklici me", "Klic ni uspel prosimo poskusite �e enkrat",
					e);
		}

	}

	public void dodajPesem() {
		Intent myIntent = new Intent(getBaseContext(), druga.class);
		startActivity(myIntent);
		
		onStop();
		setTitle(druga.name);
		
		
	
	}
	
	

	public void prikaziOprogramu() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Nekaj o programu...");
		alertDialog
				.setMessage("Metronom je merilna naprava, s katero se natan�no dolo�i hitrost izvajanega glasbenega dela. Za dodatna vpra�anja me lahko pokli�ete");
		alertDialog.setButton("Zapri", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.setIcon(R.drawable.metronomeicona);
		alertDialog.show();
	}

	private static final String PREFS = "metronome.prefs";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.meni, menu);
		return true;
	}

	private void NastavljanjePeriod() {

		mTaktRadio = new RadioButton[steviloPeriodvTaktu];
		mTaktRadio[0] = (RadioButton) findViewById(R.id.radioButton1);
		mTaktRadio[1] = (RadioButton) findViewById(R.id.radioButton2);
		mTaktRadio[2] = (RadioButton) findViewById(R.id.radioButton3);
		mTaktRadio[3] = (RadioButton) findViewById(R.id.radioButton4);
		mTaktRadio[3].setChecked(true);
		for (int i = 0; i < steviloPeriodvTaktu; i++) {
			mTaktRadio[i].setOnClickListener(new RadioButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mTaktRadio[0].isChecked() == true)

					{
						mPerioda = 1;
					}

					if (mTaktRadio[1].isChecked() == true)

					{
						mPerioda = 2;
					}

					if (mTaktRadio[2].isChecked() == true)

					{
						mPerioda = 3;
					}

					if (mTaktRadio[3].isChecked() == true)

					{
						mPerioda = 4;
					}

					restart();

				}

			});

		}

	}

	public void restart() {

		mTrak.setProgress(mTempo);
		VrednostTempa.setText("" + mTempo);
		mTakt.setText("   " + mPerioda + "/4" + "  ");
		mMinus.setClickable(mTempo > 1);
		mPlus.setClickable(mTempo < 200);
		
		
		if (mTece) {
			tp.onStop();
			tp.onStart(mPerioda, mTempo);
			
		}
		

	}

	protected void onStop() {

		super.onStop();

		SharedPreferences settings = getSharedPreferences(PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(KLJUCNA_PERIODA, mPerioda);
		editor.putInt(KLJUCNI_TEMPO, mTempo);
		
		
		editor.commit();
	}

	// prvi� kli�emo tu
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle(druga.name);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"MetronomeLock");
		tp = new TickPlayer(this);
		m_btnStartStop = (ToggleButton) findViewById(R.id.startStop);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// �telanje
												// glasnosti
		// predavanja
		mTrak = (SeekBar) findViewById(R.id.tempo);
		mTrak.setMax(najvecjaHitrost + 1 - 60);

		VrednostTempa = (TextView) findViewById(R.id.text);
		mMinus = (Button) findViewById(R.id.minus);
		mPlus = (Button) findViewById(R.id.plus);
		mTakt = (TextView) findViewById(R.id.period);
		m_TempoIme = (TextView) findViewById(R.id.tempoview_tvTempoName);
		SharedPreferences settings = getSharedPreferences(PREFS, 0);
		mPerioda = settings.getInt(KLJUCNA_PERIODA, osnovni_takt);
		mTempo = settings.getInt(KLJUCNI_TEMPO, osnovni_tempo);

		NastavljanjePeriod();

		mMinus.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mTempo > 1)
					--mTempo;

				restart();

			}

		});
		mPlus.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mTempo < najvecjaHitrost)
					++mTempo;
				restart();

			}

		});

		mTrak.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromTouch) {
				mTempo = progress;
				VrednostTempa.setText("" + mTempo);

				String lastMatching = hitrostiMetronomaLabela[0];
				try {

					if (mTempo >= MIN_TEMPO && mTempo <= MAX_TEMPO) {
						for (int i = s_arBpm.length - 1; i >= 0; i--) {
							if (s_arBpm[i] <= mTempo) {
								lastMatching = hitrostiMetronomaLabela[i];
								break;
							}
						}
						m_TempoIme.setText(lastMatching);

					}

				} catch (NumberFormatException e) {

				}

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (mTempo < 1) {
					tp.onStop();
				} else {
					mTempo = seekBar.getProgress();
					restart();
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

		});

		m_btnStartStop.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				if (mTempo < 1) {
					tp.onStop();
				} else {
					changeState();
				}

			}
		});

		restart();
	}

	protected void changeState() {// ko gre spat telefon
		getApplicationContext().getResources().getString(R.string.app_name);
		mTece = !mTece;
		if (mTece) {
			mWakeLock.acquire();

			m_btnStartStop.setText(R.string.stop);
			tp.onStart(mPerioda, mTempo);
		} else {
			mWakeLock.release();
			tp.onStop();

			m_btnStartStop.setText(R.string.start);
		}

	}

	protected void onDestroy() {

		if (mTece) {
			changeState();
		}
		tp.onDestroy();

		super.onDestroy();

	}

	@Override
	protected void onResume() {
		SharedPreferences settings = getSharedPreferences(PREFS, 0);
		mPerioda = settings.getInt(KLJUCNA_PERIODA, osnovni_takt);
		mTempo = settings.getInt(KLJUCNI_TEMPO, osnovni_tempo);
		setTitle(druga.name);//se nastavi nov naslov
		if (mPerioda == 1)
		{mTaktRadio[0].setChecked(true);}
		if (mPerioda == 2)
		{mTaktRadio[1].setChecked(true);}
		if (mPerioda == 3)
		{mTaktRadio[2].setChecked(true);}
		if (mPerioda == 4)
		{mTaktRadio[3].setChecked(true);}
	

		
		
		String lastMatching = hitrostiMetronomaLabela[0];
		try {

			if (mTempo >= MIN_TEMPO && mTempo <= MAX_TEMPO) {
				for (int i = s_arBpm.length - 1; i >= 0; i--) {
					if (s_arBpm[i] <= mTempo) {
						lastMatching = hitrostiMetronomaLabela[i];
						break;
					}
				}
				m_TempoIme.setText(lastMatching);

			}

		} catch (NumberFormatException e) {

		}

		mTrak.setProgress(mTempo);
		VrednostTempa.setText("" + mTempo);
		mTakt.setText("   " + mPerioda + "/4" + "  ");
		mMinus.setClickable(mTempo > 1);
		mPlus.setClickable(mTempo < 200);

		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item2: // kontaktna stran
			kontaktnastranOdpri();
			break;
		case R.id.item3: // telefonska
			poklici();
			break;
		case R.id.item4:// opis programa
			prikaziOprogramu();
			break;
		case R.id.item5:
			dodajPesem();
			break;

		}
		return true;
	}

}
