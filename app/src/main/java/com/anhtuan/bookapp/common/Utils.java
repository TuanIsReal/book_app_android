package com.anhtuan.bookapp.common;

import java.util.List;

public class Utils {
    public static String covertLongToTimeString(long time){
        long number;
        time += 1000;

        if (time < 60000){
            number = Math.floorDiv(time, 1000);
            return number + " giây trước";
        }

        if (time < 3600000){
            number = Math.floorDiv(time, 60000);
            return number + " phút trước";
        }

        if (time < 43200000){
            number = Math.floorDiv(time, 3600000);
            return number + " giờ trước";
        }

        number = Math.floorDiv(time, 43200000);
        return number + " ngày trước";
    }

    public static String toStringCategory(List<String> categoriesName){
        String show = "";
        int size = categoriesName.size();
        if (size == 0){
            return show;
        } else if (size == 1) {
            return categoriesName.get(0);
        } else {
            show = categoriesName.get(0);
            for (int i = 1; i < size; i++){
                show += (", "+ categoriesName.get(i));
            }
            return show;
        }
    }
}
