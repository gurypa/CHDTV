package com.google.common.escape;

import com.google.common.base.Preconditions;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ArrayBasedEscaperMap {
    private static final char[][] EMPTY_REPLACEMENT_ARRAY = null;
    private final char[][] replacementArray;

    static {
        EMPTY_REPLACEMENT_ARRAY = (char[][]) Array.newInstance(Character.TYPE, new int[]{0, 0});
    }

    private ArrayBasedEscaperMap(char[][] r1) {
        this.replacementArray = r1;
    }

    public static ArrayBasedEscaperMap create(Map<Character, String> r2) {
        return new ArrayBasedEscaperMap(createReplacementArray(r2));
    }

    static char[][] createReplacementArray(Map<Character, String> r6) {
        Preconditions.checkNotNull(r6);
        if (r6.isEmpty() == true) goto L5;
        char[][] r1 = new char[((Character) Collections.max(r6.keySet())).charValue() + 1][];
        Iterator<Character> r2 = r6.keySet().iterator();
    L8:
        if (r2.hasNext() == false) goto L10;
        Character r3 = r2.next();
        r1[r3.charValue()] = r6.get(r3).toCharArray();
        goto L8
    L10:
        return r1;
    L5:
        return EMPTY_REPLACEMENT_ARRAY;
    }

    char[][] getReplacementArray() {
        return this.replacementArray;
    }
}
