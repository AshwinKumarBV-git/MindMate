package com.google.ai.edge.gallery.customtasks.mentalhealth.utils

/**
 * Crisis detection utility for identifying user input that indicates potential self-harm or crisis.
 * Used to intercept messages before LLM processing and redirect to static crisis resources.
 */
object CrisisDetection {
    /**
     * List of keywords that indicate a potential crisis situation.
     * Detection errs on the side of caution to ensure user safety.
     */
    private val CRISIS_KEYWORDS = listOf(
        "suicide",
        "kill myself",
        "want to die",
        "hurt myself",
        "end it",
        "give up"
    )

    /**
     * Detects whether the user input contains crisis-related keywords.
     *
     * @param input The user's input text to analyze
     * @return true if crisis keywords are detected, false otherwise
     */
    fun detectCrisis(input: String): Boolean {
        val normalizedInput = input.lowercase().trim()
        return CRISIS_KEYWORDS.any { keyword ->
            normalizedInput.contains(keyword)
        }
    }
}
