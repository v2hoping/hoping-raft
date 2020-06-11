package com.v2hoping.common;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by houping wang on 2020/6/11
 *
 * @author houping wang
 */
public class RandomUtils {

    public static Integer nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max) % (max - min + 1) + min;
    }
}
