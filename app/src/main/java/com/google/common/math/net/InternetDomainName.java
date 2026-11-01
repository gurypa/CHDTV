package com.google.common.net;

import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
import com.google.thirdparty.publicsuffix.PublicSuffixType;
import java.util.List;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
public final class InternetDomainName {
    private static final CharMatcher DASH_MATCHER = null;
    private static final CharMatcher DIGIT_MATCHER = null;
    private static final CharMatcher DOTS_MATCHER = null;
    private static final Joiner DOT_JOINER = null;
    private static final Splitter DOT_SPLITTER = null;
    private static final CharMatcher LETTER_MATCHER = null;
    private static final int MAX_DOMAIN_PART_LENGTH = 63;
    private static final int MAX_LENGTH = 253;
    private static final int MAX_PARTS = 127;
    private static final int NO_SUFFIX_FOUND = -1;
    private static final CharMatcher PART_CHAR_MATCHER = null;
    private static final int SUFFIX_NOT_INITIALIZED = -2;
    private final String name;
    private final ImmutableList<String> parts;

    @LazyInit
    private int publicSuffixIndexCache;

    @LazyInit
    private int registrySuffixIndexCache;

    static {
        DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
        DOT_SPLITTER = Splitter.m190on('.');
        DOT_JOINER = Joiner.m183on('.');
        DASH_MATCHER = CharMatcher.anyOf("-_");
        DIGIT_MATCHER = CharMatcher.inRange('0', '9');
        LETTER_MATCHER = CharMatcher.inRange('a', 'z').mo182or(CharMatcher.inRange('A', 'Z'));
        PART_CHAR_MATCHER = DIGIT_MATCHER.mo182or(LETTER_MATCHER).mo182or(DASH_MATCHER);
    }

    InternetDomainName(String r5) {
        this.publicSuffixIndexCache = -2;
        this.registrySuffixIndexCache = -2;
        String r6 = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(r5, '.'));
        boolean r1 = true;
        if (r6.endsWith(".") == false) goto L6;
        r6 = r6.substring(0, r6.length() - 1);
    L6:
        if (r6.length() > MAX_LENGTH) goto L8;
        boolean r0 = true;
    L9:
        Preconditions.checkArgument(r0, "Domain name too long: '%s':", r6);
        this.name = r6;
        this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(r6));
        if (this.parts.size() <= MAX_PARTS) goto L13;
        r1 = false;
    L13:
        Preconditions.checkArgument(r1, "Domain has too many parts: '%s'", r6);
        Preconditions.checkArgument(validateSyntax(this.parts), "Not a valid domain name: '%s'", r6);
        return;
    L8:
        r0 = false;
        goto L9
    }

    private InternetDomainName(String r3, ImmutableList<String> r4) {
        this.publicSuffixIndexCache = -2;
        this.registrySuffixIndexCache = -2;
        Preconditions.checkArgument(!r4.isEmpty(), "Cannot create an InternetDomainName with zero parts.");
        this.name = r3;
        this.parts = r4;
    }

    private InternetDomainName ancestor(int r5) {
        ImmutableList<String> r0 = this.parts.subList(r5, this.parts.size());
        int r1 = r5;
        int r2 = 0;
    L3:
        if (r2 >= r5) goto L6;
        r1 = r1 + this.parts.get(r2).length();
        r2 = r2 + 1;
        goto L3
    L6:
        return new InternetDomainName(this.name.substring(r1), r0);
    }

    private int findSuffixOfType(Optional<PublicSuffixType> r5) {
        int r0 = this.parts.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L18;
        String r2 = DOT_JOINER.join(this.parts.subList(r1, r0));
        if (r1 <= 0) goto L11;
        if (matchesType(r5, Optional.fromNullable(PublicSuffixPatterns.UNDER.get(r2))) == false) goto L11;
        return r1 - 1;
    L11:
        if (matchesType(r5, Optional.fromNullable(PublicSuffixPatterns.EXACT.get(r2))) == true) goto L12;
        if (PublicSuffixPatterns.EXCLUDED.containsKey(r2) == true) goto L16;
        r1 = r1 + 1;
        goto L3
    L16:
        return r1 + 1;
    L12:
        return r1;
    L18:
        return -1;
    }

    public static InternetDomainName from(String r2) {
        return new InternetDomainName((String) Preconditions.checkNotNull(r2));
    }

    public static boolean isValid(String r2) {
        from(r2);     // Catch: IllegalArgumentException -> L5
        return true;
    L6:
        return false;
    }

    private static boolean matchesType(Optional<PublicSuffixType> r1, Optional<PublicSuffixType> r2) {
        if (r1.isPresent() == false) goto L6;
        return r1.equals(r2);
    L6:
        return r2.isPresent();
    }

    private int publicSuffixIndex() {
        int r0 = this.publicSuffixIndexCache;
        if (r0 != (-2)) goto L6;
        int r1 = findSuffixOfType(Optional.absent());
        this.publicSuffixIndexCache = r1;
        return r1;
    L6:
        return r0;
    }

    private int registrySuffixIndex() {
        int r0 = this.registrySuffixIndexCache;
        if (r0 != (-2)) goto L6;
        int r1 = findSuffixOfType(Optional.m185of(PublicSuffixType.REGISTRY));
        this.registrySuffixIndexCache = r1;
        return r1;
    L6:
        return r0;
    }

    private static boolean validatePart(String r5, boolean r6) {
        if (r5.length() >= 1) goto L5;
    L21:
        return false;
    L5:
        if (r5.length() > 63) goto L21;
        String r0 = CharMatcher.ascii().retainFrom(r5);
        if (PART_CHAR_MATCHER.matchesAllOf(r0) == true) goto L11;
        return false;
    L11:
        if (DASH_MATCHER.matches(r5.charAt(0)) == false) goto L13;
    L20:
        return false;
    L13:
        if (DASH_MATCHER.matches(r5.charAt(r5.length() - 1)) == true) goto L20;
        if (r6 == true) goto L17;
    L19:
        return true;
    L17:
        if (DIGIT_MATCHER.matches(r5.charAt(0)) == false) goto L19;
        return false;
    }

    private static boolean validateSyntax(List<String> r6) {
        int r0 = r6.size() - 1;
        if (validatePart(r6.get(r0), true) == true) goto L5;
        return false;
    L5:
        int r2 = 0;
    L6:
        if (r2 >= r0) goto L11;
        if (validatePart(r6.get(r2), false) == false) goto L9;
        r2 = r2 + 1;
        goto L6
    L9:
        return false;
    L11:
        return true;
    }

    public InternetDomainName child(String r3) {
        return from(((String) Preconditions.checkNotNull(r3)) + "." + this.name);
    }

    public boolean equals(@CheckForNull Object r4) {
        if (r4 != this) goto L6;
        return true;
    L6:
        if ((r4 instanceof InternetDomainName) == true) goto L8;
        return false;
    L8:
        return this.name.equals(((InternetDomainName) r4).name);
    }

    public boolean hasParent() {
        if (this.parts.size() > 1) goto L7;
        return false;
    L7:
        return true;
    }

    public boolean hasPublicSuffix() {
        if (publicSuffixIndex() == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean hasRegistrySuffix() {
        if (registrySuffixIndex() == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean isPublicSuffix() {
        if (publicSuffixIndex() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isRegistrySuffix() {
        if (registrySuffixIndex() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isTopDomainUnderRegistrySuffix() {
        if (registrySuffixIndex() == 1) goto L7;
        return false;
    L7:
        return true;
    }

    public boolean isTopPrivateDomain() {
        if (publicSuffixIndex() == 1) goto L7;
        return false;
    L7:
        return true;
    }

    public boolean isUnderPublicSuffix() {
        if (publicSuffixIndex() <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isUnderRegistrySuffix() {
        if (registrySuffixIndex() <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    public InternetDomainName parent() {
        Preconditions.checkState(hasParent(), "Domain '%s' has no parent", this.name);
        return ancestor(1);
    }

    public ImmutableList<String> parts() {
        return this.parts;
    }

    @CheckForNull
    public InternetDomainName publicSuffix() {
        if (hasPublicSuffix() == true) goto L7;
        return null;
    L7:
        return ancestor(publicSuffixIndex());
    }

    @CheckForNull
    public InternetDomainName registrySuffix() {
        if (hasRegistrySuffix() == true) goto L7;
        return null;
    L7:
        return ancestor(registrySuffixIndex());
    }

    public String toString() {
        return this.name;
    }

    public InternetDomainName topDomainUnderRegistrySuffix() {
        if (isTopDomainUnderRegistrySuffix() == false) goto L5;
        return this;
    L5:
        Preconditions.checkState(isUnderRegistrySuffix(), "Not under a registry suffix: %s", this.name);
        return ancestor(registrySuffixIndex() - 1);
    }

    public InternetDomainName topPrivateDomain() {
        if (isTopPrivateDomain() == false) goto L5;
        return this;
    L5:
        Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", this.name);
        return ancestor(publicSuffixIndex() - 1);
    }
}
