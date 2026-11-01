package com.google.common.escape;

import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.CheckForNull;
import kotlin.jvm.internal.CharCompanionObject;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ArrayBasedCharEscaper extends CharEscaper {
    private final char[][] replacements;
    private final int replacementsLength;
    private final char safeMax;
    private final char safeMin;

    protected ArrayBasedCharEscaper(ArrayBasedEscaperMap r2, char r3, char r4) {
        Preconditions.checkNotNull(r2);
        this.replacements = r2.getReplacementArray();
        this.replacementsLength = this.replacements.length;
        if (r4 >= r3) goto L5;
        r4 = 0;
        r3 = CharCompanionObject.MAX_VALUE;
    L5:
        this.safeMin = r3;
        this.safeMax = r4;
    }

    protected ArrayBasedCharEscaper(Map<Character, String> r2, char r3, char r4) {
        this(ArrayBasedEscaperMap.create(r2), r3, r4);
    }

    @Override // com.google.common.escape.CharEscaper, com.google.common.escape.Escaper
    public final String escape(String r4) {
        Preconditions.checkNotNull(r4);
        int r0 = 0;
    L4:
        if (r0 >= r4.length()) goto L17;
        char r1 = r4.charAt(r0);
        if (r1 >= this.replacementsLength) goto L10;
        if (this.replacements[r1] == null) goto L10;
    L16:
        return escapeSlow(r4, r0);
    L10:
        if (r1 > this.safeMax) goto L16;
        if (r1 < this.safeMin) goto L16;
        r0 = r0 + 1;
        goto L4
    L17:
        return r4;
    }

    @Override // com.google.common.escape.CharEscaper
    @CheckForNull
    protected final char[] escape(char r2) {
        if (r2 >= this.replacementsLength) goto L8;
        char[] r0 = this.replacements[r2];
        if (r0 == null) goto L8;
        return r0;
    L8:
        if (r2 < this.safeMin) goto L14;
        if (r2 > this.safeMax) goto L14;
        return null;
    L14:
        return escapeUnsafe(r2);
    }

    @CheckForNull
    protected abstract char[] escapeUnsafe(char r1);
}
