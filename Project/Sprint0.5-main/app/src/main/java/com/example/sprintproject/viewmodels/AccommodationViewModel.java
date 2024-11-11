package com.example.sprintproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprintproject.model.Accommodation;


import java.util.List;

public class AccommodationViewModel extends ViewModel {
    private final MutableLiveData<List<Accommodation>> accommodationsLiveData;

    public AccommodationViewModel() {
        accommodationsLiveData = new MutableLiveData<>();
        loadAccommodations();
    }

    private void loadAccommodations() {
        // Fetch data from Firebase or other source and set value to accommodationsLiveData
        // For example:
        // List<Accommodation> accommodations = fetchDataFromFirebase();
        // accommodationsLiveData.setValue(accommodations);
    }

    public LiveData<List<Accommodation>> getAccommodations() {
        return accommodationsLiveData;
    }

    public void addAccommodation(Accommodation accommodation) {
        // Logic to add accommodation to Firebase or database
        // Update LiveData if necessary
    }
}

