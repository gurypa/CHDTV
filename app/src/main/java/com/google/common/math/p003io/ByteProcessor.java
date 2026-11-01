package com.google.common.p003io;

import com.google.errorprone.annotations.DoNotMock;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
@DoNotMock("Implement it normally")
public interface ByteProcessor<T> {
    @ParametricNullness
    T getResult();

    boolean processBytes(byte[] r1, int r2, int r3) throws IOException;
}
