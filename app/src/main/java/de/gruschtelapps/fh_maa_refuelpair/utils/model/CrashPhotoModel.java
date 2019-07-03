package de.gruschtelapps.fh_maa_refuelpair.utils.model;

import android.graphics.Bitmap;

public class CrashPhotoModel {
    private String photoPath;
    private int id;

    public CrashPhotoModel() {
        this.photoPath = "";
        this.id = -1;
    }

    public CrashPhotoModel(int id, String photoPath) {
        this.photoPath = photoPath;
        this.id = id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
