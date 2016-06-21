package com.cezarykluczynski.carmen.util.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class DateFactory {

    private NowDateProvider nowDateProvider;

    @Autowired
    public DateFactory(NowDateProvider nowDateProvider) {
        this.nowDateProvider = nowDateProvider;
    }

    public Date getEndOfAlreadyClosedMonth() {
        Date now = nowDateProvider.createNowDate();
        boolean isTwoMonthsApart = !isPreviousMonthClosed(now);
        int currentMonth = now.getMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.roll(Calendar.MONTH, false);
        if (isTwoMonthsApart) {
            calendar.roll(Calendar.MONTH, false);
        }

        if (isTwoMonthsApart && currentMonth <= 1 || !isTwoMonthsApart && currentMonth == 0) {
            calendar.roll(Calendar.YEAR, false);
        }

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    public Date getBeginningOfNotYetOpenedMonth() {
        Date now = nowDateProvider.createNowDate();
        boolean isOneMonthApart = isPreviousMonthClosed(now);
        int currentMonth = now.getMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (!isOneMonthApart) {
            calendar.roll(Calendar.MONTH, false);

            if (currentMonth == 0) {
                calendar.roll(Calendar.YEAR, false);
            }
        }

        return calendar.getTime();
    }

    public Date getDateSinceForever() {
        // Git does not interpret --since=0 as since Epoch,
        // and --since=-1 has to be used as an equivalent of since forever
        Date date = new Date();
        date.setTime(-1);
        return date;
    }

    private static boolean isPreviousMonthClosed(Date date) {
        return date.getDate() >= 15;
    }

}
