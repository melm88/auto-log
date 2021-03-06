package com.taramt.audiolevel;

import java.util.Date;

import com.taramt.utils.DBAdapter;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
/*
 * AudioLevelService computes the audio level of buffered audio samples. 
 * 
 */
public class AudioLevelService extends Service {
	private static final int sampleRate = 8000;
	private AudioRecord audio;
	private int bufferSize;
	private double lastLevel = 0;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		try {
			bufferSize = AudioRecord
					.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
							AudioFormat.ENCODING_PCM_16BIT);
		} catch (Exception e) {
			android.util.Log.e("TrackingFlow", "Exception", e);
		}
		try {
			audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
					AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize);
			audio.startRecording();
			//After this call we can get the last value assigned to the lastLevel variable
			readAudioBuffer();
			audio.stop();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return START_STICKY;
	}

	/**
	 * Functionality that gets the sound level out of the sample
	 */
	private void readAudioBuffer() {
		try {
			if(bufferSize==0)
				bufferSize = 16;
			
			short[] buffer = new short[bufferSize];
			int bufferReadResult = 1;
			if (audio != null) {
				// Sense the voice...
				bufferReadResult = audio.read(buffer, 0, bufferSize);
				double sumLevel = 0;
				for (int i = 0; i < bufferReadResult; i++) {
					Log.d("Audio Level", buffer[i]+" buffer");
					sumLevel += buffer[i];
				}
				lastLevel = Math.abs((sumLevel / bufferReadResult));
				Log.d("Audio Level", lastLevel+"");
				DBAdapter db = new DBAdapter(this);
				db.open();
				db.insertAudioLevelValue(lastLevel+"", new Date().toString());
				db.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}
}