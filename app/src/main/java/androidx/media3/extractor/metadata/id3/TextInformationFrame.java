package androidx.media3.extractor.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class TextInformationFrame extends Id3Frame {
    public static final Parcelable.Creator<TextInformationFrame> CREATOR = null;
    public final String description;

    @Deprecated
    public final String value;
    public final ImmutableList<String> values;

    static {
        CREATOR = new C04251();
    }

    private TextInformationFrame(Parcel r4) {
        this((String) Assertions.checkNotNull(r4.readString()), r4.readString(), ImmutableList.copyOf((String[]) Assertions.checkNotNull(r4.createStringArray())));
    }

    /* synthetic */ TextInformationFrame(Parcel r1, C04251 r2) {
        this(r1);
    }

    @Deprecated
    public TextInformationFrame(String r2, String r3, String r4) {
        this(r2, r3, ImmutableList.m213of(r4));
    }

    public TextInformationFrame(String r3, String r4, List<String> r5) {
        super(r3);
        Assertions.checkArgument(!r5.isEmpty());
        this.description = r4;
        this.values = ImmutableList.copyOf(r5);
        this.value = this.values.get(0);
    }

    private static List<Integer> parseId3v2point4TimestampFrameForDate(String r7) {
        ArrayList r0 = new ArrayList();
        if (r7.length() < 10) goto L7;
        r0.add(Integer.valueOf(Integer.parseInt(r7.substring(0, 4))));     // Catch: NumberFormatException -> L14
        r0.add(Integer.valueOf(Integer.parseInt(r7.substring(5, 7))));     // Catch: NumberFormatException -> L14
        r0.add(Integer.valueOf(Integer.parseInt(r7.substring(8, 10))));     // Catch: NumberFormatException -> L14
    L13:
        return r0;
    L7:
        if (r7.length() < 7) goto L10;
        r0.add(Integer.valueOf(Integer.parseInt(r7.substring(0, 4))));     // Catch: NumberFormatException -> L14
        r0.add(Integer.valueOf(Integer.parseInt(r7.substring(5, 7))));     // Catch: NumberFormatException -> L14
        goto L13
    L10:
        if (r7.length() < 4) goto L13;
        r0.add(Integer.valueOf(Integer.parseInt(r7.substring(0, 4))));     // Catch: NumberFormatException -> L14
        goto L13
    L16:
        return new ArrayList();
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L19:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L19;
        TextInformationFrame r2 = (TextInformationFrame) r6;
        if (Util.areEqual(this.f55id, r2.f55id) == false) goto L18;
        if (Util.areEqual(this.description, r2.description) == false) goto L18;
        if (this.values.equals(r2.values) == false) goto L18;
        return true;
    L18:
        return false;
    }

    public int hashCode() {
        int r0 = ((17 * 31) + this.f55id.hashCode()) * 31;
        if (this.description == null) goto L5;
        int r2 = this.description.hashCode();
    L7:
        return ((r0 + r2) * 31) + this.values.hashCode();
    L5:
        r2 = 0;
        goto L7
    }

    @Override // androidx.media3.extractor.metadata.id3.Id3Frame, androidx.media3.common.Metadata.Entry
    public void populateMediaMetadata(MediaMetadata.Builder r7) {
        String r0 = this.f55id;
        switch(r0.hashCode()) {
            case 82815: goto L72;
            case 82878: goto L69;
            case 82897: goto L66;
            case 83253: goto L63;
            case 83254: goto L60;
            case 83255: goto L57;
            case 83341: goto L54;
            case 83378: goto L51;
            case 83536: goto L48;
            case 83552: goto L45;
            case 2567331: goto L42;
            case 2569357: goto L39;
            case 2569358: goto L36;
            case 2569891: goto L33;
            case 2570401: goto L30;
            case 2570410: goto L27;
            case 2571565: goto L24;
            case 2575251: goto L21;
            case 2581512: goto L18;
            case 2581513: goto L15;
            case 2581514: goto L12;
            case 2583398: goto L9;
            case 2590194: goto L6;
            default: goto L74;
        };
    L74:
        byte r1 = -1;
    L75:
        switch(r1) {
            case 0: goto L120;
            case 1: goto L120;
            case 2: goto L119;
            case 3: goto L119;
            case 4: goto L118;
            case 5: goto L118;
            case 6: goto L117;
            case 7: goto L117;
            case 8: goto L108;
            case 9: goto L108;
            case 10: goto L124;
            case 11: goto L124;
            case 12: goto L126;
            case 13: goto L126;
            case 14: goto L92;
            case 15: goto L86;
            case 16: goto L85;
            case 17: goto L85;
            case 18: goto L84;
            case 19: goto L84;
            case 20: goto L83;
            case 21: goto L83;
            case 22: goto L77;
            default: goto L139;
        };
    L77:
        Integer r2 = Ints.tryParse(this.values.get(0));
        if (r2 != null) goto L80;
        r7.setGenre(this.values.get(0));
        return;
    L80:
        String r3 = Id3Util.resolveV1Genre(r2.intValue());
        if (r3 == null) goto L129;
        r7.setGenre(r3);
        return;
    L129:
        return;
    L83:
        r7.setWriter(this.values.get(0));
        return;
    L84:
        r7.setConductor(this.values.get(0));
        return;
    L85:
        r7.setComposer(this.values.get(0));
        return;
    L86:
        List<Integer> r4 = parseId3v2point4TimestampFrameForDate(this.values.get(0));
        switch(r4.size()) {
            case 1: goto L91;
            case 2: goto L90;
            case 3: goto L89;
            default: goto L140;
        };
    L89:
        r7.setReleaseDay(r4.get(2));
    L90:
        r7.setReleaseMonth(r4.get(1));
    L91:
        r7.setReleaseYear(r4.get(0));
        return;
    L140:
        return;
    L92:
        List<Integer> r5 = parseId3v2point4TimestampFrameForDate(this.values.get(0));
        switch(r5.size()) {
            case 1: goto L97;
            case 2: goto L96;
            case 3: goto L95;
            default: goto L141;
        };
    L95:
        r7.setRecordingDay(r5.get(2));
    L96:
        r7.setRecordingMonth(r5.get(1));
    L97:
        r7.setRecordingYear(r5.get(0));
        return;
    L141:
        return;
    L108:
        String[] r6 = Util.split(this.values.get(0), "/");
        int r8 = Integer.parseInt(r6[0]);     // Catch: NumberFormatException -> L115
        if (r6.length <= 1) goto L112;
        Integer r9 = Integer.valueOf(Integer.parseInt(r6[1]));     // Catch: NumberFormatException -> L115
    L113:
        r7.setTrackNumber(Integer.valueOf(r8)).setTotalTrackCount(r9);     // Catch: NumberFormatException -> L115
        return;
    L112:
        r9 = null;
    L148:
        return;
    L117:
        r7.setAlbumTitle(this.values.get(0));
        return;
    L118:
        r7.setAlbumArtist(this.values.get(0));
        return;
    L119:
        r7.setArtist(this.values.get(0));
        return;
    L120:
        r7.setTitle(this.values.get(0));
        return;
    L124:
        r7.setRecordingYear(Integer.valueOf(Integer.parseInt(this.values.get(0))));     // Catch: NumberFormatException -> L106
        return;
    L146:
        return;
    L126:
        String r10 = this.values.get(0);     // Catch: StringIndexOutOfBoundsException -> L100 NumberFormatException -> L102
        r7.setRecordingMonth(Integer.valueOf(Integer.parseInt(r10.substring(2, 4)))).setRecordingDay(Integer.valueOf(Integer.parseInt(r10.substring(0, 2))));     // Catch: StringIndexOutOfBoundsException -> L100 NumberFormatException -> L102
    L142:
        return;
    L143:
        return;
    L144:
        return;
    L139:
        return;
    L6:
        if (r0.equals("TYER") == false) goto L74;
        r1 = Ascii.f92VT;
        goto L75
    L9:
        if (r0.equals("TRCK") == false) goto L74;
        r1 = 9;
        goto L75
    L12:
        if (r0.equals("TPE3") == false) goto L74;
        r1 = 19;
        goto L75
    L15:
        if (r0.equals("TPE2") == false) goto L74;
        r1 = 5;
        goto L75
    L18:
        if (r0.equals("TPE1") == false) goto L74;
        r1 = 3;
        goto L75
    L21:
        if (r0.equals("TIT2") == false) goto L74;
        r1 = 1;
        goto L75
    L24:
        if (r0.equals("TEXT") == false) goto L74;
        r1 = Ascii.NAK;
        goto L75
    L27:
        if (r0.equals("TDRL") == false) goto L74;
        r1 = Ascii.f88SI;
        goto L75
    L30:
        if (r0.equals("TDRC") == false) goto L74;
        r1 = Ascii.f89SO;
        goto L75
    L33:
        if (r0.equals("TDAT") == false) goto L74;
        r1 = Ascii.f79CR;
        goto L75
    L36:
        if (r0.equals("TCON") == false) goto L74;
        r1 = Ascii.SYN;
        goto L75
    L39:
        if (r0.equals("TCOM") == false) goto L74;
        r1 = 17;
        goto L75
    L42:
        if (r0.equals("TALB") == false) goto L74;
        r1 = 7;
        goto L75
    L45:
        if (r0.equals("TYE") == false) goto L74;
        r1 = 10;
        goto L75
    L48:
        if (r0.equals("TXT") == false) goto L74;
        r1 = Ascii.DC4;
        goto L75
    L51:
        if (r0.equals("TT2") == false) goto L74;
        r1 = 0;
        goto L75
    L54:
        if (r0.equals("TRK") == false) goto L74;
        r1 = 8;
        goto L75
    L57:
        if (r0.equals("TP3") == false) goto L74;
        r1 = Ascii.DC2;
        goto L75
    L60:
        if (r0.equals("TP2") == false) goto L74;
        r1 = 4;
        goto L75
    L63:
        if (r0.equals("TP1") == false) goto L74;
        r1 = 2;
        goto L75
    L66:
        if (r0.equals("TDA") == false) goto L74;
        r1 = Ascii.f81FF;
        goto L75
    L69:
        if (r0.equals("TCM") == false) goto L74;
        r1 = Ascii.DLE;
        goto L75
    L72:
        if (r0.equals("TAL") == false) goto L74;
        r1 = 6;
        goto L75
    }

    @Override // androidx.media3.extractor.metadata.id3.Id3Frame
    public String toString() {
        return this.f55id + ": description=" + this.description + ": values=" + this.values;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r3, int r4) {
        r3.writeString(this.f55id);
        r3.writeString(this.description);
        r3.writeStringArray((String[]) this.values.toArray(new String[0]));
    }
}
