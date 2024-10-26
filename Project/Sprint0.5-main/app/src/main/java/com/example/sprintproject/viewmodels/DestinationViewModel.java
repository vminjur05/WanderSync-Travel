package com.example.sprintproject.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.DestinationFragmentModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DestinationViewModel extends AndroidViewModel {
    private final MutableLiveData<DestinationFragmentModel> travelLog = new MutableLiveData<>();
    private final MutableLiveData<String> duration = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public DestinationViewModel(Application application) {
        super(application);
    }

    public LiveData<DestinationFragmentModel> getTravelLog() {
        return travelLog;
    }

    public LiveData<String> getDuration() {
        return duration;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void logTravel(String location, String estimatedStart, String estimatedEnd) {
        if (location.isEmpty() || estimatedStart.isEmpty() || estimatedEnd.isEmpty()) {
            errorMessage.setValue("Please fill in all fields");
        } else {
            travelLog.setValue(new DestinationFragmentModel(location, estimatedStart, estimatedEnd));
            errorMessage.setValue("Travel logged: " + location);
        }
    }

    public void calculateDuration(String start, String end, String durationText) {
        try {
            if (!start.isEmpty() && !end.isEmpty()) {
                Date startDateParsed = dateFormat.parse(start);
                Date endDateParsed = dateFormat.parse(end);
                long diffInMillis = endDateParsed.getTime() - startDateParsed.getTime();
                long days = diffInMillis / (1000 * 60 * 60 * 24);
                duration.setValue(String.valueOf(days));
            } else if (!start.isEmpty() && !durationText.isEmpty()) {
                Date startDateParsed = dateFormat.parse(start);
                int days = Integer.parseInt(durationText);
                long endInMillis = startDateParsed.getTime() + days * (1000 * 60 * 60 * 24);
                Date endDateCalculated = new Date(endInMillis);
                duration.setValue(dateFormat.format(endDateCalculated));
            } else if (!end.isEmpty() && !durationText.isEmpty()) {
                Date endDateParsed = dateFormat.parse(end);
                int days = Integer.parseInt(durationText);
                long startInMillis = endDateParsed.getTime() - days * (1000 * 60 * 60 * 24);
                Date startDateCalculated = new Date(startInMillis);
                duration.setValue(dateFormat.format(startDateCalculated));
            } else {
                errorMessage.setValue("Please provide at least two values");
            }
        } catch (ParseException | NumberFormatException e) {
            errorMessage.setValue("Invalid input. Please check the date format or duration.");
        }
    }

    public void resetFields() {
        duration.setValue("");
        errorMessage.setValue("Fields reset");
    }
}
