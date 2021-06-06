package com.example.firebasecrud;

public class Track {
        private String trackId;
        private String trackName;
        private int trackRating;

        public Track() {

        }

        public Track(String id, String trackName, int trackRating) {
            this.trackName = trackName;
            this.trackRating = trackRating;
            this.trackId = trackId;
        }

        public String getTrackName() {
            return trackName;
        }

        public int gettrackRating() {
            return trackRating;
        }
    }
