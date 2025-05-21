package com.example.data.remote.datasource

import android.graphics.Color
import com.example.data.remote.mapper.toDto
import com.example.data.remote.mapper.toModel
import com.example.data.remote.model.TodoItemDto
import com.example.model.Importance
import com.example.model.Note
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class) // for color usage
class RemoteRepositoryImplTest {

    @Test
    fun `toDto should map Note to TodoItemDto correctly`() {
        // Arrange
        val note = Note(
            uid = "test-uid",
            title = "Test Title",
            content = "Test Content",
            color = Color.RED,
            importance = Importance.HIGH,
            selfDestructDate = 1234567890L,
            createdAt = 987654321L,
            updatedAt = 1122334455L
        )
        val deviceId = "device-123"

        // Act
        val result = note.toDto(deviceId)

        // Assert
        assertEquals(note.uid, result.id)
        assertEquals(note.title, result.text)
        assertEquals("important", result.importance)
        assertEquals(note.selfDestructDate, result.deadline)
        assertEquals(false, result.done)
        assertEquals(note.createdAt, result.createdAt)
        assertEquals(note.updatedAt, result.changedAt)
        assertEquals(deviceId, result.lastUpdatedBy)
        assertEquals("#FF0000", result.color)
    }

    @Test
    fun `toDto should handle default values correctly`() {
        // Arrange
        val note = Note(
            title = "Test",
            content = "Content"
        ) // Using defaults for other fields
        val deviceId = "device-123"

        // Act
        val result = note.toDto(deviceId)

        // Assert
        assertNotNull(result.id) // Should be generated if not provided
        assertEquals("Test", result.text)
        assertEquals("basic", result.importance)
        assertNull(result.deadline)
        assertEquals(false, result.done)
        assertNotNull(result.createdAt) // Should be current time if not provided
        assertNotNull(result.changedAt) // Should be current time if not provided
        assertEquals(deviceId, result.lastUpdatedBy)
        assertNull(result.color) // Default color is WHITE which should be null in DTO
    }

    @Test
    fun `toModel should map TodoItemDto to Note correctly`() {
        // Arrange
        val dto = TodoItemDto(
            id = "test-uid",
            text = "Test Title",
            importance = "important",
            deadline = 1234567890L,
            done = false,
            createdAt = 987654321L,
            changedAt = 1122334455L,
            lastUpdatedBy = "device-123",
            color = "#FF0000"
        )

        // Act
        val result = dto.toModel()

        // Assert
        assertEquals(dto.id, result.uid)
        assertEquals(dto.text, result.title)
        assertEquals("", result.content) // Content is always empty in this mapping
        assertEquals(Importance.HIGH, result.importance)
        assertEquals(Color.RED, result.color)
        assertEquals(dto.deadline, result.selfDestructDate)
        assertEquals(dto.createdAt, result.createdAt)
        assertEquals(dto.changedAt, result.updatedAt)
    }

    @Test
    fun `toModel should handle default values correctly`() {
        // Arrange
        val dto = TodoItemDto(
            id = "test-uid",
            text = "Test",
            importance = "basic",
            done = false,
            createdAt = 987654321L,
            changedAt = 1122334455L,
            lastUpdatedBy = "device-123"
        ) // No color or deadline

        // Act
        val result = dto.toModel()

        // Assert
        assertEquals(dto.id, result.uid)
        assertEquals(dto.text, result.title)
        assertEquals("", result.content)
        assertEquals(Importance.NORMAL, result.importance)
        assertEquals(Color.WHITE, result.color) // Default when color is null
        assertNull(result.selfDestructDate) // When deadline is null
        assertEquals(dto.createdAt, result.createdAt)
        assertEquals(dto.changedAt, result.updatedAt)
    }

    @Test
    fun `toModel should handle different importance values`() {
        // Test LOW importance
        val lowDto = TodoItemDto(
            id = "test-uid",
            text = "Test",
            importance = "low",
            done = false,
            createdAt = 987654321L,
            changedAt = 1122334455L,
            lastUpdatedBy = "device-123"
        )
        assertEquals(Importance.LOW, lowDto.toModel().importance)

        // Test HIGH importance
        val highDto = TodoItemDto(
            id = "test-uid",
            text = "Test",
            importance = "important",
            done = false,
            createdAt = 987654321L,
            changedAt = 1122334455L,
            lastUpdatedBy = "device-123"
        )
        assertEquals(Importance.HIGH, highDto.toModel().importance)

        // Test unknown importance (should default to NORMAL)
        val unknownDto = TodoItemDto(
            id = "test-uid",
            text = "Test",
            importance = "unknown",
            done = false,
            createdAt = 987654321L,
            changedAt = 1122334455L,
            lastUpdatedBy = "device-123"
        )
        assertEquals(Importance.NORMAL, unknownDto.toModel().importance)
    }

    @Test
    fun `toModel should handle invalid color values`() {
        // Arrange
        val dto = TodoItemDto(
            id = "test-uid",
            text = "Test",
            importance = "basic",
            done = false,
            createdAt = 987654321L,
            changedAt = 1122334455L,
            lastUpdatedBy = "device-123",
            color = "invalid-color"
        )

        // Act
        val result = dto.toModel()

        // Assert
        assertEquals(Color.WHITE, result.color) // Should fall back to white
    }

    @Test
    fun `toDto should correctly convert color to hex string`() {
        // Test with RED color
        val redNote = Note(
            title = "Red",
            content = "Content",
            color = Color.RED
        )
        assertEquals("#FF0000", redNote.toDto("device").color)

        // Test with GREEN color
        val greenNote = Note(
            title = "Green",
            content = "Content",
            color = Color.GREEN
        )
        assertEquals("#00FF00", greenNote.toDto("device").color)

        // Test with BLUE color
        val blueNote = Note(
            title = "Blue",
            content = "Content",
            color = Color.BLUE
        )
        assertEquals("#0000FF", blueNote.toDto("device").color)

        // Test with WHITE color (should be null)
        val whiteNote = Note(
            title = "White",
            content = "Content",
            color = Color.WHITE
        )
        assertNull(whiteNote.toDto("device").color)
    }
}