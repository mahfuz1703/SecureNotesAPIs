package mahfuz.mia.SecureNotesAPIs.service;

import lombok.RequiredArgsConstructor;
import mahfuz.mia.SecureNotesAPIs.entity.Note;
import mahfuz.mia.SecureNotesAPIs.repository.NotesRepository;
import mahfuz.mia.SecureNotesAPIs.repository.UserRepository;
import mahfuz.mia.SecureNotesAPIs.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NotesRepository notesRepository;
    private  final UserService userService;

    // get all notes by user id
    public ApiResponse<List<Note>> getAllNotes(Authentication auth) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Long userId = userService.getCurrentUserId(auth);

        return new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "Notes fetched successfully",
                false,
                notesRepository.findNoteByUserId(userId)
        );
    }

    // add notes
    public ApiResponse<Note> addNote(Note note, Authentication auth) {
        if(note.getTitle() == null || note.getTitle().isEmpty()) {
            throw new RuntimeException("Note title cannot be empty or null");
        }
        if(note.getContent() == null || note.getContent().isEmpty()) {
            throw new RuntimeException("Note content cannot be empty or null");
        }

        Long userId = userService.getCurrentUserId(auth);
        note.setUserId(userId);
        notesRepository.save(note);

        ApiResponse<Note> response = new ApiResponse<>(
                HttpStatus.CREATED.toString(),
                201,
                "Note added successfully",
                false,
                note
        );
        return response;
    }

    // Update notes
    public  ApiResponse<Note> updateNote(Note note, Authentication auth) {
        Long userId = userService.getCurrentUserId(auth);

        Note noteToUpdate = notesRepository.findById(note.getId())
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + note.getId()));

        if (!noteToUpdate.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this note");
        }

        if(note.getTitle() != null) {
            noteToUpdate.setTitle(note.getTitle());
        }
        if (note.getContent() != null) {
            noteToUpdate.setContent(note.getContent());
        }

        notesRepository.save(noteToUpdate);
        return new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "Note updated successfully",
                false,
                noteToUpdate
        );
    }

    // Delete notes of a user
    public ApiResponse<Note> deleteNote(Long noteId, Authentication auth){
        Long userId = userService.getCurrentUserId(auth);

        Note noteToDelete = notesRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + noteId));

        if (!noteToDelete.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this note");
        }

        notesRepository.delete(noteToDelete);
        return new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "Note deleted successfully",
                false,
                noteToDelete
        );
    }
}
