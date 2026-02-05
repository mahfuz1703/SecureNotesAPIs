package mahfuz.mia.SecureNotesAPIs.service;

import mahfuz.mia.SecureNotesAPIs.entity.Note;
import mahfuz.mia.SecureNotesAPIs.repository.NotesRepository;
import mahfuz.mia.SecureNotesAPIs.response.ApiResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
    @Mock
    private NotesRepository notesRepository;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private NoteService noteService;

    private Note note;
    private Long userId;

    @BeforeEach
    void setUp(){
        userId = 1L;
        note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setContent("This is a test note.");
        note.setUserId(userId);
    }

    @Test
    @DisplayName("Should get all notes for authenticated user")
    void testGetAllNotes(){
        List<Note> notes = Arrays.asList(note);
        when(userService.getCurrentUserId(authentication)).thenReturn(userId);
        when(notesRepository.findNoteByUserId(userId)).thenReturn(Optional.ofNullable(note).stream().toList());

        ApiResponse<List<Note>> response = noteService.getAllNotes(authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Notes fetched successfully", response.getMessage());
        assertEquals(notes, response.getData());
        verify(notesRepository).findNoteByUserId(userId);
    }

    @Test
    @DisplayName("Should add note successfully")
    void testAddNote(){
        when(userService.getCurrentUserId(authentication)).thenReturn(userId);
        when(notesRepository.save(any(Note.class))).thenReturn(note);

        ApiResponse<Note> response = noteService.addNote(note, authentication);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode());
        assertEquals("Note added successfully", response.getMessage());
        assertEquals(note, response.getData());
        verify(notesRepository).save(note);
    }

    @Test
    @DisplayName("Should throw exception when note title is null")
    void testAddNoteWithNullTitle() {
        note.setTitle(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.addNote(note, authentication));

        assertEquals("Note title cannot be empty or null", exception.getMessage());
        verify(notesRepository, never()).save(any(Note.class));
    }

    @Test
    @DisplayName("Should throw exception when note title is empty")
    void testAddNoteWithEmptyTitle() {
        note.setTitle("");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.addNote(note, authentication));

        assertEquals("Note title cannot be empty or null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when note content is null")
    void testAddNoteWithNullContent() {
        note.setContent(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.addNote(note, authentication));

        assertEquals("Note content cannot be empty or null", exception.getMessage());
    }

    @Test
    @DisplayName("Should update note successfully")
    void testUpdateNote() {
        Note updatedNote = new Note();
        updatedNote.setId(1L);
        updatedNote.setTitle("Updated Title");
        updatedNote.setContent("Updated Content");

        when(userService.getCurrentUserId(authentication)).thenReturn(userId);
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));
        when(notesRepository.save(any(Note.class))).thenReturn(note);

        ApiResponse<Note> response = noteService.updateNote(updatedNote, authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Note updated successfully", response.getMessage());
        assertEquals("Updated Title", response.getData().getTitle());
        assertEquals("Updated Content", response.getData().getContent());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent note")
    void testUpdateNonExistentNote() {
        when(userService.getCurrentUserId(authentication)).thenReturn(userId);
        when(notesRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.updateNote(note, authentication));

        assertTrue(exception.getMessage().contains("Note not found with id:"));
    }

    @Test
    @DisplayName("Should throw exception when updating note of another user")
    void testUpdateNoteUnauthorized() {
        when(userService.getCurrentUserId(authentication)).thenReturn(2L);
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.updateNote(note, authentication));

        assertEquals("Unauthorized to update this note", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete note successfully")
    void testDeleteNote() {
        when(userService.getCurrentUserId(authentication)).thenReturn(userId);
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));

        ApiResponse<Note> response = noteService.deleteNote(1L, authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Note deleted successfully", response.getMessage());
        verify(notesRepository).delete(note);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent note")
    void testDeleteNonExistentNote() {
        when(userService.getCurrentUserId(authentication)).thenReturn(userId);
        when(notesRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.deleteNote(1L, authentication));

        assertTrue(exception.getMessage().contains("Note not found with id:"));
    }

    @Test
    @DisplayName("Should throw exception when deleting note of another user")
    void testDeleteNoteUnauthorized() {
        when(userService.getCurrentUserId(authentication)).thenReturn(2L);
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.deleteNote(1L, authentication));

        assertEquals("Unauthorized to delete this note", exception.getMessage());
        verify(notesRepository, never()).delete(any(Note.class));
    }

}
