package com.example.jobs.ui.ui_stateholders

import com.example.common.ui.layout.ui_stateholders.UiText
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import java.util.stream.Stream

class InputTextFieldManagerTest {

    companion object {
        @JvmStatic
        fun getManagerConfiguration(): Stream<InputTextFieldManager> = Stream.of(
            InputTextFieldManager(
                fieldId = "MANDATORY",
                isRequired = true,
                label = UiText.DynamicString("Title")
            ),
            // Scenario 2: Optional field
            InputTextFieldManager(
                fieldId = "OPTIONAL",
                isRequired = false,
                label = UiText.DynamicString("Title")
            ),
            // Scenario 3: Field with only a max character limit
            InputTextFieldManager(
                fieldId = "MAX_CHARS",
                isRequired = true,
                maxChars = 10,
                label = UiText.DynamicString("Title")
            ),
            // Scenario 4: Field with only a min character limit
            InputTextFieldManager(
                fieldId = "MIN_CHARS",
                isRequired = true,
                minChars = 10,
                label = UiText.DynamicString("Title")
            ),
            // Scenario 5: Field with both min and max character limits
            InputTextFieldManager(
                fieldId = "MIN_MAX_CHARS",
                isRequired = true,
                minChars = 5,
                maxChars = 10,
                label = UiText.DynamicString("Title")
            )
        )

    }

    @Before
    fun setUp() {

    }

    @Test
    fun `getDataToSubmit return the changed text`() {
        val newText = "Test"

        getManagerConfiguration().forEach {
            it?.onTextChange(newText)
            assertEquals(newText, it.getDataToSubmit())
        }
    }

    @Test
    fun `isValid() return true when field is not required and empty`() {
        val manager = InputTextFieldManager(
            fieldId = "OPTIONAL",
            isRequired = false,
            label = UiText.DynamicString("Title")
        )

        manager.onTextChange("")
        assertEquals(true, manager.isValid())
    }


    @Test
    fun `isValid() return false when field is required and empty`() {
        val manager = InputTextFieldManager(
            fieldId = "MANDATORY",
            isRequired = true,
            label = UiText.DynamicString("Title")
        )

        manager.onTextChange("")
        assertEquals(false, manager.isValid())
    }

    @Test
    fun `isValid() return false when field is required and filled but with more than max chars`() {
        val manager = InputTextFieldManager(
            fieldId = "MANDATORY",
            isRequired = true,
            label = UiText.DynamicString("Title"),
            maxChars = 5
        )

        manager.onTextChange("More than 5 chars")
        assertEquals(false, manager.isValid())
    }

    @Test
    fun `isValid() return false when field is required and filled but with less than min chars`() {
        val manager = InputTextFieldManager(
            fieldId = "MANDATORY",
            isRequired = true,
            label = UiText.DynamicString("Title"),
            minChars = 5
        )

        manager.onTextChange("abc")
        assertEquals(false, manager.isValid())
    }

    @Test
    fun `isValid() return true when field is required and filled and within min max char limit`() {
        val manager = InputTextFieldManager(
            fieldId = "MANDATORY",
            isRequired = true,
            label = UiText.DynamicString("Title"),
            minChars = 5,
            maxChars = 50
        )

        manager.onTextChange("Within min max limit")
        assertEquals(true, manager.isValid())
    }

    @Test
    fun `showError() does nothing when field is not required`() {
        val manager = InputTextFieldManager(
            fieldId = "ID",
            isRequired = false,
            label = UiText.DynamicString("Title")
        )

        manager.onTextChange("    ")
        manager.showErrors()

        assertNull("Error should be null when isRequired is fals", manager.textState.error)
    }

    @Test
    fun `showErrors() sets blank error when required and text is blank`() {
        // Arrange
        val manager = InputTextFieldManager(
            fieldId = "testField",
            isRequired = true
        )
        manager.onTextChange("   ")
        manager.showErrors()

        assertTrue(
            "Should show a StringResource error for blank text",
            manager.textState.error is UiText.StringResource
        )
    }

    @Test
    fun `showErrors() sets min limit error when text is shorter than minChars`() {
        // Arrange
        val minChars = 10
        val manager = InputTextFieldManager(
            fieldId = "testField",
            isRequired = true,
            minChars = minChars
        )
        manager.onTextChange("short")
        manager.showErrors()

        val error = manager.textState.error
        assertTrue(
            "Error should be of type StringResource",
            error is UiText.StringResource
        )

        assertEquals(
            "Error arguments should contain the minChars value",
            listOf(minChars),
            (error as UiText.StringResource).args
        )
    }

    @Test
    fun `showErrors() sets max limit error when text is longer than maxChars`() {
        // Arrange
        val maxChars = 5
        val manager = InputTextFieldManager(
            fieldId = "testField",
            isRequired = true,
            maxChars = maxChars
        )

        manager.onTextChange("This is too long")
        manager.showErrors()

        val error = manager.textState.error
        assertTrue(
            "Error should be of type StringResource",
            error is UiText.StringResource
        )
        assertEquals(
            "Error arguments should contain the maxChars value",
            listOf(maxChars),
            (error as UiText.StringResource).args
        )
    }

    @Test
    fun `showErrors() sets error to null when text is valid`() {
        val manager = InputTextFieldManager(
            fieldId = "testField",
            isRequired = true,
            minChars = 3,
            maxChars = 20
        )
        manager.onTextChange("This is a valid text")
        manager.showErrors()

        assertNull("Error should be null for valid text", manager.textState.error)
    }

    @Test
    fun `showErrors() sets error to null when text length is equal to minChars`() {
        // Arrange
        val minChars = 5
        val manager = InputTextFieldManager(
            fieldId = "testField",
            isRequired = true,
            minChars = minChars
        )
        manager.onTextChange("Five!")
        manager.showErrors()

        assertNull("Error should be null when text length is exactly minChars", manager.textState.error)
    }

    @Test
    fun `showErrors() sets error to null when text length is equal to maxChars`() {
        val maxChars = 10
        val manager = InputTextFieldManager(
            fieldId = "testField",
            isRequired = true,
            maxChars = maxChars
        )
        
        manager.onTextChange("Ten Chars.")
        manager.showErrors()

        assertNull("Error should be null when text length is exactly maxChars", manager.textState.error)
    }

    @Test
    fun `onTextChange() set text state when called`() {
        val manager = InputTextFieldManager(
            fieldId = "testField",
            isRequired = true,
        )

        val text = "Test"
        manager.onTextChange(text)

        assertEquals(text, manager.textState.text)
    }

    @Test
    fun `onTextChange() set error if the text is over maxChars`() {
        val maxChars = 5
        val manager = InputTextFieldManager(
            fieldId = "testField",
            isRequired = true,
            maxChars = maxChars,
        )

        manager.onTextChange("Ten Chars.")
        val error = manager.textState.error

        assertTrue(
            "Error should be of type StringResource",
            error is UiText.StringResource
        )
        assertEquals(
            "Error arguments should contain the maxChars value",
            listOf(maxChars),
            (error as UiText.StringResource).args
        )
    }
}
