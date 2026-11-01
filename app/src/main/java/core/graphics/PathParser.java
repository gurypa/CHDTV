package androidx.core.graphics;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class PathParser {
    private static final String LOGTAG = "PathParser";

    private static class ExtractFloatResult {
        int mEndPosition;
        boolean mEndWithNegOrDot;

        ExtractFloatResult() {
        }
    }

    public static class PathDataNode {
        public float[] mParams;
        public char mType;

        PathDataNode(char r1, float[] r2) {
            this.mType = r1;
            this.mParams = r2;
        }

        PathDataNode(PathDataNode r4) {
            this.mType = r4.mType;
            this.mParams = PathParser.copyOfRange(r4.mParams, 0, r4.mParams.length);
        }

        private static void addCommand(Path r27, float[] r28, char r29, char r30, float[] r31) {
            Path r0 = r27;
            boolean r11 = false;
            float r2 = r28[0];
            boolean r12 = true;
            float r3 = r28[1];
            char r13 = 2;
            float r4 = r28[2];
            float r5 = r28[3];
            float r6 = r28[4];
            float r7 = r28[5];
            switch(r30) {
                case 65: goto L10;
                case 67: goto L9;
                case 72: goto L8;
                case 76: goto L7;
                case 77: goto L7;
                case 81: goto L6;
                case 83: goto L6;
                case 84: goto L7;
                case 86: goto L8;
                case 90: goto L5;
                case 97: goto L10;
                case 99: goto L9;
                case 104: goto L8;
                case 108: goto L7;
                case 109: goto L7;
                case 113: goto L6;
                case 115: goto L6;
                case 116: goto L7;
                case 118: goto L8;
                case 122: goto L5;
                default: goto L4;
            };
        L4:
            int r17 = 2;
        L11:
            int r8 = 0;
            float r9 = r2;
            float r18 = r3;
            float r19 = r4;
            float r20 = r5;
            float r21 = r6;
            float r22 = r7;
            char r10 = r29;
        L13:
            if (r8 >= r31.length) goto L86;
            boolean r23 = r11;
            boolean r24 = r12;
            char r26 = r13;
            switch(r30) {
                case 65: goto L76;
                case 67: goto L75;
                case 72: goto L74;
                case 76: goto L73;
                case 77: goto L69;
                case 81: goto L68;
                case 83: goto L59;
                case 84: goto L52;
                case 86: goto L51;
                case 97: goto L42;
                case 99: goto L41;
                case 104: goto L40;
                case 108: goto L39;
                case 109: goto L35;
                case 113: goto L34;
                case 115: goto L25;
                case 116: goto L18;
                case 118: goto L17;
                default: goto L16;
            };
        L16:
            float r14 = r18;
            int r110 = r8;
            float r1 = r14;
        L85:
            r10 = r30;
            r8 = r110 + r17;
            r0 = r27;
            r18 = r1;
            r11 = r23;
            r12 = r24;
            r13 = r26;
            goto L13
        L17:
            r0.rLineTo(0.0f, r31[r8 + 0]);
            r1 = r18 + r31[r8 + 0];
            r110 = r8;
            goto L85
        L18:
            float r15 = 0.0f;
            float r16 = 0.0f;
            if (r10 == 'q') goto L23;
            if (r10 == 't') goto L23;
            if (r10 == 'Q') goto L23;
            if (r10 == 'T') goto L23;
        L24:
            r0.rQuadTo(r15, r16, r31[r8 + 0], r31[r8 + 1]);
            float r25 = r9 + r15;
            r9 = r9 + r31[r8 + 0];
            r19 = r25;
            r20 = r18 + r16;
            r1 = r18 + r31[r8 + 1];
            r110 = r8;
        L23:
            r15 = r9 - r19;
            r16 = r18 - r20;
            goto L24
        L25:
            float r32 = 0.0f;
            if (r10 == 'c') goto L32;
            if (r10 == 's') goto L32;
            if (r10 == 'C') goto L32;
            if (r10 == 'S') goto L32;
            float r33 = 0.0f;
        L33:
            r0.rCubicTo(r32, r33, r31[r8 + 0], r31[r8 + 1], r31[r8 + 2], r31[r8 + 3]);
            float r34 = r31[r8 + 0] + r9;
            float r35 = r18 + r31[r8 + 1];
            r9 = r9 + r31[r8 + 2];
            r19 = r34;
            r20 = r35;
            r1 = r18 + r31[r8 + 3];
            r110 = r8;
        L32:
            r32 = r9 - r19;
            r33 = r18 - r20;
            goto L33
        L34:
            r0.rQuadTo(r31[r8 + 0], r31[r8 + 1], r31[r8 + 2], r31[r8 + 3]);
            float r36 = r31[r8 + 0] + r9;
            float r37 = r18 + r31[r8 + 1];
            r9 = r9 + r31[r8 + 2];
            r19 = r36;
            r20 = r37;
            r1 = r18 + r31[r8 + 3];
            r110 = r8;
            goto L85
        L35:
            r9 = r9 + r31[r8 + 0];
            float r111 = r18 + r31[r8 + 1];
            if (r8 <= 0) goto L38;
            r0.rLineTo(r31[r8 + 0], r31[r8 + 1]);
            r1 = r111;
            r110 = r8;
            goto L85
        L38:
            r0.rMoveTo(r31[r8 + 0], r31[r8 + 1]);
            r21 = r9;
            r22 = r111;
            r1 = r111;
            r110 = r8;
            goto L85
        L39:
            r0.rLineTo(r31[r8 + 0], r31[r8 + 1]);
            r9 = r9 + r31[r8 + 0];
            r1 = r18 + r31[r8 + 1];
            r110 = r8;
            goto L85
        L40:
            r0.rLineTo(r31[r8 + 0], 0.0f);
            r9 = r9 + r31[r8 + 0];
            r1 = r18;
            r110 = r8;
            goto L85
        L41:
            r0.rCubicTo(r31[r8 + 0], r31[r8 + 1], r31[r8 + 2], r31[r8 + 3], r31[r8 + 4], r31[r8 + 5]);
            float r38 = r31[r8 + 2] + r9;
            float r39 = r18 + r31[r8 + 3];
            r9 = r9 + r31[r8 + 4];
            r19 = r38;
            r20 = r39;
            r1 = r18 + r31[r8 + 5];
            r110 = r8;
            goto L85
        L42:
            float r40 = r31[r8 + 5] + r9;
            float r41 = r31[r8 + 6] + r18;
            float r42 = r31[r8 + 0];
            float r43 = r31[r8 + 1];
            float r44 = r31[r8 + 2];
            if (r31[r8 + 3] == 0.0f) goto L45;
            boolean r45 = r24;
        L47:
            if (r31[r8 + 4] == 0.0f) goto L49;
            boolean r46 = r24;
        L50:
            float r112 = r18;
            r110 = r8;
            drawArc(r27, r9, r112, r40, r41, r42, r43, r44, r45, r46);
            r9 = r9 + r31[r110 + 5];
            r1 = r31[r110 + 6] + r112;
            r19 = r9;
            r20 = r1;
            goto L85
        L49:
            r46 = r23;
            goto L50
        L45:
            r45 = r23;
            goto L47
        L51:
            r110 = r8;
            r0.lineTo(r9, r31[r110 + 0]);
            r1 = r31[r110 + 0];
            goto L85
        L52:
            float r113 = r18;
            r110 = r8;
            char r114 = r10;
            float r47 = r9;
            float r48 = r47;
            float r49 = r113;
            if (r114 == 'q') goto L57;
            if (r114 == 't') goto L57;
            if (r114 == 'Q') goto L57;
            if (r114 == 'T') goto L57;
        L58:
            r0.quadTo(r48, r49, r31[r110 + 0], r31[r110 + 1]);
            r19 = r48;
            r20 = r49;
            r9 = r31[r110 + 0];
            r1 = r31[r110 + 1];
        L57:
            r48 = (r47 * 2.0f) - r19;
            r49 = (r113 * 2.0f) - r20;
            goto L58
        L59:
            float r115 = r18;
            r110 = r8;
            char r116 = r10;
            float r50 = r9;
            float r51 = r50;
            if (r116 == 'c') goto L66;
            if (r116 == 's') goto L66;
            if (r116 == 'C') goto L66;
            if (r116 == 'S') goto L66;
            float r52 = r115;
        L67:
            r0.cubicTo(r51, r52, r31[r110 + 0], r31[r110 + 1], r31[r110 + 2], r31[r110 + 3]);
            float r53 = r31[r110 + 0];
            float r54 = r31[r110 + 1];
            r19 = r53;
            r20 = r54;
            r9 = r31[r110 + 2];
            r1 = r31[r110 + 3];
        L66:
            r51 = (r50 * 2.0f) - r19;
            r52 = (r115 * 2.0f) - r20;
            goto L67
        L68:
            r110 = r8;
            r0.quadTo(r31[r110 + 0], r31[r110 + 1], r31[r110 + 2], r31[r110 + 3]);
            float r55 = r31[r110 + 0];
            float r56 = r31[r110 + 1];
            r19 = r55;
            r20 = r56;
            r9 = r31[r110 + 2];
            r1 = r31[r110 + 3];
            goto L85
        L69:
            r110 = r8;
            float r57 = r31[r110 + 0];
            float r58 = r31[r110 + 1];
            if (r110 <= 0) goto L72;
            r0.lineTo(r31[r110 + 0], r31[r110 + 1]);
            r9 = r57;
            r1 = r58;
            goto L85
        L72:
            r0.moveTo(r31[r110 + 0], r31[r110 + 1]);
            r9 = r57;
            r1 = r58;
            r21 = r57;
            r22 = r58;
            goto L85
        L73:
            r110 = r8;
            r0.lineTo(r31[r110 + 0], r31[r110 + 1]);
            r9 = r31[r110 + 0];
            r1 = r31[r110 + 1];
            goto L85
        L74:
            float r117 = r18;
            r110 = r8;
            r0.lineTo(r31[r110 + 0], r117);
            r9 = r31[r110 + 0];
            r1 = r117;
            goto L85
        L75:
            r110 = r8;
            r0.cubicTo(r31[r110 + 0], r31[r110 + 1], r31[r110 + 2], r31[r110 + 3], r31[r110 + 4], r31[r110 + 5]);
            float r59 = r31[r110 + 4];
            r1 = r31[r110 + 5];
            r9 = r59;
            r19 = r31[r110 + 2];
            r20 = r31[r110 + 3];
            goto L85
        L76:
            float r118 = r18;
            r110 = r8;
            float r60 = r9;
            float r61 = r31[r110 + 5];
            float r62 = r31[r110 + 6];
            float r63 = r31[r110 + 0];
            float r64 = r31[r110 + 1];
            float r65 = r31[r110 + 2];
            if (r31[r110 + 3] == 0.0f) goto L79;
            boolean r66 = r24;
        L81:
            if (r31[r110 + 4] == 0.0f) goto L83;
            boolean r67 = r24;
        L84:
            drawArc(r27, r60, r118, r61, r62, r63, r64, r65, r66, r67);
            float r68 = r31[r110 + 5];
            r1 = r31[r110 + 6];
            r9 = r68;
            r19 = r68;
            r20 = r1;
            goto L85
        L83:
            r67 = r23;
            goto L84
        L79:
            r66 = r23;
            goto L81
        L86:
            r28[r11 ? 1 : 0] = r9;
            r28[r12 ? 1 : 0] = r18;
            r28[r13] = r19;
            r28[3] = r20;
            r28[4] = r21;
            r28[5] = r22;
            return;
        L5:
            r0.close();
            r2 = r6;
            r3 = r7;
            r4 = r6;
            r5 = r7;
            r0.moveTo(r2, r3);
            r17 = 2;
            goto L11
        L6:
            r17 = 4;
            goto L11
        L7:
            r17 = 2;
            goto L11
        L8:
            r17 = 1;
            goto L11
        L9:
            r17 = 6;
            goto L11
        L10:
            r17 = 7;
            goto L11
        }

        private static void arcToBezier(Path r58, double r59, double r61, double r63, double r65, double r67, double r69, double r71, double r73, double r75) {
            double r0 = r63;
            int r4 = (int) Math.ceil(Math.abs((r75 * 4.0d) / 3.141592653589793d));
            double r7 = Math.cos(r71);
            double r9 = Math.sin(r71);
            double r11 = Math.cos(r73);
            double r13 = Math.sin(r73);
            double r19 = (((-r0) * r9) * r13) + ((r65 * r7) * r11);
            double r2 = r75 / ((double) r4);
            double r23 = r73;
            int r15 = 0;
            double r5 = r67;
            double r21 = (((-r0) * r7) * r13) - ((r65 * r9) * r11);
            double r17 = r69;
        L3:
            if (r15 >= r4) goto L5;
            double r27 = r23 + r2;
            double r29 = Math.sin(r27);
            double r31 = Math.cos(r27);
            double r37 = r2;
            double r3 = (r59 + ((r0 * r7) * r31)) - ((r65 * r9) * r29);
            int r16 = r4;
            double r6 = (r61 + ((r0 * r9) * r31)) + ((r65 * r7) * r29);
            double r33 = r7;
            double r8 = (((-r0) * r33) * r29) - ((r65 * r9) * r31);
            double r10 = (((-r0) * r9) * r29) + ((r65 * r33) * r31);
            double r35 = Math.tan((r27 - r23) / 2.0d);
            double r39 = (Math.sin(r27 - r23) * (Math.sqrt(((r35 * 3.0d) * r35) + 4.0d) - 1.0d)) / 3.0d;
            r58.rLineTo(0.0f, 0.0f);
            r58.cubicTo((float) (r5 + (r39 * r21)), (float) (r17 + (r39 * r19)), (float) (r3 - (r39 * r8)), (float) (r6 - (r39 * r10)), (float) r3, (float) r6);
            r23 = r27;
            r17 = r6;
            r21 = r8;
            r19 = r10;
            r15 = r15 + 1;
            r5 = r3;
            r4 = r16;
            r7 = r33;
            r2 = r37;
            r13 = r13;
            r9 = r9;
            r11 = r11;
            r0 = r63;
            goto L3
        }

        private static void drawArc(Path r61, float r62, float r63, float r64, float r65, float r66, float r67, float r68, boolean r69, boolean r70) {
            double r25 = Math.toRadians(r68);
            double r31 = Math.cos(r25);
            double r33 = Math.sin(r25);
            double r35 = ((((double) r62) * r31) + (((double) r63) * r33)) / ((double) r66);
            double r37 = ((((double) (-r62)) * r33) + (((double) r63) * r31)) / ((double) r67);
            double r39 = ((((double) r64) * r31) + (((double) r65) * r33)) / ((double) r66);
            double r41 = ((((double) (-r64)) * r33) + (((double) r65) * r31)) / ((double) r67);
            double r43 = r35 - r39;
            double r45 = r37 - r41;
            double r47 = (r35 + r39) / 2.0d;
            double r49 = (r37 + r41) / 2.0d;
            double r12 = (r43 * r43) + (r45 * r45);
            if (r12 != 0.0d) goto L6;
            Log.w(PathParser.LOGTAG, " Points are coincident");
            return;
        L6:
            double r51 = (1.0d / r12) - 0.25d;
            if (r51 >= 0.0d) goto L10;
            Log.w(PathParser.LOGTAG, "Points are too far apart " + r12);
            float r14 = (float) (Math.sqrt(r12) / 1.99999d);
            drawArc(r61, r62, r63, r64, r65, r66 * r14, r67 * r14, r68, r69, r70);
            return;
        L10:
            double r3 = Math.sqrt(r51);
            double r7 = r3 * r43;
            double r53 = r3 * r45;
            if (r69 != r70) goto L13;
            double r15 = r47 - r53;
            double r16 = r49 + r7;
        L14:
            double r27 = Math.atan2(r37 - r16, r35 - r15);
            double r5 = Math.atan2(r41 - r16, r39 - r15) - r27;
            if (r5 < 0.0d) goto L17;
            boolean r20 = true;
        L19:
            if (r70 != r20) goto L21;
            double r29 = r5;
        L25:
            double r17 = r15 * ((double) r66);
            double r18 = r16 * ((double) r67);
            arcToBezier(r61, (r17 * r31) - (r18 * r33), (r17 * r33) + (r18 * r31), r66, r67, r62, r63, r25, r27, r29);
            return;
        L21:
            if (r5 <= 0.0d) goto L23;
            r29 = r5 - 6.283185307179586d;
            goto L25
        L23:
            r29 = r5 + 6.283185307179586d;
            goto L25
        L17:
            r20 = false;
            goto L19
        L13:
            r15 = r47 + r53;
            r16 = r49 - r7;
            goto L14
        }

        public static void nodesToPath(PathDataNode[] r5, Path r6) {
            float[] r0 = new float[6];
            char r1 = 'm';
            int r2 = 0;
        L4:
            if (r2 >= r5.length) goto L6;
            addCommand(r6, r0, r1, r5[r2].mType, r5[r2].mParams);
            r1 = r5[r2].mType;
            r2 = r2 + 1;
            goto L4
        }

        public void interpolatePathDataNode(PathDataNode r5, PathDataNode r6, float r7) {
            this.mType = r5.mType;
            int r0 = 0;
        L4:
            if (r0 >= r5.mParams.length) goto L6;
            this.mParams[r0] = (r5.mParams[r0] * (1.0f - r7)) + (r6.mParams[r0] * r7);
            r0 = r0 + 1;
            goto L4
        }
    }

    private PathParser() {
    }

    private static void addNode(ArrayList<PathDataNode> r1, char r2, float[] r3) {
        r1.add(new PathDataNode(r2, r3));
    }

    public static boolean canMorph(PathDataNode[] r4, PathDataNode[] r5) {
        if (r4 == null) goto L21;
        if (r5 == null) goto L21;
        if (r4.length == r5.length) goto L9;
        return false;
    L9:
        int r1 = 0;
    L11:
        if (r1 >= r4.length) goto L19;
        if (r4[r1].mType != r5[r1].mType) goto L18;
        if (r4[r1].mParams.length != r5[r1].mParams.length) goto L18;
        r1 = r1 + 1;
    L18:
        return false;
    L19:
        return true;
    L21:
        return false;
    }

    static float[] copyOfRange(float[] r5, int r6, int r7) {
        if (r6 > r7) goto L11;
        int r0 = r5.length;
        if (r6 < 0) goto L9;
        if (r6 > r0) goto L9;
        int r1 = r7 - r6;
        float[] r3 = new float[r1];
        System.arraycopy(r5, r6, r3, 0, Math.min(r1, r0 - r6));
        return r3;
    L9:
        throw new ArrayIndexOutOfBoundsException();
    L11:
        throw new IllegalArgumentException();
    }

    public static PathDataNode[] createNodesFromPathData(String r6) {
        if (r6 != null) goto L5;
        return null;
    L5:
        int r0 = 0;
        int r1 = 1;
        ArrayList r2 = new ArrayList();
    L7:
        if (r1 >= r6.length()) goto L13;
        int r3 = nextStart(r6, r1);
        String r4 = r6.substring(r0, r3).trim();
        if (r4.length() <= 0) goto L11;
        float[] r5 = getFloats(r4);
        addNode(r2, r4.charAt(0), r5);
    L11:
        r0 = r3;
        r1 = r3 + 1;
        goto L7
    L13:
        if ((r1 - r0) != 1) goto L18;
        if (r0 >= r6.length()) goto L18;
        addNode(r2, r6.charAt(r0), new float[0]);
    L18:
        return (PathDataNode[]) r2.toArray(new PathDataNode[r2.size()]);
    }

    public static Path createPathFromPathData(String r6) {
        Path r0 = new Path();
        PathDataNode[] r1 = createNodesFromPathData(r6);
        if (r1 != null) goto L11;
        return null;
    L11:
        PathDataNode.nodesToPath(r1, r0);     // Catch: RuntimeException -> L6
        return r0;
    L6:
        e = move-exception;
        throw new RuntimeException("Error in parsing " + r6, e);
    }

    public static PathDataNode[] deepCopyNodes(PathDataNode[] r4) {
        if (r4 != null) goto L5;
        return null;
    L5:
        PathDataNode[] r0 = new PathDataNode[r4.length];
        int r1 = 0;
    L7:
        if (r1 >= r4.length) goto L9;
        r0[r1] = new PathDataNode(r4[r1]);
        r1 = r1 + 1;
        goto L7
    L9:
        return r0;
    }

    private static void extract(String r7, int r8, ExtractFloatResult r9) {
        int r0 = r8;
        boolean r1 = false;
        r9.mEndWithNegOrDot = false;
        boolean r2 = false;
        boolean r3 = false;
    L4:
        if (r0 >= r7.length()) goto L19;
        boolean r4 = r3;
        r3 = false;
        switch(r7.charAt(r0)) {
            case 32: goto L15;
            case 44: goto L15;
            case 45: goto L12;
            case 46: goto L9;
            case 69: goto L8;
            case 101: goto L8;
            default: goto L16;
        };
    L8:
        r3 = true;
        goto L16
    L9:
        if (r2 == true) goto L11;
        r2 = true;
        goto L16
    L11:
        r1 = true;
        r9.mEndWithNegOrDot = true;
        goto L16
    L12:
        if (r0 == r8) goto L16;
        if (r4 == true) goto L16;
        r1 = true;
        r9.mEndWithNegOrDot = true;
        goto L16
    L15:
        r1 = true;
    L16:
        if (r1 == true) goto L19;
        r0 = r0 + 1;
    L19:
        r9.mEndPosition = r0;
    }

    private static float[] getFloats(String r9) {
        if (r9.charAt(0) == 'z') goto L22;
        if (r9.charAt(0) == 'Z') goto L22;
        float[] r1 = new float[r9.length()];     // Catch: NumberFormatException -> L18
        int r2 = 0;
        int r3 = 1;
        ExtractFloatResult r5 = new ExtractFloatResult();     // Catch: NumberFormatException -> L18
        int r6 = r9.length();     // Catch: NumberFormatException -> L18
    L8:
        if (r3 >= r6) goto L16;
        extract(r9, r3, r5);     // Catch: NumberFormatException -> L18
        int r7 = r5.mEndPosition;     // Catch: NumberFormatException -> L18
        if (r3 >= r7) goto L13;
        r1[r2] = Float.parseFloat(r9.substring(r3, r7));     // Catch: NumberFormatException -> L18
        r2 = r2 + 1;     // Catch: NumberFormatException -> L18
    L13:
        if (r5.mEndWithNegOrDot == true) goto L14;
        r3 = r7 + 1;     // Catch: NumberFormatException -> L18
        goto L8
    L14:
        r3 = r7;
        goto L8
    L16:
        return copyOfRange(r1, 0, r2);
    L18:
        e = move-exception;
        throw new RuntimeException("error in parsing \"" + r9 + "\"", e);
    L22:
        return new float[0];
    }

    public static boolean interpolatePathDataNodes(PathDataNode[] r4, PathDataNode[] r5, PathDataNode[] r6, float r7) {
        if (r4 == null) goto L22;
        if (r5 == null) goto L22;
        if (r6 == null) goto L22;
        if (r4.length != r5.length) goto L20;
        if (r5.length != r6.length) goto L20;
        if (canMorph(r5, r6) == true) goto L13;
        return false;
    L13:
        int r0 = 0;
    L15:
        if (r0 >= r4.length) goto L17;
        r4[r0].interpolatePathDataNode(r5[r0], r6[r0], r7);
        r0 = r0 + 1;
        goto L15
    L17:
        return true;
    L20:
        throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes must have the same length");
    L22:
        throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes cannot be null");
    }

    private static int nextStart(String r3, int r4) {
    L3:
        if (r4 >= r3.length()) goto L14;
        char r0 = r3.charAt(r4);
        if (((r0 - 'A') * (r0 - 'Z')) <= 0) goto L9;
        if (((r0 - 'a') * (r0 - 'z')) <= 0) goto L9;
    L13:
        r4 = r4 + 1;
    L9:
        if (r0 == 'e') goto L13;
        if (r0 == 'E') goto L13;
        return r4;
    L14:
        return r4;
    }

    public static void updateNodes(PathDataNode[] r4, PathDataNode[] r5) {
        int r0 = 0;
    L4:
        if (r0 >= r5.length) goto L10;
        PathDataNode r1 = r4[r0];
        r1.mType = r5[r0].mType;
        int r2 = 0;
    L7:
        if (r2 >= r5[r0].mParams.length) goto L9;
        r4[r0].mParams[r2] = r5[r0].mParams[r2];
        r2 = r2 + 1;
        goto L7
    L9:
        r0 = r0 + 1;
        goto L4
    }
}
