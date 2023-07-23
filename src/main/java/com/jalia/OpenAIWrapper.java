package com.jalia;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.ai.openai.models.CompletionsUsage;
import com.azure.core.credential.AzureKeyCredential;

import java.util.ArrayList;
import java.util.List;

public class OpenAIWrapper {
    private static final String MODEL_NAME = "SpeakToGPT";
    private static final String AZURE_OPENAI_KEY = System.getenv("AZURE_OPENAI_KEY");
    private static final String AZURE_OPENAI_ENDPOINT = System.getenv("AZURE_OPENAI_ENDPOINT");

    private final OpenAIClient openAIClient;

    public OpenAIWrapper() {
        openAIClient = new OpenAIClientBuilder()
            .endpoint(AZURE_OPENAI_ENDPOINT)
            .credential(new AzureKeyCredential(AZURE_OPENAI_KEY))
            .buildClient();
    }

    public String SendMessage(String message) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.USER,message));

        System.out.println("calling openAI");
        ChatCompletions chatCompletions =
            openAIClient.getChatCompletions(MODEL_NAME, new ChatCompletionsOptions(chatMessages));
        System.out.println("openAI call completed");

        if (chatCompletions.getChoices().size() != 1) {
            throw new RuntimeException("More than 1 chat completion choice returned.");
        }

        ChatChoice choice = chatCompletions.getChoices().get(0);
        ChatMessage chatMessage = choice.getMessage();
        System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), chatMessage.getRole());
        System.out.println("Chat message:");
        System.out.println(chatMessage.getContent());

        System.out.println();
        CompletionsUsage usage = chatCompletions.getUsage();
        System.out.printf("Usage: number of prompt token is %d, "
            + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
            usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());

        return chatMessage.getContent();
    }
}
