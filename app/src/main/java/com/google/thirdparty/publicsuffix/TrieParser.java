package com.google.thirdparty.publicsuffix;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Queues;
import java.util.Deque;

/* JADX INFO: loaded from: classes.dex */
final class TrieParser {
    private static final Joiner DIRECT_JOINER = null;

    static {
        DIRECT_JOINER = Joiner.m184on("");
    }

    TrieParser() {
    }

    private static int doParseTrieToBuilder(Deque<CharSequence> r8, CharSequence r9, int r10, ImmutableMap.Builder<String, PublicSuffixType> r11) {
        int r0 = r9.length();
        int r1 = r10;
        char r2 = 0;
    L4:
        if (r1 >= r0) goto L13;
        r2 = r9.charAt(r1);
        if (r2 == '&') goto L13;
        if (r2 == '?') goto L13;
        if (r2 == '!') goto L13;
        if (r2 == ':') goto L13;
        if (r2 == ',') goto L13;
        r1 = r1 + 1;
    L13:
        r8.push(reverse(r9.subSequence(r10, r1)));
        if (r2 == '!') goto L18;
        if (r2 == '?') goto L18;
        if (r2 == ':') goto L18;
        if (r2 == ',') goto L18;
    L21:
        int r3 = r1 + 1;
        if (r2 == '?') goto L30;
        if (r2 == ',') goto L30;
    L24:
        if (r3 >= r0) goto L30;
        r3 = r3 + doParseTrieToBuilder(r8, r9, r3, r11);
        if (r9.charAt(r3) == '?') goto L29;
        if (r9.charAt(r3) != ',') goto L24;
    L29:
        r3 = r3 + 1;
    L30:
        r8.pop();
        return r3 - r10;
    L18:
        String r4 = DIRECT_JOINER.join(r8);
        if (r4.length() <= 0) goto L21;
        r11.put(r4, PublicSuffixType.fromCode(r2));
        goto L21
    }

    static ImmutableMap<String, PublicSuffixType> parseFullString(String r4) {
        ImmutableMap.Builder r0 = ImmutableMap.builder();
        int r1 = r4.length();
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L6;
        r2 = r2 + doParseTrieToBuilder(Queues.newArrayDeque(), r4, r2, r0);
        goto L3
    L6:
        return r0.buildOrThrow();
    }

    static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence... r2) {
        return parseFullString(DIRECT_JOINER.join(r2));
    }

    private static CharSequence reverse(CharSequence r1) {
        return new StringBuilder(r1).reverse();
    }
}
