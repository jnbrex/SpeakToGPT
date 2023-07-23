package com.jalia;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SpeechRecognitionWrapper {
    private static final String speechKey = System.getenv("AZURE_SPEECH_KEY");
    private static final String speechRegion = System.getenv("AZURE_SPEECH_REGION");

    private final SpeechConfig speechConfig;
    private final  AudioConfig audioConfig;
    private final SpeechRecognizer speechRecognizer;
    private final SpeechSynthesizer speechSynthesizer;

    public SpeechRecognitionWrapper() {
        speechConfig = SpeechConfig.fromSubscription(speechKey, speechRegion);
        speechConfig.setSpeechRecognitionLanguage("en-US");
        speechConfig.setSpeechSynthesisVoiceName("en-US-BrandonNeural");

        audioConfig = AudioConfig.fromDefaultMicrophoneInput();

        speechRecognizer = new SpeechRecognizer(speechConfig, audioConfig);

        speechSynthesizer = new SpeechSynthesizer(speechConfig);
    }

    public String speechToText() throws InterruptedException, ExecutionException {
        System.out.println("Speak into your microphone.");
        Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
        SpeechRecognitionResult speechRecognitionResult = task.get();

        if (speechRecognitionResult.getReason() == ResultReason.RecognizedSpeech) {
            System.out.println("RECOGNIZED: Text=" + speechRecognitionResult.getText());

            return speechRecognitionResult.getText();
        }

        else if (speechRecognitionResult.getReason() == ResultReason.NoMatch) {
            System.out.println("NOMATCH: Speech could not be recognized.");
        }
        else if (speechRecognitionResult.getReason() == ResultReason.Canceled) {
            CancellationDetails cancellation = CancellationDetails.fromResult(speechRecognitionResult);
            System.out.println("CANCELED: Reason=" + cancellation.getReason());

            if (cancellation.getReason() == CancellationReason.Error) {
                System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                System.out.println("CANCELED: Did you set the speech resource key and region values?");
            }
        }

        throw new RuntimeException("Speech to Text failed.");
    }

    public void textToSpeech(String text) throws InterruptedException, ExecutionException {
        if (text.isEmpty())
        {
            return;
        }

        SpeechSynthesisResult speechSynthesisResult = speechSynthesizer.SpeakTextAsync(text).get();

        if (speechSynthesisResult.getReason() == ResultReason.SynthesizingAudioCompleted) {
            System.out.println("Speech synthesized to speaker for text [" + text + "]");
        }
        else if (speechSynthesisResult.getReason() == ResultReason.Canceled) {
            SpeechSynthesisCancellationDetails cancellation =
                SpeechSynthesisCancellationDetails.fromResult(speechSynthesisResult);
            System.out.println("CANCELED: Reason=" + cancellation.getReason());

            if (cancellation.getReason() == CancellationReason.Error) {
                System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                System.out.println("CANCELED: Did you set the speech resource key and region values?");
            }
        }
    }

    public void close() {
        speechRecognizer.close();
        speechSynthesizer.close();
    }
}
