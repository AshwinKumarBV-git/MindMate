# MindMate Data Flow Documentation

## Overview

This document provides detailed visualizations and explanations of how data flows through the MindMate application, from user input to AI-generated responses and data storage.

## Table of Contents

1. [High-Level Data Flow](#high-level-data-flow)
2. [Feature-Specific Flows](#feature-specific-flows)
3. [Model Management Flow](#model-management-flow)
4. [Security and Encryption Flow](#security-and-encryption-flow)
5. [Crisis Detection Flow](#crisis-detection-flow)

## High-Level Data Flow

### Complete System Data Flow

```
┌─────────────┐
│    User     │
└──────┬──────┘
       │ Input (text, selection, etc.)
       ↓
┌─────────────────────────────────────┐
│      Presentation Layer             │
│  (Jetpack Compose UI Components)    │
└──────┬──────────────────────────────┘
       │ User Action
       ↓
┌─────────────────────────────────────┐
│      ViewModel Layer                │
│  (State Management & Business Logic)│
└──────┬──────────────────────────────┘
       │ Data Request
       ↓
┌─────────────────────────────────────┐
│      Repository Layer               │
│  (Data Access Abstraction)          │
└──────┬──────────────────────────────┘
       │
       ├─────────────┬─────────────────┐
       │             │                 │
       ↓             ↓                 ↓
┌──────────┐  ┌──────────┐    ┌──────────────┐
│   Room   │  │   Model  │    │  TensorFlow  │
│ Database │  │  Files   │    │     Lite     │
└──────────┘  └──────────┘    └──────┬───────┘
                                      │
                                      ↓
                              ┌──────────────┐
                              │ Gemma Model  │
                              │  Inference   │
                              └──────┬───────┘
                                     │ Response
                                     ↓
                              ┌──────────────┐
                              │   Process    │
                              │   Response   │
                              └──────┬───────┘
                                     │
                                     ↓
                              ┌──────────────┐
                              │ Update State │
                              └──────┬───────┘
                                     │
                                     ↓
                              ┌──────────────┐
                              │  Update UI   │
                              └──────────────┘
```

### Data Flow Principles

1. **Unidirectional Flow**: Data flows in one direction from UI → ViewModel → Repository → Data Source
2. **State Updates**: State changes flow back up through StateFlow/LiveData
3. **Separation of Concerns**: Each layer has specific responsibilities
4. **No Direct Access**: UI never directly accesses data sources

## Feature-Specific Flows

### 1. Mood Check-In Flow

```mermaid
sequenceDiagram
    participant U as User
    participant UI as MoodCheckInScreen
    participant VM as LlmSingleTurnViewModel
    participant MM as ModelManager
    participant TFL as TensorFlow Lite
    participant GM as Gemma Model
    participant DB as Room Database
    participant EM as EncryptionManager
    
    U->>UI: Select mood (1-5)
    U->>UI: Add context (optional)
    UI->>VM: generateResponse(prompt, model)
    
    Note over VM: Build prompt with system prompt
    VM->>MM: generateResponse(model, prompt)
    
    MM->>TFL: run inference
    TFL->>GM: forward pass
    GM-->>TFL: output tokens
    TFL-->>MM: decoded response
    
    MM-->>VM: AI response
    VM->>VM: Update UI state
    VM-->>UI: Success(response)
    
    UI-->>U: Display insight
    
    opt User saves mood
        UI->>VM: saveMood(mood, context, insight)
        VM->>EM: encrypt(moodData)
        EM-->>VM: encryptedData
        VM->>DB: insert(encryptedMoodEntry)
        DB-->>VM: success
        VM-->>UI: Saved confirmation
    end
```

### 2. Thought Reframing Flow

```mermaid
sequenceDiagram
    participant U as User
    participant UI as ThoughtReframingScreen
    participant VM as LlmSingleTurnViewModel
    participant Prompts as MentalHealthPrompts
    participant MM as ModelManager
    participant TFL as TensorFlow Lite
    
    U->>UI: Enter negative thought
    UI->>UI: Validate input
    UI->>VM: generateResponse(thought, model)
    
    VM->>Prompts: getThoughtReframingPrompt(thought)
    Prompts-->>VM: formatted prompt with system prompt
    
    VM->>MM: generateResponse(model, prompt)
    MM->>TFL: run inference
    TFL-->>MM: AI response
    
    MM-->>VM: reframed thought
    VM->>VM: Parse response
    VM->>VM: Update UI state
    VM-->>UI: Success(reframedThought)
    
    UI-->>U: Display reframed perspective
```

### 3. Safe Companion Chat Flow

```mermaid
sequenceDiagram
    participant U as User
    participant UI as SafeCompanionChatScreen
    participant CD as CrisisDetection
    participant VM as LlmChatViewModel
    participant MM as ModelManager
    participant TFL as TensorFlow Lite
    participant DB as Room Database
    
    U->>UI: Type message
    UI->>CD: detectCrisis(message)
    
    alt Crisis Detected
        CD-->>UI: Crisis detected
        UI->>UI: Navigate to CrisisSupportScreen
        UI-->>U: Show crisis resources
    else No Crisis
        CD-->>UI: Safe to proceed
        UI->>VM: sendMessage(message, model)
        
        Note over VM: Add system prompt to context
        VM->>MM: generateResponse(model, prompt)
        MM->>TFL: run inference
        TFL-->>MM: AI response
        MM-->>VM: response
        
        VM->>VM: Add to chat history
        VM->>DB: saveMessage(message, response)
        VM->>VM: Update UI state
        VM-->>UI: New message
        
        UI-->>U: Display AI response
    end
```

### 4. Journal Tools Flow

```mermaid
sequenceDiagram
    participant U as User
    participant UI as JournalToolsScreen
    participant VM as LlmSingleTurnViewModel
    participant Prompts as MentalHealthPrompts
    participant MM as ModelManager
    
    U->>UI: Write journal entry
    U->>UI: Select action (Summarize/Suggest)
    
    alt Summarize Action
        UI->>VM: generateResponse(entry, model, SUMMARY)
        VM->>Prompts: getJournalSummaryPrompt(entry)
    else Suggestion Action
        UI->>VM: generateResponse(entry, model, SUGGESTION)
        VM->>Prompts: getJournalSuggestionPrompt(entry)
    end
    
    Prompts-->>VM: formatted prompt
    VM->>MM: generateResponse(model, prompt)
    MM-->>VM: AI response
    VM-->>UI: Success(response)
    UI-->>U: Display result
```

## Model Management Flow

### Model Download and Initialization

```mermaid
sequenceDiagram
    participant U as User
    participant UI as ModelManagerScreen
    participant VM as ModelManagerViewModel
    participant DR as DownloadRepository
    participant WM as WorkManager
    participant FS as FileSystem
    participant CT as CustomTask
    participant TFL as TensorFlow Lite
    
    Note over U,TFL: Download Phase
    U->>UI: Tap "Download Model"
    UI->>VM: downloadModel(task, model)
    VM->>DR: downloadModel(task, model, callback)
    DR->>WM: enqueue download work
    
    loop Download Progress
        WM->>FS: Write chunks
        WM->>DR: Progress update
        DR->>VM: onStatusUpdated(progress)
        VM->>VM: Update download status
        VM-->>UI: Progress update
        UI-->>U: Update progress bar
    end
    
    WM-->>DR: Download complete
    DR->>DR: Validate checksum
    DR->>VM: onStatusUpdated(SUCCEEDED)
    VM-->>UI: Download complete
    
    Note over U,TFL: Initialization Phase
    U->>UI: Tap "Initialize Model"
    UI->>VM: initializeModel(context, task, model)
    VM->>VM: Set initializing status
    VM-->>UI: Show initializing indicator
    
    VM->>CT: initializeModelFn(context, scope, model, onDone)
    CT->>TFL: Load model file
    TFL->>TFL: Allocate tensors
    TFL->>TFL: Load tokenizer
    TFL-->>CT: Model ready
    CT->>CT: Store model instance
    CT-->>VM: onDone(success)
    
    VM->>VM: Set initialized status
    VM-->>UI: Model ready
    UI-->>U: Show "Ready" status
```

### Model Cleanup Flow

```mermaid
flowchart TD
    A[User navigates away] --> B{Model in use?}
    B -->|Yes| C[Mark for cleanup]
    B -->|No| D[Immediate cleanup]
    
    C --> E[Wait for operation complete]
    E --> D
    
    D --> F[Call cleanUpModelFn]
    F --> G[Release TensorFlow Lite resources]
    G --> H[Clear model instance]
    H --> I[Free memory]
    I --> J[Update status to NOT_INITIALIZED]
    J --> K[Cleanup complete]
```

## Security and Encryption Flow

### Data Encryption Flow

```mermaid
sequenceDiagram
    participant App as Application
    participant EM as EncryptionManager
    participant KS as Android Keystore
    participant Cipher as AES Cipher
    participant DB as Room Database
    
    Note over App,DB: Encryption Process
    App->>EM: encrypt(sensitiveData)
    EM->>KS: Get encryption key
    KS-->>EM: SecretKey
    
    EM->>Cipher: init(ENCRYPT_MODE, key)
    Cipher-->>EM: Cipher ready
    
    EM->>Cipher: doFinal(data)
    Cipher-->>EM: encryptedBytes + IV
    
    EM-->>App: EncryptedData(bytes, iv)
    App->>DB: insert(encryptedData)
    
    Note over App,DB: Decryption Process
    App->>DB: query(encryptedData)
    DB-->>App: EncryptedData(bytes, iv)
    
    App->>EM: decrypt(encryptedData)
    EM->>KS: Get decryption key
    KS-->>EM: SecretKey
    
    EM->>Cipher: init(DECRYPT_MODE, key, iv)
    Cipher-->>EM: Cipher ready
    
    EM->>Cipher: doFinal(encryptedBytes)
    Cipher-->>EM: decryptedBytes
    
    EM-->>App: Original data
```

### Privacy Data Flow

```
User Data Input
       ↓
[Input Validation]
       ↓
[Sanitization]
       ↓
[Encryption with AES-256-GCM]
       ↓
[Store in Room Database]
       ↓
[Internal App Storage]
       ↓
[Android App Sandbox]
       ↓
[Device Storage]

NO NETWORK TRANSMISSION
NO CLOUD BACKUP
NO EXTERNAL ACCESS
```

## Crisis Detection Flow

### Crisis Detection Decision Tree

```mermaid
flowchart TD
    A[User Input] --> B[Crisis Detection]
    B --> C{Contains explicit<br/>crisis keywords?}
    
    C -->|Yes| D[IMMEDIATE RISK]
    C -->|No| E[Sentiment Analysis]
    
    E --> F{Negative sentiment<br/>score > threshold?}
    F -->|Yes| G[Context Analysis]
    F -->|No| H[LOW RISK]
    
    G --> I{Multiple risk<br/>indicators?}
    I -->|Yes| J[HIGH RISK]
    I -->|No| K[MEDIUM RISK]
    
    D --> L[Navigate to Crisis Support]
    J --> L
    K --> M[Show supportive response<br/>+ resource suggestions]
    H --> N[Normal AI response]
    
    L --> O[Display emergency contacts]
    L --> P[Show immediate actions]
    L --> Q[Provide crisis hotlines]
```

### Crisis Detection Implementation Flow

```mermaid
sequenceDiagram
    participant UI as Chat UI
    participant CD as CrisisDetection
    participant VM as ViewModel
    participant Nav as Navigation
    participant CS as CrisisSupportScreen
    
    UI->>CD: detectCrisis(userMessage)
    
    CD->>CD: Normalize text (lowercase, trim)
    CD->>CD: Check explicit keywords
    
    alt Explicit Crisis Keywords Found
        CD-->>UI: Crisis detected (HIGH)
        UI->>Nav: navigateToCrisisSupport()
        Nav->>CS: Show crisis resources
        CS-->>UI: Display emergency info
    else No Explicit Keywords
        CD->>CD: Analyze sentiment
        CD->>CD: Check context patterns
        
        alt High Risk Indicators
            CD-->>UI: Crisis detected (MEDIUM)
            UI->>VM: Generate supportive response
            VM-->>UI: Response + resources
        else Low Risk
            CD-->>UI: No crisis detected
            UI->>VM: Generate normal response
            VM-->>UI: Normal AI response
        end
    end
```

## Performance Optimization Flow

### Memory Management Flow

```mermaid
flowchart TD
    A[App Running] --> B{Check Memory Usage}
    B -->|< 75% threshold| C[Continue Normal Operation]
    B -->|> 75% threshold| D[Memory Pressure Detected]
    
    D --> E[Unload Inactive Models]
    E --> F[Clear Response Cache]
    F --> G[Clear Image Cache]
    G --> H[Trigger GC]
    H --> I{Memory < 75%?}
    
    I -->|Yes| C
    I -->|No| J[Show Low Memory Warning]
    J --> K[Suggest Closing Apps]
    
    C --> L[Monitor Continuously]
    L --> B
```

### Inference Optimization Flow

```
User Request
     ↓
[Check Model Status]
     ↓
[Model Loaded?] ──No──> [Load Model]
     ↓ Yes                    ↓
[Prepare Input]         [Initialize]
     ↓                        ↓
[Tokenize] <─────────────────┘
     ↓
[Optimize Batch Size]
     ↓
[Select Hardware Accelerator]
     ├─> GPU (if available)
     ├─> NNAPI (if supported)
     └─> CPU (fallback)
     ↓
[Run Inference]
     ↓
[Decode Output]
     ↓
[Post-process Response]
     ↓
[Return to User]
```

## Data Persistence Flow

### Room Database Operations

```mermaid
sequenceDiagram
    participant VM as ViewModel
    participant Repo as Repository
    participant EM as EncryptionManager
    participant DAO as MoodDao
    participant DB as SQLite Database
    
    Note over VM,DB: Insert Operation
    VM->>Repo: saveMood(mood)
    Repo->>EM: encrypt(moodData)
    EM-->>Repo: encryptedData
    Repo->>DAO: insertMoodEntry(encryptedEntry)
    DAO->>DB: INSERT INTO mood_entries
    DB-->>DAO: Success
    DAO-->>Repo: Success
    Repo-->>VM: Result.success()
    
    Note over VM,DB: Query Operation
    VM->>Repo: getMoodHistory()
    Repo->>DAO: getRecentMoodEntries(30)
    DAO->>DB: SELECT * FROM mood_entries
    DB-->>DAO: List<EncryptedMoodEntry>
    DAO-->>Repo: Flow<List<EncryptedMoodEntry>>
    
    loop For each entry
        Repo->>EM: decrypt(encryptedEntry)
        EM-->>Repo: decryptedMoodData
    end
    
    Repo-->>VM: Flow<List<MoodEntry>>
```

## Conclusion

This document provides comprehensive visualizations of data flow throughout the MindMate application. Key takeaways:

1. **Unidirectional Flow**: Data flows predictably through architectural layers
2. **Privacy-First**: All sensitive data encrypted before storage
3. **On-Device Processing**: No external data transmission for core features
4. **Safety Mechanisms**: Crisis detection integrated at multiple points
5. **Performance Optimization**: Efficient memory and inference management

For implementation details, refer to:
- [ARCHITECTURE.md](./ARCHITECTURE.md) for technical architecture
- [README.md](./README.md) for feature descriptions
- Source code in `Android/src/app/src/main/java/com/google/ai/edge/gallery/`

---

*Last Updated: 2025-01-XX*
