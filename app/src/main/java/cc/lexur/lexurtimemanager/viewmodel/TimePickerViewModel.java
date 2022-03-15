package cc.lexur.lexurtimemanager.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class TimePickerViewModel extends ViewModel {
    Calendar selectedCalendar;


    public TimePickerViewModel() {
        selectedCalendar = Calendar.getInstance();
    }

    public Calendar getSelectedCalendar() {
        return selectedCalendar;
    }

    public void setSelectedCalendar(Calendar calendar) {
        selectedCalendar = calendar;
    }

    public void setDate(Calendar calendar) {
        selectedCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), Calendar.DATE);
    }

    public void setTime(Calendar calendar) {
        selectedCalendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR));
        selectedCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        selectedCalendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
    }
}
