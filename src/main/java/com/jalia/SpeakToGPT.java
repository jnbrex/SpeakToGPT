package com.jalia;

public class SpeakToGPT {
    private final SpeechRecognitionWrapper speechRecognitionWrapper;
    private final OpenAIWrapper openAIWrapper;

    public SpeakToGPT() {
        speechRecognitionWrapper = new SpeechRecognitionWrapper();
        openAIWrapper = new OpenAIWrapper();
    }

    public String speechToText() {
        try {
            return speechRecognitionWrapper.speechToText();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public void textToSpeech(String text) {
        try {
            speechRecognitionWrapper.textToSpeech(text);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public String sendMessageToGPT(String message) {
        try {
            return openAIWrapper.SendMessage(message);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public void close() {
        speechRecognitionWrapper.close();
    }
}
