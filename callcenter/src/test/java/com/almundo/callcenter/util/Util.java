package com.almundo.callcenter.util;

import com.almundo.callcenter.object.Call;

import java.util.concurrent.ThreadLocalRandom;

public final class Util {

    private Util(){

        throw new AssertionError();
    }

    public static Call buildRandomCall(int minCallDuration, int maxCallDuration) {

        return new Call(ThreadLocalRandom.current().nextInt(minCallDuration, maxCallDuration));

    }
}