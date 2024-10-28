package com.example.sprintproject.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.sprintproject.model.DestinationFragmentModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DestinationViewModel extends AndroidViewModel {
    private static DestinationViewModel instance;

    private final MutableLiveData<DestinationFragmentModel> travelLog = new MutableLiveData<>();
    private final MutableLiveData<String> duration = new MutableLiveData<>();
    private final MutableLiveData<String> startDate = new MutableLiveData<>();
    private final MutableLiveData<String> endDate = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
            Locale.getDefault());

    // Private constructor to prevent instantiation from outside
    private DestinationViewModel(Application application) {
        super(application);
    }

    // Thread-safe method to get the single instance of DestinationViewModel
    public static synchronized DestinationViewModel getInstance(Application application) {
        if (instance == null) { // Singleton
            instance = new DestinationViewModel(application);
        }
        return instance;
    }

    public void resetFields() {
        startDate.setValue("");
        endDate.setValue("");
        duration.setValue("");
        errorMessage.setValue("Fields reset");
    }

    public LiveData<DestinationFragmentModel> getTravelLog() {
        return travelLog;
    }

    public LiveData<String> getDuration() {
        return duration;
    }

    public LiveData<String> getStartDate() {
        return startDate;
    }

    public LiveData<String> getEndDate() {
        return endDate;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void logTravel(String location, String estimatedStart, String estimatedEnd) {
        if (location.isEmpty() || estimatedStart.isEmpty() || estimatedEnd.isEmpty()) {
            errorMessage.setValue("Please fill in all fields");
        } else {
            travelLog.setValue(new DestinationFragmentModel(location,
                    estimatedStart, estimatedEnd));
            errorMessage.setValue("Travel logged: " + location);
            startDate.setValue(estimatedStart);
            endDate.setValue(estimatedEnd);
        }
    }

    public DurationResult calculateDuration(String start, String end, String duration) {
        DurationResult result = new DurationResult();
        try {
            if (!start.isEmpty() && !end.isEmpty()) {
                Date startDateParsed = dateFormat.parse(start);
                Date endDateParsed = dateFormat.parse(end);
                long diffInMillis = endDateParsed.getTime() - startDateParsed.getTime();
                long days = diffInMillis / (1000 * 60 * 60 * 24);
                result.setStartDate(startDateParsed);
                result.setEndDate(endDateParsed);
                result.setDuration(days);
            } else if (!start.isEmpty() && !duration.isEmpty()) {
                Date startDateParsed = dateFormat.parse(start);
                long days = Long.parseLong(duration);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDateParsed);
                calendar.add(Calendar.DATE, (int) days);
                result.setStartDate(startDateParsed);
                result.setEndDate(calendar.getTime());
                result.setDuration(days);
            } else if (!end.isEmpty() && !duration.isEmpty()) {
                Date endDateParsed = dateFormat.parse(end);
                long days = Long.parseLong(duration);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDateParsed);
                calendar.add(Calendar.DATE, -(int) days);
                result.setStartDate(calendar.getTime());
                result.setEndDate(endDateParsed);
                result.setDuration(days);
            }
        } catch (ParseException e) {
            errorMessage.setValue("Invalid input. Please check the date format.");
        }
        return result;
    }

    public static class DurationResult {
        private Date startDate;
        private Date endDate;
        private long duration;

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
    }


}
