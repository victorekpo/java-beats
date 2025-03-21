package com.example.demo;

import be.tarsos.dsp.*;
import be.tarsos.dsp.io.jvm.*;
import be.tarsos.dsp.resample.*;
import be.tarsos.dsp.pitch.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class JavaBeatMaker4 {

    private static final String KICK_PATH = "07_Kick_03_SP.wav";
    private static final String SNARE_PATH = "07_Snare_01_SP.wav";
    private static final String HAT_PATH = "07_Hats_14_SP.wav";

    public static void main(String[] args) throws Exception {
        // Define pattern and instrument configurations
        String[] pattern = {"kick", "hihat", "snare", "hihat", "kick", "hihat", "snare", "hihat"};

        // Define configurations for each instrument
        InstrumentConfig kickConfig = new InstrumentConfig("kick", 1.0, null, null, null);
        InstrumentConfig snareConfig = new InstrumentConfig("snare", 1.1, null, null, null);
        InstrumentConfig hihatConfig = new InstrumentConfig("hihat", 0.9, 500L, 2000L, 1500L);  // Example trimming

        // Play the pattern with appropriate instrument configurations
        for (String sound : pattern) {
            switch (sound) {
                case "kick":
                    playSoundWithEffects(KICK_PATH, kickConfig);
                    break;
                case "snare":
                    playSoundWithEffects(SNARE_PATH, snareConfig);
                    break;
                case "hihat":
                    playSoundWithEffects(HAT_PATH, hihatConfig);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + sound);
            }
        }
    }

    private static void playSoundWithEffects(String path, InstrumentConfig config) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File file = new File(path);
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(file, 2048, 1024);

        // Apply tempo and pitch shift effects
        dispatcher.addAudioProcessor(new RateTransposer(config.tempo)); // Tempo change
        dispatcher.addAudioProcessor(new PitchShifter(1.5, 44100, 1024, 4)); // Optionally pitch shift

        // Play sound
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine sourceLine = AudioSystem.getSourceDataLine(format);
        AudioFormat audioFormat = sourceLine.getFormat();
        dispatcher.addAudioProcessor(new AudioPlayer(audioFormat)); // Play sound
        dispatcher.run();
    }

    // Instrument configuration class that holds the instrument's settings
    static class InstrumentConfig {
        String name;
        double tempo;
        Long trimStart;
        Long trimEnd;
        Long trimDuration;

        public InstrumentConfig(String name, double tempo, Long trimStart, Long trimEnd, Long trimDuration) {
            this.name = name;
            this.tempo = tempo;
            this.trimStart = trimStart;
            this.trimEnd = trimEnd;
            this.trimDuration = trimDuration;
        }
    }
}