package mahfuz.mia.SecureNotesAPIs.controller;

import lombok.RequiredArgsConstructor;
import mahfuz.mia.SecureNotesAPIs.entity.Note;
import mahfuz.mia.SecureNotesAPIs.response.ApiResponse;
import mahfuz.mia.SecureNotesAPIs.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // find all notes of all users
    @GetMapping("/notes")
    public ResponseEntity<ApiResponse<List<Note>>> getAllNotes(){
        return ResponseEntity.ok(adminService.getAllNotes());
    }

    // find all notes of a user by userId
    @GetMapping("/notes/user/{userId}")
    public ResponseEntity<ApiResponse<List<Note>>> getAllNotesByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(adminService.getAllNotesByUserId(userId));
    }

    // delete note by id
    @DeleteMapping("/notes/delete/{noteId}")
    public ResponseEntity<ApiResponse<Note>> deleteNoteById(@PathVariable Long noteId) {
        return ResponseEntity.ok(adminService.deleteNoteById(noteId));
    }
}
