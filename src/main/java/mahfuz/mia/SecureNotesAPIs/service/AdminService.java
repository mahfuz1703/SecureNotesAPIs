package mahfuz.mia.SecureNotesAPIs.service;

import lombok.RequiredArgsConstructor;
import mahfuz.mia.SecureNotesAPIs.entity.Note;
import mahfuz.mia.SecureNotesAPIs.repository.NotesRepository;
import mahfuz.mia.SecureNotesAPIs.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final NotesRepository notesRepository;

    // get all notes of all users
    public ApiResponse<List<Note>> getAllNotes(){
        return new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "All notes fetched successfully",
                false,
                notesRepository.findAll()
        );
    }

    // get all notes of a user by userId
    public ApiResponse<List<Note>> getAllNotesByUserId(Long userId){
        return new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "Notes fetched successfully for userId: " + userId,
                false,
                notesRepository.findNoteByUserId(userId)
        );
    }

    // delete note by id
    public ApiResponse<Note> deleteNoteById(Long noteId) {
        Note noteToDelete = notesRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + noteId));
        notesRepository.delete(noteToDelete);
        return new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "Note deleted successfully with id: " + noteId,
                false,
                noteToDelete
        );
    }
}
