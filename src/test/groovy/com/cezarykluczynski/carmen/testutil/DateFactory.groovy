package com.cezarykluczynski.carmen.testutil

class DateFactory {

    public static of(int year, int month, int day) {
        return new Date(year: year, month: month, date: day)
    }

}
