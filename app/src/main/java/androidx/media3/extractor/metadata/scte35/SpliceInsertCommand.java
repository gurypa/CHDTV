package androidx.media3.extractor.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class SpliceInsertCommand extends SpliceCommand {
    public static final Parcelable.Creator<SpliceInsertCommand> CREATOR = null;
    public final boolean autoReturn;
    public final int availNum;
    public final int availsExpected;
    public final long breakDurationUs;
    public final List<ComponentSplice> componentSpliceList;
    public final boolean outOfNetworkIndicator;
    public final boolean programSpliceFlag;
    public final long programSplicePlaybackPositionUs;
    public final long programSplicePts;
    public final boolean spliceEventCancelIndicator;
    public final long spliceEventId;
    public final boolean spliceImmediateFlag;
    public final int uniqueProgramId;

    public static final class ComponentSplice {
        public final long componentSplicePlaybackPositionUs;
        public final long componentSplicePts;
        public final int componentTag;

        private ComponentSplice(int r1, long r2, long r4) {
            this.componentTag = r1;
            this.componentSplicePts = r2;
            this.componentSplicePlaybackPositionUs = r4;
        }

        /* synthetic */ ComponentSplice(int r1, long r2, long r4, C04321 r6) {
            this(r1, r2, r4);
        }

        public static ComponentSplice createFromParcel(Parcel r6) {
            return new ComponentSplice(r6.readInt(), r6.readLong(), r6.readLong());
        }

        public void writeToParcel(Parcel r3) {
            r3.writeInt(this.componentTag);
            r3.writeLong(this.componentSplicePts);
            r3.writeLong(this.componentSplicePlaybackPositionUs);
        }
    }

    static {
        CREATOR = new C04321();
    }

    private SpliceInsertCommand(long r17, boolean r19, boolean r20, boolean r21, boolean r22, long r23, long r25, List<ComponentSplice> r27, boolean r28, long r29, int r31, int r32, int r33) {
        this.spliceEventId = r17;
        this.spliceEventCancelIndicator = r19;
        this.outOfNetworkIndicator = r20;
        this.programSpliceFlag = r21;
        this.spliceImmediateFlag = r22;
        this.programSplicePts = r23;
        this.programSplicePlaybackPositionUs = r25;
        this.componentSpliceList = Collections.unmodifiableList(r27);
        this.autoReturn = r28;
        this.breakDurationUs = r29;
        this.uniqueProgramId = r31;
        this.availNum = r32;
        this.availsExpected = r33;
    }

    private SpliceInsertCommand(Parcel r7) {
        this.spliceEventId = r7.readLong();
        boolean r1 = false;
        if (r7.readByte() != 1) goto L5;
        boolean r0 = true;
    L6:
        this.spliceEventCancelIndicator = r0;
        if (r7.readByte() != 1) goto L9;
        boolean r2 = true;
    L10:
        this.outOfNetworkIndicator = r2;
        if (r7.readByte() != 1) goto L13;
        boolean r3 = true;
    L14:
        this.programSpliceFlag = r3;
        if (r7.readByte() != 1) goto L17;
        boolean r4 = true;
    L18:
        this.spliceImmediateFlag = r4;
        this.programSplicePts = r7.readLong();
        this.programSplicePlaybackPositionUs = r7.readLong();
        int r5 = r7.readInt();
        ArrayList r6 = new ArrayList(r5);
        int r8 = 0;
    L19:
        if (r8 >= r5) goto L21;
        r6.add(ComponentSplice.createFromParcel(r7));
        r8 = r8 + 1;
        goto L19
    L21:
        this.componentSpliceList = Collections.unmodifiableList(r6);
        if (r7.readByte() != 1) goto L24;
        r1 = true;
    L24:
        this.autoReturn = r1;
        this.breakDurationUs = r7.readLong();
        this.uniqueProgramId = r7.readInt();
        this.availNum = r7.readInt();
        this.availsExpected = r7.readInt();
        return;
    L17:
        r4 = false;
        goto L18
    L13:
        r3 = false;
        goto L14
    L9:
        r2 = false;
        goto L10
    L5:
        r0 = false;
        goto L6
    }

    /* synthetic */ SpliceInsertCommand(Parcel r1, C04321 r2) {
        this(r1);
    }

    static SpliceInsertCommand parseFromSection(ParsableByteArray r34, long r35, TimestampAdjuster r37) {
        long r2 = r34.readUnsignedInt();
        if ((r34.readUnsignedByte() & 128) == 0) goto L5;
        boolean r1 = true;
    L6:
        long r9 = C0180C.TIME_UNSET;
        List r11 = Collections.emptyList();
        boolean r15 = false;
        long r16 = C0180C.TIME_UNSET;
        if (r1 == true) goto L45;
        int r4 = r34.readUnsignedByte();
        if ((r4 & 128) == 0) goto L11;
        boolean r5 = true;
    L12:
        boolean r6 = r5;
        if ((r4 & 64) == 0) goto L15;
        boolean r7 = true;
    L16:
        boolean r8 = r7;
        if ((r4 & 32) == 0) goto L19;
        boolean r10 = true;
    L21:
        if ((r4 & 16) == 0) goto L23;
        boolean r20 = true;
    L24:
        boolean r12 = r20;
        if (r8 == false) goto L28;
        if (r12 == true) goto L28;
        r9 = TimeSignalCommand.parseSpliceTime(r34, r35);
    L28:
        if (r8 == true) goto L38;
        int r3 = r34.readUnsignedByte();
        ArrayList r13 = new ArrayList(r3);
        int r14 = 0;
    L30:
        if (r14 >= r3) goto L36;
        int r24 = r34.readUnsignedByte();
        long r25 = C0180C.TIME_UNSET;
        if (r12 == true) goto L35;
        r25 = TimeSignalCommand.parseSpliceTime(r34, r35);
    L35:
        int r17 = r14;
        long r18 = r25;
        r13.add(new ComponentSplice(r24, r18, r37.adjustTsTimestamp(r18), null));
        r14 = r17 + 1;
        r4 = r4;
        goto L30
    L36:
        r11 = r13;
    L38:
        if (r10 == false) goto L44;
        long r19 = r34.readUnsignedByte();
        if ((128 & r19) == 0) goto L42;
        boolean r21 = true;
    L43:
        r15 = r21;
        r16 = (1000 * (((r19 & 1) << 32) | r34.readUnsignedInt())) / 90;
        goto L44
    L42:
        r21 = false;
    L44:
        int r110 = r34.readUnsignedShort();
        int r111 = r34.readUnsignedByte();
        boolean r22 = r6;
        boolean r23 = r8;
        boolean r26 = r12;
        long r27 = r9;
        int r112 = r34.readUnsignedByte();
        List r113 = r11;
        long r32 = r16;
        int r114 = r110;
        int r115 = r111;
        boolean r116 = r15;
        long r117 = r32;
    L47:
        return new SpliceInsertCommand(r2, r1, r22, r23, r26, r27, r37.adjustTsTimestamp(r27), r113, r116, r117, r114, r115, r112);
    L23:
        r20 = false;
        goto L24
    L19:
        r10 = false;
        goto L21
    L15:
        r7 = false;
        goto L16
    L11:
        r5 = false;
        goto L12
    L45:
        r1 = r1;
        r2 = r2;
        r22 = false;
        r23 = false;
        r26 = false;
        r27 = -9223372036854775807L;
        r112 = 0;
        r113 = r11;
        r114 = 0;
        r115 = 0;
        r116 = false;
        r117 = -9223372036854775807L;
        goto L47
    L5:
        r1 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.metadata.scte35.SpliceCommand
    public String toString() {
        return "SCTE-35 SpliceInsertCommand { programSplicePts=" + this.programSplicePts + ", programSplicePlaybackPositionUs= " + this.programSplicePlaybackPositionUs + " }";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r4, int r5) {
        r4.writeLong(this.spliceEventId);
        r4.writeByte(this.spliceEventCancelIndicator ? 1 : 0);
        r4.writeByte(this.outOfNetworkIndicator ? 1 : 0);
        r4.writeByte(this.programSpliceFlag ? 1 : 0);
        r4.writeByte(this.spliceImmediateFlag ? 1 : 0);
        r4.writeLong(this.programSplicePts);
        r4.writeLong(this.programSplicePlaybackPositionUs);
        int r0 = this.componentSpliceList.size();
        r4.writeInt(r0);
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        this.componentSpliceList.get(r1).writeToParcel(r4);
        r1 = r1 + 1;
        goto L3
    L5:
        r4.writeByte(this.autoReturn ? 1 : 0);
        r4.writeLong(this.breakDurationUs);
        r4.writeInt(this.uniqueProgramId);
        r4.writeInt(this.availNum);
        r4.writeInt(this.availsExpected);
    }
}
