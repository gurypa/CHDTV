package com.google.common.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Splitter {
    private final int limit;
    private final boolean omitEmptyStrings;
    private final Strategy strategy;
    private final CharMatcher trimmer;

    public static final class MapSplitter {
        private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
        private final Splitter entrySplitter;
        private final Splitter outerSplitter;

        private MapSplitter(Splitter r2, Splitter r3) {
            this.outerSplitter = r2;
            this.entrySplitter = (Splitter) Preconditions.checkNotNull(r3);
        }

        /* synthetic */ MapSplitter(Splitter r1, Splitter r2, C05501 r3) {
            this(r1, r2);
        }

        public Map<String, String> split(CharSequence r9) {
            LinkedHashMap r0 = new LinkedHashMap();
            Iterator<String> r1 = this.outerSplitter.split(r9).iterator();
        L4:
            if (r1.hasNext() == false) goto L7;
            String r2 = r1.next();
            Iterator r3 = Splitter.access$000(this.entrySplitter, r2);
            Preconditions.checkArgument(r3.hasNext(), INVALID_ENTRY_MESSAGE, r2);
            String r4 = (String) r3.next();
            Preconditions.checkArgument(!r0.containsKey(r4), "Duplicate key [%s] found.", r4);
            Preconditions.checkArgument(r3.hasNext(), INVALID_ENTRY_MESSAGE, r2);
            r0.put(r4, (String) r3.next());
            Preconditions.checkArgument(!r3.hasNext(), INVALID_ENTRY_MESSAGE, r2);
            goto L4
        L7:
            return Collections.unmodifiableMap(r0);
        }
    }

    private static abstract class SplittingIterator extends AbstractIterator<String> {
        int limit;
        int offset;
        final boolean omitEmptyStrings;
        final CharSequence toSplit;
        final CharMatcher trimmer;

        protected SplittingIterator(Splitter r2, CharSequence r3) {
            this.offset = 0;
            this.trimmer = Splitter.access$200(r2);
            this.omitEmptyStrings = Splitter.access$300(r2);
            this.limit = Splitter.access$400(r2);
            this.toSplit = r3;
        }

        @Override // com.google.common.base.AbstractIterator
        @CheckForNull
        protected /* bridge */ /* synthetic */ String computeNext() {
            return computeNext2();
        }

        @Override // com.google.common.base.AbstractIterator
        @CheckForNull
        /* JADX INFO: renamed from: computeNext, reason: avoid collision after fix types in other method */
        protected String computeNext2() {
            int r0 = this.offset;
        L4:
            if (this.offset == (-1)) goto L37;
            int r1 = r0;
            int r3 = separatorStart(this.offset);
            if (r3 != (-1)) goto L8;
            int r4 = this.toSplit.length();
            this.offset = -1;
        L10:
            if (this.offset == r0) goto L11;
        L14:
            if (r1 >= r4) goto L18;
            if (this.trimmer.matches(this.toSplit.charAt(r1)) == false) goto L18;
            r1 = r1 + 1;
        L18:
            if (r4 <= r1) goto L23;
            if (this.trimmer.matches(this.toSplit.charAt(r4 - 1)) == false) goto L23;
            r4 = r4 - 1;
        L23:
            if (this.omitEmptyStrings == false) goto L27;
            if (r1 != r4) goto L27;
            r0 = this.offset;
        L27:
            if (this.limit != 1) goto L33;
            r4 = this.toSplit.length();
            this.offset = -1;
        L29:
            if (r4 <= r1) goto L35;
            if (this.trimmer.matches(this.toSplit.charAt(r4 - 1)) == false) goto L35;
            r4 = r4 - 1;
        L35:
            return this.toSplit.subSequence(r1, r4).toString();
        L33:
            this.limit--;
            goto L35
        L11:
            this.offset++;
            if (this.offset <= this.toSplit.length()) goto L4;
            this.offset = -1;
            goto L4
        L8:
            r4 = r3;
            this.offset = separatorEnd(r3);
            goto L10
        L37:
            return endOfData();
        }

        abstract int separatorEnd(int r1);

        abstract int separatorStart(int r1);
    }

    private interface Strategy {
        Iterator<String> iterator(Splitter r1, CharSequence r2);
    }

    private Splitter(Strategy r4) {
        this(r4, false, CharMatcher.none(), Integer.MAX_VALUE);
    }

    private Splitter(Strategy r1, boolean r2, CharMatcher r3, int r4) {
        this.strategy = r1;
        this.omitEmptyStrings = r2;
        this.trimmer = r3;
        this.limit = r4;
    }

    static /* synthetic */ Iterator access$000(Splitter r1, CharSequence r2) {
        return r1.splittingIterator(r2);
    }

    static /* synthetic */ CharMatcher access$200(Splitter r1) {
        return r1.trimmer;
    }

    static /* synthetic */ boolean access$300(Splitter r1) {
        return r1.omitEmptyStrings;
    }

    static /* synthetic */ int access$400(Splitter r1) {
        return r1.limit;
    }

    public static Splitter fixedLength(final int r2) {
        if (r2 <= 0) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0, "The length may not be less than 1");
        return new Splitter(new C05534(r2));
    L4:
        r0 = false;
        goto L5
    }

    /* JADX INFO: renamed from: on */
    public static Splitter m190on(char r1) {
        return m191on(CharMatcher.m181is(r1));
    }

    /* JADX INFO: renamed from: on */
    public static Splitter m191on(final CharMatcher r2) {
        Preconditions.checkNotNull(r2);
        return new Splitter(new C05501(r2));
    }

    /* JADX INFO: renamed from: on */
    public static Splitter m192on(final String r4) {
        if (r4.length() == 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "The separator may not be the empty string.");
        if (r4.length() != 1) goto L11;
        return m190on(r4.charAt(0));
    L11:
        return new Splitter(new C05512(r4));
    L5:
        r0 = false;
        goto L6
    }

    /* JADX INFO: renamed from: on */
    public static Splitter m193on(Pattern r1) {
        return onPatternInternal(new JdkPattern(r1));
    }

    public static Splitter onPattern(String r1) {
        return onPatternInternal(Platform.compilePattern(r1));
    }

    static Splitter onPatternInternal(final CommonPattern r2) {
        Preconditions.checkArgument(!r2.matcher("").matches(), "The pattern may not match the empty string: %s", r2);
        return new Splitter(new C05523(r2));
    }

    private Iterator<String> splittingIterator(CharSequence r2) {
        return this.strategy.iterator(this, r2);
    }

    public Splitter limit(int r5) {
        if (r5 <= 0) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0, "must be greater than zero: %s", r5);
        return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, r5);
    L4:
        r0 = false;
        goto L5
    }

    public Splitter omitEmptyStrings() {
        return new Splitter(this.strategy, true, this.trimmer, this.limit);
    }

    public Iterable<String> split(final CharSequence r2) {
        Preconditions.checkNotNull(r2);
        return new C05545(this, r2);
    }

    public List<String> splitToList(CharSequence r4) {
        Preconditions.checkNotNull(r4);
        Iterator<String> r0 = splittingIterator(r4);
        ArrayList r1 = new ArrayList();
    L4:
        if (r0.hasNext() == false) goto L7;
        r1.add(r0.next());
        goto L4
    L7:
        return Collections.unmodifiableList(r1);
    }

    public Splitter trimResults() {
        return trimResults(CharMatcher.whitespace());
    }

    public Splitter trimResults(CharMatcher r5) {
        Preconditions.checkNotNull(r5);
        return new Splitter(this.strategy, this.omitEmptyStrings, r5, this.limit);
    }

    public MapSplitter withKeyValueSeparator(char r2) {
        return withKeyValueSeparator(m190on(r2));
    }

    public MapSplitter withKeyValueSeparator(Splitter r3) {
        return new MapSplitter(this, r3, null);
    }

    public MapSplitter withKeyValueSeparator(String r2) {
        return withKeyValueSeparator(m192on(r2));
    }
}
