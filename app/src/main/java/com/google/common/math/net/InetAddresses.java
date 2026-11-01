package com.google.common.net;

import com.google.common.base.CharMatcher;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import com.google.common.p003io.ByteStreams;
import com.google.common.primitives.Ints;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.CheckForNull;
import kotlin.UShort;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class InetAddresses {
    private static final Inet4Address ANY4 = null;
    private static final char IPV4_DELIMITER = '.';
    private static final CharMatcher IPV4_DELIMITER_MATCHER = null;
    private static final int IPV4_PART_COUNT = 4;
    private static final char IPV6_DELIMITER = ':';
    private static final CharMatcher IPV6_DELIMITER_MATCHER = null;
    private static final int IPV6_PART_COUNT = 8;
    private static final Inet4Address LOOPBACK4 = null;

    /* JADX INFO: renamed from: com.google.common.net.InetAddresses$1 */
    static /* synthetic */ class C08671 {
    }

    private static final class Scope {
        private String scope;

        private Scope() {
        }

        /* synthetic */ Scope(C08671 r1) {
            this();
        }

        static /* synthetic */ String access$100(Scope r1) {
            return r1.scope;
        }

        static /* synthetic */ String access$102(Scope r0, String r1) {
            r0.scope = r1;
            return r1;
        }
    }

    public static final class TeredoInfo {
        private final Inet4Address client;
        private final int flags;
        private final int port;
        private final Inet4Address server;

        public TeredoInfo(@CheckForNull Inet4Address r6, @CheckForNull Inet4Address r7, int r8, int r9) {
            boolean r0 = true;
            if (r8 < 0) goto L6;
            if (r8 > 65535) goto L6;
            boolean r3 = true;
        L7:
            Preconditions.checkArgument(r3, "port '%s' is out of range (0 <= port <= 0xffff)", r8);
            if (r9 < 0) goto L11;
            if (r9 > 65535) goto L11;
        L12:
            Preconditions.checkArgument(r0, "flags '%s' is out of range (0 <= flags <= 0xffff)", r9);
            this.server = (Inet4Address) MoreObjects.firstNonNull(r6, InetAddresses.access$200());
            this.client = (Inet4Address) MoreObjects.firstNonNull(r7, InetAddresses.access$200());
            this.port = r8;
            this.flags = r9;
            return;
        L11:
            r0 = false;
        L6:
            r3 = false;
            goto L7
        }

        public Inet4Address getClient() {
            return this.client;
        }

        public int getFlags() {
            return this.flags;
        }

        public int getPort() {
            return this.port;
        }

        public Inet4Address getServer() {
            return this.server;
        }
    }

    static {
        IPV4_DELIMITER_MATCHER = CharMatcher.m181is(IPV4_DELIMITER);
        IPV6_DELIMITER_MATCHER = CharMatcher.m181is(IPV6_DELIMITER);
        LOOPBACK4 = (Inet4Address) forString("127.0.0.1");
        ANY4 = (Inet4Address) forString("0.0.0.0");
    }

    private InetAddresses() {
    }

    static /* synthetic */ Inet4Address access$200() {
        return ANY4;
    }

    private static InetAddress bytesToInetAddress(byte[] r7, String r8) {
        InetAddress r0 = InetAddress.getByAddress(r7);     // Catch: UnknownHostException -> L20
        if (r8 != null) goto L5;
        return r0;
    L5:
        Preconditions.checkArgument(r0 instanceof Inet6Address, "Unexpected state, scope should only appear for ipv6");     // Catch: UnknownHostException -> L20
        Inet6Address r1 = (Inet6Address) r0;     // Catch: UnknownHostException -> L20
        int r2 = tryParseDecimal(r8, 0, r8.length());     // Catch: UnknownHostException -> L20
        if (r2 != (-1)) goto L8;
        NetworkInterface r4 = NetworkInterface.getByName(r8);     // Catch: Throwable -> L15 SocketException -> L17
        if (r4 == null) goto L14;
        return Inet6Address.getByAddress(r1.getHostAddress(), r1.getAddress(), r4);
    L14:
        throw formatIllegalArgumentException("No such interface: '%s'", new Object[]{r8});     // Catch: Throwable -> L15 SocketException -> L17
    L15:
        e = move-exception;
        throw new IllegalArgumentException("No such interface: " + r8, e);     // Catch: UnknownHostException -> L20
    L8:
        return Inet6Address.getByAddress(r1.getHostAddress(), r1.getAddress(), r2);
    L20:
        e = move-exception;
        throw new AssertionError(e);
    }

    public static int coerceToInteger(InetAddress r1) {
        return ByteStreams.newDataInput(getCoercedIPv4Address(r1).getAddress()).readInt();
    }

    private static void compressLongestRunOfZeroes(int[] r5) {
        int r0 = -1;
        int r1 = -1;
        int r2 = -1;
        int r3 = 0;
    L4:
        if (r3 >= (r5.length + 1)) goto L18;
        if (r3 < r5.length) goto L8;
    L11:
        if (r2 < 0) goto L16;
        int r4 = r3 - r2;
        if (r4 <= r1) goto L15;
        r0 = r2;
        r1 = r4;
    L15:
        r2 = -1;
    L16:
        r3 = r3 + 1;
        goto L4
    L8:
        if (r5[r3] != 0) goto L11;
        if (r2 >= 0) goto L16;
        r2 = r3;
        goto L16
    L18:
        if (r1 < 2) goto L26;
        Arrays.fill(r5, r0, r0 + r1, -1);
        return;
    }

    @CheckForNull
    private static String convertDottedQuadToHex(String r8) {
        int r0 = r8.lastIndexOf(58);
        String r1 = r8.substring(0, r0 + 1);
        byte[] r4 = textToNumericFormatV4(r8.substring(r0 + 1));
        if (r4 != null) goto L7;
        return null;
    L7:
        return r1 + Integer.toHexString(((r4[0] & 255) << 8) | (r4[1] & 255)) + ":" + Integer.toHexString(((r4[2] & 255) << 8) | (r4[3] & 255));
    }

    public static InetAddress decrement(InetAddress r5) {
        byte[] r0 = r5.getAddress();
        int r1 = r0.length - 1;
    L3:
        if (r1 < 0) goto L7;
        if (r0[r1] != 0) goto L7;
        r0[r1] = -1;
        r1 = r1 - 1;
    L7:
        if (r1 < 0) goto L9;
        boolean r3 = true;
    L10:
        Preconditions.checkArgument(r3, "Decrementing %s would wrap.", r5);
        r0[r1] = (byte) (r0[r1] - 1);
        return bytesToInetAddress(r0, null);
    L9:
        r3 = false;
        goto L10
    }

    public static InetAddress forString(String r4) {
        Scope r0 = new Scope(null);
        byte[] r1 = ipStringToBytes(r4, r0);
        if (r1 == null) goto L7;
        return bytesToInetAddress(r1, Scope.access$100(r0));
    L7:
        throw formatIllegalArgumentException("'%s' is not an IP string literal.", new Object[]{r4});
    }

    public static InetAddress forUriString(String r3) {
        InetAddress r1 = forUriStringOrNull(r3, true);
        if (r1 == null) goto L6;
        return r1;
    L6:
        throw formatIllegalArgumentException("Not a valid URI IP literal: '%s'", new Object[]{r3});
    }

    /* JADX WARN: Multi-variable type inference failed */
    @CheckForNull
    private static InetAddress forUriStringOrNull(String r6, boolean r7) {
        Preconditions.checkNotNull(r6);
        if (r6.startsWith("[") == true) goto L5;
    L7:
        String r0 = r6;
        int r1 = 4;
    L8:
        String r2 = null;
        Object[] r3 = 0;
        if (r7 == false) goto L11;
        Scope r4 = new Scope(r3 == true ? 1 : 0);
    L12:
        byte[] r5 = ipStringToBytes(r0, r4);
        if (r5 != null) goto L15;
    L21:
        return null;
    L15:
        if (r5.length != r1) goto L21;
        if (r4 == null) goto L20;
        r2 = Scope.access$100(r4);
    L20:
        return bytesToInetAddress(r5, r2);
    L11:
        r4 = null;
        goto L12
    L5:
        if (r6.endsWith("]") == false) goto L7;
        r0 = r6.substring(1, r6.length() - 1);
        r1 = 16;
        goto L8
    }

    private static IllegalArgumentException formatIllegalArgumentException(String r2, Object... r3) {
        return new IllegalArgumentException(String.format(Locale.ROOT, r2, r3));
    }

    private static InetAddress fromBigInteger(BigInteger r11, boolean r12) {
        if (r11.signum() < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "BigInteger must be greater than or equal to 0");
        if (r12 == false) goto L9;
        int r1 = 16;
    L10:
        byte[] r3 = r11.toByteArray();
        byte[] r4 = new byte[r1];
        int r5 = Math.max(0, r3.length - r1);
        int r6 = r3.length - r5;
        int r7 = r1 - r6;
        int r8 = 0;
    L11:
        if (r8 >= r5) goto L17;
        if (r3[r8] != 0) goto L16;
        r8 = r8 + 1;
        goto L11
    L16:
        throw formatIllegalArgumentException("BigInteger cannot be converted to InetAddress because it has more than %d bytes: %s", new Object[]{Integer.valueOf(r1), r11});
    L17:
        System.arraycopy(r3, r5, r4, r7, r6);
        return InetAddress.getByAddress(r4);
    L20:
        e = move-exception;
        throw new AssertionError(e);
    L9:
        r1 = 4;
        goto L10
    L5:
        r0 = false;
        goto L6
    }

    public static Inet4Address fromIPv4BigInteger(BigInteger r1) {
        return (Inet4Address) fromBigInteger(r1, false);
    }

    public static Inet6Address fromIPv6BigInteger(BigInteger r1) {
        return (Inet6Address) fromBigInteger(r1, true);
    }

    public static Inet4Address fromInteger(int r1) {
        return getInet4Address(Ints.toByteArray(r1));
    }

    public static InetAddress fromLittleEndianByteArray(byte[] r3) throws UnknownHostException {
        byte[] r0 = new byte[r3.length];
        int r1 = 0;
    L4:
        if (r1 >= r3.length) goto L7;
        r0[r1] = r3[(r3.length - r1) - 1];
        r1 = r1 + 1;
        goto L4
    L7:
        return InetAddress.getByAddress(r0);
    }

    public static Inet4Address get6to4IPv4Address(Inet6Address r3) {
        Preconditions.checkArgument(is6to4Address(r3), "Address '%s' is not a 6to4 address.", toAddrString(r3));
        return getInet4Address(Arrays.copyOfRange(r3.getAddress(), 2, 6));
    }

    public static Inet4Address getCoercedIPv4Address(InetAddress r8) {
        if ((r8 instanceof Inet4Address) == true) goto L5;
        byte[] r0 = r8.getAddress();
        boolean r1 = true;
        int r2 = 0;
    L8:
        if (r2 >= 15) goto L13;
        if (r0[r2] != 0) goto L11;
        r2 = r2 + 1;
        goto L8
    L11:
        r1 = false;
    L13:
        if (r1 == true) goto L15;
    L18:
        if (r1 == true) goto L20;
    L23:
        Inet6Address r3 = (Inet6Address) r8;
        if (hasEmbeddedIPv4ClientAddress(r3) == false) goto L26;
        long r4 = getEmbeddedIPv4ClientAddress(r3).hashCode();
    L27:
        int r5 = Hashing.murmur3_32_fixed().hashLong(r4).asInt() | (-536870912);
        if (r5 != (-1)) goto L31;
        r5 = -2;
    L31:
        return getInet4Address(Ints.toByteArray(r5));
    L26:
        r4 = ByteBuffer.wrap(r3.getAddress(), 0, 8).getLong();
        goto L27
    L20:
        if (r0[15] != 0) goto L23;
        return ANY4;
    L15:
        if (r0[15] != 1) goto L18;
        return LOOPBACK4;
    L5:
        return (Inet4Address) r8;
    }

    public static Inet4Address getCompatIPv4Address(Inet6Address r3) {
        Preconditions.checkArgument(isCompatIPv4Address(r3), "Address '%s' is not IPv4-compatible.", toAddrString(r3));
        return getInet4Address(Arrays.copyOfRange(r3.getAddress(), 12, 16));
    }

    public static Inet4Address getEmbeddedIPv4ClientAddress(Inet6Address r3) {
        if (isCompatIPv4Address(r3) == false) goto L7;
        return getCompatIPv4Address(r3);
    L7:
        if (is6to4Address(r3) == false) goto L11;
        return get6to4IPv4Address(r3);
    L11:
        if (isTeredoAddress(r3) == false) goto L15;
        return getTeredoInfo(r3).getClient();
    L15:
        throw formatIllegalArgumentException("'%s' has no embedded IPv4 address.", new Object[]{toAddrString(r3)});
    }

    private static Inet4Address getInet4Address(byte[] r3) {
        if (r3.length != 4) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Byte array has invalid length for an IPv4 address: %s != 4.", r3.length);
        return (Inet4Address) bytesToInetAddress(r3, null);
    L5:
        r0 = false;
        goto L6
    }

    public static Inet4Address getIsatapIPv4Address(Inet6Address r3) {
        Preconditions.checkArgument(isIsatapAddress(r3), "Address '%s' is not an ISATAP address.", toAddrString(r3));
        return getInet4Address(Arrays.copyOfRange(r3.getAddress(), 12, 16));
    }

    public static TeredoInfo getTeredoInfo(Inet6Address r7) {
        Preconditions.checkArgument(isTeredoAddress(r7), "Address '%s' is not a Teredo address.", toAddrString(r7));
        byte[] r0 = r7.getAddress();
        Inet4Address r1 = getInet4Address(Arrays.copyOfRange(r0, 4, 8));
        int r2 = ByteStreams.newDataInput(r0, 8).readShort() & UShort.MAX_VALUE;
        int r3 = 65535 & (~ByteStreams.newDataInput(r0, 10).readShort());
        byte[] r4 = Arrays.copyOfRange(r0, 12, 16);
        int r5 = 0;
    L4:
        if (r5 >= r4.length) goto L7;
        r4[r5] = (byte) (~r4[r5]);
        r5 = r5 + 1;
        goto L4
    L7:
        return new TeredoInfo(r1, getInet4Address(r4), r3, r2);
    }

    public static boolean hasEmbeddedIPv4ClientAddress(Inet6Address r1) {
        if (isCompatIPv4Address(r1) == false) goto L5;
    L10:
        return true;
    L5:
        if (is6to4Address(r1) == true) goto L10;
        if (isTeredoAddress(r1) == true) goto L10;
        return false;
    }

    private static String hextetsToIPv6String(int[] r5) {
        StringBuilder r0 = new StringBuilder(39);
        boolean r1 = false;
        int r2 = 0;
    L4:
        if (r2 >= r5.length) goto L18;
        if (r5[r2] < 0) goto L8;
        boolean r3 = true;
    L9:
        if (r3 == false) goto L13;
        if (r1 == false) goto L12;
        r0.append(IPV6_DELIMITER);
    L12:
        r0.append(Integer.toHexString(r5[r2]));
    L16:
        r1 = r3;
        r2 = r2 + 1;
        goto L4
    L13:
        if (r2 == 0) goto L15;
        if (r1 == false) goto L16;
    L15:
        r0.append("::");
        goto L16
    L8:
        r3 = false;
        goto L9
    L18:
        return r0.toString();
    }

    public static InetAddress increment(InetAddress r6) {
        byte[] r0 = r6.getAddress();
        int r1 = r0.length - 1;
    L3:
        boolean r3 = false;
        if (r1 < 0) goto L8;
        if (r0[r1] != (-1)) goto L8;
        r0[r1] = 0;
        r1 = r1 - 1;
    L8:
        if (r1 < 0) goto L10;
        r3 = true;
    L10:
        Preconditions.checkArgument(r3, "Incrementing %s would wrap.", r6);
        r0[r1] = (byte) (r0[r1] + 1);
        return bytesToInetAddress(r0, null);
    }

    @CheckForNull
    private static byte[] ipStringToBytes(String r9, Scope r10) {
        String r0 = r9;
        boolean r1 = false;
        boolean r2 = false;
        int r3 = -1;
        int r4 = 0;
    L4:
        if (r4 >= r0.length()) goto L20;
        char r5 = r0.charAt(r4);
        if (r5 != '.') goto L9;
        r2 = true;
    L19:
        r4 = r4 + 1;
        goto L4
    L9:
        if (r5 != ':') goto L14;
        if (r2 == true) goto L11;
        r1 = true;
        goto L19
    L11:
        return null;
    L14:
        if (r5 == '%') goto L15;
        if (Character.digit(r5, 16) != (-1)) goto L19;
        return null;
    L15:
        r3 = r4;
    L20:
        if (r1 == false) goto L31;
        if (r2 == false) goto L25;
        r0 = convertDottedQuadToHex(r0);
        if (r0 != null) goto L25;
        return null;
    L25:
        if (r3 == (-1)) goto L30;
        if (r10 == null) goto L28;
        Scope.access$102(r10, r0.substring(r3 + 1));
    L28:
        r0 = r0.substring(0, r3);
    L30:
        return textToNumericFormatV6(r0);
    L31:
        if (r2 == false) goto L36;
        if (r3 == (-1)) goto L35;
        return null;
    L35:
        return textToNumericFormatV4(r0);
    L36:
        return null;
    }

    public static boolean is6to4Address(Inet6Address r5) {
        byte[] r0 = r5.getAddress();
        if (r0[0] == 32) goto L5;
        return false;
    L5:
        if (r0[1] == 2) goto L7;
        return false;
    L7:
        return true;
    }

    public static boolean isCompatIPv4Address(Inet6Address r5) {
        if (r5.isIPv4CompatibleAddress() == true) goto L5;
        return false;
    L5:
        byte[] r0 = r5.getAddress();
        if (r0[12] == 0) goto L8;
    L16:
        return true;
    L8:
        if (r0[13] != 0) goto L16;
        if (r0[14] != 0) goto L16;
        if (r0[15] != 0) goto L14;
    L15:
        return false;
    L14:
        if (r0[15] != 1) goto L16;
        goto L15
    }

    public static boolean isInetAddress(String r1) {
        if (ipStringToBytes(r1, null) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public static boolean isIsatapAddress(Inet6Address r4) {
        if (isTeredoAddress(r4) == false) goto L5;
        return false;
    L5:
        byte[] r0 = r4.getAddress();
        if ((r0[8] | 3) == 3) goto L9;
        return false;
    L9:
        if (r0[9] == 0) goto L11;
        return false;
    L11:
        if (r0[10] == 94) goto L13;
        return false;
    L13:
        if (r0[11] != (-2)) goto L18;
        return true;
    L18:
        return false;
    }

    public static boolean isMappedIPv4Address(String r5) {
        byte[] r0 = ipStringToBytes(r5, null);
        if (r0 != null) goto L5;
    L22:
        return false;
    L5:
        if (r0.length != 16) goto L22;
        int r2 = 0;
    L8:
        if (r2 >= 10) goto L13;
        if (r0[r2] != 0) goto L11;
        r2 = r2 + 1;
        goto L8
    L11:
        return false;
    L13:
        int r3 = 10;
    L15:
        if (r3 >= 12) goto L20;
        if (r0[r3] != (-1)) goto L18;
        r3 = r3 + 1;
        goto L15
    L18:
        return false;
    L20:
        return true;
    }

    public static boolean isMaximum(InetAddress r6) {
        byte[] r0 = r6.getAddress();
        int r1 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L8;
        if (r0[r3] != (-1)) goto L6;
        r3 = r3 + 1;
        goto L3
    L6:
        return false;
    L8:
        return true;
    }

    public static boolean isTeredoAddress(Inet6Address r4) {
        byte[] r0 = r4.getAddress();
        if (r0[0] == 32) goto L5;
        return false;
    L5:
        if (r0[1] == 1) goto L7;
        return false;
    L7:
        if (r0[2] == 0) goto L9;
        return false;
    L9:
        if (r0[3] == 0) goto L11;
        return false;
    L11:
        return true;
    }

    public static boolean isUriInetAddress(String r2) {
        if (forUriStringOrNull(r2, false) == null) goto L6;
        return true;
    L6:
        return false;
    }

    private static short parseHextet(String r5, int r6, int r7) {
        int r0 = r7 - r6;
        if (r0 <= 0) goto L12;
        if (r0 > 4) goto L12;
        int r1 = 0;
        int r2 = r6;
    L7:
        if (r2 >= r7) goto L10;
        r1 = (r1 << 4) | Character.digit(r5.charAt(r2), 16);
        r2 = r2 + 1;
        goto L7
    L10:
        return (short) r1;
    L12:
        throw new NumberFormatException();
    }

    private static byte parseOctet(String r5, int r6, int r7) {
        int r0 = r7 - r6;
        if (r0 <= 0) goto L27;
        if (r0 > 3) goto L27;
        if (r0 > 1) goto L9;
    L13:
        int r1 = 0;
        int r2 = r6;
    L14:
        if (r2 >= r7) goto L21;
        int r3 = r1 * 10;
        int r4 = Character.digit(r5.charAt(r2), 10);
        if (r4 < 0) goto L19;
        r1 = r3 + r4;
        r2 = r2 + 1;
        goto L14
    L19:
        throw new NumberFormatException();
    L21:
        if (r1 > 255) goto L25;
        return (byte) r1;
    L25:
        throw new NumberFormatException();
    L9:
        if (r5.charAt(r6) != '0') goto L13;
        throw new NumberFormatException();
    L27:
        throw new NumberFormatException();
    }

    private static String scopeWithDelimiter(Inet6Address r4) {
        NetworkInterface r0 = r4.getScopedInterface();
        if (r0 != null) goto L5;
        int r2 = r4.getScopeId();
        if (r2 != 0) goto L9;
        return "";
    L9:
        return "%" + r2;
    L5:
        return "%" + r0.getName();
    }

    @CheckForNull
    private static byte[] textToNumericFormatV4(String r7) {
        if ((IPV4_DELIMITER_MATCHER.countIn(r7) + 1) == 4) goto L5;
        return null;
    L5:
        byte[] r0 = new byte[4];
        int r3 = 0;
        int r4 = 0;
    L6:
        if (r4 >= 4) goto L14;
        int r5 = r7.indexOf(46, r3);
        if (r5 != (-1)) goto L15;
        r5 = r7.length();
    L15:
        r0[r4] = parseOctet(r7, r3, r5);     // Catch: NumberFormatException -> L12
        r3 = r5 + 1;
        r4 = r4 + 1;
    L13:
        return null;
    L14:
        return r0;
    }

    @CheckForNull
    private static byte[] textToNumericFormatV6(String r10) {
        int r0 = IPV6_DELIMITER_MATCHER.countIn(r10);
        if (r0 >= 2) goto L5;
    L61:
        return null;
    L5:
        if (r0 > 8) goto L61;
        int r4 = 8 - (r0 + 1);
        boolean r5 = false;
        int r6 = 0;
    L9:
        if (r6 >= (r10.length() - 1)) goto L24;
        if (r10.charAt(r6) != ':') goto L22;
        if (r10.charAt(r6 + 1) != ':') goto L22;
        if (r5 == true) goto L15;
        r5 = true;
        r4 = r4 + 1;
        if (r6 != 0) goto L20;
        r4 = r4 + 1;
    L20:
        if (r6 != (r10.length() - 2)) goto L22;
        r4 = r4 + 1;
        goto L22
    L15:
        return null;
    L22:
        r6 = r6 + 1;
        goto L9
    L24:
        if (r10.charAt(0) != ':') goto L29;
        if (r10.charAt(1) == ':') goto L29;
        return null;
    L29:
        if (r10.charAt(r10.length() - 1) == ':') goto L31;
    L33:
        if (r5 == false) goto L36;
        if (r4 > 0) goto L36;
        return null;
    L36:
        if (r5 == false) goto L38;
    L40:
        ByteBuffer r2 = ByteBuffer.allocate(16);
        int r3 = 0;
        if (r10.charAt(0) != ':') goto L44;
        r3 = 1;
    L44:
        if (r3 >= r10.length()) goto L58;
        int r7 = r10.indexOf(58, r3);     // Catch: NumberFormatException -> L59
        if (r7 != (-1)) goto L50;
        r7 = r10.length();     // Catch: NumberFormatException -> L59
    L50:
        if (r10.charAt(r3) != ':') goto L55;
        int r8 = 0;
    L52:
        if (r8 >= r4) goto L56;
        r2.putShort(0);     // Catch: NumberFormatException -> L59
        r8 = r8 + 1;     // Catch: NumberFormatException -> L59
    L56:
        r3 = r7 + 1;
        goto L44
    L55:
        r2.putShort(parseHextet(r10, r3, r7));     // Catch: NumberFormatException -> L59
        goto L56
    L58:
        return r2.array();
    L60:
        return null;
    L38:
        if ((r0 + 1) == 8) goto L40;
        return null;
    L31:
        if (r10.charAt(r10.length() - 2) == ':') goto L33;
        return null;
    }

    public static String toAddrString(InetAddress r6) {
        Preconditions.checkNotNull(r6);
        if ((r6 instanceof Inet4Address) == true) goto L5;
        byte[] r0 = r6.getAddress();
        int[] r1 = new int[8];
        int r2 = 0;
    L8:
        if (r2 >= r1.length) goto L10;
        r1[r2] = Ints.fromBytes((byte) 0, (byte) 0, r0[r2 * 2], r0[(r2 * 2) + 1]);
        r2 = r2 + 1;
        goto L8
    L10:
        compressLongestRunOfZeroes(r1);
        return hextetsToIPv6String(r1) + scopeWithDelimiter((Inet6Address) r6);
    L5:
        return (String) Objects.requireNonNull(r6.getHostAddress());
    }

    public static BigInteger toBigInteger(InetAddress r3) {
        return new BigInteger(1, r3.getAddress());
    }

    public static String toUriString(InetAddress r2) {
        if ((r2 instanceof Inet6Address) == false) goto L7;
        return "[" + toAddrString(r2) + "]";
    L7:
        return toAddrString(r2);
    }

    private static int tryParseDecimal(String r6, int r7, int r8) {
        int r0 = 0;
        int r2 = r7;
    L3:
        if (r2 >= r8) goto L11;
        if (r0 > 214748364) goto L6;
        int r1 = r0 * 10;
        int r3 = Character.digit(r6.charAt(r2), 10);
        if (r3 < 0) goto L9;
        r0 = r1 + r3;
        r2 = r2 + 1;
        goto L3
    L9:
        return -1;
    L6:
        return -1;
    L11:
        return r0;
    }
}
