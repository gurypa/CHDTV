package com.google.common.escape;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.CheckForNull;
import kotlin.jvm.internal.CharCompanionObject;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Escapers {
    private static final Escaper NULL_ESCAPER = null;

    public static final class Builder {
        private final Map<Character, String> replacementMap;
        private char safeMax;
        private char safeMin;

        @CheckForNull
        private String unsafeReplacement;

        private Builder() {
            this.replacementMap = new HashMap();
            this.safeMin = 0;
            this.safeMax = CharCompanionObject.MAX_VALUE;
            this.unsafeReplacement = null;
        }

        /* synthetic */ Builder(C07811 r1) {
            this();
        }

        static /* synthetic */ String access$100(Builder r1) {
            return r1.unsafeReplacement;
        }

        public Builder addEscape(char r3, String r4) {
            Preconditions.checkNotNull(r4);
            this.replacementMap.put(Character.valueOf(r3), r4);
            return this;
        }

        public Escaper build() {
            final Map<Character, String> r1 = this.replacementMap;
            final char r2 = this.safeMin;
            final char r3 = this.safeMax;
            return new C07821(this, r1, r2, r3);
        }

        public Builder setSafeRange(char r1, char r2) {
            this.safeMin = r1;
            this.safeMax = r2;
            return this;
        }

        public Builder setUnsafeReplacement(String r1) {
            this.unsafeReplacement = r1;
            return this;
        }
    }

    static {
        NULL_ESCAPER = new C07811();
    }

    private Escapers() {
    }

    public static Builder builder() {
        return new Builder(null);
    }

    @CheckForNull
    public static String computeReplacement(CharEscaper r1, char r2) {
        return stringOrNull(r1.escape(r2));
    }

    @CheckForNull
    public static String computeReplacement(UnicodeEscaper r1, int r2) {
        return stringOrNull(r1.escape(r2));
    }

    public static Escaper nullEscaper() {
        return NULL_ESCAPER;
    }

    @CheckForNull
    private static String stringOrNull(@CheckForNull char[] r1) {
        if (r1 != null) goto L5;
        return null;
    L5:
        return new String(r1);
    }
}
