package com.google.common.base;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class Joiner {
    private final String separator;

    public static final class MapJoiner {
        private final Joiner joiner;
        private final String keyValueSeparator;

        private MapJoiner(Joiner r2, String r3) {
            this.joiner = r2;
            this.keyValueSeparator = (String) Preconditions.checkNotNull(r3);
        }

        /* synthetic */ MapJoiner(Joiner r1, String r2, C05391 r3) {
            this(r1, r2);
        }

        public <A extends Appendable> A appendTo(A r2, Iterable<? extends Map.Entry<?, ?>> r3) throws IOException {
            return (A) appendTo(r2, r3.iterator());
        }

        public <A extends Appendable> A appendTo(A r5, Iterator<? extends Map.Entry<?, ?>> r6) throws IOException {
            Preconditions.checkNotNull(r5);
            if (r6.hasNext() == false) goto L8;
            Map.Entry<?, ?> r0 = r6.next();
            r5.append(this.joiner.toString(r0.getKey()));
            r5.append(this.keyValueSeparator);
            r5.append(this.joiner.toString(r0.getValue()));
        L6:
            if (r6.hasNext() == false) goto L8;
            r5.append(Joiner.access$100(this.joiner));
            Map.Entry<?, ?> r1 = r6.next();
            r5.append(this.joiner.toString(r1.getKey()));
            r5.append(this.keyValueSeparator);
            r5.append(this.joiner.toString(r1.getValue()));
        L8:
            return r5;
        }

        public <A extends Appendable> A appendTo(A r2, Map<?, ?> r3) throws IOException {
            return (A) appendTo(r2, r3.entrySet());
        }

        public StringBuilder appendTo(StringBuilder r2, Iterable<? extends Map.Entry<?, ?>> r3) {
            return appendTo(r2, r3.iterator());
        }

        public StringBuilder appendTo(StringBuilder r3, Iterator<? extends Map.Entry<?, ?>> r4) {
            appendTo(r3, r4);     // Catch: IOException -> L4
            return r3;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        public StringBuilder appendTo(StringBuilder r2, Map<?, ?> r3) {
            return appendTo(r2, r3.entrySet());
        }

        public String join(Iterable<? extends Map.Entry<?, ?>> r2) {
            return join(r2.iterator());
        }

        public String join(Iterator<? extends Map.Entry<?, ?>> r2) {
            return appendTo(new StringBuilder(), r2).toString();
        }

        public String join(Map<?, ?> r2) {
            return join(r2.entrySet());
        }

        public MapJoiner useForNull(String r4) {
            return new MapJoiner(this.joiner.useForNull(r4), this.keyValueSeparator);
        }
    }

    private Joiner(Joiner r2) {
        this.separator = r2.separator;
    }

    /* synthetic */ Joiner(Joiner r1, C05391 r2) {
        this(r1);
    }

    private Joiner(String r2) {
        this.separator = (String) Preconditions.checkNotNull(r2);
    }

    static /* synthetic */ String access$100(Joiner r1) {
        return r1.separator;
    }

    private static Iterable<Object> iterable(@CheckForNull final Object r1, @CheckForNull final Object r2, final Object[] r3) {
        Preconditions.checkNotNull(r3);
        return new C05413(r3, r1, r2);
    }

    /* JADX INFO: renamed from: on */
    public static Joiner m183on(char r2) {
        return new Joiner(String.valueOf(r2));
    }

    /* JADX INFO: renamed from: on */
    public static Joiner m184on(String r1) {
        return new Joiner(r1);
    }

    public <A extends Appendable> A appendTo(A r2, Iterable<? extends Object> r3) throws IOException {
        return (A) appendTo(r2, r3.iterator());
    }

    public final <A extends Appendable> A appendTo(A r2, @CheckForNull Object r3, @CheckForNull Object r4, Object... r5) throws IOException {
        return (A) appendTo(r2, iterable(r3, r4, r5));
    }

    public <A extends Appendable> A appendTo(A r2, Iterator<? extends Object> r3) throws IOException {
        Preconditions.checkNotNull(r2);
        if (r3.hasNext() == false) goto L8;
        r2.append(toString(r3.next()));
    L6:
        if (r3.hasNext() == false) goto L8;
        r2.append(this.separator);
        r2.append(toString(r3.next()));
    L8:
        return r2;
    }

    public final <A extends Appendable> A appendTo(A r3, Object[] r4) throws IOException {
        return (A) appendTo(r3, Arrays.asList(r4));
    }

    public final StringBuilder appendTo(StringBuilder r2, Iterable<? extends Object> r3) {
        return appendTo(r2, r3.iterator());
    }

    public final StringBuilder appendTo(StringBuilder r2, @CheckForNull Object r3, @CheckForNull Object r4, Object... r5) {
        return appendTo(r2, iterable(r3, r4, r5));
    }

    public final StringBuilder appendTo(StringBuilder r3, Iterator<? extends Object> r4) {
        appendTo(r3, r4);     // Catch: IOException -> L4
        return r3;
    L4:
        e = move-exception;
        throw new AssertionError(e);
    }

    public final StringBuilder appendTo(StringBuilder r3, Object[] r4) {
        return appendTo(r3, Arrays.asList(r4));
    }

    public final String join(Iterable<? extends Object> r2) {
        return join(r2.iterator());
    }

    public final String join(@CheckForNull Object r2, @CheckForNull Object r3, Object... r4) {
        return join(iterable(r2, r3, r4));
    }

    public final String join(Iterator<? extends Object> r2) {
        return appendTo(new StringBuilder(), r2).toString();
    }

    public final String join(Object[] r3) {
        return join(Arrays.asList(r3));
    }

    public Joiner skipNulls() {
        return new C05402(this, this);
    }

    CharSequence toString(@CheckForNull Object r2) {
        java.util.Objects.requireNonNull(r2);
        if ((r2 instanceof CharSequence) == false) goto L6;
        return (CharSequence) r2;
    L6:
        return r2.toString();
    }

    public Joiner useForNull(final String r2) {
        Preconditions.checkNotNull(r2);
        return new C05391(this, this, r2);
    }

    public MapJoiner withKeyValueSeparator(char r2) {
        return withKeyValueSeparator(String.valueOf(r2));
    }

    public MapJoiner withKeyValueSeparator(String r3) {
        return new MapJoiner(this, r3, null);
    }
}
