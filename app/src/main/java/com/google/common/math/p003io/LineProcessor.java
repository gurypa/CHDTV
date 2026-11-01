package com.google.common.p003io;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface LineProcessor<T> {
    @ParametricNullness
    T getResult();

    boolean processLine(String r1) throws IOException;
}
