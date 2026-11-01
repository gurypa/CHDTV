package es.antonio.m3u;

import androidx.core.os.EnvironmentCompat;
import androidx.media3.extractor.p001ts.TsExtractor;
import com.google.common.base.Ascii;
import com.google.common.primitives.SignedBytes;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes7.dex */
public final class HlsTsReport225 {

    /* JADX INFO: renamed from: es.antonio.m3u.HlsTsReport225$1 */
    static /* synthetic */ class C09981 {
    }

    private static final class Pid {
        int adaptErrors;
        int ccGaps;
        int ccRepeat;
        int flags;
        int lastCc;
        int optional;
        int packets;
        int pcr;
        int pes;
        int pesLength;
        int pusi;
        int scrambled;
        int streamId;
        int tei;

        private Pid() {
            this.lastCc = -1;
            this.streamId = -1;
            this.pesLength = -1;
            this.flags = -1;
            this.optional = -1;
        }

        /* synthetic */ Pid(C09981 r1) {
            this();
        }
    }

    private static final class Section {
        int count;
        byte[] data;
        int expected;

        private Section() {
            this.data = new byte[4096];
            this.expected = -1;
        }

        /* synthetic */ Section(C09981 r1) {
            this();
        }

        void reset() {
            this.count = 0;
            this.expected = -1;
        }
    }

    private HlsTsReport225() {
    }

    public static String cause(Throwable r11) {
        Set r0 = Collections.newSetFromMap(new IdentityHashMap());
    L3:
        if (r11 == null) goto L28;
        if (r0.size() >= 16) goto L40;
        if (r0.add(r11) == false) goto L41;
        String r1 = r11.getMessage();
        if (r1 == null) goto L27;
        if (r1.startsWith("Invalid encrypted MPEG-TS segment; ") == false) goto L27;
        StringBuilder r2 = new StringBuilder();
        String[] r3 = {"native", "kind", "site", "check", "phase", "packet", "pid"};
        String[] r4 = {"TS_PARSE|NAL_START_CODE|IO|ID3_PARSE|PANIC", "InvalidInput|Unsupported|Other|Bounds|DivisionByZero|UnexpectedEof|InvalidData|WriteZero|Interrupted|OutOfMemory", "(?:ts/|pes/)?[a-z_]{1,32}\\.rs:[0-9]{1,6}|unknown", "[0-9a-f]{16}", "TS_READ|TS_PACKET|PES_PROCESS|PES_FINALIZE", "[0-9]{1,10}", "[0-9]{1,5}|unknown"};
        int r5 = 0;
    L15:
        if (r5 >= 7) goto L24;
        Matcher r6 = Pattern.compile("(?:^|[ ;])" + r3[r5] + "=(" + r4[r5] + ")(?:$|[ ;])").matcher(r1);
        if (r6.find() == false) goto L22;
        if (r2.length() <= 0) goto L21;
        r2.append(' ');
    L21:
        r2.append(r3[r5]).append('=').append(r6.group(1));
    L22:
        r5 = r5 + 1;
        goto L15
    L24:
        if (r2.indexOf("native=") != 0) goto L27;
        return r2.toString();
    L27:
        r11 = r11.getCause();
        goto L3
    L41:
        return "native=unavailable";
    L40:
        return "native=unavailable";
    L28:
        return "native=unavailable";
    }

    private static String failurePoint(byte[] r8, String r9) {
        if (r9 != null) goto L5;
        return "";
    L5:
        Matcher r1 = Pattern.compile("(?:^| )packet=([0-9]{1,10})(?: |$)").matcher(r9);
        if (r1.find() == true) goto L8;
        return "";
    L8:
        long r3 = Long.parseLong(r1.group(1));
        if (r3 <= 2147483647L) goto L11;
        return "";
    L11:
        int r2 = (int) r3;
        int r5 = r8.length / TsExtractor.TS_PACKET_SIZE;
        if (r9.contains("phase=PES_FINALIZE") == false) goto L15;
        r2 = r5 - 1;
    L17:
        if (r2 < 0) goto L57;
        if (r2 >= r5) goto L57;
        Matcher r10 = Pattern.compile("(?:^| )pid=([0-9]{1,5})(?: |$)").matcher(r9);
        if (r10.find() == false) goto L23;
        int r11 = Integer.parseInt(r10.group(1));
    L24:
        int r6 = r5 - 1;
        int r0 = Math.min((int) Math.min(r3, r6), r6);
        String r4 = "failure_packet=" + r0;
        if (r0 < 0) goto L28;
        int r7 = r0 * TsExtractor.TS_PACKET_SIZE;
        r4 = r4 + " header_pid=" + (u16(r8, r7 + 1) & 8191) + " header_control=" + ((r8[r7 + 3] >>> 4) & 3);
    L28:
        if (r2 < 0) goto L69;
        int r12 = r2 * TsExtractor.TS_PACKET_SIZE;
        if ((r8[r12] & 255) != 71) goto L55;
        int r13 = r12 + 1;
        int r14 = u16(r8, r13) & 8191;
        if (r11 < 0) goto L37;
        if (r14 != r11) goto L55;
    L37:
        if ((r8[r13] & SignedBytes.MAX_POWER_OF_TWO) == 0) goto L55;
        int r15 = (r8[r12 + 3] >>> 4) & 3;
        if ((r15 & 1) == 0) goto L55;
        int r16 = r12 + 4;
        if ((r15 & 2) == 0) goto L46;
        r16 = r16 + ((r8[r16] & 255) + 1);
    L46:
        if ((r16 + 9) > (r12 + TsExtractor.TS_PACKET_SIZE)) goto L55;
        if (r8[r16] != 0) goto L55;
        if (r8[r16 + 1] != 0) goto L55;
        if (r8[r16 + 2] != 1) goto L55;
        return r4 + " pes_start_packet=" + r2 + " pes_pid=" + r14 + " pes_stream_id=" + (r8[r16 + 3] & 255) + " pes_declared_length=" + u16(r8, r16 + 4) + " pes_flags=" + (r8[r16 + 7] & 255) + " pes_optional_length=" + (r8[r16 + 8] & 255);
    L55:
        r2 = r2 - 1;
        goto L28
    L69:
        return r4;
    L23:
        r11 = -1;
    L57:
        return "";
    L15:
        if (r9.contains("phase=PES_PROCESS") == false) goto L17;
        r2 = r2 - 1;
        goto L17
    }

    private static int feed(Section r8, byte[] r9, int r10, int r11, int r12, Set<Integer> r13, Set<String> r14) {
        int r0 = 0;
    L3:
        if (r10 >= r11) goto L41;
        if (r8.count != 0) goto L10;
        if ((r9[r10] & 255) != 255) goto L10;
        return r0;
    L10:
        if (r8.count == 4096) goto L11;
        byte[] r1 = r8.data;
        int r4 = r8.count;
        r8.count = r4 + 1;
        r10 = r10 + 1;
        r1[r4] = r9[r10];
        if (r8.count != 3) goto L20;
        r8.expected = (((r8.data[1] & Ascii.f88SI) << 8) | (255 & r8.data[2])) + 3;
        if (r8.expected < 8) goto L18;
        if (r8.expected <= 4096) goto L20;
    L18:
        r8.reset();
        return r0 + 1;
    L20:
        if (r8.expected <= 0) goto L3;
        if (r8.count != r8.expected) goto L3;
        if (parseSection(r8.data, r8.count, r12, r13, r14) == true) goto L26;
        r0 = r0 + 1;
    L26:
        r8.reset();
        goto L3
    L11:
        r8.reset();
        return r0 + 1;
    L41:
        return r0;
    }

    public static String fingerprint(byte[] r7) {
        byte[] r8 = MessageDigest.getInstance("SHA-256").digest(r7);     // Catch: Exception -> L8
        StringBuilder r0 = new StringBuilder();     // Catch: Exception -> L8
        int r2 = 0;
    L4:
        if (r2 >= 8) goto L6;
        r0.append(String.format(Locale.ROOT, "%02x", new Object[]{Integer.valueOf(r8[r2] & 255)}));     // Catch: Exception -> L8
        r2 = r2 + 1;     // Catch: Exception -> L8
        goto L4
    L6:
        return r0.toString();
    L9:
        return "unavailable";
    }

    public static String format(byte[] r8) {
        if (r8 != null) goto L4;
        return "empty";
    L4:
        if (r8.length != 0) goto L7;
        return "empty";
    L7:
        if ((r8[0] & 255) != 71) goto L11;
        return "MPEG_TS";
    L11:
        if (r8.length < 8) goto L17;
        String r1 = new String(r8, 4, 4, StandardCharsets.US_ASCII);
        if (Arrays.asList(new String[]{"ftyp", "styp", "moof", "moov", "sidx"}).contains(r1) == false) goto L17;
        return "fMP4";
    L17:
        if (r8.length < 3) goto L27;
        if (r8[0] != 73) goto L27;
        if (r8[1] != 68) goto L27;
        if (r8[2] != 51) goto L27;
        return "ID3_PACKED_AUDIO";
    L27:
        if (r8.length >= 2) goto L29;
        return EnvironmentCompat.MEDIA_UNKNOWN;
    L29:
        if ((r8[0] & 255) == 255) goto L31;
        return EnvironmentCompat.MEDIA_UNKNOWN;
    L31:
        if ((r8[1] & 246) != 240) goto L39;
        return "ADTS";
    L39:
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }

    private static boolean parseSection(byte[] r16, int r17, int r18, Set<Integer> r19, Set<String> r20) {
        int r3 = 8;
        if (r18 != 0) goto L19;
        if ((r16[0] & 255) != 0) goto L19;
        if (r17 >= 12) goto L8;
        return false;
    L8:
        int r2 = r3 + 4;
        if (r2 > (r17 - 4)) goto L17;
        if (u16(r16, r3) == 0) goto L16;
        if (r19.size() >= 32) goto L16;
        r19.add(Integer.valueOf(u16(r16, r3 + 2) & 8191));
    L16:
        r3 = r2;
        goto L8
    L17:
        return true;
    L19:
        if ((r16[0] & 255) == 2) goto L21;
    L55:
        return false;
    L21:
        if (r17 < 16) goto L55;
        int r7 = (u16(r16, 10) & 4095) + 12;
        int r4 = u16(r16, 8) & 8191;
        int r1 = r17 - 4;
        if (r7 <= r1) goto L26;
        return false;
    L26:
        int r5 = r7 + 5;
        if (r5 > r1) goto L51;
        int r8 = r16[r7] & 255;
        int r9 = u16(r16, r7 + 1) & 8191;
        int r10 = u16(r16, r7 + 3) & 4095;
        int r11 = r5 + r10;
        if (r11 > r1) goto L30;
        StringBuilder r12 = new StringBuilder();
    L32:
        if (r5 >= r11) goto L47;
        int r13 = r5 + 2;
        if (r13 > r11) goto L35;
        int r14 = r16[r5 + 1] & 255;
        if ((r13 + r14) > r11) goto L38;
        if (r12.length() >= 150) goto L45;
        if (r12.length() <= 0) goto L44;
        r12.append(',');
    L44:
        r12.append(r16[r5] & 255);
    L45:
        r5 = r5 + (r14 + 2);
        goto L32
    L38:
        return false;
    L35:
        return false;
    L47:
        if (r20.size() >= 64) goto L50;
        r20.add("pmt_pid=" + r18 + " pcr_pid=" + r4 + " track_pid=" + r9 + " stream_type=" + r8 + " descriptor_tags=" + r12);
    L50:
        r7 = r7 + (r10 + 5);
        goto L26
    L30:
        return false;
    L51:
        if (r7 != r1) goto L54;
        return true;
    L54:
        return false;
    }

    public static List<String> rows(byte[] r23, String r24) {
        byte[] r7 = r23;
        ArrayList r8 = new ArrayList();
        String r0 = format(r23);
        if (r7 != null) goto L5;
        int r10 = 0;
    L6:
        r8.add("container=" + r0 + " bytes=" + r10 + " remainder188=" + (r10 % TsExtractor.TS_PACKET_SIZE));
        if (r0.equals("MPEG_TS") == true) goto L9;
        return r8;
    L9:
        TreeMap r11 = new TreeMap();
        HashMap r12 = new HashMap();
        HashSet r13 = new HashSet();
        TreeSet r14 = new TreeSet();
        r12.put(0, new Section(null));
        int r1 = 0;
        int r2 = 0;
        int r3 = 0;
        int r4 = 0;
        int r5 = -1;
    L10:
        int r6 = r1 + TsExtractor.TS_PACKET_SIZE;
        if (r6 > r10) goto L113;
        if ((r7[r1] & 255) == 71) goto L17;
        if (r5 >= 0) goto L16;
        r5 = r1 / TsExtractor.TS_PACKET_SIZE;
    L16:
        r4 = r4 + 1;
        r6 = r6;
        r8 = r8;
        r11 = r11;
    L112:
        r7 = r23;
        r1 = r6;
        r11 = r11;
        r8 = r8;
        goto L10
    L17:
        int r9 = r1 + 1;
        int r15 = u16(r7, r9) & 8191;
        int r17 = r1 + 3;
        int r16 = r7[r17] & Ascii.f88SI;
        int r18 = (r7[r17] >>> 4) & 3;
        int r19 = r4;
        Pid r20 = (Pid) r11.get(Integer.valueOf(r15));
        int r21 = r5;
        if (r20 == null) goto L20;
    L23:
        int r22 = r3;
        int r25 = 1;
        r20.packets++;
        if ((r7[r9] & SignedBytes.MAX_POWER_OF_TWO) == 0) goto L26;
        boolean r26 = true;
    L27:
        if (r26 == false) goto L30;
        r20.pusi++;
    L30:
        if ((r7[r9] & 128) == 0) goto L33;
        r20.tei++;
    L33:
        if ((r7[r17] & 192) == 0) goto L35;
        r20.scrambled++;
    L35:
        int r27 = r1 + 4;
        if ((r18 & 2) == 0) goto L53;
        int r28 = r7[r27] & 255;
        if (r28 <= 183) goto L40;
    L52:
        r20.adaptErrors++;
    L111:
        r3 = r22;
        r5 = r21;
        r4 = r19;
        r2 = r2;
        goto L112
    L40:
        if (((r27 + r28) + 1) > r6) goto L52;
        if (r28 <= 0) goto L50;
        int r29 = r7[r27 + 1] & 255;
        if ((r29 & 128) == 0) goto L46;
        boolean r30 = true;
    L48:
        if ((r29 & 16) == 0) goto L51;
        r20.pcr++;
    L51:
        r27 = r27 + (r28 + 1);
        boolean r31 = r30;
        r25 = 1;
    L54:
        if (r18 != 0) goto L58;
        r20.adaptErrors += r25;
        goto L111
    L58:
        if ((r18 & 1) == 0) goto L111;
        if (r27 >= r6) goto L111;
        if (r31 == false) goto L63;
        byte r32 = -1;
        r20.lastCc = -1;
    L65:
        if (r20.lastCc >= 0) goto L67;
    L72:
        r20.lastCc = r16;
        if (r26 == true) goto L75;
    L83:
        if (r15 != 0) goto L85;
    L88:
        Section r33 = (Section) r12.get(Integer.valueOf(r15));
        if (r33 != null) goto L92;
        r33 = new Section(null);
        r12.put(Integer.valueOf(r15), r33);
    L92:
        Section r34 = r33;
        if (r26 == false) goto L102;
        int r35 = r27 + 1;
        int r36 = r35 + (r7[r27] & 255);
        if (r36 <= r6) goto L98;
        r2 = r2 + 1;
        r34.reset();
        r6 = r6;
        r11 = r11;
        r4 = r19;
        r5 = r21;
        r3 = r22;
        r8 = r8;
        goto L112
    L98:
        if (r34.count <= 0) goto L101;
        r2 = r2 + feed(r34, r23, r35, r36, r15, r13, r14);
    L101:
        r34.reset();
        int r110 = r2;
        int r37 = r36;
    L103:
        if (r26 == true) goto L108;
        Section r38 = r34;
        if (r38.count > 0) goto L109;
        r3 = r22;
        r5 = r21;
        r4 = r19;
        r2 = r110;
    L109:
        r2 = r110 + feed(r38, r23, r37, r6, r15, r13, r14);
        r3 = r22;
        r5 = r21;
        r4 = r19;
        goto L112
    L108:
        r38 = r34;
        goto L109
    L102:
        r6 = r6;
        r110 = r2;
        r11 = r11;
        r19 = r19;
        r22 = r22;
        r8 = r8;
        r21 = r21;
        r34 = r34;
        r37 = r27;
        goto L103
    L85:
        if (r13.contains(Integer.valueOf(r15)) == false) goto L111;
    L75:
        if ((r27 + 9) > r6) goto L83;
        if (r7[r27] != 0) goto L83;
        if (r7[r27 + 1] != 0) goto L83;
        if (r7[r27 + 2] != 1) goto L83;
        r20.pes++;
        r20.streamId = r7[r27 + 3] & 255;
        r20.pesLength = u16(r7, r27 + 4);
        r20.flags = r7[r27 + 7] & 255;
        r20.optional = r7[r27 + 8] & 255;
        goto L83
    L67:
        if (r16 != r20.lastCc) goto L70;
        r20.ccRepeat++;
        goto L72
    L70:
        if (r16 == ((r20.lastCc + 1) & 15)) goto L72;
        r20.ccGaps++;
        goto L72
    L63:
        r32 = -1;
        goto L65
    L46:
        r30 = false;
        goto L48
    L50:
        r30 = false;
        goto L51
    L53:
        r31 = false;
        goto L54
    L26:
        r26 = false;
        goto L27
    L20:
        if (r11.size() < 64) goto L22;
        r3 = r3 + 1;
        r6 = r6;
        r11 = r11;
        r4 = r19;
        r5 = r21;
        r8 = r8;
        goto L112
    L22:
        r20 = new Pid(null);
        r11.put(Integer.valueOf(r15), r20);
        goto L23
    L113:
        int r111 = r2;
        int r39 = r3;
        ArrayList r112 = r8;
        TreeMap r113 = r11;
        int r114 = r4;
        int r40 = r5;
        Iterator r41 = r12.values().iterator();
        int r42 = 0;
    L115:
        if (r41.hasNext() == false) goto L119;
        if (((Section) r41.next()).count <= 0) goto L115;
        r42 = r42 + 1;
        goto L115
    L119:
        r112.add("packets=" + (r10 / TsExtractor.TS_PACKET_SIZE) + " psi_incomplete_sections=" + r42 + " sync_errors=" + r114 + " first_sync_error=" + r40 + " psi_errors=" + r111 + " pid_packets_omitted=" + r39);
        Iterator r43 = r113.entrySet().iterator();
    L121:
        if (r43.hasNext() == false) goto L123;
        Map.Entry r44 = (Map.Entry) r43.next();
        Pid r45 = (Pid) r44.getValue();
        r112.add("pid=" + r44.getKey() + " packets=" + r45.packets + " pusi=" + r45.pusi + " tei=" + r45.tei + " scrambled=" + r45.scrambled + " pcr=" + r45.pcr + " cc_gap=" + r45.ccGaps + " cc_repeat=" + r45.ccRepeat + " adaptation_errors=" + r45.adaptErrors + " pes=" + r45.pes + " last_stream_id=" + r45.streamId + " last_pes_length=" + r45.pesLength + " last_pes_flags=" + r45.flags + " last_optional_length=" + r45.optional);
        goto L121
    L123:
        Iterator r46 = r14.iterator();
    L125:
        if (r46.hasNext() == false) goto L127;
        r112.add((String) r46.next());
        goto L125
    L127:
        String r47 = failurePoint(r23, r24);
        if (r47.isEmpty() == true) goto L130;
        r112.add(r47);
    L130:
        return r112;
    L5:
        r10 = r7.length;
        goto L6
    }

    private static int u16(byte[] r1, int r2) {
        int r0 = (r1[r2] & 255) << 8;
        return (r1[r2 + 1] & 255) | r0;
    }
}
