package com.example.examscanner.persistence.local;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class GenreConverter {
    @TypeConverter
    public String[] gettingListFromString(String genreIds) {
        List<String> list = new ArrayList<>();

        String[] array = genreIds.split(",");

        return array;
    }

    @TypeConverter
    public String writingStringFromList(String[] list) {
        String genreIds = "";
        for (String i : list) {
            genreIds += "," + i;
        }
        return genreIds;
    }}
