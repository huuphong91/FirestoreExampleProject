package com.example.huu.firestoreexampleproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText edtTitle, edtDescription;
    private TextView tvData;
    private CollectionReference noteBookRef = db.collection("NoteBook");
    private DocumentReference noteRef = db.collection("NoteBook").document("My First Note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        tvData = findViewById(R.id.tvData);
    }

    @Override
    protected void onStart() {
        super.onStart();
       noteBookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
               if (e != null) {
                   return;
               }
               String data = "";
               for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                   Note note = documentSnapshot.toObject(Note.class);
                   note.setDocumentId(documentSnapshot.getId());
                   String documentId = note.getDocumentId();
                   String title = note.getTitle();
                   String description = note.getDescription();
                   data += "ID: "+documentId+"\nTitle: " + title + "\nDescription: " + description + "\n\n";
               }
               tvData.setText(data);
           }
       });
    }

    public void addButton(View view) {
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        Note note = new Note(title, description);

        noteBookRef.add(note);
    }

    public void loadButton(View view) {
     noteBookRef.get().addOnSuccessListener( new OnSuccessListener<QuerySnapshot>() {
         @Override
         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
             String data = "";
             for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                 Note note = documentSnapshot.toObject(Note.class);
                 note.setDocumentId(documentSnapshot.getId());
                 String documentId = note.getDocumentId();
                 String title = note.getTitle();
                 String description = note.getDescription();
                 data += "ID: "+documentId+"\nTitle: " + title + "\nDescription: " + description + "\n\n";
             }
             tvData.setText(data);
         }
     });
    }




}
