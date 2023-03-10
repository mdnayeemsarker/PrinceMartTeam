/*
 * Copyright 2017 Yan Zhenjie.
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
 */
package com.princemartbd.team.helper.album.app.album.data;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import com.princemartbd.team.helper.album.Album;
import com.princemartbd.team.helper.album.AlbumFile;
import com.princemartbd.team.helper.album.AlbumFolder;


public class MediaReadTask extends AsyncTask<Void, Void, MediaReadTask.ResultWrapper> {

    public interface Callback {
        /**
         * Callback the results.
         *
         * @param albumFolders album folder list.
         */
        void onScanCallback(ArrayList<AlbumFolder> albumFolders, ArrayList<AlbumFile> checkedFiles);
    }

    static class ResultWrapper {
        private ArrayList<AlbumFolder> mAlbumFolders;
        private ArrayList<AlbumFile> mAlbumFiles;
    }

    private final int mFunction;
    private final List<AlbumFile> mCheckedFiles;
    private final MediaReader mMediaReader;
    private final Callback mCallback;

    public MediaReadTask(int function, List<AlbumFile> checkedFiles, MediaReader mediaReader, Callback callback) {
        this.mFunction = function;
        this.mCheckedFiles = checkedFiles;
        this.mMediaReader = mediaReader;
        this.mCallback = callback;
    }

    @Override
    protected ResultWrapper doInBackground(Void... params) {
        ArrayList<AlbumFolder> albumFolders;
        switch (mFunction) {
            case Album.FUNCTION_CHOICE_IMAGE: {
                albumFolders = mMediaReader.getAllImage();
                break;
            }
            case Album.FUNCTION_CHOICE_VIDEO: {
                albumFolders = mMediaReader.getAllVideo();
                break;
            }
            case Album.FUNCTION_CHOICE_ALBUM: {
                albumFolders = mMediaReader.getAllMedia();
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }

        ArrayList<AlbumFile> checkedFiles = new ArrayList<>();

        if (mCheckedFiles != null && !mCheckedFiles.isEmpty()) {
            List<AlbumFile> albumFiles = albumFolders.get(0).getAlbumFiles();
            for (AlbumFile checkAlbumFile : mCheckedFiles) {
                for (int i = 0; i < albumFiles.size(); i++) {
                    AlbumFile albumFile = albumFiles.get(i);
                    if (checkAlbumFile.equals(albumFile)) {
                        albumFile.setChecked(true);
                        checkedFiles.add(albumFile);
                    }
                }
            }
        }
        ResultWrapper wrapper = new ResultWrapper();
        wrapper.mAlbumFolders = albumFolders;
        wrapper.mAlbumFiles = checkedFiles;
        return wrapper;
    }

    @Override
    protected void onPostExecute(ResultWrapper wrapper) {
        mCallback.onScanCallback(wrapper.mAlbumFolders, wrapper.mAlbumFiles);
    }
}