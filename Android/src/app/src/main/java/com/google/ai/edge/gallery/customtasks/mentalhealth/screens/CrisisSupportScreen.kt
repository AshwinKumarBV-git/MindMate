/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ai.edge.gallery.customtasks.mentalhealth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Crisis Support screen displaying static emergency resources and contact information.
 * No LLM processing required - provides immediate access to crisis support resources.
 *
 * @param onNavigateBack Callback to navigate back to the dashboard
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrisisSupportScreen(
  onNavigateBack: () -> Unit
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Crisis Support") },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.errorContainer,
          titleContentColor = MaterialTheme.colorScheme.onErrorContainer,
          navigationIconContentColor = MaterialTheme.colorScheme.onErrorContainer
        )
      )
    }
  ) { innerPadding ->

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Urgent supportive message
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.errorContainer
        )
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "You deserve support right now",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center
          )
          
          Spacer(modifier = Modifier.height(8.dp))
          
          Text(
            text = "You are not alone. Help is available 24/7.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center
          )
        }
      }
      
      Spacer(modifier = Modifier.height(24.dp))
      
      // Emergency contacts section
      Text(
        text = "Emergency Contacts",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
      )
      
      // National Suicide Prevention Lifeline
      ContactCard(
        icon = Icons.Outlined.Phone,
        title = "National Suicide Prevention Lifeline",
        contact = "988",
        description = "Call or text 988 for free, confidential support 24/7"
      )
      
      Spacer(modifier = Modifier.height(12.dp))
      
      // Crisis Text Line
      ContactCard(
        icon = Icons.Outlined.Message,
        title = "Crisis Text Line",
        contact = "Text HOME to 741741",
        description = "Free, 24/7 crisis support via text message"
      )
      
      Spacer(modifier = Modifier.height(12.dp))
      
      // International resources
      ContactCard(
        icon = Icons.Outlined.Public,
        title = "International Association for Suicide Prevention",
        contact = "iasp.info/resources/Crisis_Centres",
        description = "Find crisis centers worldwide"
      )
      
      Spacer(modifier = Modifier.height(24.dp))

      
      // Immediate actions section
      Text(
        text = "Immediate Actions You Can Take",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
      )
      
      ActionCard(
        number = "1",
        title = "Call Emergency Services",
        description = "If you're in immediate danger, call 911 or your local emergency number"
      )
      
      Spacer(modifier = Modifier.height(12.dp))
      
      ActionCard(
        number = "2",
        title = "Reach Out to a Trusted Person",
        description = "Contact a friend, family member, therapist, or spiritual advisor"
      )
      
      Spacer(modifier = Modifier.height(12.dp))
      
      ActionCard(
        number = "3",
        title = "Go to the Nearest Emergency Room",
        description = "Emergency departments can provide immediate mental health support"
      )
      
      Spacer(modifier = Modifier.height(24.dp))
      
      // Additional message
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
      ) {
        Text(
          text = "Your life matters. These feelings are temporary, and support is available to help you through this difficult time.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSecondaryContainer,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(16.dp)
        )
      }
    }
  }
}


/**
 * Composable for displaying a contact card with icon, title, and contact information.
 *
 * @param icon The icon to display
 * @param title The title of the contact resource
 * @param contact The contact information (phone number, text code, or URL)
 * @param description Additional description of the resource
 */
@Composable
private fun ContactCard(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  contact: String,
  description: String
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer
    )
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier
          .size(32.dp)
          .padding(end = 4.dp)
      )
      
      Column(
        modifier = Modifier.padding(start = 12.dp)
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
          text = contact,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
          text = description,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
      }
    }
  }
}

/**
 * Composable for displaying an action card with a numbered step.
 *
 * @param number The step number
 * @param title The action title
 * @param description The action description
 */
@Composable
private fun ActionCard(
  number: String,
  title: String,
  description: String
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(32.dp)
      ) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier.fillMaxSize()
        ) {
          Text(
            text = number,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
          )
        }
      }
      
      Column(
        modifier = Modifier.padding(start = 12.dp)
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
          text = description,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
