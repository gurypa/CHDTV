package androidx.media3.extractor.mkv;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.extractor.ExtractorInput;
import java.io.IOException;
import java.util.ArrayDeque;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
final class DefaultEbmlReader implements EbmlReader {
    private static final int ELEMENT_STATE_READ_CONTENT = 2;
    private static final int ELEMENT_STATE_READ_CONTENT_SIZE = 1;
    private static final int ELEMENT_STATE_READ_ID = 0;
    private static final int MAX_ID_BYTES = 4;
    private static final int MAX_INTEGER_ELEMENT_SIZE_BYTES = 8;
    private static final int MAX_LENGTH_BYTES = 8;
    private static final int VALID_FLOAT32_ELEMENT_SIZE_BYTES = 4;
    private static final int VALID_FLOAT64_ELEMENT_SIZE_BYTES = 8;
    private long elementContentSize;
    private int elementId;
    private int elementState;
    private final ArrayDeque<MasterElement> masterElementsStack;
    private EbmlProcessor processor;
    private final byte[] scratch;
    private final VarintReader varintReader;

    /* JADX INFO: renamed from: androidx.media3.extractor.mkv.DefaultEbmlReader$1 */
    static /* synthetic */ class C04371 {
    }

    private static final class MasterElement {
        private final long elementEndPosition;
        private final int elementId;

        private MasterElement(int r1, long r2) {
            this.elementId = r1;
            this.elementEndPosition = r2;
        }

        /* synthetic */ MasterElement(int r1, long r2, C04371 r4) {
            this(r1, r2);
        }

        static /* synthetic */ long access$000(MasterElement r2) {
            return r2.elementEndPosition;
        }

        static /* synthetic */ int access$100(MasterElement r1) {
            return r1.elementId;
        }
    }

    public DefaultEbmlReader() {
        this.scratch = new byte[8];
        this.masterElementsStack = new ArrayDeque();
        this.varintReader = new VarintReader();
    }

    @RequiresNonNull({"processor"})
    private long maybeResyncToNextLevel1Element(ExtractorInput r5) throws IOException {
        r5.resetPeekPosition();
    L3:
        r5.peekFully(this.scratch, 0, 4);
        int r0 = VarintReader.parseUnsignedVarintLength(this.scratch[0]);
        if (r0 == (-1)) goto L10;
        if (r0 > 4) goto L10;
        int r1 = (int) VarintReader.assembleVarint(this.scratch, r0, false);
        if (this.processor.isLevel1Element(r1) == false) goto L10;
        r5.skipFully(r0);
        return r1;
    L10:
        r5.skipFully(1);
        goto L3
    }

    private double readFloat(ExtractorInput r5, int r6) throws IOException {
        long r0 = readInteger(r5, r6);
        if (r6 != 4) goto L6;
        return Float.intBitsToFloat((int) r0);
    L6:
        return Double.longBitsToDouble(r0);
    }

    private long readInteger(ExtractorInput r8, int r9) throws IOException {
        r8.readFully(this.scratch, 0, r9);
        long r0 = 0;
        int r2 = 0;
    L3:
        if (r2 >= r9) goto L5;
        r0 = (r0 << 8) | ((long) (this.scratch[r2] & 255));
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static String readString(ExtractorInput r4, int r5) throws IOException {
        if (r5 != 0) goto L5;
        return "";
    L5:
        byte[] r0 = new byte[r5];
        r4.readFully(r0, 0, r5);
        int r2 = r5;
    L6:
        if (r2 <= 0) goto L11;
        if (r0[r2 - 1] != 0) goto L11;
        r2 = r2 - 1;
    L11:
        return new String(r0, 0, r2);
    }

    @Override // androidx.media3.extractor.mkv.EbmlReader
    public void init(EbmlProcessor r1) {
        this.processor = r1;
    }

    @Override // androidx.media3.extractor.mkv.EbmlReader
    public boolean read(ExtractorInput r13) throws IOException {
        Assertions.checkStateNotNull(this.processor);
    L3:
        MasterElement r0 = this.masterElementsStack.peek();
        if (r0 == null) goto L10;
        if (r13.getPosition() < MasterElement.access$000(r0)) goto L10;
        this.processor.endMasterElement(MasterElement.access$100(this.masterElementsStack.pop()));
        return true;
    L10:
        if (this.elementState != 0) goto L19;
        long r4 = this.varintReader.readUnsignedVarint(r13, true, false, 4);
        if (r4 != (-2)) goto L15;
        r4 = maybeResyncToNextLevel1Element(r13);
    L15:
        if (r4 == (-1)) goto L16;
        this.elementId = (int) r4;
        this.elementState = 1;
        goto L19
    L16:
        return false;
    L19:
        if (this.elementState != 1) goto L21;
        this.elementContentSize = this.varintReader.readUnsignedVarint(r13, false, true, 8);
        this.elementState = 2;
    L21:
        int r2 = this.processor.getElementType(this.elementId);
        C04371 r6 = null;
        switch(r2) {
            case 0: goto L50;
            case 1: goto L48;
            case 2: goto L43;
            case 3: goto L37;
            case 4: goto L34;
            case 5: goto L26;
            default: goto L24;
        };
    L50:
        r13.skipFully((int) this.elementContentSize);
        this.elementState = 0;
        goto L3
    L24:
        throw ParserException.createForMalformedContainer("Invalid element type " + r2, null);
    L26:
        if (this.elementContentSize != 4) goto L28;
    L32:
        this.processor.floatElement(this.elementId, readFloat(r13, (int) this.elementContentSize));
        this.elementState = 0;
        return true;
    L28:
        if (this.elementContentSize == 8) goto L32;
        throw ParserException.createForMalformedContainer("Invalid float size: " + this.elementContentSize, null);
    L34:
        this.processor.binaryElement(this.elementId, (int) this.elementContentSize, r13);
        this.elementState = 0;
        return true;
    L37:
        if (this.elementContentSize > 2147483647L) goto L41;
        this.processor.stringElement(this.elementId, readString(r13, (int) this.elementContentSize));
        this.elementState = 0;
        return true;
    L41:
        throw ParserException.createForMalformedContainer("String element size: " + this.elementContentSize, null);
    L43:
        if (this.elementContentSize > 8) goto L47;
        this.processor.integerElement(this.elementId, readInteger(r13, (int) this.elementContentSize));
        this.elementState = 0;
        return true;
    L47:
        throw ParserException.createForMalformedContainer("Invalid integer size: " + this.elementContentSize, null);
    L48:
        long r7 = r13.getPosition();
        this.masterElementsStack.push(new MasterElement(this.elementId, r7 + this.elementContentSize, r6));
        this.processor.startMasterElement(this.elementId, r7, this.elementContentSize);
        this.elementState = 0;
        return true;
    }

    @Override // androidx.media3.extractor.mkv.EbmlReader
    public void reset() {
        this.elementState = 0;
        this.masterElementsStack.clear();
        this.varintReader.reset();
    }
}
