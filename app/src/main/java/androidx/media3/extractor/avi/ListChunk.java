package androidx.media3.extractor.avi;

import androidx.media3.common.util.ParsableByteArray;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

/* JADX INFO: loaded from: classes.dex */
final class ListChunk implements AviChunk {
    public final ImmutableList<AviChunk> children;
    private final int type;

    private ListChunk(int r1, ImmutableList<AviChunk> r2) {
        this.type = r1;
        this.children = r2;
    }

    private static AviChunk createBox(int r1, int r2, ParsableByteArray r3) {
        switch(r1) {
            case 1718776947: goto L12;
            case 1751742049: goto L10;
            case 1752331379: goto L8;
            case 1852994675: goto L6;
            default: goto L3;
        };
    L3:
        return null;
    L6:
        return StreamNameChunk.parseFrom(r3);
    L8:
        return AviStreamHeaderChunk.parseFrom(r3);
    L10:
        return AviMainHeaderChunk.parseFrom(r3);
    L12:
        return StreamFormatChunk.parseFrom(r2, r3);
    }

    public static ListChunk parseFrom(int r9, ParsableByteArray r10) {
        ImmutableList.Builder r0 = new ImmutableList.Builder();
        int r1 = r10.limit();
        int r2 = -2;
    L4:
        if (r10.bytesLeft() <= 8) goto L16;
        int r3 = r10.readLittleEndianInt();
        int r4 = r10.readLittleEndianInt();
        int r5 = r10.getPosition() + r4;
        r10.setLimit(r5);
        if (r3 != 1414744396) goto L8;
        AviChunk r6 = parseFrom(r10.readLittleEndianInt(), r10);
    L9:
        if (r6 == null) goto L14;
        if (r6.getType() != 1752331379) goto L13;
        r2 = ((AviStreamHeaderChunk) r6).getTrackType();
    L13:
        r0.add(r6);
    L14:
        r10.setPosition(r5);
        r10.setLimit(r1);
        goto L4
    L8:
        r6 = createBox(r3, r2, r10);
        goto L9
    L16:
        return new ListChunk(r9, r0.build());
    }

    public <T extends AviChunk> T getChild(Class<T> r4) {
        UnmodifiableIterator<AviChunk> r0 = this.children.iterator();
    L4:
        if (r0.hasNext() == false) goto L9;
        T r1 = (T) r0.next();
        if (r1.getClass() != r4) goto L4;
        return r1;
    L9:
        return null;
    }

    @Override // androidx.media3.extractor.avi.AviChunk
    public int getType() {
        return this.type;
    }
}
