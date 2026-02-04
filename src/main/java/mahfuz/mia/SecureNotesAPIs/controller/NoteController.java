package mahfuz.mia.SecureNotesAPIs.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mahfuz.mia.SecureNotesAPIs.entity.Note;
import mahfuz.mia.SecureNotesAPIs.response.ApiResponse;
import mahfuz.mia.SecureNotesAPIs.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    // Get all notes for a user
    @GetMapping
    public ResponseEntity<ApiResponse<List<Note>>> getAllNotes(Authentication auth) {
        return ResponseEntity.ok(noteService.getAllNotes(auth));
    }

    // Add a new note for a user
    @PostMapping
    public ResponseEntity<ApiResponse<Note>> addNote(@RequestBody Note note, Authentication auth) {
        return ResponseEntity.ok(noteService.addNote(note, auth));
    }

    // Update an existing note for a user
    @PutMapping
    public ResponseEntity<ApiResponse<Note>> updateNote(@RequestBody Note note, Authentication auth) {
        return ResponseEntity.ok(noteService.updateNote(note, auth));
    }

    // Delete a note for a user
    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApiResponse<Note>> deleteNote(@PathVariable Long noteId, Authentication auth) {
        return ResponseEntity.ok(noteService.deleteNote(noteId, auth));
    }
}
