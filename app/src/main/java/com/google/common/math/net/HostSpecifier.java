package com.google.common.net;

import com.google.common.base.Preconditions;
import java.net.InetAddress;
import java.text.ParseException;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class HostSpecifier {
    private final String canonicalForm;

    private HostSpecifier(String r1) {
        this.canonicalForm = r1;
    }

    public static HostSpecifier from(String r4) throws ParseException {
        return fromValid(r4);
    L4:
        e = move-exception;
        ParseException r1 = new ParseException("Invalid host specifier: " + r4, 0);
        r1.initCause(e);
        throw r1;
    }

    public static HostSpecifier fromValid(String r7) {
        HostAndPort r0 = HostAndPort.fromString(r7);
        Preconditions.checkArgument(!r0.hasPort());
        String r1 = r0.getHost();
        InetAddress r2 = null;
        r2 = InetAddresses.forString(r1);     // Catch: IllegalArgumentException -> L5
    L6:
        if (r2 != null) goto L8;
        InternetDomainName r3 = InternetDomainName.from(r1);
        if (r3.hasPublicSuffix() == false) goto L14;
        return new HostSpecifier(r3.toString());
    L14:
        throw new IllegalArgumentException("Domain name does not have a recognized public suffix: " + r1);
    L8:
        return new HostSpecifier(InetAddresses.toUriString(r2));
    }

    public static boolean isValid(String r2) {
        fromValid(r2);     // Catch: IllegalArgumentException -> L5
        return true;
    L6:
        return false;
    }

    public boolean equals(@CheckForNull Object r4) {
        if (this != r4) goto L6;
        return true;
    L6:
        if ((r4 instanceof HostSpecifier) == true) goto L8;
        return false;
    L8:
        return this.canonicalForm.equals(((HostSpecifier) r4).canonicalForm);
    }

    public int hashCode() {
        return this.canonicalForm.hashCode();
    }

    public String toString() {
        return this.canonicalForm;
    }
}
