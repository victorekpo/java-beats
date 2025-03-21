package com.example.demo;

import be.tarsos.dsp.*;
import be.tarsos.dsp.io.jvm.*;
import be.tarsos.dsp.resample.*;
import be.tarsos.dsp.pitch.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class JavaBeatMaker2 {

    private static final String KICK_PATH = "07_Kick_03_SP.wav";
    private static final String SNARE_PATH = "07_Snare_01_SP.wav";
    private static final String HAT_PATH = "07_Hats_14_SP.wav";

    public static void main(String[] args) throws Exception {
        String[] pattern = {"kick", "hihat", "snare", "hihat", "kick", "hihat", "snare", "hihat"};
        playPattern(pattern);
    }

    private static void playPattern(String[] pattern) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        for (String sound : pattern) {
            String path = switch (sound) {
                case "kick" -> KICK_PATH;
                case "snare" -> SNARE_PATH;
                case "hihat" -> HAT_PATH;
                default -> throw new IllegalStateException("Unexpected value: " + sound);
            };
            playSoundWithEffects(path);
        }
    }

    private static void playSoundWithEffects(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File file = new File(path);
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(file, 2048, 1024);

        // Apply Effects
        dispatcher.addAudioProcessor(new RateTransposer(1.2)); // Speed up by 20%
        dispatcher.addAudioProcessor(new PitchShifter(1.5, 44100, 1024, 4)); // Pitch shift up

        // Play sound
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine sourceLine = AudioSystem.getSourceDataLine(format);
        AudioFormat audioFormat = sourceLine.getFormat(); // Get the AudioFormat from the SourceDataLine
        dispatcher.addAudioProcessor(new AudioPlayer(audioFormat)); // Pass the AudioFormat to the AudioPlayer constructor
        dispatcher.run();
    }
}
