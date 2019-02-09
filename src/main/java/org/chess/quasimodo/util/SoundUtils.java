package org.chess.quasimodo.util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

import org.springframework.stereotype.Component;

@Component ("soundUtils")
public class SoundUtils {
	private static String moveSoundFilepath = "./resources/sounds/move-fics.au";
	private AudioClip clip;
	
	
	public SoundUtils() throws MalformedURLException {
		clip = Applet.newAudioClip(new File(moveSoundFilepath).toURI().toURL());
	}
	
	public void playAudio() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					clip.play( );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
