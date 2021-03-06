package com.brindyblitz.artemis.utils;

import com.brindyblitz.artemis.engconsole.ui.damcon.Internal;
import com.brindyblitz.artemis.engconsole.ui.damcon.InternalTeam;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class AudioManager {
    private HashMap<String, File> soundBank = new HashMap<>();
    private SoundQueue soundQueue;

    public AudioManager(String path) {
        loadAssetsInDirectory(path, "");

        soundQueue = new SoundQueue();
        new Thread(soundQueue).start();
    }

    private void loadAssetsInDirectory(String path, String prefix) {
        for (File f : new File(path).listFiles()) {
            if (f.isDirectory()) {
                loadAssetsInDirectory(f.getPath(), new File(prefix, f.getName()).getPath());
            } else {
                String name = new File(prefix, f.getName()).getPath().substring(1);
                soundBank.put(name, f);

                if (path.endsWith("/voice/on_order")) {
                    InternalTeam.ON_ORDER_RESPONSES.put(new Integer(InternalTeam.ON_ORDER_RESPONSES.size()), name);
                }
            }
        }
    }

    private File lookupSound(String name) {
        File sound = soundBank.get(name);
        if (sound == null) {
            throw new RuntimeException("Unable to locate sound effect '" + name + "'");
        }
        return sound;
    }

    public void playSound(String name) {
        try
        {
            File sound = lookupSound(name);
            AudioInputStream ais = AudioSystem.getAudioInputStream(sound);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {  // LineUnavailableException, IOException, UnsupportedAudioFileException
            e.printStackTrace(System.err);
        }
    }

    public void queueSound(String name) {
        this.soundQueue.queueSound(name);
    }

    private class SoundQueue implements Runnable {
        private Queue<File> queue = new LinkedList<>();
        private Clip playing = null;

        public void queueSound(String name) {
            queue.add(lookupSound(name));
        }

        public void run() {
            while (true) {
                if (!queue.isEmpty()) {
                    if (playing == null || !playing.isActive()) {
                        File next = queue.poll();
                        try {
                            AudioInputStream ais = AudioSystem.getAudioInputStream(next);
                            playing = AudioSystem.getClip();
                            playing.open(ais);
                            playing.start();
                        } catch (Exception e) { // LineUnavailableException, IOException, UnsupportedAudioFileException
                            e.printStackTrace(System.err);
                            break;
                        }
                    }
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                    break;
                }
            }
        }
    }
}
