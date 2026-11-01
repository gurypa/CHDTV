package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Funnels {

    private static final enum ByteArrayFunnel extends Enum<ByteArrayFunnel> implements Funnel<byte[]> {
        private static final /* synthetic */ ByteArrayFunnel[] $VALUES = null;
        public static final ByteArrayFunnel INSTANCE = null;

        private static /* synthetic */ ByteArrayFunnel[] $values() {
            return new ByteArrayFunnel[]{INSTANCE};
        }

        static {
            INSTANCE = new ByteArrayFunnel("INSTANCE", 0);
            $VALUES = $values();
        }

        private ByteArrayFunnel(String r1, int r2) {
            super(r1, r2);
        }

        public static ByteArrayFunnel valueOf(String r1) {
            return (ByteArrayFunnel) Enum.valueOf(ByteArrayFunnel.class, r1);
        }

        public static ByteArrayFunnel[] values() {
            return (ByteArrayFunnel[]) $VALUES.clone();
        }

        @Override // com.google.common.hash.Funnel
        public /* bridge */ /* synthetic */ void funnel(byte[] r1, PrimitiveSink r2) {
            funnel2(r1, r2);
        }

        /* JADX INFO: renamed from: funnel, reason: avoid collision after fix types in other method */
        public void funnel2(byte[] r1, PrimitiveSink r2) {
            r2.putBytes(r1);
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Funnels.byteArrayFunnel()";
        }
    }

    private static final enum IntegerFunnel extends Enum<IntegerFunnel> implements Funnel<Integer> {
        private static final /* synthetic */ IntegerFunnel[] $VALUES = null;
        public static final IntegerFunnel INSTANCE = null;

        private static /* synthetic */ IntegerFunnel[] $values() {
            return new IntegerFunnel[]{INSTANCE};
        }

        static {
            INSTANCE = new IntegerFunnel("INSTANCE", 0);
            $VALUES = $values();
        }

        private IntegerFunnel(String r1, int r2) {
            super(r1, r2);
        }

        public static IntegerFunnel valueOf(String r1) {
            return (IntegerFunnel) Enum.valueOf(IntegerFunnel.class, r1);
        }

        public static IntegerFunnel[] values() {
            return (IntegerFunnel[]) $VALUES.clone();
        }

        /* JADX INFO: renamed from: funnel, reason: avoid collision after fix types in other method */
        public void funnel2(Integer r2, PrimitiveSink r3) {
            r3.putInt(r2.intValue());
        }

        @Override // com.google.common.hash.Funnel
        public /* bridge */ /* synthetic */ void funnel(Integer r1, PrimitiveSink r2) {
            funnel2(r1, r2);
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Funnels.integerFunnel()";
        }
    }

    private static final enum LongFunnel extends Enum<LongFunnel> implements Funnel<Long> {
        private static final /* synthetic */ LongFunnel[] $VALUES = null;
        public static final LongFunnel INSTANCE = null;

        private static /* synthetic */ LongFunnel[] $values() {
            return new LongFunnel[]{INSTANCE};
        }

        static {
            INSTANCE = new LongFunnel("INSTANCE", 0);
            $VALUES = $values();
        }

        private LongFunnel(String r1, int r2) {
            super(r1, r2);
        }

        public static LongFunnel valueOf(String r1) {
            return (LongFunnel) Enum.valueOf(LongFunnel.class, r1);
        }

        public static LongFunnel[] values() {
            return (LongFunnel[]) $VALUES.clone();
        }

        /* JADX INFO: renamed from: funnel, reason: avoid collision after fix types in other method */
        public void funnel2(Long r3, PrimitiveSink r4) {
            r4.putLong(r3.longValue());
        }

        @Override // com.google.common.hash.Funnel
        public /* bridge */ /* synthetic */ void funnel(Long r1, PrimitiveSink r2) {
            funnel2(r1, r2);
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Funnels.longFunnel()";
        }
    }

    private static class SequentialFunnel<E> implements Funnel<Iterable<? extends E>>, Serializable {
        private final Funnel<E> elementFunnel;

        SequentialFunnel(Funnel<E> r2) {
            this.elementFunnel = (Funnel) Preconditions.checkNotNull(r2);
        }

        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof SequentialFunnel) == true) goto L5;
            return false;
        L5:
            return this.elementFunnel.equals(((SequentialFunnel) r4).elementFunnel);
        }

        public void funnel(Iterable<? extends E> r4, PrimitiveSink r5) {
            Iterator<? extends E> r0 = r4.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            E r1 = r0.next();
            this.elementFunnel.funnel(r1, r5);
            goto L4
        }

        @Override // com.google.common.hash.Funnel
        public /* bridge */ /* synthetic */ void funnel(Object r1, PrimitiveSink r2) {
            funnel((Iterable) r1, r2);
        }

        public int hashCode() {
            return SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode();
        }

        public String toString() {
            return "Funnels.sequentialFunnel(" + this.elementFunnel + ")";
        }
    }

    private static class SinkAsStream extends OutputStream {
        final PrimitiveSink sink;

        SinkAsStream(PrimitiveSink r2) {
            this.sink = (PrimitiveSink) Preconditions.checkNotNull(r2);
        }

        public String toString() {
            return "Funnels.asOutputStream(" + this.sink + ")";
        }

        @Override // java.io.OutputStream
        public void write(int r3) {
            this.sink.putByte((byte) r3);
        }

        @Override // java.io.OutputStream
        public void write(byte[] r2) {
            this.sink.putBytes(r2);
        }

        @Override // java.io.OutputStream
        public void write(byte[] r2, int r3, int r4) {
            this.sink.putBytes(r2, r3, r4);
        }
    }

    private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable {
        private final Charset charset;

        private static class SerializedForm implements Serializable {
            private static final long serialVersionUID = 0;
            private final String charsetCanonicalName;

            SerializedForm(Charset r2) {
                this.charsetCanonicalName = r2.name();
            }

            private Object readResolve() {
                return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName));
            }
        }

        StringCharsetFunnel(Charset r2) {
            this.charset = (Charset) Preconditions.checkNotNull(r2);
        }

        private void readObject(ObjectInputStream r3) throws InvalidObjectException {
            throw new InvalidObjectException("Use SerializedForm");
        }

        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof StringCharsetFunnel) == true) goto L5;
            return false;
        L5:
            return this.charset.equals(((StringCharsetFunnel) r4).charset);
        }

        /* JADX INFO: renamed from: funnel, reason: avoid collision after fix types in other method */
        public void funnel2(CharSequence r2, PrimitiveSink r3) {
            r3.putString(r2, this.charset);
        }

        @Override // com.google.common.hash.Funnel
        public /* bridge */ /* synthetic */ void funnel(CharSequence r1, PrimitiveSink r2) {
            funnel2(r1, r2);
        }

        public int hashCode() {
            return StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode();
        }

        public String toString() {
            return "Funnels.stringFunnel(" + this.charset.name() + ")";
        }

        Object writeReplace() {
            return new SerializedForm(this.charset);
        }
    }

    private static final enum UnencodedCharsFunnel extends Enum<UnencodedCharsFunnel> implements Funnel<CharSequence> {
        private static final /* synthetic */ UnencodedCharsFunnel[] $VALUES = null;
        public static final UnencodedCharsFunnel INSTANCE = null;

        private static /* synthetic */ UnencodedCharsFunnel[] $values() {
            return new UnencodedCharsFunnel[]{INSTANCE};
        }

        static {
            INSTANCE = new UnencodedCharsFunnel("INSTANCE", 0);
            $VALUES = $values();
        }

        private UnencodedCharsFunnel(String r1, int r2) {
            super(r1, r2);
        }

        public static UnencodedCharsFunnel valueOf(String r1) {
            return (UnencodedCharsFunnel) Enum.valueOf(UnencodedCharsFunnel.class, r1);
        }

        public static UnencodedCharsFunnel[] values() {
            return (UnencodedCharsFunnel[]) $VALUES.clone();
        }

        /* JADX INFO: renamed from: funnel, reason: avoid collision after fix types in other method */
        public void funnel2(CharSequence r1, PrimitiveSink r2) {
            r2.putUnencodedChars(r1);
        }

        @Override // com.google.common.hash.Funnel
        public /* bridge */ /* synthetic */ void funnel(CharSequence r1, PrimitiveSink r2) {
            funnel2(r1, r2);
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Funnels.unencodedCharsFunnel()";
        }
    }

    private Funnels() {
    }

    public static OutputStream asOutputStream(PrimitiveSink r1) {
        return new SinkAsStream(r1);
    }

    public static Funnel<byte[]> byteArrayFunnel() {
        return ByteArrayFunnel.INSTANCE;
    }

    public static Funnel<Integer> integerFunnel() {
        return IntegerFunnel.INSTANCE;
    }

    public static Funnel<Long> longFunnel() {
        return LongFunnel.INSTANCE;
    }

    public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> r1) {
        return new SequentialFunnel(r1);
    }

    public static Funnel<CharSequence> stringFunnel(Charset r1) {
        return new StringCharsetFunnel(r1);
    }

    public static Funnel<CharSequence> unencodedCharsFunnel() {
        return UnencodedCharsFunnel.INSTANCE;
    }
}
