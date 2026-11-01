package androidx.media3.extractor.mkv;

import android.support.v4.media.session.PlaybackStateCompat;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.ExtractorInput;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class Sniffer {
    private static final int ID_EBML = 440786851;
    private static final int SEARCH_LENGTH = 1024;
    private int peekLength;
    private final ParsableByteArray scratch;

    public Sniffer() {
        this.scratch = new ParsableByteArray(8);
    }

    private long readUint(ExtractorInput r7) throws IOException {
        r7.peekFully(this.scratch.getData(), 0, 1);
        int r0 = this.scratch.getData()[0] & 255;
        if (r0 != 0) goto L6;
        return Long.MIN_VALUE;
    L6:
        int r1 = 128;
        int r3 = 0;
    L8:
        if ((r0 & r1) != 0) goto L10;
        r1 = r1 >> 1;
        r3 = r3 + 1;
        goto L8
    L10:
        int r2 = r0 & (~r1);
        r7.peekFully(this.scratch.getData(), 1, r3);
        int r4 = 0;
    L11:
        if (r4 >= r3) goto L13;
        r2 = (r2 << 8) + (this.scratch.getData()[r4 + 1] & 255);
        r4 = r4 + 1;
        goto L11
    L13:
        this.peekLength += r3 + 1;
        return r2;
    }

    public boolean sniff(ExtractorInput r24) throws IOException {
        ExtractorInput r1 = r24;
        long r2 = r1.getLength();
        long r7 = PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        if (r2 != (-1)) goto L5;
    L9:
        int r6 = (int) r7;
        boolean r8 = false;
        r1.peekFully(this.scratch.getData(), 0, 4);
        long r10 = this.scratch.readUnsignedInt();
        this.peekLength = 4;
    L11:
        if (r10 == 440786851) goto L16;
        int r9 = this.peekLength + 1;
        this.peekLength = r9;
        if (r9 == r6) goto L14;
        r1.peekFully(this.scratch.getData(), 0, 1);
        r10 = ((r10 << 8) & (-256)) | ((long) (this.scratch.getData()[0] & 255));
        goto L11
    L14:
        return false;
    L16:
        long r12 = readUint(r24);
        long r14 = this.peekLength;
        if (r12 == Long.MIN_VALUE) goto L46;
        if (r2 == (-1)) goto L24;
        if ((r14 + r12) < r2) goto L24;
        return false;
    L24:
        if (this.peekLength >= (r14 + r12)) goto L40;
        if (readUint(r24) == Long.MIN_VALUE) goto L27;
        boolean r11 = r8;
        long r13 = readUint(r24);
        if (r13 < 0) goto L39;
        if (r13 > 2147483647L) goto L56;
        if (r13 == 0) goto L37;
        int r15 = (int) r13;
        r1.advancePeekPosition(r15);
        this.peekLength += r15;
    L37:
        r1 = r24;
        r8 = r11;
        goto L24
    L56:
        return r11;
    L39:
        return r11;
    L27:
        return r8;
    L40:
        boolean r19 = r8;
        if (this.peekLength != (r14 + r12)) goto L44;
        return true;
    L44:
        return r19;
    L46:
        return false;
    L5:
        if (r2 > PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) goto L9;
        r7 = r2;
        goto L9
    }
}
