package com.mobdeve.xx22.gilo.joshua.myapplication.tracking

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview

data class LessonPlan(
    val title: String,
    val modules: List<Module>
)

data class Module(
    val title: String,
    val tasks: List<TaskItem>
)

data class TaskItem(
    val text: String,
    val isCompleted: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen() {
    // Sample data
    val lessonPlans = listOf(
        LessonPlan(
            "Learn Android Development",
            listOf(
                Module(
                    "Introduction to Android Development",
                    listOf(
                        TaskItem("Android Studio & Basics", true),
                        TaskItem("Set up Android Studio", true),
                        TaskItem("Create first project", true)
                    )
                ),
                Module(
                    "Basic UI Components",
                    listOf(
                        TaskItem("Learn about Layouts", false),
                        TaskItem("Understanding Views", false),
                        TaskItem("Basic UI Elements", false)
                    )
                )
            )
        ),
        LessonPlan(
            "Learn Kotlin Programming",
            listOf(
                Module(
                    "Kotlin Basics",
                    listOf(
                        TaskItem("Variables and Data Types", true),
                        TaskItem("Control Flow", false),
                        TaskItem("Functions", false)
                    )
                )
            )
        )
    )

    var selectedLessonPlan by remember { mutableStateOf<LessonPlan?>(lessonPlans.firstOrNull()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Plans",
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = { /* TODO: Handle profile click */ }) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Person",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Content section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Dropdown for selecting lesson plan
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedLessonPlan?.title ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    lessonPlans.forEach { lessonPlan ->
                        DropdownMenuItem(
                            text = { Text(lessonPlan.title) },
                            onClick = {
                                selectedLessonPlan = lessonPlan
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Modules list
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedLessonPlan?.let { plan ->
                    items(plan.modules.size) { moduleIndex ->
                        ModuleSection(plan.modules[moduleIndex])
                    }
                }
            }
        }
    }
}

@Composable
fun ModuleSection(module: Module) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "arrow_rotation"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Module Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expand/Collapse Module",
                    modifier = Modifier.rotate(rotationState)
                )
            }

            // Tasks
            if (isExpanded) {
                module.tasks.forEach { task ->
                    TaskRow(task)
                }
            }
        }
    }
}

@Composable
fun TaskRow(task: TaskItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = if (task.isCompleted) "Completed" else "Not completed",
            tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = task.text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (task.isCompleted) Color.Black else Color.Gray,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLessonPlansScreen() {
    MaterialTheme {
        TrackingScreen()
    }
}