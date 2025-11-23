package com.example.jobs.ui.ui_stateholders

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TaskFormStateTest {
    private lateinit var taskFormState: TaskFormState

    @Before
    fun setup() {
        taskFormState = TaskFormState()
    }

    @Test
    fun `fields are initialized correctly`() {
        val fields = taskFormState.fields

        assertEquals(fields.size, 3)
        assertTrue(fields.containsKey(TaskFormFields.TITLE))
        assertTrue(fields.containsKey(TaskFormFields.DESCRIPTION))
        assertTrue(fields.containsKey(TaskFormFields.PLACE))
    }

    @Test
    fun `get() returns the correct InputFieldManager`() {
        val titleManager = taskFormState.get<InputTextFieldManager>(TaskFormFields.TITLE)

        assertEquals( TaskFormFields.TITLE.name, titleManager.fieldId)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `get() throws IllegalArgumentException if no key exist`() {
        taskFormState.fields.remove(TaskFormFields.TITLE)
        taskFormState.get<InputTextFieldManager>(TaskFormFields.TITLE)
    }

    @Test
    fun `isValid() returns true if all the fields are valid`() {
        val titleField = taskFormState.get<InputTextFieldManager>(TaskFormFields.TITLE)
        val descField = taskFormState.get<InputTextFieldManager>(TaskFormFields.DESCRIPTION)
        val placeField = taskFormState.get<InputTextFieldManager>(TaskFormFields.PLACE)

        titleField.onTextChange("This is a valid title") // >10 chars
        descField.onTextChange("This is a valid description")
        placeField.onTextChange("Winterfell")

        assertTrue(taskFormState.isValid())
    }

    @Test
    fun `isValid() returns false if any one of the fields is inValid`() {
        val titleField = taskFormState.get<InputTextFieldManager>(TaskFormFields.TITLE)
        val descField = taskFormState.get<InputTextFieldManager>(TaskFormFields.DESCRIPTION)

        titleField.onTextChange("This") // <10 chars
        descField.onTextChange("This is a valid description")

        assertFalse(taskFormState.isValid())
    }

    @Test
    fun `getDataToSubmit returns correct TaskSubmissionModel`() {
        val titleField = taskFormState.get<InputTextFieldManager>(TaskFormFields.TITLE)
        val descField = taskFormState.get<InputTextFieldManager>(TaskFormFields.DESCRIPTION)
        val placeField = taskFormState.get<InputTextFieldManager>(TaskFormFields.PLACE)

        titleField.onTextChange("My task title")
        descField.onTextChange("Detailed task description")
        placeField.onTextChange("King's Landing")

        val data = taskFormState.getDataToSubmit()

        assertEquals("My task title", data.title)
        assertEquals("Detailed task description", data.description)
        assertEquals("King's Landing", data.place)
    }

    @Test
    fun `showErrors() sets text error state on invalid fields`() {
        val titleField = taskFormState.get<InputTextFieldManager>(TaskFormFields.TITLE)
        val descField = taskFormState.get<InputTextFieldManager>(TaskFormFields.DESCRIPTION)

        titleField.onTextChange("This") // <10 chars, Invalid fields
        descField.onTextChange("This is a valid description")

        taskFormState.showErrors()

        assertNotNull("Invalid field should have error state ui", titleField.textState.error)
        assertNull("Valid field should have no error state", descField.textState.error)
    }
}