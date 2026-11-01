package es.antonio.m3u;

import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.DefaultLoadControl;

/* JADX INFO: loaded from: classes7.dex */
final class PlaybackBufferPolicy {
    final int bytes;
    final int max;
    final int min;
    final int rebuffer;
    final int start;

    private PlaybackBufferPolicy(int r1, int r2, int r3, int r4, int r5) {
        this.min = r1;
        this.max = r2;
        this.start = r3;
        this.rebuffer = r4;
        this.bytes = r5;
    }

    static PlaybackBufferPolicy forEntry(Entry r1, boolean r2) {
        return forMime(r1.kind, StreamFormat.mime(r1), r2);
    }

    static PlaybackBufferPolicy forMime(String r1, String r2, boolean r3) {
        return forMime(r1, r2, r3, true);
    }

    static PlaybackBufferPolicy forMime(String r15, String r16, boolean r17, boolean r18) {
        int r1 = 1000;
        if (r18 == false) goto L5;
        int r6 = 300;
    L6:
        int r3 = 24;
        if ("Canales".equals(r15) == true) goto L9;
    L19:
        int r4 = 30000;
        int r5 = 60000;
        if ("Películas".equals(r15) == true) goto L35;
        if ("Series".equals(r15) == true) goto L35;
        if (r17 == false) goto L26;
        r4 = 15000;
    L26:
        if (r17 == false) goto L29;
        r5 = 35000;
    L29:
        if (r17 == true) goto L33;
        r3 = 48;
    L33:
        return new PlaybackBufferPolicy(r4, r5, r6, 3500, (r3 * 1024) * 1024);
    L35:
        if (r17 == false) goto L37;
        int r10 = 30000;
    L38:
        if (r17 == true) goto L41;
        r5 = 120000;
    L41:
        int r11 = r5;
        if (r17 == false) goto L44;
        int r12 = 10000;
    L45:
        if (r17 == false) goto L47;
        int r13 = 10000;
    L49:
        return new PlaybackBufferPolicy(r10, r11, r12, r13, Integer.MAX_VALUE);
    L47:
        r13 = 15000;
        goto L49
    L44:
        r12 = 20000;
        goto L45
    L37:
        r10 = 60000;
        goto L38
    L9:
        if (MimeTypes.APPLICATION_M3U8.equals(r16) == false) goto L19;
        if (r18 == false) goto L13;
        r1 = 200;
    L13:
        int r14 = r1;
        if (r17 == true) goto L18;
        r3 = 32;
    L18:
        return new PlaybackBufferPolicy(15000, 35000, r14, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS, (r3 * 1024) * 1024);
    L5:
        r6 = 1000;
        goto L6
    }
}
