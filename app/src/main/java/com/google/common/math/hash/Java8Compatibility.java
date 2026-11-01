package com.google.common.hash;

import java.nio.Buffer;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class Java8Compatibility {
    private Java8Compatibility() {
    }

    static void clear(Buffer r0) {
        r0.clear();
    }

    static void flip(Buffer r0) {
        r0.flip();
    }

    static void limit(Buffer r0, int r1) {
        r0.limit(r1);
    }

    static void position(Buffer r0, int r1) {
        r0.position(r1);
    }
}
