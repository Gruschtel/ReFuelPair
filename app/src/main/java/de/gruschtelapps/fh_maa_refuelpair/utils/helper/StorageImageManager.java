package de.gruschtelapps.fh_maa_refuelpair.utils.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;
/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *  Edit by Eric Werner
 *  Class adapted to the app (input variables changed), bugfixing (read and save images) switch from public to private context
 */
public class StorageImageManager implements ImagesRepository{
    // https://developer.android.com/training/data-storage/files

    private static final String TAG = "LocalImagesRepository";
    private static final String PATH = "secureimages/";

    private File mStorage;

    public StorageImageManager(Context context) {
        File internalStorage = context.getFilesDir();
        mStorage = new File(internalStorage, PATH);

        if (!mStorage.exists()) {
            if (!mStorage.mkdirs()) {
                Timber.e("Could not create storage directory: %s", mStorage.getAbsolutePath());

            }
        }
    }

    /**
     * Generates a file name for the png image and stores it in local storage.
     *
     * @param image The bitmap to store.
     * @return The name of the image file.
     */
    @Override
    public String saveImage(Bitmap image) {
        final String fileName = UUID.randomUUID().toString() + ".png";
        //final String fileName = "test.png";
        File file = new File(mStorage, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            Timber.e("Error during saving of image: %s", e.getMessage());
            return null;
        }

        return fileName;
    }

    /**
     * Returns a list of all images stored in this repository.
     * An {@link Bitmap} contains a {@link Bitmap} and a string with its filename.
     *
     * @return
     */
    @Override
    public List<Bitmap> getImages() {
        File[] files = mStorage.listFiles();
        if (files == null) {
            Timber.e("Could not list files.");
            return null;
        }
        ArrayList<Bitmap> list = new ArrayList<>(files.length);
        for (File f : files) {
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            list.add(bitmap);
        }
        return list;
    }


    /**
     * Deletes the given image.
     *
     * @param fileName Filename of the image to delete.
     */
    @Override
    public void deleteImage(String fileName) {
        File file = new File(mStorage, fileName);
        if (!file.delete()) {
            Timber.e("File could not be deleted: %s", fileName);
        }else {
            Timber.d("File is deleted: %s", fileName);
        }
    }


    /**
     * Loads the given file as a bitmap.
     */
    @Override
    public Bitmap getImage(String fileName) {
        File file = new File(mStorage, fileName);
        if (!file.exists()) {
            Timber.e("File could not opened. It does not exist: %s", fileName);
            return null;
        }

        return BitmapFactory.decodeFile(file.getAbsolutePath());

    }
}