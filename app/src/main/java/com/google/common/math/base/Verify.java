package com.google.common.base;

import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Verify {
    private Verify() {
    }

    public static void verify(boolean r1) {
        if (r1 == false) goto L5;
        return;
    L5:
        throw new VerifyException();
    }

    public static void verify(boolean r4, String r5, char r6) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r5, new Object[]{Character.valueOf(r6)}));
    }

    public static void verify(boolean r5, String r6, char r7, char r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Character.valueOf(r8)}));
    }

    public static void verify(boolean r5, String r6, char r7, int r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Integer.valueOf(r8)}));
    }

    public static void verify(boolean r5, String r6, char r7, long r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Long.valueOf(r8)}));
    }

    public static void verify(boolean r4, String r5, char r6, @CheckForNull Object r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r5, new Object[]{Character.valueOf(r6), r7}));
    }

    public static void verify(boolean r4, String r5, int r6) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r5, new Object[]{Integer.valueOf(r6)}));
    }

    public static void verify(boolean r5, String r6, int r7, char r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Character.valueOf(r8)}));
    }

    public static void verify(boolean r5, String r6, int r7, int r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Integer.valueOf(r8)}));
    }

    public static void verify(boolean r5, String r6, int r7, long r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Long.valueOf(r8)}));
    }

    public static void verify(boolean r4, String r5, int r6, @CheckForNull Object r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r5, new Object[]{Integer.valueOf(r6), r7}));
    }

    public static void verify(boolean r4, String r5, long r6) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r5, new Object[]{Long.valueOf(r6)}));
    }

    public static void verify(boolean r5, String r6, long r7, char r9) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Character.valueOf(r9)}));
    }

    public static void verify(boolean r5, String r6, long r7, int r9) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Integer.valueOf(r9)}));
    }

    public static void verify(boolean r5, String r6, long r7, long r9) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Long.valueOf(r9)}));
    }

    public static void verify(boolean r4, String r5, long r6, @CheckForNull Object r8) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r5, new Object[]{Long.valueOf(r6), r8}));
    }

    public static void verify(boolean r3, String r4, @CheckForNull Object r5) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r4, new Object[]{r5}));
    }

    public static void verify(boolean r4, String r5, @CheckForNull Object r6, char r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r5, new Object[]{r6, Character.valueOf(r7)}));
    }

    public static void verify(boolean r4, String r5, @CheckForNull Object r6, int r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r5, new Object[]{r6, Integer.valueOf(r7)}));
    }

    public static void verify(boolean r4, String r5, @CheckForNull Object r6, long r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r5, new Object[]{r6, Long.valueOf(r7)}));
    }

    public static void verify(boolean r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r4, new Object[]{r5, r6}));
    }

    public static void verify(boolean r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6, @CheckForNull Object r7) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r4, new Object[]{r5, r6, r7}));
    }

    public static void verify(boolean r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6, @CheckForNull Object r7, @CheckForNull Object r8) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r4, new Object[]{r5, r6, r7, r8}));
    }

    public static void verify(boolean r2, String r3, @CheckForNull Object... r4) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new VerifyException(Strings.lenientFormat(r3, r4));
    }

    public static <T> T verifyNotNull(@CheckForNull T r2) {
        return (T) verifyNotNull(r2, "expected a non-null reference", new Object[0]);
    }

    public static <T> T verifyNotNull(@CheckForNull T r2, String r3, @CheckForNull Object... r4) {
        if (r2 == null) goto L5;
        return r2;
    L5:
        throw new VerifyException(Strings.lenientFormat(r3, r4));
    }
}
