package com.google.common.base;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Charsets {
    public static final Charset ISO_8859_1 = null;
    public static final Charset US_ASCII = null;
    public static final Charset UTF_16 = null;
    public static final Charset UTF_16BE = null;
    public static final Charset UTF_16LE = null;
    public static final Charset UTF_8 = null;

    static {
        US_ASCII = StandardCharsets.US_ASCII;
        ISO_8859_1 = StandardCharsets.ISO_8859_1;
        UTF_8 = StandardCharsets.UTF_8;
        UTF_16BE = StandardCharsets.UTF_16BE;
        UTF_16LE = StandardCharsets.UTF_16LE;
        UTF_16 = StandardCharsets.UTF_16;
    }

    private Charsets() {
    }
}
