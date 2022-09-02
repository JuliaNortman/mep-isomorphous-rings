package ua.knu.csc.iss.ynortman.utils;

public class ArrayUtils {
    public static <T> T[] swap (T[] arr, int i, int j) {
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
        return arr;
    }
}
