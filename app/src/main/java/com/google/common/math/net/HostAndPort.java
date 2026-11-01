package com.google.common.net;

import com.google.common.base.CharMatcher;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
public final class HostAndPort implements Serializable {
    private static final int NO_PORT = -1;
    private static final long serialVersionUID = 0;
    private final boolean hasBracketlessColons;
    private final String host;
    private final int port;

    private HostAndPort(String r1, int r2, boolean r3) {
        this.host = r1;
        this.port = r2;
        this.hasBracketlessColons = r3;
    }

    public static HostAndPort fromHost(String r3) {
        HostAndPort r0 = fromString(r3);
        Preconditions.checkArgument(!r0.hasPort(), "Host has a port: %s", r3);
        return r0;
    }

    public static HostAndPort fromParts(String r4, int r5) {
        Preconditions.checkArgument(isValidPort(r5), "Port out of range: %s", r5);
        HostAndPort r0 = fromString(r4);
        Preconditions.checkArgument(!r0.hasPort(), "Host has a port: %s", r4);
        return new HostAndPort(r0.host, r5, r0.hasBracketlessColons);
    }

    public static HostAndPort fromString(String r8) {
        Preconditions.checkNotNull(r8);
        String r0 = null;
        boolean r1 = false;
        boolean r3 = false;
        if (r8.startsWith("[") == false) goto L5;
        String[] r2 = getHostAndPortFromBracketedHost(r8);
        String r5 = r2[0];
        r0 = r2[1];
    L15:
        int r4 = -1;
        if (Strings.isNullOrEmpty(r0) == true) goto L29;
        if (r0.startsWith("+") == false) goto L20;
    L22:
        Preconditions.checkArgument(r3, "Unparseable port number: %s", r8);
        r4 = Integer.parseInt(r0);     // Catch: NumberFormatException -> L25
        Preconditions.checkArgument(isValidPort(r4), "Port number out of range: %s", r8);
    L27:
        throw new IllegalArgumentException("Unparseable port number: " + r8);
    L20:
        if (CharMatcher.ascii().matchesAllOf(r0) == false) goto L22;
        r3 = true;
    L29:
        return new HostAndPort(r5, r4, r1);
    L5:
        int r6 = r8.indexOf(58);
        if (r6 < 0) goto L11;
        if (r8.indexOf(58, r6 + 1) != (-1)) goto L11;
        String r7 = r8.substring(0, r6);
        r0 = r8.substring(r6 + 1);
        r5 = r7;
    L11:
        if (r6 < 0) goto L13;
        boolean r9 = true;
    L14:
        r1 = r9;
        r5 = r8;
        goto L15
    L13:
        r9 = false;
        goto L14
    }

    private static String[] getHostAndPortFromBracketedHost(String r8) {
        boolean r0 = false;
        if (r8.charAt(0) != '[') goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1, "Bracketed host-port string must start with a bracket: %s", r8);
        int r2 = r8.indexOf(58);
        int r4 = r8.lastIndexOf(93);
        if (r2 <= (-1)) goto L10;
        if (r4 <= r2) goto L10;
        boolean r5 = true;
    L11:
        Preconditions.checkArgument(r5, "Invalid bracketed host/port: %s", r8);
        String r6 = r8.substring(1, r4);
        if ((r4 + 1) != r8.length()) goto L16;
        return new String[]{r6, ""};
    L16:
        if (r8.charAt(r4 + 1) != ':') goto L18;
        r0 = true;
    L18:
        Preconditions.checkArgument(r0, "Only a colon may follow a close bracket: %s", r8);
        int r3 = r4 + 2;
    L20:
        if (r3 >= r8.length()) goto L23;
        Preconditions.checkArgument(Character.isDigit(r8.charAt(r3)), "Port must be numeric: %s", r8);
        r3 = r3 + 1;
        goto L20
    L23:
        return new String[]{r6, r8.substring(r4 + 2)};
    L10:
        r5 = false;
        goto L11
    L5:
        r1 = false;
        goto L6
    }

    private static boolean isValidPort(int r1) {
        if (r1 >= 0) goto L4;
    L6:
        return false;
    L4:
        if (r1 > 65535) goto L6;
        return true;
    }

    public boolean equals(@CheckForNull Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof HostAndPort) == false) goto L14;
        HostAndPort r1 = (HostAndPort) r6;
        if (Objects.equal(this.host, r1.host) == false) goto L13;
        if (this.port != r1.port) goto L13;
        return true;
    L13:
        return false;
    L14:
        return false;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        Preconditions.checkState(hasPort());
        return this.port;
    }

    public int getPortOrDefault(int r2) {
        if (hasPort() == false) goto L6;
        return this.port;
    L6:
        return r2;
    }

    public boolean hasPort() {
        if (this.port < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.host, Integer.valueOf(this.port)});
    }

    public HostAndPort requireBracketsForIPv6() {
        Preconditions.checkArgument(!this.hasBracketlessColons, "Possible bracketless IPv6 literal: %s", this.host);
        return this;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder(this.host.length() + 8);
        if (this.host.indexOf(58) < 0) goto L5;
        r0.append('[').append(this.host).append(']');
    L7:
        if (hasPort() == false) goto L10;
        r0.append(':').append(this.port);
    L10:
        return r0.toString();
    L5:
        r0.append(this.host);
        goto L7
    }

    public HostAndPort withDefaultPort(int r4) {
        Preconditions.checkArgument(isValidPort(r4));
        if (hasPort() == false) goto L6;
        return this;
    L6:
        return new HostAndPort(this.host, r4, this.hasBracketlessColons);
    }
}
