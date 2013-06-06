package tim.draksler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;

import android.media.AudioManager;
import android.media.SoundPool;

public class TickPlayer extends Activity {
	private int m_BeatMerjenje;

	private ScheduledThreadPoolExecutor m_scheduledExecutor;
	boolean mTece = false;
	int mStetje = 0;
	int mPerioda = 1;

	private SoundPool soundPool;

	int tick = -1;
	int tock = -1;

	public TickPlayer(Context ctx) {

		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

		tick = soundPool.load(ctx, R.raw.room__perc_duma_hit2_11, 1);
		tock = soundPool.load(ctx, R.raw.room__perc_duma_hit2_5, 1);

	}

	public void onStart(int period, int ticksPerSec) {
		m_BeatMerjenje = period;
		mTece = true;

		final int mTrajanje = 60000 / ticksPerSec;
		if (!mTece)
			return;
		m_scheduledExecutor = new ScheduledThreadPoolExecutor(1);
		m_scheduledExecutor.scheduleAtFixedRate(new Runnable() {

			private long startVrh = System.currentTimeMillis();
			int zadniPredvajaniBeat = 0;

			@Override
			public void run() {

				long sedanjiVrh = System.currentTimeMillis();
				long TrajanjeCelotno = sedanjiVrh - startVrh;
				int steviloBeatovOdZacetka = (int) (TrajanjeCelotno / mTrajanje);

				if (steviloBeatovOdZacetka > zadniPredvajaniBeat) {
					int SteviloBeatovOdMerjenja = steviloBeatovOdZacetka
							% m_BeatMerjenje;

					if (SteviloBeatovOdMerjenja == 0) {

						soundPool.play(tick, 1, 2, 1, 0, 0);

					} else {
						soundPool.play(tock, 1, 1, 1, 0, 0);

					}
					zadniPredvajaniBeat = steviloBeatovOdZacetka;
				}
			}
		}, 0, mTrajanje < 300 ? 30 : 50, TimeUnit.MILLISECONDS);

	}

	public void onStop() {
		try {
			if (m_scheduledExecutor != null) {
				m_scheduledExecutor.shutdown();
				m_scheduledExecutor
						.awaitTermination(100, TimeUnit.MILLISECONDS);
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		mTece = false;

	}

	public void onDestroy() {
		onStop();

	}

}
