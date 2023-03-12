package ua.knu.csc.iss.ynortman.enoder;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;

import java.util.Arrays;

@Slf4j
public class Encoder {
    private final String alphabet = "abcdefghijklmnopqrstuvwxyz " +
            "0123456789" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            ".,?/!@#$;{}[]%^:&*()_-+=";
    private final BiMap<String, RingInteger> map;
    private static int p = 0;
    private static Encoder instance = null;
    private Encoder() {
        this.map = HashBiMap.create();
        String[] chars = alphabet.split("");
        for(int i = 0; i < chars.length && i < p-1; ++i) {
            try {
                map.put(chars[i], RingInteger.valueOf(i, p)/*FieldUtils.getRandomBigInteger(p)*/);
            } catch (IllegalArgumentException e) {
                log.error("Duplicate values");
                i--;
            }
        }
        map.put("", RingInteger.valueOf(
                p <= chars.length ? p-1 : chars.length,
                p));
        log.debug("Map: {}", this.map);
    }

    public static Encoder getInstance(int p1) {
        if(instance == null || p != p1) {
            p = p1;
            log.debug("New Encoder");
            instance = new Encoder();

        }
        return instance;
    }

    public RingInteger[] textToNumbers(String text) {
        log.debug("Numbers to text");
        String[] chars = text.split("");
        RingInteger[] numbers = new RingInteger[text.length()];
        for(int i = 0; i < chars.length; ++i) {
            RingInteger number = map.get(chars[i]);
            if(number == null) {
                log.warn("Symbol {} cannot be found in map", chars[i]);
                number = map.get("");
            }
            numbers[i] = number;
        }
        log.debug("Text: {}, numbers: {}", text, Arrays.toString(numbers));
        return numbers;
    }

    public String numbersToText(RingInteger[] numbers) {
        StringBuilder builder = new StringBuilder();
        for (RingInteger number : numbers) {
            builder.append(map.inverse().get(number));
        }
        log.debug("Numbers: {}, text: {}", Arrays.toString(numbers), builder.toString());
        return builder.toString();
    }

    public RingInteger getCharNumber(String ch) {
        return map.get(ch);
    }
}
