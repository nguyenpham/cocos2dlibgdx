package org.cclgdx.sound;

import java.util.Map.Entry;

import org.cclgdx.utils.CacheMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class CCSoundEngine {
	public static class SoundPack implements Disposable {
		public Sound sound;
		public long soundId;

		public SoundPack(Sound sound) {
			this.sound = sound;
			soundId = -1;
		}

		public void play(float volume, boolean loop) {
			if (sound != null) {
				if (loop) {
					soundId = sound.loop(volume);
				} else {
					soundId = sound.play(volume);
				}
			}
		}

		public void stop() {
			if (sound != null) {
				sound.stop();
			}
		}

		public void setVolume(float soundsVolume) {
			if (sound != null && soundId >= 0) {
				sound.setVolume(soundId, soundsVolume);
			}
		}

		public void pause() {
			if (sound != null && soundId >= 0) {
				sound.pause();
				;
			}
		}

		public void resumeSound() {
			if (sound != null && soundId >= 0) {
				sound.resume();
			}
		}

		@Override
		public void dispose() {
			if (sound != null) {
				sound.dispose();
			}
		}
	}

	private class SoundCache extends CacheMap<SoundPack> {
		@Override
		public SoundPack create(String soundFile) {
			Sound sound = Gdx.audio.newSound(Gdx.files.internal(soundFile));
			return sound != null ? new SoundPack(sound) : null;
		}

		public void setSoundVolume(float volume) {
			for (Entry<String, SoundPack> each : getCacheMap().entrySet()) {
				SoundPack soundRec = each.getValue();
				if (soundRec != null) {
					soundRec.setVolume(volume);
				}
			}
		}
	}

	// effects are sounds that less than 5 seconds, better in 3 seconds
	private final SoundCache effectsMap = new SoundCache();
	// sounds are background sounds, usually longer than 5 seconds
	private final SoundCache soundsMap = new SoundCache();

	private SoundPack lastSoundRec = null;
	private float effectsVolume = 1.0f;
	private float soundsVolume = 1.0f;
	private boolean soundMute = false;
	private boolean effectMute = false;

	private static CCSoundEngine _sharedEngine = null;

	public static CCSoundEngine sharedEngine() {
		synchronized (CCSoundEngine.class) {
			if (_sharedEngine == null) {
				_sharedEngine = new CCSoundEngine();
			}
		}
		return _sharedEngine;
	}

	public static void purgeSharedEngine() {
		synchronized (CCSoundEngine.class) {
			_sharedEngine = null;
		}
	}

	public void setEffectsVolume(float volume) {
		effectsVolume = volume;
	}

	public float getEffectsVolume() {
		return effectsVolume;
	}

	public void setSoundVolume(float volume) {
		soundsVolume = volume;
		if (!soundMute) {
			soundsMap.setSoundVolume(volume);
		}
	}

	public float getSoundsVolume() {
		return soundsVolume;
	}

	public void setMute(boolean mute) {
		setSoundMute(mute);
		setEffectdMute(mute);
	}

	private void setEffectdMute(boolean mute) {
		effectMute = mute;
	}

	private void setSoundMute(boolean mute) {
		soundMute = mute;
		float volume = mute ? 0 : soundsVolume;
		soundsMap.setSoundVolume(volume);
	}

	public void mute() {
		soundMute = true;
		effectMute = true;
		setSoundMute(soundMute);
	}

	public void unmute() {
		soundMute = false;
		effectMute = false;
		setSoundMute(soundMute);
	}

	public boolean isMute() {
		return soundMute && effectMute;
	}

	public SoundPack preloadEffect(String soundFile) {
		return preloadSound(effectsMap, soundFile);
	}

	public void playEffect(String soundFile) {
		if (!effectMute) {
			playSound(effectsMap, soundFile, false);
		}
	}

	public void stopEffect(String soundFile) {
		stop(effectsMap, soundFile);
	}

	public SoundPack preloadSound(String soundFile) {
		return preloadSound(soundsMap, soundFile);
	}

	public SoundPack preloadSound(SoundCache soundCache, String soundFile) {
		SoundPack soundRec = null;
		synchronized (soundCache) {
			soundRec = soundCache.get(soundFile);
		}
		return soundRec;
	}

	public void playSound(String soundFile, boolean loop) {
		if (lastSoundRec != null) {
			lastSoundRec.stop();
		}

		if (!soundMute) {
			lastSoundRec = playSound(soundsMap, soundFile, loop);
		}
	}

	private SoundPack playSound(SoundCache soundCache, String soundFile, boolean loop) {
		SoundPack rec = preloadSound(soundCache, soundFile);
		rec.play(soundsVolume, loop);
		return rec;
	}

	private void stop(SoundCache soundCache, String soundFile) {
		if (soundCache.containsKey(soundFile)) {
			SoundPack rec = soundCache.get(soundFile);
			rec.stop();
		}
	}

	public void pauseSound() {
		if (lastSoundRec != null) {
			lastSoundRec.pause();
		}

	}

	public void resumeSound() {
		if (lastSoundRec != null) {
			lastSoundRec.resumeSound();
		}

	}

	public void stopSound(String soundFile) {
		stop(soundsMap, soundFile);
	}

	public void disposeSound(String soundFile) {
		synchronized (soundsMap) {
			soundsMap.remove(soundFile);
		}
	}

	public void disposeEffect(String soundFile) {
		synchronized (effectsMap) {
			effectsMap.remove(soundFile);
		}
	}

	public void disposeAllSounds() {
		synchronized (soundsMap) {
			soundsMap.clear();
		}
	}

	public void disposeAllEffects() {
		synchronized (effectsMap) {
			effectsMap.clear();
		}
	}

	public void stopAllSounds() {
		for (Entry<String, SoundPack> each : soundsMap.getCacheMap().entrySet()) {
			SoundPack soundRec = each.getValue();
			if (soundRec != null) {
				soundRec.stop();
			}
		}
	}
}
