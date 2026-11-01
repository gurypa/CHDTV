package es.antonio.m3u;

import androidx.exifinterface.media.ExifInterface;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes7.dex */
final class DemandMpdInput extends InputStream {
    long count;
    long duration;
    long elements;
    boolean enabled;
    boolean eof;
    boolean fold;

    /* JADX INFO: renamed from: in */
    final InputStream f181in;
    long inputBytes;
    boolean inside;
    long next;
    byte[] output;
    long outputBytes;
    boolean pass;
    int peakOutput;
    boolean pending;
    int position;
    boolean root;
    long runs;
    final byte[] source;
    int sourceLength;
    int sourcePosition;
    long start;
    final byte[] tag;

    DemandMpdInput(InputStream r3) {
        this.source = new byte[65536];
        this.tag = new byte[65536];
        this.output = new byte[0];
        this.f181in = r3;
    }

    static boolean space(int r1) {
        if (r1 != 32) goto L5;
        return true;
    L5:
        if (r1 != 9) goto L7;
        return true;
    L7:
        if (r1 != 10) goto L9;
        return true;
    L9:
        if (r1 == 13) goto L16;
        return false;
    L16:
        return true;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.f181in.close();
    }

    boolean ends(int r5, String r6) {
        if (r5 >= r6.length()) goto L5;
        return false;
    L5:
        int r0 = 0;
    L7:
        if (r0 >= r6.length()) goto L8;
        if (this.tag[(r5 - r6.length()) + r0] != r6.charAt(r0)) goto L12;
        r0 = r0 + 1;
        goto L7
    L12:
        return false;
    L8:
        return true;
    }

    boolean fill() throws IOException {
        if (this.eof == false) goto L5;
        return false;
    L5:
        ByteArrayOutputStream r0 = new ByteArrayOutputStream(16384);
    L7:
        if (r0.size() >= 16384) goto L17;
        int r3 = take();
        if (r3 < 0) goto L11;
        if (this.pass == true) goto L14;
        if (r3 != 60) goto L23;
        this.tag[0] = (byte) r3;
        boolean r6 = false;
        boolean r7 = false;
        int r8 = 0;
        int r5 = 1;
    L42:
        if (r5 >= this.tag.length) goto L77;
        r3 = take();
        if (r3 < 0) goto L77;
        int r11 = r5 + 1;
        this.tag[r5] = (byte) r3;
        if (r11 != 4) goto L53;
        if (prefix(r11, "<!--") == false) goto L53;
        r6 = true;
    L53:
        if (r11 == 9) goto L55;
    L57:
        if (r6 == true) goto L59;
        if (r7 == true) goto L63;
        if (r8 == 0) goto L69;
        if (r3 != r8) goto L155;
        r8 = 0;
    L155:
        r5 = r11;
        goto L42
    L69:
        if (r3 != 39) goto L71;
    L156:
        r8 = r3;
        goto L155
    L71:
        if (r3 == 34) goto L156;
        if (r3 != 62) goto L155;
    L75:
        r5 = r11;
        goto L77
    L63:
        if (ends(r11, "]]>") == false) goto L155;
    L59:
        if (ends(r11, "-->") == false) goto L155;
    L55:
        if (prefix(r11, "<![CDATA[") == false) goto L57;
        r7 = true;
    L77:
        if (r5 == this.tag.length) goto L154;
        if (r3 < 0) goto L154;
        if (r6 == true) goto L151;
        if (r7 == true) goto L151;
        if (prefix(r5, "<!") == true) goto L85;
        if (r5 > 2) goto L89;
    L97:
        String r9 = name(r5);
    L98:
        boolean r10 = prefix(r5, "</");
        if (this.root == true) goto L107;
        if (r10 == true) goto L107;
        if (this.tag[1] == 63) goto L107;
        if ("MPD".equals(r9) == true) goto L107;
        this.pass = true;
    L107:
        if (this.root == true) goto L121;
        if ("MPD".equals(r9) == false) goto L121;
        if (r10 == true) goto L121;
        this.root = true;
        String r12 = new String(this.tag, 0, r5, StandardCharsets.US_ASCII);
        if (r12.matches("(?s).*\\btype\\s*=\\s*(['\"])static\\1.*") == false) goto L114;
    L116:
        boolean r13 = true;
    L117:
        this.enabled = r13;
        if (this.enabled == true) goto L121;
        this.pass = true;
        goto L121
    L114:
        if (r12.matches("(?s).*\\btype\\s*=.*") == false) goto L116;
        r13 = false;
    L121:
        if (this.enabled == false) goto L131;
        if (this.inside == true) goto L131;
        if ("SegmentTimeline".equals(r9) == false) goto L131;
        if (r10 == true) goto L131;
        if (ends(r5, "/>") == true) goto L131;
        this.inside = true;
        this.fold = true;
        this.next = 0;
        r0.write(this.tag, 0, r5);
    L131:
        if (this.inside == false) goto L137;
        if (r10 == false) goto L137;
        if ("SegmentTimeline".equals(r9) == false) goto L137;
        flush(r0);
        this.inside = false;
        this.fold = false;
        r0.write(this.tag, 0, r5);
    L137:
        if (this.inside == false) goto L147;
        if (this.fold == false) goto L147;
        if (ExifInterface.LATITUDE_SOUTH.equals(r9) == false) goto L147;
        if (r10 == true) goto L147;
        if (segment(r5, r0) == true) goto L7;
    L147:
        if (this.inside == false) goto L149;
        flush(r0);
        this.fold = false;
    L149:
        r0.write(this.tag, 0, r5);
        goto L7
    L89:
        if (this.tag[1] != 83) goto L97;
        if (space(this.tag[2]) == false) goto L93;
    L96:
        r9 = ExifInterface.LATITUDE_SOUTH;
        goto L98
    L93:
        if (this.tag[2] == 47) goto L96;
        if (this.tag[2] != 62) goto L97;
    L85:
        flush(r0);
        this.pass = true;
        r0.write(this.tag, 0, r5);
    L151:
        if (this.inside == false) goto L153;
        flush(r0);
        this.fold = false;
    L153:
        r0.write(this.tag, 0, r5);
    L154:
        flush(r0);
        this.pass = true;
        r0.write(this.tag, 0, r5);
        goto L7
    L23:
        if (r3 != 0) goto L28;
        if (this.root == true) goto L28;
        this.pass = true;
    L28:
        if (this.inside == false) goto L35;
        if (this.fold == false) goto L35;
        if (space(r3) == true) goto L7;
    L35:
        if (this.inside == true) goto L37;
    L39:
        r0.write(r3);
        goto L7
    L37:
        if (this.fold == false) goto L39;
        flush(r0);
        this.fold = false;
        goto L39
    L14:
        r0.write(r3);
        int r2 = Math.min(16384, this.sourceLength - this.sourcePosition);
        if (r2 <= 0) goto L17;
        this.inputBytes += (long) r2;
        r0.write(this.source, this.sourcePosition, r2);
        this.sourcePosition += r2;
        goto L17
    L11:
        flush(r0);
        this.eof = true;
    L17:
        this.output = r0.toByteArray();
        this.position = 0;
        this.outputBytes += (long) this.output.length;
        this.peakOutput = Math.max(this.peakOutput, this.output.length);
        if (this.output.length <= 0) goto L20;
        return true;
    L20:
        return false;
    }

    void flush(ByteArrayOutputStream r9) throws IOException {
        if (this.pending == true) goto L5;
        return;
    L5:
        StringBuilder r0 = new StringBuilder("<S t=\"").append(this.start).append("\" d=\"").append(this.duration).append("\"");
        if (this.count <= 1) goto L8;
        String r1 = " r=\"" + (this.count - 1) + "\"";
    L9:
        r9.write(r0.append(r1).append("/>").toString().getBytes(StandardCharsets.US_ASCII));
        this.pending = false;
        this.runs++;
        return;
    L8:
        r1 = "";
        goto L9
    }

    String name(int r6) {
        int r1 = 1;
        if (this.tag[1] != 47) goto L5;
        r1 = 2;
    L5:
        int r0 = r1;
    L6:
        if (r0 >= r6) goto L16;
        if (space(this.tag[r0]) == true) goto L16;
        if (this.tag[r0] == 47) goto L16;
        if (this.tag[r0] == 62) goto L16;
        r0 = r0 + 1;
    L16:
        return new String(this.tag, r1, r0 - r1, StandardCharsets.US_ASCII);
    }

    boolean prefix(int r4, String r5) {
        if (r4 >= r5.length()) goto L5;
        return false;
    L5:
        int r6 = 0;
    L7:
        if (r6 >= r5.length()) goto L8;
        if (this.tag[r6] != r5.charAt(r6)) goto L12;
        r6 = r6 + 1;
        goto L7
    L12:
        return false;
    L8:
        return true;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.position == this.output.length) goto L5;
    L8:
        byte[] r0 = this.output;
        int r1 = this.position;
        this.position = r1 + 1;
        return r0[r1] & 255;
    L5:
        if (fill() == true) goto L8;
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] r3, int r4, int r5) throws IOException {
        if (r5 != 0) goto L6;
        return 0;
    L6:
        if (this.position == this.output.length) goto L8;
    L11:
        int r6 = Math.min(r5, this.output.length - this.position);
        System.arraycopy(this.output, this.position, r3, r4, r6);
        this.position += r6;
        return r6;
    L8:
        if (fill() == true) goto L11;
        return -1;
    }

    boolean segment(int r28, ByteArrayOutputStream r29) throws IOException {
        int r0 = r28;
        if (ends(r0, "/>") == true) goto L5;
    L139:
        return false;
    L5:
        if (prefix(r0, "<S") == false) goto L139;
        if (r0 < 4) goto L139;
        if (space(this.tag[2]) == false) goto L11;
    L13:
        long r5 = 0;
        int r2 = 2;
        long r7 = 0;
        long r9 = 0;
        long r13 = 0;
        boolean r11 = false;
        boolean r12 = false;
        boolean r15 = false;
    L14:
        int r3 = r0 - 2;
        if (r2 >= r3) goto L26;
    L17:
        if (r2 >= r3) goto L24;
        if (space(this.tag[r2]) == false) goto L24;
        r2 = r2 + 1;     // Catch: ArithmeticException -> L22
    L136:
        return false;
    L24:
        if (r2 >= r3) goto L26;
        int r6 = r2;
    L58:
        if (r6 >= r3) goto L65;
        if (this.tag[r6] == 61) goto L65;
        if (space(this.tag[r6]) == true) goto L65;
        r6 = r6 + 1;     // Catch: ArithmeticException -> L22
    L65:
        int r1 = r6 - r2;     // Catch: ArithmeticException -> L22
    L66:
        if (r6 >= r3) goto L72;
        if (space(this.tag[r6]) == false) goto L72;
        r6 = r6 + 1;     // Catch: ArithmeticException -> L22
    L72:
        if (r1 != 1) goto L137;
        if (r6 >= r3) goto L137;
        int r19 = r6 + 1;     // Catch: ArithmeticException -> L22
        if (this.tag[r6] != 61) goto L178;
        int r4 = r19;
    L78:
        if (r4 >= r3) goto L83;
        if (space(this.tag[r4]) == false) goto L83;
        r4 = r4 + 1;     // Catch: ArithmeticException -> L22
    L83:
        if (r4 >= r3) goto L182;
        if (this.tag[r4] != 39) goto L87;
    L89:
        int r8 = r4 + 1;     // Catch: ArithmeticException -> L22
        byte r10 = this.tag[r4];     // Catch: ArithmeticException -> L22
        int r14 = r8;
        long r20 = 0;
    L90:
        if (r14 >= r3) goto L111;
        if (this.tag[r14] == r10) goto L111;
        int r22 = r14 + 1;     // Catch: ArithmeticException -> L22
        int r16 = this.tag[r14] - 48;
        if (r16 < 0) goto L109;
        if (r16 > 9) goto L99;
        long r17 = r16;
        if (r20 > ((Long.MAX_VALUE - r17) / 10)) goto L104;
        r20 = (r20 * 10) + r17;
        r14 = r22;
        goto L90
    L104:
        return false;
    L177:
        return false;
    L99:
        return false;
    L109:
        return false;
    L111:
        if (r14 >= r3) goto L132;
        if (r14 == r8) goto L113;
        r2 = r14 + 1;
        switch(this.tag[r2]) {
            case 100: goto L128;
            case 114: goto L124;
            case 116: goto L119;
            default: goto L117;
        };
    L119:
        if (r12 == true) goto L120;
        r13 = r20;
        r5 = 0;
        r12 = true;
    L131:
        r0 = r28;
        goto L14
    L120:
        return false;
    L124:
        if (r15 == true) goto L125;
        r9 = r20;
        r5 = 0;
        r15 = true;
        goto L131
    L125:
        return false;
    L128:
        if (r11 == true) goto L129;
        r7 = r20;
        r5 = 0;
        r11 = true;
        goto L131
    L129:
        return false;
    L117:
        return false;
    L113:
        return false;
    L132:
        return false;
    L87:
        if (this.tag[r4] == 34) goto L89;
        return false;
    L182:
        return false;
    L178:
        return false;
    L137:
        return false;
    L26:
        if (r11 == true) goto L28;
        return false;
    L28:
        if (r7 > r5) goto L30;
        return false;
    L30:
        if (r9 >= 2147483647L) goto L181;
        if (r12 == true) goto L35;
        r13 = this.next;     // Catch: ArithmeticException -> L22
    L35:
        long r18 = r9 + 1;     // Catch: ArithmeticException -> L22
        if (r18 <= ((Long.MAX_VALUE - r13) / r7)) goto L40;
        return false;
    L40:
        Long.signum(r7);
        long r110 = r13 + (r7 * r18);
        if (this.pending == false) goto L51;
        if (this.duration == r7) goto L46;
    L49:
        flush(r29);     // Catch: ArithmeticException -> L22
        goto L51
    L46:
        if (this.next != r13) goto L49;
        if ((this.count + r18) > 2147483647L) goto L49;
    L51:
        if (this.pending == true) goto L53;
        this.start = r13;     // Catch: ArithmeticException -> L22
        this.duration = r7;     // Catch: ArithmeticException -> L22
        this.count = 0;     // Catch: ArithmeticException -> L22
        this.pending = true;     // Catch: ArithmeticException -> L22
    L53:
        this.count += r18;     // Catch: ArithmeticException -> L22
        this.next = r110;     // Catch: ArithmeticException -> L22
        this.elements++;     // Catch: ArithmeticException -> L22
        return true;
    L181:
        return false;
    L11:
        if (this.tag[2] == 47) goto L13;
        return false;
    }

    int take() throws IOException {
        if (this.sourcePosition != this.sourceLength) goto L10;
    L4:
        this.sourceLength = this.f181in.read(this.source);
        if (this.sourceLength == 0) goto L4;
        this.sourcePosition = 0;
        if (this.sourceLength >= 0) goto L10;
        this.sourceLength = 0;
        return -1;
    L10:
        this.inputBytes++;
        byte[] r0 = this.source;
        int r1 = this.sourcePosition;
        this.sourcePosition = r1 + 1;
        return r0[r1] & 255;
    }
}
