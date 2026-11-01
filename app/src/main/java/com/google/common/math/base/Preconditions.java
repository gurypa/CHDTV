package com.google.common.base;

import com.arthenica.ffmpegkit.StreamInformation;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Preconditions {
    private Preconditions() {
    }

    private static String badElementIndex(int r6, int r7, String r8) {
        if (r6 < 0) goto L5;
        if (r7 < 0) goto L10;
        return Strings.lenientFormat("%s (%s) must be less than size (%s)", new Object[]{r8, Integer.valueOf(r6), Integer.valueOf(r7)});
    L10:
        throw new IllegalArgumentException("negative size: " + r7);
    L5:
        return Strings.lenientFormat("%s (%s) must not be negative", new Object[]{r8, Integer.valueOf(r6)});
    }

    private static String badPositionIndex(int r6, int r7, String r8) {
        if (r6 < 0) goto L5;
        if (r7 < 0) goto L10;
        return Strings.lenientFormat("%s (%s) must not be greater than size (%s)", new Object[]{r8, Integer.valueOf(r6), Integer.valueOf(r7)});
    L10:
        throw new IllegalArgumentException("negative size: " + r7);
    L5:
        return Strings.lenientFormat("%s (%s) must not be negative", new Object[]{r8, Integer.valueOf(r6)});
    }

    private static String badPositionIndexes(int r4, int r5, int r6) {
        if (r4 < 0) goto L13;
        if (r4 > r6) goto L13;
        if (r5 < 0) goto L11;
        if (r5 > r6) goto L11;
        return Strings.lenientFormat("end index (%s) must not be less than start index (%s)", new Object[]{Integer.valueOf(r5), Integer.valueOf(r4)});
    L11:
        return badPositionIndex(r5, r6, "end index");
    L13:
        return badPositionIndex(r4, r6, "start index");
    }

    public static void checkArgument(boolean r1) {
        if (r1 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException();
    }

    public static void checkArgument(boolean r2, @CheckForNull Object r3) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(String.valueOf(r3));
    }

    public static void checkArgument(boolean r4, String r5, char r6) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r5, new Object[]{Character.valueOf(r6)}));
    }

    public static void checkArgument(boolean r5, String r6, char r7, char r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Character.valueOf(r8)}));
    }

    public static void checkArgument(boolean r5, String r6, char r7, int r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Integer.valueOf(r8)}));
    }

    public static void checkArgument(boolean r5, String r6, char r7, long r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Long.valueOf(r8)}));
    }

    public static void checkArgument(boolean r4, String r5, char r6, @CheckForNull Object r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r5, new Object[]{Character.valueOf(r6), r7}));
    }

    public static void checkArgument(boolean r4, String r5, int r6) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r5, new Object[]{Integer.valueOf(r6)}));
    }

    public static void checkArgument(boolean r5, String r6, int r7, char r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Character.valueOf(r8)}));
    }

    public static void checkArgument(boolean r5, String r6, int r7, int r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Integer.valueOf(r8)}));
    }

    public static void checkArgument(boolean r5, String r6, int r7, long r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Long.valueOf(r8)}));
    }

    public static void checkArgument(boolean r4, String r5, int r6, @CheckForNull Object r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r5, new Object[]{Integer.valueOf(r6), r7}));
    }

    public static void checkArgument(boolean r4, String r5, long r6) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r5, new Object[]{Long.valueOf(r6)}));
    }

    public static void checkArgument(boolean r5, String r6, long r7, char r9) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Character.valueOf(r9)}));
    }

    public static void checkArgument(boolean r5, String r6, long r7, int r9) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Integer.valueOf(r9)}));
    }

    public static void checkArgument(boolean r5, String r6, long r7, long r9) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Long.valueOf(r9)}));
    }

    public static void checkArgument(boolean r4, String r5, long r6, @CheckForNull Object r8) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r5, new Object[]{Long.valueOf(r6), r8}));
    }

    public static void checkArgument(boolean r3, String r4, @CheckForNull Object r5) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r4, new Object[]{r5}));
    }

    public static void checkArgument(boolean r4, String r5, @CheckForNull Object r6, char r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r5, new Object[]{r6, Character.valueOf(r7)}));
    }

    public static void checkArgument(boolean r4, String r5, @CheckForNull Object r6, int r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r5, new Object[]{r6, Integer.valueOf(r7)}));
    }

    public static void checkArgument(boolean r4, String r5, @CheckForNull Object r6, long r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r5, new Object[]{r6, Long.valueOf(r7)}));
    }

    public static void checkArgument(boolean r3, @CheckForNull String r4, @CheckForNull Object r5, @CheckForNull Object r6) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r4, new Object[]{r5, r6}));
    }

    public static void checkArgument(boolean r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6, @CheckForNull Object r7) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r4, new Object[]{r5, r6, r7}));
    }

    public static void checkArgument(boolean r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6, @CheckForNull Object r7, @CheckForNull Object r8) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r4, new Object[]{r5, r6, r7, r8}));
    }

    public static void checkArgument(boolean r2, String r3, @CheckForNull Object... r4) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(Strings.lenientFormat(r3, r4));
    }

    public static int checkElementIndex(int r1, int r2) {
        return checkElementIndex(r1, r2, StreamInformation.KEY_INDEX);
    }

    public static int checkElementIndex(int r2, int r3, String r4) {
        if (r2 < 0) goto L6;
        if (r2 >= r3) goto L6;
        return r2;
    L6:
        throw new IndexOutOfBoundsException(badElementIndex(r2, r3, r4));
    }

    public static <T> T checkNotNull(@CheckForNull T r1) {
        if (r1 == null) goto L5;
        return r1;
    L5:
        throw new NullPointerException();
    }

    public static <T> T checkNotNull(@CheckForNull T r2, @CheckForNull Object r3) {
        if (r2 == null) goto L5;
        return r2;
    L5:
        throw new NullPointerException(String.valueOf(r3));
    }

    public static <T> T checkNotNull(@CheckForNull T r4, String r5, char r6) {
        if (r4 == null) goto L5;
        return r4;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r5, new Object[]{Character.valueOf(r6)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r5, String r6, char r7, char r8) {
        if (r5 == null) goto L5;
        return r5;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Character.valueOf(r8)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r5, String r6, char r7, int r8) {
        if (r5 == null) goto L5;
        return r5;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Integer.valueOf(r8)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r5, String r6, char r7, long r8) {
        if (r5 == null) goto L5;
        return r5;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Long.valueOf(r8)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r4, String r5, char r6, @CheckForNull Object r7) {
        if (r4 == null) goto L5;
        return r4;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r5, new Object[]{Character.valueOf(r6), r7}));
    }

    public static <T> T checkNotNull(@CheckForNull T r4, String r5, int r6) {
        if (r4 == null) goto L5;
        return r4;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r5, new Object[]{Integer.valueOf(r6)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r5, String r6, int r7, char r8) {
        if (r5 == null) goto L5;
        return r5;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Character.valueOf(r8)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r5, String r6, int r7, int r8) {
        if (r5 == null) goto L5;
        return r5;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Integer.valueOf(r8)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r5, String r6, int r7, long r8) {
        if (r5 == null) goto L5;
        return r5;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Long.valueOf(r8)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r4, String r5, int r6, @CheckForNull Object r7) {
        if (r4 == null) goto L5;
        return r4;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r5, new Object[]{Integer.valueOf(r6), r7}));
    }

    public static <T> T checkNotNull(@CheckForNull T r4, String r5, long r6) {
        if (r4 == null) goto L5;
        return r4;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r5, new Object[]{Long.valueOf(r6)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r5, String r6, long r7, char r9) {
        if (r5 == null) goto L5;
        return r5;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Character.valueOf(r9)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r5, String r6, long r7, int r9) {
        if (r5 == null) goto L5;
        return r5;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Integer.valueOf(r9)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r5, String r6, long r7, long r9) {
        if (r5 == null) goto L5;
        return r5;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Long.valueOf(r9)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r4, String r5, long r6, @CheckForNull Object r8) {
        if (r4 == null) goto L5;
        return r4;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r5, new Object[]{Long.valueOf(r6), r8}));
    }

    public static <T> T checkNotNull(@CheckForNull T r3, String r4, @CheckForNull Object r5) {
        if (r3 == null) goto L5;
        return r3;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r4, new Object[]{r5}));
    }

    public static <T> T checkNotNull(@CheckForNull T r4, String r5, @CheckForNull Object r6, char r7) {
        if (r4 == null) goto L5;
        return r4;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r5, new Object[]{r6, Character.valueOf(r7)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r4, String r5, @CheckForNull Object r6, int r7) {
        if (r4 == null) goto L5;
        return r4;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r5, new Object[]{r6, Integer.valueOf(r7)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r4, String r5, @CheckForNull Object r6, long r7) {
        if (r4 == null) goto L5;
        return r4;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r5, new Object[]{r6, Long.valueOf(r7)}));
    }

    public static <T> T checkNotNull(@CheckForNull T r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6) {
        if (r3 == null) goto L5;
        return r3;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r4, new Object[]{r5, r6}));
    }

    public static <T> T checkNotNull(@CheckForNull T r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6, @CheckForNull Object r7) {
        if (r3 == null) goto L5;
        return r3;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r4, new Object[]{r5, r6, r7}));
    }

    public static <T> T checkNotNull(@CheckForNull T r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6, @CheckForNull Object r7, @CheckForNull Object r8) {
        if (r3 == null) goto L5;
        return r3;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r4, new Object[]{r5, r6, r7, r8}));
    }

    public static <T> T checkNotNull(@CheckForNull T r2, String r3, @CheckForNull Object... r4) {
        if (r2 == null) goto L5;
        return r2;
    L5:
        throw new NullPointerException(Strings.lenientFormat(r3, r4));
    }

    public static int checkPositionIndex(int r1, int r2) {
        return checkPositionIndex(r1, r2, StreamInformation.KEY_INDEX);
    }

    public static int checkPositionIndex(int r2, int r3, String r4) {
        if (r2 < 0) goto L6;
        if (r2 > r3) goto L6;
        return r2;
    L6:
        throw new IndexOutOfBoundsException(badPositionIndex(r2, r3, r4));
    }

    public static void checkPositionIndexes(int r2, int r3, int r4) {
        if (r2 < 0) goto L7;
        if (r3 < r2) goto L7;
        if (r3 > r4) goto L7;
        return;
    L7:
        throw new IndexOutOfBoundsException(badPositionIndexes(r2, r3, r4));
    }

    public static void checkState(boolean r1) {
        if (r1 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException();
    }

    public static void checkState(boolean r2, @CheckForNull Object r3) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(String.valueOf(r3));
    }

    public static void checkState(boolean r4, String r5, char r6) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r5, new Object[]{Character.valueOf(r6)}));
    }

    public static void checkState(boolean r5, String r6, char r7, char r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Character.valueOf(r8)}));
    }

    public static void checkState(boolean r5, String r6, char r7, int r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Integer.valueOf(r8)}));
    }

    public static void checkState(boolean r5, String r6, char r7, long r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r6, new Object[]{Character.valueOf(r7), Long.valueOf(r8)}));
    }

    public static void checkState(boolean r4, String r5, char r6, @CheckForNull Object r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r5, new Object[]{Character.valueOf(r6), r7}));
    }

    public static void checkState(boolean r4, String r5, int r6) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r5, new Object[]{Integer.valueOf(r6)}));
    }

    public static void checkState(boolean r5, String r6, int r7, char r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Character.valueOf(r8)}));
    }

    public static void checkState(boolean r5, String r6, int r7, int r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Integer.valueOf(r8)}));
    }

    public static void checkState(boolean r5, String r6, int r7, long r8) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r6, new Object[]{Integer.valueOf(r7), Long.valueOf(r8)}));
    }

    public static void checkState(boolean r4, String r5, int r6, @CheckForNull Object r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r5, new Object[]{Integer.valueOf(r6), r7}));
    }

    public static void checkState(boolean r4, String r5, long r6) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r5, new Object[]{Long.valueOf(r6)}));
    }

    public static void checkState(boolean r5, String r6, long r7, char r9) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Character.valueOf(r9)}));
    }

    public static void checkState(boolean r5, String r6, long r7, int r9) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Integer.valueOf(r9)}));
    }

    public static void checkState(boolean r5, String r6, long r7, long r9) {
        if (r5 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r6, new Object[]{Long.valueOf(r7), Long.valueOf(r9)}));
    }

    public static void checkState(boolean r4, String r5, long r6, @CheckForNull Object r8) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r5, new Object[]{Long.valueOf(r6), r8}));
    }

    public static void checkState(boolean r3, String r4, @CheckForNull Object r5) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r4, new Object[]{r5}));
    }

    public static void checkState(boolean r4, String r5, @CheckForNull Object r6, char r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r5, new Object[]{r6, Character.valueOf(r7)}));
    }

    public static void checkState(boolean r4, String r5, @CheckForNull Object r6, int r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r5, new Object[]{r6, Integer.valueOf(r7)}));
    }

    public static void checkState(boolean r4, String r5, @CheckForNull Object r6, long r7) {
        if (r4 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r5, new Object[]{r6, Long.valueOf(r7)}));
    }

    public static void checkState(boolean r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r4, new Object[]{r5, r6}));
    }

    public static void checkState(boolean r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6, @CheckForNull Object r7) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r4, new Object[]{r5, r6, r7}));
    }

    public static void checkState(boolean r3, String r4, @CheckForNull Object r5, @CheckForNull Object r6, @CheckForNull Object r7, @CheckForNull Object r8) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r4, new Object[]{r5, r6, r7, r8}));
    }

    public static void checkState(boolean r2, @CheckForNull String r3, @CheckForNull Object... r4) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(Strings.lenientFormat(r3, r4));
    }
}
