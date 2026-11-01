package androidx.media3.extractor.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class SpliceScheduleCommand extends SpliceCommand {
    public static final Parcelable.Creator<SpliceScheduleCommand> CREATOR = null;
    public final List<Event> events;

    public static final class ComponentSplice {
        public final int componentTag;
        public final long utcSpliceTime;

        private ComponentSplice(int r1, long r2) {
            this.componentTag = r1;
            this.utcSpliceTime = r2;
        }

        /* synthetic */ ComponentSplice(int r1, long r2, C04341 r4) {
            this(r1, r2);
        }

        static /* synthetic */ ComponentSplice access$000(Parcel r1) {
            return createFromParcel(r1);
        }

        static /* synthetic */ void access$200(ComponentSplice r0, Parcel r1) {
            r0.writeToParcel(r1);
        }

        private static ComponentSplice createFromParcel(Parcel r4) {
            return new ComponentSplice(r4.readInt(), r4.readLong());
        }

        private void writeToParcel(Parcel r3) {
            r3.writeInt(this.componentTag);
            r3.writeLong(this.utcSpliceTime);
        }
    }

    public static final class Event {
        public final boolean autoReturn;
        public final int availNum;
        public final int availsExpected;
        public final long breakDurationUs;
        public final List<ComponentSplice> componentSpliceList;
        public final boolean outOfNetworkIndicator;
        public final boolean programSpliceFlag;
        public final boolean spliceEventCancelIndicator;
        public final long spliceEventId;
        public final int uniqueProgramId;
        public final long utcSpliceTime;

        private Event(long r2, boolean r4, boolean r5, boolean r6, List<ComponentSplice> r7, long r8, boolean r10, long r11, int r13, int r14, int r15) {
            this.spliceEventId = r2;
            this.spliceEventCancelIndicator = r4;
            this.outOfNetworkIndicator = r5;
            this.programSpliceFlag = r6;
            this.componentSpliceList = Collections.unmodifiableList(r7);
            this.utcSpliceTime = r8;
            this.autoReturn = r10;
            this.breakDurationUs = r11;
            this.uniqueProgramId = r13;
            this.availNum = r14;
            this.availsExpected = r15;
        }

        private Event(Parcel r7) {
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
            int r4 = r7.readInt();
            ArrayList r5 = new ArrayList(r4);
            int r6 = 0;
        L15:
            if (r6 >= r4) goto L17;
            r5.add(ComponentSplice.access$000(r7));
            r6 = r6 + 1;
            goto L15
        L17:
            this.componentSpliceList = Collections.unmodifiableList(r5);
            this.utcSpliceTime = r7.readLong();
            if (r7.readByte() != 1) goto L20;
            r1 = true;
        L20:
            this.autoReturn = r1;
            this.breakDurationUs = r7.readLong();
            this.uniqueProgramId = r7.readInt();
            this.availNum = r7.readInt();
            this.availsExpected = r7.readInt();
            return;
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

        static /* synthetic */ Event access$300(Parcel r1) {
            return createFromParcel(r1);
        }

        static /* synthetic */ Event access$400(ParsableByteArray r1) {
            return parseFromSection(r1);
        }

        static /* synthetic */ void access$500(Event r0, Parcel r1) {
            r0.writeToParcel(r1);
        }

        private static Event createFromParcel(Parcel r1) {
            return new Event(r1);
        }

        private static Event parseFromSection(ParsableByteArray r28) {
            long r1 = r28.readUnsignedInt();
            if ((r28.readUnsignedByte() & 128) == 0) goto L5;
            boolean r0 = true;
        L6:
            long r7 = C0180C.TIME_UNSET;
            ArrayList r9 = new ArrayList();
            boolean r13 = false;
            long r14 = C0180C.TIME_UNSET;
            if (r0 == true) goto L35;
            int r3 = r28.readUnsignedByte();
            if ((r3 & 128) == 0) goto L11;
            boolean r4 = true;
        L12:
            boolean r5 = r4;
            if ((r3 & 64) == 0) goto L15;
            boolean r6 = true;
        L16:
            boolean r8 = r6;
            if ((r3 & 32) == 0) goto L19;
            boolean r10 = true;
        L20:
            if (r8 == false) goto L22;
            r7 = r28.readUnsignedInt();
        L22:
            if (r8 == true) goto L27;
            int r2 = r28.readUnsignedByte();
            ArrayList r11 = new ArrayList(r2);
            int r12 = 0;
        L24:
            if (r12 >= r2) goto L26;
            r11.add(new ComponentSplice(r28.readUnsignedByte(), r28.readUnsignedInt(), null));
            r12 = r12 + 1;
            r3 = r3;
            r2 = r2;
            r10 = r10;
            goto L24
        L26:
            boolean r24 = r10;
            r9 = r11;
        L28:
            if (r24 == false) goto L34;
            long r15 = r28.readUnsignedByte();
            if ((128 & r15) == 0) goto L32;
            boolean r16 = true;
        L33:
            r13 = r16;
            r14 = (1000 * (((r15 & 1) << 32) | r28.readUnsignedInt())) / 90;
            goto L34
        L32:
            r16 = false;
        L34:
            int r17 = r28.readUnsignedShort();
            boolean r18 = r5;
            boolean r19 = r8;
            ArrayList r20 = r9;
            boolean r21 = r13;
            int r110 = r28.readUnsignedByte();
            int r111 = r17;
            long r112 = r14;
            int r113 = r28.readUnsignedByte();
        L37:
            return new Event(r1, r0, r18, r19, r20, r7, r21, r112, r111, r110, r113);
        L27:
            r24 = r10;
            goto L28
        L19:
            r10 = false;
            goto L20
        L15:
            r6 = false;
            goto L16
        L11:
            r4 = false;
            goto L12
        L35:
            r0 = r0;
            r1 = r1;
            r18 = false;
            r19 = false;
            r20 = r9;
            r21 = false;
            r110 = 0;
            r111 = 0;
            r112 = -9223372036854775807L;
            r113 = 0;
            goto L37
        L5:
            r0 = false;
            goto L6
        }

        private void writeToParcel(Parcel r4) {
            r4.writeLong(this.spliceEventId);
            r4.writeByte(this.spliceEventCancelIndicator ? 1 : 0);
            r4.writeByte(this.outOfNetworkIndicator ? 1 : 0);
            r4.writeByte(this.programSpliceFlag ? 1 : 0);
            int r0 = this.componentSpliceList.size();
            r4.writeInt(r0);
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L5;
            ComponentSplice.access$200(this.componentSpliceList.get(r1), r4);
            r1 = r1 + 1;
            goto L3
        L5:
            r4.writeLong(this.utcSpliceTime);
            r4.writeByte(this.autoReturn ? 1 : 0);
            r4.writeLong(this.breakDurationUs);
            r4.writeInt(this.uniqueProgramId);
            r4.writeInt(this.availNum);
            r4.writeInt(this.availsExpected);
        }
    }

    static {
        CREATOR = new C04341();
    }

    private SpliceScheduleCommand(Parcel r5) {
        int r0 = r5.readInt();
        ArrayList r1 = new ArrayList(r0);
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1.add(Event.access$300(r5));
        r2 = r2 + 1;
        goto L3
    L5:
        this.events = Collections.unmodifiableList(r1);
    }

    /* synthetic */ SpliceScheduleCommand(Parcel r1, C04341 r2) {
        this(r1);
    }

    private SpliceScheduleCommand(List<Event> r2) {
        this.events = Collections.unmodifiableList(r2);
    }

    static SpliceScheduleCommand parseFromSection(ParsableByteArray r4) {
        int r0 = r4.readUnsignedByte();
        ArrayList r1 = new ArrayList(r0);
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L6;
        r1.add(Event.access$400(r4));
        r2 = r2 + 1;
        goto L3
    L6:
        return new SpliceScheduleCommand(r1);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r4, int r5) {
        int r0 = this.events.size();
        r4.writeInt(r0);
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        Event.access$500(this.events.get(r1), r4);
        r1 = r1 + 1;
        goto L3
    }
}
