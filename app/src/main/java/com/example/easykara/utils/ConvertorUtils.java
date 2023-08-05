package com.example.easykara.utils;

import java.util.List;

public class ConvertorUtils {

    public static int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        for(int i = 0; i < ret.length; i++)
            ret[i] = list.get(i);
        return ret;
    }
}
