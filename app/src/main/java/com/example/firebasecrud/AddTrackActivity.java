package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTrackActivity extends AppCompatActivity {
    //define the view
    Button btnAddTrack;
    EditText editTextTrackName;
    SeekBar seekBarRating;
    TextView textViewRating, textViewArtistName;
    ListView listViewTracks;

    //define the databaseReference for Tracks Database
    DatabaseReference databaseTracks;

    List<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        //initialize all the view
        btnAddTrack = (Button) findViewById(R.id.btnAddTrack);
        editTextTrackName = (EditText) findViewById(R.id.editTrackName);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        textViewRating = (TextView) findViewById(R.id.textViewRating);
        textViewArtistName = (TextView) findViewById(R.id.textViewArtistName);
        listViewTracks = (ListView) findViewById(R.id.listViewTracks);

        //Add another intent
        Intent intent = getIntent();

        tracks = new ArrayList<>();

        String id = intent.getStringExtra(MainActivity.ARTIST_ID);
        String name = intent.getStringExtra(MainActivity.ARTIST_NAME);

        textViewArtistName.setText(name);

        //get the track node from the firebase database
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        btnAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTrack();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshotsnapshot) {
            tracks.clear();
            for (DataSnapshot trackSnapshot : dataSnapshotsnapshot.getChildren()){
                Track track = trackSnapshot.getValue(Track.class);
                tracks.add(track);
            }
            TrackList trackListAdapter = new TrackList(AddTrackActivity.this, tracks);
            listViewTracks.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

    }
    private void saveTrack() {
        String trackName = editTextTrackName.getText().toString().trim();
        int Trackrating = seekBarRating.getProgress();
        if (!TextUtils.isEmpty(trackName)) {
            String id = databaseTracks.push().getKey();
            Track track = new Track(id, trackName, Trackrating);
            databaseTracks.child(id).setValue(track);
            Toast.makeText(this, "Track saved", Toast.LENGTH_LONG).show();
            editTextTrackName.setText("");
        } else {
            Toast.makeText(this, "Please enter track name", Toast.LENGTH_LONG).show();
        }
    }

}