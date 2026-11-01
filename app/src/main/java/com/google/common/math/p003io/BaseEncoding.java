package com.google.common.p003io;

import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class BaseEncoding {
    private static final BaseEncoding BASE16 = null;
    private static final BaseEncoding BASE32 = null;
    private static final BaseEncoding BASE32_HEX = null;
    private static final BaseEncoding BASE64 = null;
    private static final BaseEncoding BASE64_URL = null;

    static final class Alphabet {
        final int bitsPerChar;
        final int bytesPerChunk;
        private final char[] chars;
        final int charsPerChunk;
        private final byte[] decodabet;
        private final boolean ignoreCase;
        final int mask;
        private final String name;
        private final boolean[] validPadding;

        Alphabet(String r3, char[] r4) {
            this(r3, r4, decodabetFor(r4), false);
        }

        private Alphabet(String r8, char[] r9, byte[] r10, boolean r11) {
            this.name = (String) Preconditions.checkNotNull(r8);
            this.chars = (char[]) Preconditions.checkNotNull(r9);
            this.bitsPerChar = IntMath.log2(r9.length, RoundingMode.UNNECESSARY);     // Catch: ArithmeticException -> L10
            int r0 = Integer.numberOfTrailingZeros(this.bitsPerChar);
            this.charsPerChunk = 1 << (3 - r0);
            this.bytesPerChunk = this.bitsPerChar >> r0;
            this.mask = r9.length - 1;
            this.decodabet = r10;
            boolean[] r1 = new boolean[this.charsPerChunk];
            int r3 = 0;
        L6:
            if (r3 >= this.bytesPerChunk) goto L8;
            r1[IntMath.divide(r3 * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
            r3 = r3 + 1;
            goto L6
        L8:
            this.validPadding = r1;
            this.ignoreCase = r11;
            return;
        L10:
            e = move-exception;
            throw new IllegalArgumentException("Illegal alphabet length " + r9.length, e);
        }

        static /* synthetic */ char[] access$000(Alphabet r1) {
            return r1.chars;
        }

        private static byte[] decodabetFor(char[] r8) {
            byte[] r0 = new byte[128];
            Arrays.fill(r0, (byte) -1);
            int r2 = 0;
        L4:
            if (r2 >= r8.length) goto L13;
            char r3 = r8[r2];
            boolean r5 = false;
            if (r3 >= r0.length) goto L8;
            boolean r4 = true;
        L9:
            Preconditions.checkArgument(r4, "Non-ASCII character: %s", r3);
            if (r0[r3] != (-1)) goto L12;
            r5 = true;
        L12:
            Preconditions.checkArgument(r5, "Duplicate character: %s", r3);
            r0[r3] = (byte) r2;
            r2 = r2 + 1;
            goto L4
        L8:
            r4 = false;
            goto L9
        L13:
            return r0;
        }

        private boolean hasLowerCase() {
            char[] r0 = this.chars;
            int r1 = r0.length;
            int r3 = 0;
        L3:
            if (r3 >= r1) goto L9;
            if (Ascii.isLowerCase(r0[r3]) == true) goto L6;
            r3 = r3 + 1;
            goto L3
        L6:
            return true;
        L9:
            return false;
        }

        private boolean hasUpperCase() {
            char[] r0 = this.chars;
            int r1 = r0.length;
            int r3 = 0;
        L3:
            if (r3 >= r1) goto L9;
            if (Ascii.isUpperCase(r0[r3]) == true) goto L6;
            r3 = r3 + 1;
            goto L3
        L6:
            return true;
        L9:
            return false;
        }

        boolean canDecode(char r3) {
            if (r3 <= 127) goto L5;
        L7:
            return false;
        L5:
            if (this.decodabet[r3] == (-1)) goto L7;
            return true;
        }

        int decode(char r5) throws DecodingException {
            if (r5 > 127) goto L16;
            byte r2 = this.decodabet[r5];
            if (r2 == (-1)) goto L7;
            return r2;
        L7:
            if (r5 <= ' ') goto L13;
            if (r5 == 127) goto L13;
            throw new DecodingException("Unrecognized character: " + r5);
        L13:
            throw new DecodingException("Unrecognized character: 0x" + Integer.toHexString(r5));
        L16:
            throw new DecodingException("Unrecognized character: 0x" + Integer.toHexString(r5));
        }

        char encode(int r2) {
            return this.chars[r2];
        }

        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof Alphabet) == false) goto L10;
            Alphabet r0 = (Alphabet) r5;
            if (this.ignoreCase == r0.ignoreCase) goto L7;
            return false;
        L7:
            if (Arrays.equals(this.chars, r0.chars) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        public int hashCode() {
            int r0 = Arrays.hashCode(this.chars);
            if (this.ignoreCase == false) goto L5;
            int r1 = 1231;
        L7:
            return r0 + r1;
        L5:
            r1 = 1237;
            goto L7
        }

        Alphabet ignoreCase() {
            if (this.ignoreCase == false) goto L5;
            return this;
        L5:
            byte[] r0 = Arrays.copyOf(this.decodabet, this.decodabet.length);
            int r1 = 65;
        L6:
            boolean r3 = true;
            if (r1 > 90) goto L17;
            int r2 = r1 | 32;
            byte r4 = this.decodabet[r1];
            byte r5 = this.decodabet[r2];
            if (r4 != (-1)) goto L11;
            r0[r1] = r5;
        L15:
            r1 = r1 + 1;
            goto L6
        L11:
            if (r5 == (-1)) goto L14;
            r3 = false;
        L14:
            Preconditions.checkState(r3, "Can't ignoreCase() since '%s' and '%s' encode different values", (char) r1, (char) r2);
            r0[r2] = r4;
            goto L15
        L17:
            return new Alphabet(this.name + ".ignoreCase()", this.chars, r0, true);
        }

        boolean isValidPaddingStartPosition(int r3) {
            return this.validPadding[r3 % this.charsPerChunk];
        }

        Alphabet lowerCase() {
            if (hasUpperCase() == true) goto L5;
            return this;
        L5:
            Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
            char[] r0 = new char[this.chars.length];
            int r1 = 0;
        L7:
            if (r1 >= this.chars.length) goto L9;
            r0[r1] = Ascii.toLowerCase(this.chars[r1]);
            r1 = r1 + 1;
            goto L7
        L9:
            Alphabet r2 = new Alphabet(this.name + ".lowerCase()", r0);
            if (this.ignoreCase == false) goto L13;
            return r2.ignoreCase();
        L13:
            return r2;
        }

        public boolean matches(char r3) {
            if (r3 < this.decodabet.length) goto L5;
        L7:
            return false;
        L5:
            if (this.decodabet[r3] == (-1)) goto L7;
            return true;
        }

        public String toString() {
            return this.name;
        }

        Alphabet upperCase() {
            if (hasLowerCase() == true) goto L5;
            return this;
        L5:
            Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
            char[] r0 = new char[this.chars.length];
            int r1 = 0;
        L7:
            if (r1 >= this.chars.length) goto L9;
            r0[r1] = Ascii.toUpperCase(this.chars[r1]);
            r1 = r1 + 1;
            goto L7
        L9:
            Alphabet r2 = new Alphabet(this.name + ".upperCase()", r0);
            if (this.ignoreCase == false) goto L13;
            return r2.ignoreCase();
        L13:
            return r2;
        }
    }

    private static final class Base16Encoding extends StandardBaseEncoding {
        final char[] encoding;

        private Base16Encoding(Alphabet r5) {
            super(r5, null);
            this.encoding = new char[512];
            if (Alphabet.access$000(r5).length != 16) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0);
            int r1 = 0;
        L8:
            if (r1 >= 256) goto L10;
            this.encoding[r1] = r5.encode(r1 >>> 4);
            this.encoding[r1 | 256] = r5.encode(r1 & 15);
            r1 = r1 + 1;
            goto L8
        L10:
            return;
        L5:
            r0 = false;
            goto L6
        }

        Base16Encoding(String r3, String r4) {
            this(new Alphabet(r3, r4.toCharArray()));
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.p003io.BaseEncoding
        int decodeTo(byte[] r6, CharSequence r7) throws DecodingException {
            Preconditions.checkNotNull(r6);
            if ((r7.length() % 2) == 1) goto L10;
            int r0 = 0;
            int r1 = 0;
        L6:
            if (r1 >= r7.length()) goto L8;
            r6[r0] = (byte) ((this.alphabet.decode(r7.charAt(r1)) << 4) | this.alphabet.decode(r7.charAt(r1 + 1)));
            r1 = r1 + 2;
            r0 = r0 + 1;
            goto L6
        L8:
            return r0;
        L10:
            throw new DecodingException("Invalid input length " + r7.length());
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.p003io.BaseEncoding
        void encodeTo(Appendable r5, byte[] r6, int r7, int r8) throws IOException {
            Preconditions.checkNotNull(r5);
            Preconditions.checkPositionIndexes(r7, r7 + r8, r6.length);
            int r0 = 0;
        L3:
            if (r0 >= r8) goto L5;
            int r1 = r6[r7 + r0] & 255;
            r5.append(this.encoding[r1]);
            r5.append(this.encoding[r1 | 256]);
            r0 = r0 + 1;
            goto L3
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding
        BaseEncoding newInstance(Alphabet r2, @CheckForNull Character r3) {
            return new Base16Encoding(r2);
        }
    }

    private static final class Base64Encoding extends StandardBaseEncoding {
        private Base64Encoding(Alphabet r3, @CheckForNull Character r4) {
            super(r3, r4);
            if (Alphabet.access$000(r3).length != 64) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0);
            return;
        L5:
            r0 = false;
            goto L6
        }

        Base64Encoding(String r3, String r4, @CheckForNull Character r5) {
            this(new Alphabet(r3, r4.toCharArray()), r5);
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.p003io.BaseEncoding
        int decodeTo(byte[] r6, CharSequence r7) throws DecodingException {
            Preconditions.checkNotNull(r6);
            CharSequence r8 = trimTrailingPadding(r7);
            if (this.alphabet.isValidPaddingStartPosition(r8.length()) == false) goto L17;
            int r0 = 0;
            int r1 = 0;
        L6:
            if (r1 >= r8.length()) goto L15;
            int r3 = r1 + 1;
            int r4 = r3 + 1;
            int r2 = (this.alphabet.decode(r8.charAt(r1)) << 18) | (this.alphabet.decode(r8.charAt(r3)) << 12);
            int r5 = r0 + 1;
            r6[r0] = (byte) (r2 >>> 16);
            if (r4 < r8.length()) goto L9;
            r0 = r5;
            r1 = r4;
            goto L6
        L9:
            int r9 = r4 + 1;
            int r10 = (this.alphabet.decode(r8.charAt(r4)) << 6) | r2;
            int r11 = r5 + 1;
            r6[r5] = (byte) ((r10 >>> 8) & 255);
            if (r9 < r8.length()) goto L11;
            r0 = r11;
            r1 = r9;
            goto L6
        L11:
            r6[r11] = (byte) ((r10 | this.alphabet.decode(r8.charAt(r9))) & 255);
            r0 = r11 + 1;
            r1 = r9 + 1;
            goto L6
        L15:
            return r0;
        L17:
            throw new DecodingException("Invalid input length " + r8.length());
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.p003io.BaseEncoding
        void encodeTo(Appendable r6, byte[] r7, int r8, int r9) throws IOException {
            Preconditions.checkNotNull(r6);
            Preconditions.checkPositionIndexes(r8, r8 + r9, r7.length);
            int r0 = r8;
            int r1 = r9;
        L4:
            if (r1 < 3) goto L7;
            int r2 = r0 + 1;
            int r3 = r2 + 1;
            int r4 = ((r7[r0] & 255) << 16) | ((r7[r2] & 255) << 8);
            int r5 = r4 | (r7[r3] & 255);
            r6.append(this.alphabet.encode(r5 >>> 18));
            r6.append(this.alphabet.encode((r5 >>> 12) & 63));
            r6.append(this.alphabet.encode((r5 >>> 6) & 63));
            r6.append(this.alphabet.encode(r5 & 63));
            r1 = r1 - 3;
            r0 = r3 + 1;
            goto L4
        L7:
            if (r0 >= (r8 + r9)) goto L11;
            encodeChunkTo(r6, r7, r0, (r8 + r9) - r0);
            return;
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding
        BaseEncoding newInstance(Alphabet r2, @CheckForNull Character r3) {
            return new Base64Encoding(r2, r3);
        }
    }

    public static final class DecodingException extends IOException {
        DecodingException(String r1) {
            super(r1);
        }
    }

    static final class SeparatedBaseEncoding extends BaseEncoding {
        private final int afterEveryChars;
        private final BaseEncoding delegate;
        private final String separator;

        SeparatedBaseEncoding(BaseEncoding r3, String r4, int r5) {
            this.delegate = (BaseEncoding) Preconditions.checkNotNull(r3);
            this.separator = (String) Preconditions.checkNotNull(r4);
            this.afterEveryChars = r5;
            if (r5 <= 0) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "Cannot add a separator after every %s chars", r5);
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Override // com.google.common.p003io.BaseEncoding
        public boolean canDecode(CharSequence r5) {
            StringBuilder r0 = new StringBuilder();
            int r1 = 0;
        L4:
            if (r1 >= r5.length()) goto L10;
            char r2 = r5.charAt(r1);
            if (this.separator.indexOf(r2) >= 0) goto L8;
            r0.append(r2);
        L8:
            r1 = r1 + 1;
            goto L4
        L10:
            return this.delegate.canDecode(r0);
        }

        @Override // com.google.common.p003io.BaseEncoding
        int decodeTo(byte[] r5, CharSequence r6) throws DecodingException {
            StringBuilder r0 = new StringBuilder(r6.length());
            int r1 = 0;
        L4:
            if (r1 >= r6.length()) goto L10;
            char r2 = r6.charAt(r1);
            if (this.separator.indexOf(r2) >= 0) goto L8;
            r0.append(r2);
        L8:
            r1 = r1 + 1;
            goto L4
        L10:
            return this.delegate.decodeTo(r5, r0);
        }

        @Override // com.google.common.p003io.BaseEncoding
        public InputStream decodingStream(Reader r3) {
            return this.delegate.decodingStream(ignoringReader(r3, this.separator));
        }

        @Override // com.google.common.p003io.BaseEncoding
        void encodeTo(Appendable r4, byte[] r5, int r6, int r7) throws IOException {
            this.delegate.encodeTo(separatingAppendable(r4, this.separator, this.afterEveryChars), r5, r6, r7);
        }

        @Override // com.google.common.p003io.BaseEncoding
        public OutputStream encodingStream(Writer r4) {
            return this.delegate.encodingStream(separatingWriter(r4, this.separator, this.afterEveryChars));
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding ignoreCase() {
            return this.delegate.ignoreCase().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding lowerCase() {
            return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.p003io.BaseEncoding
        int maxDecodedSize(int r2) {
            return this.delegate.maxDecodedSize(r2);
        }

        @Override // com.google.common.p003io.BaseEncoding
        int maxEncodedSize(int r6) {
            int r0 = this.delegate.maxEncodedSize(r6);
            return (this.separator.length() * IntMath.divide(Math.max(0, r0 - 1), this.afterEveryChars, RoundingMode.FLOOR)) + r0;
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding omitPadding() {
            return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
        }

        public String toString() {
            return this.delegate + ".withSeparator(\"" + this.separator + "\", " + this.afterEveryChars + ")";
        }

        @Override // com.google.common.p003io.BaseEncoding
        CharSequence trimTrailingPadding(CharSequence r2) {
            return this.delegate.trimTrailingPadding(r2);
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding upperCase() {
            return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding withPadChar(char r4) {
            return this.delegate.withPadChar(r4).withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding withSeparator(String r3, int r4) {
            throw new UnsupportedOperationException("Already have a separator");
        }
    }

    private static class StandardBaseEncoding extends BaseEncoding {
        final Alphabet alphabet;

        @CheckForNull
        @LazyInit
        private volatile BaseEncoding ignoreCase;

        @CheckForNull
        @LazyInit
        private volatile BaseEncoding lowerCase;

        @CheckForNull
        final Character paddingChar;

        @CheckForNull
        @LazyInit
        private volatile BaseEncoding upperCase;

        StandardBaseEncoding(Alphabet r3, @CheckForNull Character r4) {
            this.alphabet = (Alphabet) Preconditions.checkNotNull(r3);
            if (r4 != null) goto L5;
        L8:
            boolean r0 = true;
        L9:
            Preconditions.checkArgument(r0, "Padding character %s was already in alphabet", r4);
            this.paddingChar = r4;
            return;
        L5:
            if (r3.matches(r4.charValue()) == false) goto L8;
            r0 = false;
            goto L9
        }

        StandardBaseEncoding(String r3, String r4, @CheckForNull Character r5) {
            this(new Alphabet(r3, r4.toCharArray()), r5);
        }

        @Override // com.google.common.p003io.BaseEncoding
        public boolean canDecode(CharSequence r5) {
            Preconditions.checkNotNull(r5);
            CharSequence r6 = trimTrailingPadding(r5);
            if (this.alphabet.isValidPaddingStartPosition(r6.length()) == true) goto L5;
            return false;
        L5:
            int r0 = 0;
        L7:
            if (r0 >= r6.length()) goto L12;
            if (this.alphabet.canDecode(r6.charAt(r0)) == false) goto L10;
            r0 = r0 + 1;
            goto L7
        L10:
            return false;
        L12:
            return true;
        }

        @Override // com.google.common.p003io.BaseEncoding
        int decodeTo(byte[] r13, CharSequence r14) throws DecodingException {
            Preconditions.checkNotNull(r13);
            CharSequence r15 = trimTrailingPadding(r14);
            if (this.alphabet.isValidPaddingStartPosition(r15.length()) == false) goto L20;
            int r0 = 0;
            int r1 = 0;
        L6:
            if (r1 >= r15.length()) goto L18;
            long r2 = 0;
            int r4 = 0;
            int r5 = 0;
        L9:
            if (r5 >= this.alphabet.charsPerChunk) goto L14;
            r2 = r2 << this.alphabet.bitsPerChar;
            if ((r1 + r5) >= r15.length()) goto L13;
            r2 = r2 | ((long) this.alphabet.decode(r15.charAt(r4 + r1)));
            r4 = r4 + 1;
        L13:
            r5 = r5 + 1;
            goto L9
        L14:
            int r6 = (this.alphabet.bytesPerChunk * 8) - (this.alphabet.bitsPerChar * r4);
            int r7 = (this.alphabet.bytesPerChunk - 1) * 8;
        L15:
            if (r7 < r6) goto L17;
            r13[r0] = (byte) ((r2 >>> r7) & 255);
            r7 = r7 - 8;
            r0 = r0 + 1;
            goto L15
        L17:
            r1 = r1 + this.alphabet.charsPerChunk;
            goto L6
        L18:
            return r0;
        L20:
            throw new DecodingException("Invalid input length " + r15.length());
        }

        @Override // com.google.common.p003io.BaseEncoding
        public InputStream decodingStream(final Reader r2) {
            Preconditions.checkNotNull(r2);
            return new C08442(this, r2);
        }

        void encodeChunkTo(Appendable r8, byte[] r9, int r10, int r11) throws IOException {
            Preconditions.checkNotNull(r8);
            Preconditions.checkPositionIndexes(r10, r10 + r11, r9.length);
            if (r11 > this.alphabet.bytesPerChunk) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0);
            long r1 = 0;
            int r2 = 0;
        L8:
            if (r2 >= r11) goto L10;
            r1 = (r1 | ((long) (r9[r10 + r2] & 255))) << 8;
            r2 = r2 + 1;
            goto L8
        L10:
            int r3 = ((r11 + 1) * 8) - this.alphabet.bitsPerChar;
            int r4 = 0;
        L12:
            if (r4 >= (r11 * 8)) goto L15;
            int r5 = ((int) (r1 >>> (r3 - r4))) & this.alphabet.mask;
            r8.append(this.alphabet.encode(r5));
            r4 = r4 + this.alphabet.bitsPerChar;
            goto L12
        L15:
            if (this.paddingChar != null) goto L17;
            return;
        L17:
            if (r4 >= (this.alphabet.bytesPerChunk * 8)) goto L23;
            r8.append(this.paddingChar.charValue());
            r4 = r4 + this.alphabet.bitsPerChar;
            goto L17
        L23:
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Override // com.google.common.p003io.BaseEncoding
        void encodeTo(Appendable r5, byte[] r6, int r7, int r8) throws IOException {
            Preconditions.checkNotNull(r5);
            Preconditions.checkPositionIndexes(r7, r7 + r8, r6.length);
            int r0 = 0;
        L3:
            if (r0 >= r8) goto L5;
            encodeChunkTo(r5, r6, r7 + r0, Math.min(this.alphabet.bytesPerChunk, r8 - r0));
            r0 = r0 + this.alphabet.bytesPerChunk;
            goto L3
        }

        @Override // com.google.common.p003io.BaseEncoding
        public OutputStream encodingStream(final Writer r2) {
            Preconditions.checkNotNull(r2);
            return new C08431(this, r2);
        }

        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof StandardBaseEncoding) == false) goto L11;
            StandardBaseEncoding r0 = (StandardBaseEncoding) r5;
            if (this.alphabet.equals(r0.alphabet) == true) goto L7;
            return false;
        L7:
            if (Objects.equals(this.paddingChar, r0.paddingChar) == false) goto L13;
            return true;
        L13:
            return false;
        L11:
            return false;
        }

        public int hashCode() {
            return this.alphabet.hashCode() ^ Objects.hashCode(this.paddingChar);
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding ignoreCase() {
            BaseEncoding r0 = this.ignoreCase;
            if (r0 != null) goto L10;
            Alphabet r1 = this.alphabet.ignoreCase();
            if (r1 != this.alphabet) goto L7;
            BaseEncoding r2 = this;
        L8:
            this.ignoreCase = r2;
            return r2;
        L7:
            r2 = newInstance(r1, this.paddingChar);
            goto L8
        L10:
            return r0;
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding lowerCase() {
            BaseEncoding r0 = this.lowerCase;
            if (r0 != null) goto L10;
            Alphabet r1 = this.alphabet.lowerCase();
            if (r1 != this.alphabet) goto L7;
            BaseEncoding r2 = this;
        L8:
            this.lowerCase = r2;
            return r2;
        L7:
            r2 = newInstance(r1, this.paddingChar);
            goto L8
        L10:
            return r0;
        }

        @Override // com.google.common.p003io.BaseEncoding
        int maxDecodedSize(int r5) {
            return (int) (((((long) this.alphabet.bitsPerChar) * ((long) r5)) + 7) / 8);
        }

        @Override // com.google.common.p003io.BaseEncoding
        int maxEncodedSize(int r4) {
            return this.alphabet.charsPerChunk * IntMath.divide(r4, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
        }

        BaseEncoding newInstance(Alphabet r2, @CheckForNull Character r3) {
            return new StandardBaseEncoding(r2, r3);
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding omitPadding() {
            if (this.paddingChar != null) goto L6;
            return this;
        L6:
            return newInstance(this.alphabet, null);
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder("BaseEncoding.");
            r0.append(this.alphabet);
            if ((8 % this.alphabet.bitsPerChar) == 0) goto L9;
            if (this.paddingChar != null) goto L7;
            r0.append(".omitPadding()");
            goto L9
        L7:
            r0.append(".withPadChar('").append(this.paddingChar).append("')");
        L9:
            return r0.toString();
        }

        @Override // com.google.common.p003io.BaseEncoding
        CharSequence trimTrailingPadding(CharSequence r5) {
            Preconditions.checkNotNull(r5);
            if (this.paddingChar != null) goto L5;
            return r5;
        L5:
            char r0 = this.paddingChar.charValue();
            int r1 = r5.length() - 1;
        L6:
            if (r1 < 0) goto L12;
            if (r5.charAt(r1) != r0) goto L12;
            r1 = r1 - 1;
        L12:
            return r5.subSequence(0, r1 + 1);
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding upperCase() {
            BaseEncoding r0 = this.upperCase;
            if (r0 != null) goto L10;
            Alphabet r1 = this.alphabet.upperCase();
            if (r1 != this.alphabet) goto L7;
            BaseEncoding r2 = this;
        L8:
            this.upperCase = r2;
            return r2;
        L7:
            r2 = newInstance(r1, this.paddingChar);
            goto L8
        L10:
            return r0;
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding withPadChar(char r3) {
            if ((8 % this.alphabet.bitsPerChar) != 0) goto L5;
        L11:
            return this;
        L5:
            if (this.paddingChar == null) goto L10;
            if (this.paddingChar.charValue() == r3) goto L11;
        L10:
            return newInstance(this.alphabet, Character.valueOf(r3));
        }

        @Override // com.google.common.p003io.BaseEncoding
        public BaseEncoding withSeparator(String r5, int r6) {
            int r0 = 0;
        L3:
            boolean r2 = true;
            if (r0 >= r5.length()) goto L7;
            Preconditions.checkArgument(!this.alphabet.matches(r5.charAt(r0)), "Separator (%s) cannot contain alphabet characters", r5);
            r0 = r0 + 1;
            goto L3
        L7:
            if (this.paddingChar == null) goto L14;
            if (r5.indexOf(this.paddingChar.charValue()) < 0) goto L12;
            r2 = false;
        L12:
            Preconditions.checkArgument(r2, "Separator (%s) cannot contain padding character", r5);
        L14:
            return new SeparatedBaseEncoding(this, r5, r6);
        }
    }

    static {
        BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", '=');
        BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", '=');
        BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", '=');
        BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", '=');
        BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");
    }

    BaseEncoding() {
    }

    public static BaseEncoding base16() {
        return BASE16;
    }

    public static BaseEncoding base32() {
        return BASE32;
    }

    public static BaseEncoding base32Hex() {
        return BASE32_HEX;
    }

    public static BaseEncoding base64() {
        return BASE64;
    }

    public static BaseEncoding base64Url() {
        return BASE64_URL;
    }

    private static byte[] extract(byte[] r2, int r3) {
        if (r3 != r2.length) goto L5;
        return r2;
    L5:
        byte[] r0 = new byte[r3];
        System.arraycopy(r2, 0, r0, 0, r3);
        return r0;
    }

    static Reader ignoringReader(final Reader r1, final String r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C08403(r1, r2);
    }

    static Appendable separatingAppendable(final Appendable r1, final String r2, final int r3) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        if (r3 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        return new C08414(r3, r1, r2);
    L5:
        r0 = false;
        goto L6
    }

    static Writer separatingWriter(final Writer r2, String r3, int r4) {
        final Appendable r0 = separatingAppendable(r2, r3, r4);
        return new C08425(r0, r2);
    }

    public abstract boolean canDecode(CharSequence r1);

    public final byte[] decode(CharSequence r3) {
        return decodeChecked(r3);
    L4:
        e = move-exception;
        throw new IllegalArgumentException(e);
    }

    final byte[] decodeChecked(CharSequence r4) throws DecodingException {
        CharSequence r5 = trimTrailingPadding(r4);
        byte[] r0 = new byte[maxDecodedSize(r5.length())];
        return extract(r0, decodeTo(r0, r5));
    }

    abstract int decodeTo(byte[] r1, CharSequence r2) throws DecodingException;

    public final ByteSource decodingSource(final CharSource r2) {
        Preconditions.checkNotNull(r2);
        return new C08392(this, r2);
    }

    public abstract InputStream decodingStream(Reader r1);

    public String encode(byte[] r3) {
        return encode(r3, 0, r3.length);
    }

    public final String encode(byte[] r4, int r5, int r6) {
        Preconditions.checkPositionIndexes(r5, r5 + r6, r4.length);
        StringBuilder r0 = new StringBuilder(maxEncodedSize(r6));
        encodeTo(r0, r4, r5, r6);     // Catch: IOException -> L6
        return r0.toString();
    L6:
        e = move-exception;
        throw new AssertionError(e);
    }

    abstract void encodeTo(Appendable r1, byte[] r2, int r3, int r4) throws IOException;

    public final ByteSink encodingSink(final CharSink r2) {
        Preconditions.checkNotNull(r2);
        return new C08381(this, r2);
    }

    public abstract OutputStream encodingStream(Writer r1);

    public abstract BaseEncoding ignoreCase();

    public abstract BaseEncoding lowerCase();

    abstract int maxDecodedSize(int r1);

    abstract int maxEncodedSize(int r1);

    public abstract BaseEncoding omitPadding();

    CharSequence trimTrailingPadding(CharSequence r2) {
        return (CharSequence) Preconditions.checkNotNull(r2);
    }

    public abstract BaseEncoding upperCase();

    public abstract BaseEncoding withPadChar(char r1);

    public abstract BaseEncoding withSeparator(String r1, int r2);
}
