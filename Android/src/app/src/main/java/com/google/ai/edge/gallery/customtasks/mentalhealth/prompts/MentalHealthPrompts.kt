package com.google.ai.edge.gallery.customtasks.mentalhealth.prompts

/**
 * Centralized prompt template management for the Mental Health Companion.
 * All prompts include safety guidelines and response constraints.
 */
object MentalHealthPrompts {
    /**
     * System prompt used in all LLM calls to ensure safe, supportive responses.
     * Enforces empathy, brevity, grounding focus, and crisis handling.
     */
    const val SYSTEM_PROMPT = """You are a supportive well-being companion. Be empathetic, brief, and help with grounding and small actions. Do not diagnose or mention disorders. Do not give medication or treatment advice. If the user expresses intent to harm themselves or others: Return ONLY: "I'm really sorry you're feeling this way. You deserve support right now. Please reach out to someone you trust or contact local emergency services immediately." Keep responses under 120 words. Stop after this message."""

    /**
     * Generates a mood check-in prompt based on the user's selected mood and optional context.
     *
     * @param mood The user's selected mood (e.g., "Great", "Struggling")
     * @param context Optional additional context provided by the user
     * @return A formatted prompt for the LLM
     */
    fun getMoodCheckInPrompt(mood: String, context: String): String {
        return buildString {
            append("The user is checking in with their mood. They feel: $mood")
            if (context.isNotBlank()) {
                append("\n\nAdditional context: $context")
            }
            append("\n\nProvide a brief, empathetic reflection that validates their feelings and offers a small grounding suggestion or positive observation. Keep it under 120 words.")
        }
    }

    /**
     * Generates a thought reframing prompt using CBT techniques.
     *
     * @param thought The negative thought the user wants to reframe
     * @return A formatted prompt for the LLM
     */
    fun getThoughtReframingPrompt(thought: String): String {
        return buildString {
            append("The user is experiencing this thought: \"$thought\"\n\n")
            append("Help them reframe this thought using cognitive behavioral therapy principles. ")
            append("Offer a balanced perspective and 1-2 grounding steps they can take. ")
            append("Be gentle and validating. Keep it under 120 words.")
        }
    }

    /**
     * Generates a journal summary prompt.
     *
     * @param entry The user's journal entry
     * @return A formatted prompt for the LLM
     */
    fun getJournalSummaryPrompt(entry: String): String {
        return buildString {
            append("The user has written this journal entry:\n\n")
            append("\"$entry\"\n\n")
            append("Provide a brief, empathetic summary of the key themes and emotions expressed. ")
            append("Keep it under 120 words.")
        }
    }

    /**
     * Generates a journal suggestion prompt.
     *
     * @param entry The user's journal entry
     * @return A formatted prompt for the LLM
     */
    fun getJournalSuggestionPrompt(entry: String): String {
        return buildString {
            append("The user has written this journal entry:\n\n")
            append("\"$entry\"\n\n")
            append("Based on what they've shared, offer 1-2 gentle, actionable suggestions for self-care or reflection. ")
            append("Be supportive and non-prescriptive. Keep it under 120 words.")
        }
    }
}
