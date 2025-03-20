package com.example.demo;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class JavaBeatMaker {
    // File paths for sound samples
    private static final String KICK_PATH = "07_Kick_03_SP.wav";
    private static final String SNARE_PATH = "07_Snare_01_SP.wav";
    private static final String HAT_PATH = "07_Hats_14_SP.wav";

    public static void main(String[] args) throws Exception {
        // Create a basic pattern for the drum loop
        String[] pattern = {
                "kick", "hihat", "snare", "hihat",
                "kick", "hihat", "snare", "hihat",
                "kick", "hihat", "snare", "hihat"
        };

        // Play the beat
        playPattern(pattern);
    }

    /**
     * Play an audio sample from a file.
     */
    private static void playSound(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(path);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        line.open(format);
        line.start();

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
            line.write(buffer, 0, bytesRead);
        }

        line.drain();
        line.close();
        audioInputStream.close();
    }

    /**
     * Play the pattern by looping through and triggering sound effects.
     */
    private static void playPattern(String[] pattern) throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        for (String sound : pattern) {
            switch (sound) {
                case "kick":
                    playSound(KICK_PATH);
                    break;
                case "snare":
                    playSound(SNARE_PATH);
                    break;
                case "hihat":
                    playSound(HAT_PATH);
                    break;
            }
            TimeUnit.MILLISECONDS.sleep(500); // 500ms between sounds
        }
    }
}
