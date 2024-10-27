package com.mobdeve.xx22.coursify.features.plancreation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.mobdeve.xx22.coursify.core.ui.theme.*

@Composable
fun AdvancedOptionsComponents(
    toggle: MutableState<Boolean>,
    learningPlanObjectives: MutableState<String>,
    learningPlanLearner: MutableState<String>,
    learningPlanOtherComments: MutableState<String>) {

    if (toggle.value) {
        Card {
            Column {
                TextField(
                    value = learningPlanObjectives.value,
                    onValueChange = { learningPlanObjectives.value = it },
                    label = { Text("I want to be able to...") },
                    placeholder = { Text("e.g. leverage AI tools for programming tasks") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Secondary,
                        unfocusedContainerColor = Secondary,

                        focusedTextColor = OnSecondary,
                        unfocusedTextColor = OnSecondary,

                        focusedPlaceholderColor = PrimaryDisabled,
                        unfocusedPlaceholderColor = PrimaryDisabled,

                        focusedLabelColor = Primary,
                        unfocusedLabelColor = PrimaryDisabled,

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.large
                )
                Spacer(modifier = Modifier.height(48.dp))
                TextField(
                    value = learningPlanLearner.value,
                    onValueChange = { learningPlanLearner.value = it },
                    label = { Text("This course is good for...") },
                    placeholder = { Text("e.g. CS students who want to speed up their coding skills") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Secondary,
                        unfocusedContainerColor = Secondary,

                        focusedTextColor = OnSecondary,
                        unfocusedTextColor = OnSecondary,

                        focusedPlaceholderColor = PrimaryDisabled,
                        unfocusedPlaceholderColor = PrimaryDisabled,

                        focusedLabelColor = Primary,
                        unfocusedLabelColor = PrimaryDisabled,

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.large
                )
                Spacer(modifier = Modifier.height(48.dp))
                TextField(
                    value = learningPlanOtherComments.value,
                    onValueChange = { learningPlanOtherComments.value = it },
                    label = { Text("Other comments") },
                    placeholder = { Text("e.g. This course should build up on a comprehensive final project") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .height(150.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Secondary,
                        unfocusedContainerColor = Secondary,

                        focusedTextColor = OnSecondary,
                        unfocusedTextColor = OnSecondary,

                        focusedPlaceholderColor = PrimaryDisabled,
                        unfocusedPlaceholderColor = PrimaryDisabled,

                        focusedLabelColor = Primary,
                        unfocusedLabelColor = PrimaryDisabled,

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.large
                )
            }
        }
    }
}

@Composable
fun PlanCreationScreen() {
    var learningPlanTopic = remember { mutableStateOf("") }
    var learningPlanWeeklyCommitmentFrom = remember { mutableFloatStateOf(4.0f) }
    var learningPlanWeeklyCommitmentTo = remember { mutableFloatStateOf(6.0f) }
    var learningPlanDuration = remember { mutableFloatStateOf(14.0f) }

    var learningPlanObjectives = remember { mutableStateOf("") }
    var learningPlanLearner = remember { mutableStateOf("") }
    var learningPlanOtherComments = remember { mutableStateOf("") }
    var hasAdvanced = remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .padding(32.dp, 64.dp)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(color = Background),
    ) {
        Text(
            text = "New Learning Plan",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = learningPlanTopic.value,
            onValueChange = { learningPlanTopic.value = it },
            label = { Text("I want to learn...") },
            placeholder = { Text("e.g prompt engineering, songwriting") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Secondary,
                unfocusedContainerColor = Secondary,

                focusedTextColor = OnSecondary,
                unfocusedTextColor = OnSecondary,

                focusedPlaceholderColor = PrimaryDisabled,
                unfocusedPlaceholderColor = PrimaryDisabled,

                focusedLabelColor = Primary,
                unfocusedLabelColor = PrimaryDisabled,

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.large
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(text = "I'm willing to commit...", modifier = Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.bodyMedium)
        RangeSlider(
            value = learningPlanWeeklyCommitmentFrom.value..learningPlanWeeklyCommitmentTo.value,
            onValueChange = {
                learningPlanWeeklyCommitmentFrom.value = it.start
                learningPlanWeeklyCommitmentTo.value = it.endInclusive
            },
            colors = SliderDefaults.colors(
                activeTrackColor = Primary,
                activeTickColor = Primary,
                inactiveTrackColor = PrimaryDisabled,
                inactiveTickColor = PrimaryDisabled,
                thumbColor = Primary
            ),
            valueRange = 3.0f..10.0f,
            steps = 7,
            modifier = Modifier
                .padding(bottom = 8.dp, top = 8.dp)
        )
        Text(text = "${learningPlanWeeklyCommitmentFrom.value.toInt()} hours to ${ learningPlanWeeklyCommitmentTo.value.toInt() } hours per week", modifier = Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(48.dp))

        Text(text = "The course is good for...", modifier = Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.bodyMedium)
        Slider(
            value = learningPlanDuration.value,
            onValueChange = {
                learningPlanDuration.value = it
            },
            valueRange = 1.0f..18.0f,
            steps = 17,
            colors = SliderDefaults.colors(
                activeTrackColor = Primary,
                activeTickColor = Primary,
                inactiveTrackColor = PrimaryDisabled,
                inactiveTickColor = PrimaryDisabled,
                thumbColor = Primary
            ),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 8.dp)
        )
        Text(text = "${learningPlanDuration.value.toInt()} weeks", modifier = Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.bodySmall)

        TextButton(
            onClick = { hasAdvanced.value = !(hasAdvanced.value) },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.textButtonColors(
                contentColor = AccentOne
            )
        ) {
            Text(text = "Advanced Options")
        }

        AdvancedOptionsComponents(
            toggle = hasAdvanced,
            learningPlanObjectives = learningPlanObjectives,
            learningPlanLearner = learningPlanLearner,
            learningPlanOtherComments = learningPlanOtherComments
        )

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = OnPrimary),
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 48.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Coursify")
        }

        Button(
            onClick = { },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = Secondary, contentColor = OnSecondary),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Cancel")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlanCreationScreen() {
    PlanCreationScreen()
}
