package com.example.huu.firestoreexampleproject;

import com.google.firebase.firestore.Exclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class Note {
    @Setter
    @Exclude
    private String documentId;
    private String title;
    private String description;
    @Setter
    private int priority;

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
}
