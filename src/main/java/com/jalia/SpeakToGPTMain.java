package com.jalia;

public class SpeakToGPTMain {
    public static void main(String[] args) {
        SpeakToGPT speakToGPT = new SpeakToGPT();

        while (true) {
            String message = speakToGPT.speechToText();
            if (message.equalsIgnoreCase("quit") ||
                message.equalsIgnoreCase("quit.")) {
                break;
            }
            String response = speakToGPT.sendMessageToGPT(message);
            speakToGPT.textToSpeech(response);
        }

        speakToGPT.textToSpeech("Goodbye.");
        speakToGPT.close();
    }
}
