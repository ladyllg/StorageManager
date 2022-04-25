package com.example.storagemanager.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.storagemanager.StorageModel;
import com.example.storagemanager.StorageModelDAO;

import java.util.List;

public class StorageViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public StorageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}