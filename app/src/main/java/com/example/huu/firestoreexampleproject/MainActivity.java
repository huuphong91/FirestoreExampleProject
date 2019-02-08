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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }
                if (documentSnapshot.exists()) {
                    Note note = documentSnapshot.toObject(Note.class);
                    String title = note.getTitle();
                    String description = note.getDescription();
                    tvData.setText("Title: "+ title + "\n Description: "+ description);
                } else {
                    tvData.setText("");
                }
            }
        });
    }

    public void saveButton(View view) {
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        Note note = new Note(title, description);

        noteRef.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
    }

    public void loadButton(View view) {
        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Note note = documentSnapshot.toObject(Note.class);
                    String title = note.getTitle();
                    String description = note.getDescription();
                    tvData.setText("Title: "+ title + "\n Description: "+ description);
                } else {
                    Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                } 
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateDescriptionButton(View view) {
        //Merge
//        String description = edtDescription.getText().toString();
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_DESCRIPTION, description);
//        noteRef.set(note, SetOptions.merge());

        //Update
        String description = edtDescription.getText().toString();
        noteRef.update(KEY_DESCRIPTION, description);
        //Update nếu có dữ liệu trên db thì nó mới cập nhật, còn nếu không thì sẽ không có gì thay đổi hết.
        // Còn thằng merge thì dù có dữ liệu trên db hay không nó vẫn đưa dữ liệu cập nhật lên db bằng cách tạo mới nếu không có dữ liệu.
    }

    public void deleteDescriptionButton(View view) {
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete());
    }

    public void deleteNote(View view) {
        noteRef.delete();
    }
}
