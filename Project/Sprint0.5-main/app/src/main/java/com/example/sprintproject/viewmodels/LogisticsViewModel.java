package com.example.sprintproject.viewmodels;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.LogisticsFragmentModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.util.Locale;

public class LogisticsViewModel extends ViewModel {
    private static LogisticsViewModel instance;
    // LiveData to hold the travel log
    private final MutableLiveData<LogisticsFragmentModel> travelLog = new MutableLiveData<>();
    private final MutableLiveData<String> duration = new MutableLiveData<>();
    private final MutableLiveData<String> startDate = new MutableLiveData<>();
    private final MutableLiveData<String> endDate = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // Private constructor to prevent instantiation from outside
    private LogisticsViewModel(Application application) {
        //super(application);
    }

    // Thread-safe method to get the single instance of DestinationViewModel
    public static synchronized LogisticsViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new LogisticsViewModel(application);
        }
        return instance;
    }

    public void logTravel(String location, String estimatedStart, String estimatedEnd) {
        if (location.isEmpty() || estimatedStart.isEmpty() || estimatedEnd.isEmpty()) {
            errorMessage.setValue("Please fill in all fields");
        } else {
            travelLog.setValue(new LogisticsFragmentModel(location, estimatedStart, estimatedEnd));
            errorMessage.setValue("Travel logged: " + location);
            startDate.setValue(estimatedStart);
            endDate.setValue(estimatedEnd);
        }
    }

    public DurationResultLogistics calculateDurationLogistics(String start, String end) {
        DurationResultLogistics result = new DurationResultLogistics();
        LocalDate currentDate = LocalDate.now();
        long currVsStart;
        long currVsEnd;
        try {
            Date startDateParsed = dateFormat.parse(start);
            Date currentDateParsed = dateFormat.parse(currentDate.toString());
            long diffInMillisCVS = currentDateParsed.getTime() - startDateParsed.getTime();
            currVsStart = diffInMillisCVS / (1000 * 60 * 60 * 24);

            Date endDateParsed = dateFormat.parse(end);
            long diffInMillisCVE = endDateParsed.getTime() - currentDateParsed.getTime();
            currVsEnd = diffInMillisCVE / (1000 * 60 * 60 * 24);

            if (currVsEnd < 0) {
                result.setDuration(0);
            } else if (currVsStart < 0) {
                result.setDuration(-1234);
            } else {
                result.setDuration(currVsEnd);
            }
        } catch (ParseException e) {
            errorMessage.setValue("Invalid input. Please check the date format.");
        }

        return result;

    }


    public LiveData<LogisticsFragmentModel> getTravelLog() {
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

    public static class DurationResultLogistics {
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
