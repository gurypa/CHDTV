package com.google.common.base;

import java.util.Arrays;
import java.util.BitSet;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class CharMatcher implements Predicate<Character> {
    private static final int DISTINCT_CHARS = 65536;

    private static final class And extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        And(CharMatcher r2, CharMatcher r3) {
            this.first = (CharMatcher) Preconditions.checkNotNull(r2);
            this.second = (CharMatcher) Preconditions.checkNotNull(r3);
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            if (this.first.matches(r2) == true) goto L5;
        L7:
            return false;
        L5:
            if (this.second.matches(r2) == false) goto L7;
            return true;
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet r4) {
            BitSet r0 = new BitSet();
            this.first.setBits(r0);
            BitSet r1 = new BitSet();
            this.second.setBits(r1);
            r0.and(r1);
            r4.or(r0);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.and(" + this.first + ", " + this.second + ")";
        }
    }

    private static final class Any extends NamedFastMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new Any();
        }

        private Any() {
            super("CharMatcher.any()");
        }

        @Override // com.google.common.base.CharMatcher
        public CharMatcher and(CharMatcher r2) {
            return (CharMatcher) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public String collapseFrom(CharSequence r2, char r3) {
            if (r2.length() != 0) goto L6;
            return "";
        L6:
            return String.valueOf(r3);
        }

        @Override // com.google.common.base.CharMatcher
        public int countIn(CharSequence r2) {
            return r2.length();
        }

        @Override // com.google.common.base.CharMatcher
        public int indexIn(CharSequence r2) {
            if (r2.length() != 0) goto L5;
            return -1;
        L5:
            return 0;
        }

        @Override // com.google.common.base.CharMatcher
        public int indexIn(CharSequence r3, int r4) {
            int r0 = r3.length();
            Preconditions.checkPositionIndex(r4, r0);
            if (r4 != r0) goto L6;
            return -1;
        L6:
            return r4;
        }

        @Override // com.google.common.base.CharMatcher
        public int lastIndexIn(CharSequence r2) {
            return r2.length() - 1;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            return true;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matchesAllOf(CharSequence r2) {
            Preconditions.checkNotNull(r2);
            return true;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matchesNoneOf(CharSequence r2) {
            if (r2.length() != 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.base.CharMatcher.FastMatcher, com.google.common.base.CharMatcher
        public CharMatcher negate() {
            return none();
        }

        @Override // com.google.common.base.CharMatcher
        /* JADX INFO: renamed from: or */
        public CharMatcher mo182or(CharMatcher r1) {
            Preconditions.checkNotNull(r1);
            return this;
        }

        @Override // com.google.common.base.CharMatcher
        public String removeFrom(CharSequence r2) {
            Preconditions.checkNotNull(r2);
            return "";
        }

        @Override // com.google.common.base.CharMatcher
        public String replaceFrom(CharSequence r3, char r4) {
            char[] r0 = new char[r3.length()];
            Arrays.fill(r0, r4);
            return new String(r0);
        }

        @Override // com.google.common.base.CharMatcher
        public String replaceFrom(CharSequence r4, CharSequence r5) {
            StringBuilder r0 = new StringBuilder(r4.length() * r5.length());
            int r1 = 0;
        L4:
            if (r1 >= r4.length()) goto L7;
            r0.append(r5);
            r1 = r1 + 1;
            goto L4
        L7:
            return r0.toString();
        }

        @Override // com.google.common.base.CharMatcher
        public String trimFrom(CharSequence r2) {
            Preconditions.checkNotNull(r2);
            return "";
        }
    }

    private static final class AnyOf extends CharMatcher {
        private final char[] chars;

        public AnyOf(CharSequence r2) {
            this.chars = r2.toString().toCharArray();
            Arrays.sort(this.chars);
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            if (Arrays.binarySearch(this.chars, r2) < 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet r5) {
            char[] r0 = this.chars;
            int r1 = r0.length;
            int r2 = 0;
        L3:
            if (r2 >= r1) goto L5;
            r5.set(r0[r2]);
            r2 = r2 + 1;
            goto L3
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            StringBuilder r0 = new StringBuilder("CharMatcher.anyOf(\"");
            char[] r1 = this.chars;
            int r2 = r1.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L5;
            r0.append(CharMatcher.access$100(r1[r3]));
            r3 = r3 + 1;
            goto L3
        L5:
            r0.append("\")");
            return r0.toString();
        }
    }

    private static final class Ascii extends NamedFastMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new Ascii();
        }

        Ascii() {
            super("CharMatcher.ascii()");
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            if (r2 > 127) goto L5;
            return true;
        L5:
            return false;
        }
    }

    private static final class BitSetMatcher extends NamedFastMatcher {
        private final BitSet table;

        private BitSetMatcher(BitSet r3, String r4) {
            super(r4);
            if ((r3.length() + 64) >= r3.size()) goto L5;
            r3 = (BitSet) r3.clone();
        L5:
            this.table = r3;
        }

        /* synthetic */ BitSetMatcher(BitSet r1, String r2, C05331 r3) {
            this(r1, r2);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            return this.table.get(r2);
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet r2) {
            r2.or(this.table);
        }
    }

    private static final class BreakingWhitespace extends CharMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new BreakingWhitespace();
        }

        private BreakingWhitespace() {
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r4) {
            switch(r4) {
                case 9: goto L10;
                case 10: goto L10;
                case 11: goto L10;
                case 12: goto L10;
                case 13: goto L10;
                case 32: goto L10;
                case 133: goto L10;
                case 5760: goto L10;
                case 8199: goto L9;
                case 8232: goto L10;
                case 8233: goto L10;
                case 8287: goto L10;
                case 12288: goto L10;
                default: goto L5;
            };
        L9:
            return false;
        L10:
            return true;
        L5:
            if (r4 >= 8192) goto L7;
            return false;
        L7:
            if (r4 <= 8202) goto L13;
            return false;
        L13:
            return true;
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.breakingWhitespace()";
        }
    }

    private static final class Digit extends RangesMatcher {
        static final CharMatcher INSTANCE = null;
        private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０";

        static {
            INSTANCE = new Digit();
        }

        private Digit() {
            super("CharMatcher.digit()", zeroes(), nines());
        }

        private static char[] nines() {
            char[] r1 = new char[ZEROES.length()];
            int r2 = 0;
        L4:
            if (r2 >= ZEROES.length()) goto L6;
            r1[r2] = (char) (ZEROES.charAt(r2) + '\t');
            r2 = r2 + 1;
            goto L4
        L6:
            return r1;
        }

        private static char[] zeroes() {
            return ZEROES.toCharArray();
        }
    }

    static abstract class FastMatcher extends CharMatcher {
        FastMatcher() {
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public CharMatcher negate() {
            return new NegatedFastMatcher(this);
        }

        @Override // com.google.common.base.CharMatcher
        public final CharMatcher precomputed() {
            return this;
        }
    }

    private static final class ForPredicate extends CharMatcher {
        private final Predicate<? super Character> predicate;

        ForPredicate(Predicate<? super Character> r2) {
            this.predicate = (Predicate) Preconditions.checkNotNull(r2);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.base.CharMatcher
        public boolean apply(Character r3) {
            return this.predicate.apply(Preconditions.checkNotNull(r3));
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return apply(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r3) {
            return this.predicate.apply(Character.valueOf(r3));
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.forPredicate(" + this.predicate + ")";
        }
    }

    private static final class InRange extends FastMatcher {
        private final char endInclusive;
        private final char startInclusive;

        InRange(char r2, char r3) {
            if (r3 < r2) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0);
            this.startInclusive = r2;
            this.endInclusive = r3;
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            if (this.startInclusive <= r2) goto L5;
        L7:
            return false;
        L5:
            if (r2 > this.endInclusive) goto L7;
            return true;
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet r3) {
            r3.set(this.startInclusive, this.endInclusive + 1);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.inRange('" + CharMatcher.access$100(this.startInclusive) + "', '" + CharMatcher.access$100(this.endInclusive) + "')";
        }
    }

    private static final class Invisible extends RangesMatcher {
        static final CharMatcher INSTANCE = null;
        private static final String RANGE_ENDS = "  \u00ad\u0605\u061c\u06dd\u070f\u0891\u08e2\u1680\u180e\u200f \u2064\u206f\u3000\uf8ff\ufeff\ufffb";
        private static final String RANGE_STARTS = "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f\u0890\u08e2\u1680\u180e\u2000\u2028\u205f\u2066\u3000\ud800\ufeff\ufff9";

        static {
            INSTANCE = new Invisible();
        }

        private Invisible() {
            super("CharMatcher.invisible()", RANGE_STARTS.toCharArray(), RANGE_ENDS.toCharArray());
        }
    }

    /* JADX INFO: renamed from: com.google.common.base.CharMatcher$Is */
    private static final class C0534Is extends FastMatcher {
        private final char match;

        C0534Is(char r1) {
            this.match = r1;
        }

        @Override // com.google.common.base.CharMatcher
        public CharMatcher and(CharMatcher r2) {
            if (r2.matches(this.match) == false) goto L6;
            return this;
        L6:
            return none();
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            if (r2 != this.match) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.base.CharMatcher.FastMatcher, com.google.common.base.CharMatcher
        public CharMatcher negate() {
            return isNot(this.match);
        }

        @Override // com.google.common.base.CharMatcher
        /* JADX INFO: renamed from: or */
        public CharMatcher mo182or(CharMatcher r2) {
            if (r2.matches(this.match) == false) goto L6;
            return r2;
        L6:
            return super.mo182or(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public String replaceFrom(CharSequence r3, char r4) {
            return r3.toString().replace(this.match, r4);
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet r2) {
            r2.set(this.match);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.is('" + CharMatcher.access$100(this.match) + "')";
        }
    }

    private static final class IsEither extends FastMatcher {
        private final char match1;
        private final char match2;

        IsEither(char r1, char r2) {
            this.match1 = r1;
            this.match2 = r2;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            if (r2 != this.match1) goto L5;
        L8:
            return true;
        L5:
            if (r2 == this.match2) goto L8;
            return false;
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet r2) {
            r2.set(this.match1);
            r2.set(this.match2);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.anyOf(\"" + CharMatcher.access$100(this.match1) + CharMatcher.access$100(this.match2) + "\")";
        }
    }

    private static final class IsNot extends FastMatcher {
        private final char match;

        IsNot(char r1) {
            this.match = r1;
        }

        @Override // com.google.common.base.CharMatcher
        public CharMatcher and(CharMatcher r2) {
            if (r2.matches(this.match) == false) goto L6;
            return super.and(r2);
        L6:
            return r2;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            if (r2 == this.match) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.base.CharMatcher.FastMatcher, com.google.common.base.CharMatcher
        public CharMatcher negate() {
            return m181is(this.match);
        }

        @Override // com.google.common.base.CharMatcher
        /* JADX INFO: renamed from: or */
        public CharMatcher mo182or(CharMatcher r2) {
            if (r2.matches(this.match) == false) goto L6;
            return any();
        L6:
            return this;
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet r3) {
            r3.set(0, this.match);
            r3.set(this.match + 1, 65536);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.isNot('" + CharMatcher.access$100(this.match) + "')";
        }
    }

    private static final class JavaDigit extends CharMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new JavaDigit();
        }

        private JavaDigit() {
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            return Character.isDigit(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.javaDigit()";
        }
    }

    private static final class JavaIsoControl extends NamedFastMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new JavaIsoControl();
        }

        private JavaIsoControl() {
            super("CharMatcher.javaIsoControl()");
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            if (r2 > 31) goto L5;
        L10:
            return true;
        L5:
            if (r2 >= 127) goto L7;
        L9:
            return false;
        L7:
            if (r2 > 159) goto L9;
            goto L9
        }
    }

    private static final class JavaLetter extends CharMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new JavaLetter();
        }

        private JavaLetter() {
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            return Character.isLetter(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.javaLetter()";
        }
    }

    private static final class JavaLetterOrDigit extends CharMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new JavaLetterOrDigit();
        }

        private JavaLetterOrDigit() {
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            return Character.isLetterOrDigit(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.javaLetterOrDigit()";
        }
    }

    private static final class JavaLowerCase extends CharMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new JavaLowerCase();
        }

        private JavaLowerCase() {
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            return Character.isLowerCase(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.javaLowerCase()";
        }
    }

    private static final class JavaUpperCase extends CharMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new JavaUpperCase();
        }

        private JavaUpperCase() {
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            return Character.isUpperCase(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.javaUpperCase()";
        }
    }

    static abstract class NamedFastMatcher extends FastMatcher {
        private final String description;

        NamedFastMatcher(String r2) {
            this.description = (String) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public final String toString() {
            return this.description;
        }
    }

    private static class Negated extends CharMatcher {
        final CharMatcher original;

        Negated(CharMatcher r2) {
            this.original = (CharMatcher) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public int countIn(CharSequence r3) {
            return r3.length() - this.original.countIn(r3);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            return !this.original.matches(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matchesAllOf(CharSequence r2) {
            return this.original.matchesNoneOf(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matchesNoneOf(CharSequence r2) {
            return this.original.matchesAllOf(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public CharMatcher negate() {
            return this.original;
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet r4) {
            BitSet r0 = new BitSet();
            this.original.setBits(r0);
            r0.flip(0, 65536);
            r4.or(r0);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return this.original + ".negate()";
        }
    }

    private static class NegatedFastMatcher extends Negated {
        NegatedFastMatcher(CharMatcher r1) {
            super(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public final CharMatcher precomputed() {
            return this;
        }
    }

    private static final class None extends NamedFastMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new None();
        }

        private None() {
            super("CharMatcher.none()");
        }

        @Override // com.google.common.base.CharMatcher
        public CharMatcher and(CharMatcher r1) {
            Preconditions.checkNotNull(r1);
            return this;
        }

        @Override // com.google.common.base.CharMatcher
        public String collapseFrom(CharSequence r2, char r3) {
            return r2.toString();
        }

        @Override // com.google.common.base.CharMatcher
        public int countIn(CharSequence r2) {
            Preconditions.checkNotNull(r2);
            return 0;
        }

        @Override // com.google.common.base.CharMatcher
        public int indexIn(CharSequence r2) {
            Preconditions.checkNotNull(r2);
            return -1;
        }

        @Override // com.google.common.base.CharMatcher
        public int indexIn(CharSequence r3, int r4) {
            Preconditions.checkPositionIndex(r4, r3.length());
            return -1;
        }

        @Override // com.google.common.base.CharMatcher
        public int lastIndexIn(CharSequence r2) {
            Preconditions.checkNotNull(r2);
            return -1;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            return false;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matchesAllOf(CharSequence r2) {
            if (r2.length() != 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matchesNoneOf(CharSequence r2) {
            Preconditions.checkNotNull(r2);
            return true;
        }

        @Override // com.google.common.base.CharMatcher.FastMatcher, com.google.common.base.CharMatcher
        public CharMatcher negate() {
            return any();
        }

        @Override // com.google.common.base.CharMatcher
        /* JADX INFO: renamed from: or */
        public CharMatcher mo182or(CharMatcher r2) {
            return (CharMatcher) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public String removeFrom(CharSequence r2) {
            return r2.toString();
        }

        @Override // com.google.common.base.CharMatcher
        public String replaceFrom(CharSequence r2, char r3) {
            return r2.toString();
        }

        @Override // com.google.common.base.CharMatcher
        public String replaceFrom(CharSequence r2, CharSequence r3) {
            Preconditions.checkNotNull(r3);
            return r2.toString();
        }

        @Override // com.google.common.base.CharMatcher
        public String trimFrom(CharSequence r2) {
            return r2.toString();
        }

        @Override // com.google.common.base.CharMatcher
        public String trimLeadingFrom(CharSequence r2) {
            return r2.toString();
        }

        @Override // com.google.common.base.CharMatcher
        public String trimTrailingFrom(CharSequence r2) {
            return r2.toString();
        }
    }

    /* JADX INFO: renamed from: com.google.common.base.CharMatcher$Or */
    private static final class C0535Or extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        C0535Or(CharMatcher r2, CharMatcher r3) {
            this.first = (CharMatcher) Preconditions.checkNotNull(r2);
            this.second = (CharMatcher) Preconditions.checkNotNull(r3);
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r2) {
            if (this.first.matches(r2) == false) goto L5;
        L8:
            return true;
        L5:
            if (this.second.matches(r2) == true) goto L8;
            return false;
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet r2) {
            this.first.setBits(r2);
            this.second.setBits(r2);
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.or(" + this.first + ", " + this.second + ")";
        }
    }

    private static class RangesMatcher extends CharMatcher {
        private final String description;
        private final char[] rangeEnds;
        private final char[] rangeStarts;

        RangesMatcher(String r6, char[] r7, char[] r8) {
            this.description = r6;
            this.rangeStarts = r7;
            this.rangeEnds = r8;
            if (r7.length != r8.length) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0);
            int r1 = 0;
        L8:
            if (r1 >= r7.length) goto L21;
            if (r7[r1] > r8[r1]) goto L12;
            boolean r2 = true;
        L13:
            Preconditions.checkArgument(r2);
            if ((r1 + 1) >= r7.length) goto L20;
            if (r8[r1] >= r7[r1 + 1]) goto L18;
            boolean r3 = true;
        L19:
            Preconditions.checkArgument(r3);
            goto L20
        L18:
            r3 = false;
        L20:
            r1 = r1 + 1;
            goto L8
        L12:
            r2 = false;
            goto L13
        L21:
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character r1) {
            return super.apply2(r1);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r4) {
            int r0 = Arrays.binarySearch(this.rangeStarts, r4);
            if (r0 < 0) goto L5;
            return true;
        L5:
            int r2 = (~r0) - 1;
            if (r2 >= 0) goto L8;
        L10:
            return false;
        L8:
            if (r4 > this.rangeEnds[r2]) goto L10;
            return true;
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return this.description;
        }
    }

    private static final class SingleWidth extends RangesMatcher {
        static final CharMatcher INSTANCE = null;

        static {
            INSTANCE = new SingleWidth();
        }

        private SingleWidth() {
            super("CharMatcher.singleWidth()", "\u0000־א׳\u0600ݐ\u0e00Ḁ℀ﭐﹰ｡".toCharArray(), "ӹ־ת״ۿݿ\u0e7f₯℺\ufdff\ufeffￜ".toCharArray());
        }
    }

    static final class Whitespace extends NamedFastMatcher {
        static final CharMatcher INSTANCE = null;
        static final int MULTIPLIER = 1682554634;
        static final int SHIFT = 0;
        static final String TABLE = "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001  \f\u2009\u3000\u2004\u3000\u3000\u2028\n \u3000";

        static {
            SHIFT = Integer.numberOfLeadingZeros(TABLE.length() - 1);
            INSTANCE = new Whitespace();
        }

        Whitespace() {
            super("CharMatcher.whitespace()");
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char r3) {
            if (TABLE.charAt((MULTIPLIER * r3) >>> SHIFT) != r3) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet r4) {
            int r0 = 0;
        L4:
            if (r0 >= TABLE.length()) goto L6;
            r4.set(TABLE.charAt(r0));
            r0 = r0 + 1;
            goto L4
        }
    }

    protected CharMatcher() {
    }

    static /* synthetic */ String access$100(char r1) {
        return showCharacter(r1);
    }

    public static CharMatcher any() {
        return Any.INSTANCE;
    }

    public static CharMatcher anyOf(CharSequence r2) {
        switch(r2.length()) {
            case 0: goto L11;
            case 1: goto L9;
            case 2: goto L7;
            default: goto L5;
        };
    L5:
        return new AnyOf(r2);
    L7:
        return isEither(r2.charAt(0), r2.charAt(1));
    L9:
        return m181is(r2.charAt(0));
    L11:
        return none();
    }

    public static CharMatcher ascii() {
        return Ascii.INSTANCE;
    }

    public static CharMatcher breakingWhitespace() {
        return BreakingWhitespace.INSTANCE;
    }

    @Deprecated
    public static CharMatcher digit() {
        return Digit.INSTANCE;
    }

    private String finishCollapseFrom(CharSequence r4, int r5, int r6, char r7, StringBuilder r8, boolean r9) {
        int r0 = r5;
    L3:
        if (r0 >= r6) goto L11;
        char r1 = r4.charAt(r0);
        if (matches(r1) == false) goto L8;
        if (r9 == true) goto L9;
        r8.append(r7);
        r9 = true;
    L9:
        r0 = r0 + 1;
        goto L3
    L8:
        r8.append(r1);
        r9 = false;
        goto L9
    L11:
        return r8.toString();
    }

    public static CharMatcher forPredicate(Predicate<? super Character> r1) {
        if ((r1 instanceof CharMatcher) == false) goto L6;
        return (CharMatcher) r1;
    L6:
        return new ForPredicate(r1);
    }

    public static CharMatcher inRange(char r1, char r2) {
        return new InRange(r1, r2);
    }

    @Deprecated
    public static CharMatcher invisible() {
        return Invisible.INSTANCE;
    }

    /* JADX INFO: renamed from: is */
    public static CharMatcher m181is(char r1) {
        return new C0534Is(r1);
    }

    private static IsEither isEither(char r1, char r2) {
        return new IsEither(r1, r2);
    }

    public static CharMatcher isNot(char r1) {
        return new IsNot(r1);
    }

    private static boolean isSmall(int r1, int r2) {
        if (r1 <= 1023) goto L5;
    L7:
        return false;
    L5:
        if (r2 <= ((r1 * 4) * 16)) goto L7;
        return true;
    }

    @Deprecated
    public static CharMatcher javaDigit() {
        return JavaDigit.INSTANCE;
    }

    public static CharMatcher javaIsoControl() {
        return JavaIsoControl.INSTANCE;
    }

    @Deprecated
    public static CharMatcher javaLetter() {
        return JavaLetter.INSTANCE;
    }

    @Deprecated
    public static CharMatcher javaLetterOrDigit() {
        return JavaLetterOrDigit.INSTANCE;
    }

    @Deprecated
    public static CharMatcher javaLowerCase() {
        return JavaLowerCase.INSTANCE;
    }

    @Deprecated
    public static CharMatcher javaUpperCase() {
        return JavaUpperCase.INSTANCE;
    }

    public static CharMatcher none() {
        return None.INSTANCE;
    }

    public static CharMatcher noneOf(CharSequence r1) {
        return anyOf(r1).negate();
    }

    private static CharMatcher precomputedPositive(int r3, BitSet r4, String r5) {
        switch(r3) {
            case 0: goto L12;
            case 1: goto L10;
            case 2: goto L7;
            default: goto L5;
        };
    L7:
        char r0 = (char) r4.nextSetBit(0);
        return isEither(r0, (char) r4.nextSetBit(r0 + 1));
    L5:
        if (isSmall(r3, r4.length()) == false) goto L14;
        return SmallCharMatcher.from(r4, r5);
    L14:
        return new BitSetMatcher(r4, r5, null);
    L10:
        return m181is((char) r4.nextSetBit(0));
    L12:
        return none();
    }

    private static String showCharacter(char r5) {
        char[] r1 = {'\\', 'u', 0, 0, 0, 0};
        int r2 = 0;
    L4:
        if (r2 >= 4) goto L7;
        r1[5 - r2] = "0123456789ABCDEF".charAt(r5 & 15);
        r5 = (char) (r5 >> 4);
        r2 = r2 + 1;
        goto L4
    L7:
        return String.copyValueOf(r1);
    }

    @Deprecated
    public static CharMatcher singleWidth() {
        return SingleWidth.INSTANCE;
    }

    public static CharMatcher whitespace() {
        return Whitespace.INSTANCE;
    }

    public CharMatcher and(CharMatcher r2) {
        return new And(this, r2);
    }

    @Deprecated
    /* JADX INFO: renamed from: apply, reason: avoid collision after fix types in other method */
    public boolean apply2(Character r2) {
        return matches(r2.charValue());
    }

    @Override // com.google.common.base.Predicate
    @Deprecated
    public /* bridge */ /* synthetic */ boolean apply(Character r1) {
        return apply2(r1);
    }

    public String collapseFrom(CharSequence r10, char r11) {
        int r3 = r10.length();
        int r7 = 0;
    L3:
        if (r7 >= r3) goto L17;
        char r8 = r10.charAt(r7);
        if (matches(r8) == false) goto L15;
        if (r8 != r11) goto L13;
        if (r7 != (r3 - 1)) goto L10;
    L11:
        r7 = r7 + 1;
        goto L15
    L10:
        if (matches(r10.charAt(r7 + 1)) == false) goto L11;
    L13:
        return finishCollapseFrom(r10, r7 + 1, r3, r11, new StringBuilder(r3).append(r10, 0, r7).append(r11), true);
    L15:
        r7 = r7 + 1;
        r10 = r10;
        r11 = r11;
        goto L3
    L17:
        return r10.toString();
    }

    public int countIn(CharSequence r4) {
        int r0 = 0;
        int r1 = 0;
    L4:
        if (r1 >= r4.length()) goto L9;
        if (matches(r4.charAt(r1)) == false) goto L8;
        r0 = r0 + 1;
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        return r0;
    }

    public int indexIn(CharSequence r2) {
        return indexIn(r2, 0);
    }

    public int indexIn(CharSequence r4, int r5) {
        int r0 = r4.length();
        Preconditions.checkPositionIndex(r5, r0);
        int r1 = r5;
    L3:
        if (r1 >= r0) goto L8;
        if (matches(r4.charAt(r1)) == true) goto L6;
        r1 = r1 + 1;
        goto L3
    L6:
        return r1;
    L8:
        return -1;
    }

    public int lastIndexIn(CharSequence r3) {
        int r0 = r3.length() - 1;
    L3:
        if (r0 < 0) goto L8;
        if (matches(r3.charAt(r0)) == true) goto L6;
        r0 = r0 - 1;
        goto L3
    L6:
        return r0;
    L8:
        return -1;
    }

    public abstract boolean matches(char r1);

    public boolean matchesAllOf(CharSequence r4) {
        int r0 = r4.length() - 1;
    L3:
        if (r0 < 0) goto L9;
        if (matches(r4.charAt(r0)) == false) goto L6;
        r0 = r0 - 1;
        goto L3
    L6:
        return false;
    L9:
        return true;
    }

    public boolean matchesAnyOf(CharSequence r2) {
        return !matchesNoneOf(r2);
    }

    public boolean matchesNoneOf(CharSequence r3) {
        if (indexIn(r3) != (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    public CharMatcher negate() {
        return new Negated(this);
    }

    /* JADX INFO: renamed from: or */
    public CharMatcher mo182or(CharMatcher r2) {
        return new C0535Or(this, r2);
    }

    public CharMatcher precomputed() {
        return Platform.precomputeCharMatcher(this);
    }

    CharMatcher precomputedInternal() {
        BitSet r0 = new BitSet();
        setBits(r0);
        int r1 = r0.cardinality();
        if ((r1 * 2) <= 65536) goto L5;
        r0.flip(0, 65536);
        int r3 = 65536 - r1;
        final String r5 = toString();
        if (r5.endsWith(".negate()") == false) goto L9;
        String r2 = r5.substring(0, r5.length() - ".negate()".length());
    L10:
        final CharMatcher r7 = precomputedPositive(r3, r0, r2);
        return new C05331(this, r7, r5);
    L9:
        r2 = r5 + ".negate()";
        goto L10
    L5:
        return precomputedPositive(r1, r0, toString());
    }

    public String removeFrom(CharSequence r8) {
        String r0 = r8.toString();
        int r1 = indexIn(r0);
        if (r1 != (-1)) goto L5;
        return r0;
    L5:
        char[] r2 = r0.toCharArray();
        int r3 = 1;
    L7:
        r1 = r1 + 1;
        if (r1 == r2.length) goto L10;
        if (matches(r2[r1]) == true) goto L13;
        r2[r1 - r3] = r2[r1];
        goto L7
    L13:
        r3 = r3 + 1;
        goto L7
    L10:
        return new String(r2, 0, r1 - r3);
    }

    public String replaceFrom(CharSequence r6, char r7) {
        String r0 = r6.toString();
        int r1 = indexIn(r0);
        if (r1 != (-1)) goto L5;
        return r0;
    L5:
        char[] r2 = r0.toCharArray();
        r2[r1] = r7;
        int r3 = r1 + 1;
    L7:
        if (r3 >= r2.length) goto L13;
        if (matches(r2[r3]) == false) goto L11;
        r2[r3] = r7;
    L11:
        r3 = r3 + 1;
        goto L7
    L13:
        return new String(r2);
    }

    public String replaceFrom(CharSequence r8, CharSequence r9) {
        int r0 = r9.length();
        if (r0 != 0) goto L7;
        return removeFrom(r8);
    L7:
        if (r0 == 1) goto L9;
        String r1 = r8.toString();
        int r2 = indexIn(r1);
        if (r2 != (-1)) goto L13;
        return r1;
    L13:
        int r4 = r1.length();
        StringBuilder r5 = new StringBuilder(((r4 * 3) / 2) + 16);
        int r6 = 0;
    L14:
        r5.append(r1, r6, r2);
        r5.append(r9);
        r6 = r2 + 1;
        r2 = indexIn(r1, r6);
        if (r2 != (-1)) goto L14;
        r5.append(r1, r6, r4);
        return r5.toString();
    L9:
        return replaceFrom(r8, r9.charAt(0));
    }

    public String retainFrom(CharSequence r2) {
        return negate().removeFrom(r2);
    }

    void setBits(BitSet r3) {
        int r0 = 65535;
    L3:
        if (r0 < 0) goto L8;
        if (matches((char) r0) == false) goto L7;
        r3.set(r0);
    L7:
        r0 = r0 - 1;
        goto L3
    }

    public String toString() {
        return super.toString();
    }

    public String trimAndCollapseFrom(CharSequence r11, char r12) {
        int r0 = r11.length();
        int r2 = r0 - 1;
        int r5 = 0;
    L3:
        if (r5 >= r0) goto L7;
        if (matches(r11.charAt(r5)) == false) goto L7;
        r5 = r5 + 1;
    L7:
        if (r2 <= r5) goto L11;
        if (matches(r11.charAt(r2)) == false) goto L11;
        r2 = r2 - 1;
    L11:
        if (r5 != 0) goto L16;
        if (r2 != (r0 - 1)) goto L16;
        return collapseFrom(r11, r12);
    L16:
        return finishCollapseFrom(r11, r5, r2 + 1, r12, new StringBuilder((r2 + 1) - r5), false);
    }

    public String trimFrom(CharSequence r5) {
        int r0 = r5.length();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        if (matches(r5.charAt(r1)) == false) goto L8;
        r1 = r1 + 1;
    L8:
        int r2 = r0 - 1;
    L9:
        if (r2 <= r1) goto L15;
        if (matches(r5.charAt(r2)) == false) goto L15;
        r2 = r2 - 1;
    L15:
        return r5.subSequence(r1, r2 + 1).toString();
    }

    public String trimLeadingFrom(CharSequence r4) {
        int r0 = r4.length();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        if (matches(r4.charAt(r1)) == false) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        return r4.subSequence(r1, r0).toString();
    L9:
        return "";
    }

    public String trimTrailingFrom(CharSequence r5) {
        int r1 = r5.length() - 1;
    L3:
        if (r1 < 0) goto L9;
        if (matches(r5.charAt(r1)) == false) goto L7;
        r1 = r1 - 1;
        goto L3
    L7:
        return r5.subSequence(0, r1 + 1).toString();
    L9:
        return "";
    }
}
