package androidx.media3.p002ui;

import android.content.res.Resources;
import android.text.TextUtils;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class DefaultTrackNameProvider implements TrackNameProvider {
    private final Resources resources;

    public DefaultTrackNameProvider(Resources r2) {
        this.resources = (Resources) Assertions.checkNotNull(r2);
    }

    private String buildAudioChannelString(Format r4) {
        int r0 = r4.channelCount;
        if (r0 != (-1)) goto L5;
        return "";
    L5:
        if (r0 < 1) goto L20;
        switch(r0) {
            case 1: goto L17;
            case 2: goto L15;
            case 3: goto L9;
            case 4: goto L9;
            case 5: goto L9;
            case 6: goto L13;
            case 7: goto L13;
            case 8: goto L11;
            default: goto L9;
        };
    L9:
        return this.resources.getString(C0467R.string.exo_track_surround);
    L11:
        return this.resources.getString(C0467R.string.exo_track_surround_7_point_1);
    L13:
        return this.resources.getString(C0467R.string.exo_track_surround_5_point_1);
    L15:
        return this.resources.getString(C0467R.string.exo_track_stereo);
    L17:
        return this.resources.getString(C0467R.string.exo_track_mono);
    L20:
        return "";
    }

    private String buildBitrateString(Format r7) {
        int r0 = r7.bitrate;
        if (r0 != (-1)) goto L6;
        return "";
    L6:
        return this.resources.getString(C0467R.string.exo_track_bitrate, new Object[]{Float.valueOf(r0 / 1000000.0f)});
    }

    private String buildLabelString(Format r2) {
        if (TextUtils.isEmpty(r2.label) == false) goto L6;
        return "";
    L6:
        return r2.label;
    }

    private String buildLanguageOrLabelString(Format r3) {
        String r0 = joinWithSeparator(new String[]{buildLanguageString(r3), buildRoleString(r3)});
        if (TextUtils.isEmpty(r0) == false) goto L6;
        return buildLabelString(r3);
    L6:
        return r0;
    }

    private String buildLanguageString(Format r8) {
        String r0 = r8.language;
        if (TextUtils.isEmpty(r0) == false) goto L5;
    L15:
        return "";
    L5:
        if (C0180C.LANGUAGE_UNDETERMINED.equals(r0) == true) goto L15;
        Locale r1 = Locale.forLanguageTag(r0);
        Locale r3 = Util.getDefaultDisplayLocale();
        String r4 = r1.getDisplayName(r3);
        if (TextUtils.isEmpty(r4) == false) goto L16;
        return "";
    L16:
        int r2 = r4.offsetByCodePoints(0, 1);     // Catch: IndexOutOfBoundsException -> L13
        return r4.substring(0, r2).toUpperCase(r3) + r4.substring(r2);
    L14:
        return r4;
    }

    private String buildResolutionString(Format r9) {
        int r0 = r9.width;
        int r1 = r9.height;
        if (r0 == (-1)) goto L7;
        if (r1 == (-1)) goto L7;
        return this.resources.getString(C0467R.string.exo_track_resolution, new Object[]{Integer.valueOf(r0), Integer.valueOf(r1)});
    L7:
        return "";
    }

    private String buildRoleString(Format r4) {
        String r0 = "";
        if ((r4.roleFlags & 2) == 0) goto L6;
        r0 = this.resources.getString(C0467R.string.exo_track_role_alternate);
    L6:
        if ((r4.roleFlags & 4) == 0) goto L9;
        r0 = joinWithSeparator(new String[]{r0, this.resources.getString(C0467R.string.exo_track_role_supplementary)});
    L9:
        if ((r4.roleFlags & 8) == 0) goto L12;
        r0 = joinWithSeparator(new String[]{r0, this.resources.getString(C0467R.string.exo_track_role_commentary)});
    L12:
        if ((r4.roleFlags & 1088) != 0) goto L14;
        return r0;
    L14:
        return joinWithSeparator(new String[]{r0, this.resources.getString(C0467R.string.exo_track_role_closed_captions)});
    }

    private static int inferPrimaryTrackType(Format r5) {
        int r0 = MimeTypes.getTrackType(r5.sampleMimeType);
        if (r0 == (-1)) goto L6;
        return r0;
    L6:
        if (MimeTypes.getVideoMediaMimeType(r5.codecs) == null) goto L9;
        return 2;
    L9:
        if (MimeTypes.getAudioMediaMimeType(r5.codecs) == null) goto L12;
        return 1;
    L12:
        if (r5.width == (-1)) goto L14;
    L23:
        return 2;
    L14:
        if (r5.height != (-1)) goto L23;
        if (r5.channelCount == (-1)) goto L19;
    L22:
        return 1;
    L19:
        if (r5.sampleRate != (-1)) goto L22;
        return -1;
    }

    private String joinWithSeparator(String... r10) {
        String r0 = "";
        int r1 = r10.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L11;
        String r4 = r10[r3];
        if (r4.length() <= 0) goto L10;
        if (TextUtils.isEmpty(r0) == false) goto L9;
        r0 = r4;
        goto L10
    L9:
        r0 = this.resources.getString(C0467R.string.exo_item_list, new Object[]{r0, r4});
    L10:
        r3 = r3 + 1;
        goto L3
    L11:
        return r0;
    }

    @Override // androidx.media3.p002ui.TrackNameProvider
    public String getTrackName(Format r8) {
        int r0 = inferPrimaryTrackType(r8);
        if (r0 != 2) goto L5;
        String r1 = joinWithSeparator(new String[]{buildRoleString(r8), buildResolutionString(r8), buildBitrateString(r8)});
    L9:
        if (r1.length() == 0) goto L11;
        return r1;
    L11:
        String r3 = r8.language;
        if (r3 == null) goto L18;
        if (r3.trim().isEmpty() == true) goto L18;
        return this.resources.getString(C0467R.string.exo_track_unknown_name, new Object[]{r3});
    L18:
        return this.resources.getString(C0467R.string.exo_track_unknown);
    L5:
        if (r0 != 1) goto L7;
        r1 = joinWithSeparator(new String[]{buildLanguageOrLabelString(r8), buildAudioChannelString(r8), buildBitrateString(r8)});
        goto L9
    L7:
        r1 = buildLanguageOrLabelString(r8);
        goto L9
    }
}
