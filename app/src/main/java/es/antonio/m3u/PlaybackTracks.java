package es.antonio.m3u;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes3.dex */
final class PlaybackTracks {

    static final class Option {
        final Tracks.Group group;
        final int index;
        final String label;
        final boolean supported;

        Option(Tracks.Group r1, int r2, String r3, boolean r4) {
            this.group = r1;
            this.index = r2;
            this.label = r3;
            this.supported = r4;
        }
    }

    private PlaybackTracks() {
    }

    static TrackSelectionParameters apply(TrackSelectionParameters r4, int r5, Option r6) {
        TrackSelectionParameters.Builder r0 = r4.buildUpon().clearOverridesOfType(r5);
        if (r6 == null) goto L5;
    L7:
        boolean r1 = false;
    L8:
        TrackSelectionParameters.Builder r2 = r0.setTrackTypeDisabled(r5, r1);
        if (r6 == null) goto L16;
        if (r6.supported == false) goto L14;
        r2.setOverrideForType(new TrackSelectionOverride(r6.group.getMediaTrackGroup(), r6.index));
        goto L16
    L14:
        throw new IllegalArgumentException("Pista no compatible con este dispositivo");
    L16:
        return r2.build();
    L5:
        if (r5 != 3) goto L7;
        r1 = true;
        goto L8
    }

    static String audio(Format r4) {
        String r2 = "";
        if (r4.channelCount != 1) goto L6;
        String r0 = "Mono";
    L18:
        StringBuilder r1 = new StringBuilder().append(language(r4));
        if (r0.isEmpty() == true) goto L23;
        r2 = " · " + r0;
    L23:
        return r1.append(r2).toString();
    L6:
        if (r4.channelCount != 2) goto L9;
        r0 = "Estéreo";
        goto L18
    L9:
        if (r4.channelCount != 6) goto L12;
        r0 = "5.1";
        goto L18
    L12:
        if (r4.channelCount != 8) goto L15;
        r0 = "7.1";
        goto L18
    L15:
        if (r4.channelCount <= 0) goto L17;
        r0 = r4.channelCount + " canales";
        goto L18
    L17:
        r0 = "";
        goto L18
    }

    static int checked(List<Option> r6, TrackSelectionParameters r7, int r8) {
        if (r7.disabledTrackTypes.contains(Integer.valueOf(r8)) == false) goto L5;
        return 0;
    L5:
        int r0 = 0;
    L7:
        if (r0 >= r6.size()) goto L16;
        Option r2 = r6.get(r0);
        TrackSelectionOverride r3 = r7.overrides.get(r2.group.getMediaTrackGroup());
        if (r3 == null) goto L14;
        if (r3.trackIndices.contains(Integer.valueOf(r2.index)) == false) goto L14;
        return r0 + 1;
    L14:
        r0 = r0 + 1;
        goto L7
    L16:
        if (r8 != 3) goto L23;
        return -1;
    L23:
        return 0;
    }

    static String codec(Format r3) {
        String r0 = r3.sampleMimeType;
        if (r0 != null) goto L7;
        return "";
    L7:
        switch(r0.hashCode()) {
            case -2123537834: goto L34;
            case -1662735862: goto L31;
            case -1662541442: goto L28;
            case -53558318: goto L25;
            case 187078296: goto L22;
            case 1331836730: goto L19;
            case 1504578661: goto L16;
            case 1504891608: goto L13;
            case 1599127257: goto L10;
            default: goto L36;
        };
    L36:
        byte r1 = -1;
    L37:
        switch(r1) {
            case 0: goto L56;
            case 1: goto L54;
            case 2: goto L52;
            case 3: goto L50;
            case 4: goto L48;
            case 5: goto L46;
            case 6: goto L44;
            case 7: goto L42;
            case 8: goto L40;
            default: goto L39;
        };
    L40:
        return "Opus";
    L42:
        return "E-AC-3 / Atmos";
    L44:
        return "E-AC-3";
    L46:
        return "AC-3";
    L48:
        return "AAC";
    L50:
        return "VP9";
    L52:
        return "AV1";
    L54:
        return "H.265";
    L56:
        return "H.264";
    L39:
        return r0.substring(r0.indexOf(47) + 1).toUpperCase(Locale.ROOT);
    L10:
        if (r0.equals(MimeTypes.VIDEO_VP9) == false) goto L36;
        r1 = 3;
        goto L37
    L13:
        if (r0.equals(MimeTypes.AUDIO_OPUS) == false) goto L36;
        r1 = 8;
        goto L37
    L16:
        if (r0.equals(MimeTypes.AUDIO_E_AC3) == false) goto L36;
        r1 = 6;
        goto L37
    L19:
        if (r0.equals(MimeTypes.VIDEO_H264) == false) goto L36;
        r1 = 0;
        goto L37
    L22:
        if (r0.equals(MimeTypes.AUDIO_AC3) == false) goto L36;
        r1 = 5;
        goto L37
    L25:
        if (r0.equals(MimeTypes.AUDIO_AAC) == false) goto L36;
        r1 = 4;
        goto L37
    L28:
        if (r0.equals(MimeTypes.VIDEO_H265) == false) goto L36;
        r1 = 1;
        goto L37
    L31:
        if (r0.equals(MimeTypes.VIDEO_AV1) == false) goto L36;
        r1 = 2;
        goto L37
    L34:
        if (r0.equals(MimeTypes.AUDIO_E_AC3_JOC) == false) goto L36;
        r1 = 7;
        goto L37
    }

    static String describe(Format r11, int r12, int r13) {
        if (r12 != 2) goto L12;
        String r4 = resolution(r11);
        if (r11.width <= 0) goto L10;
        if (r11.height <= 0) goto L10;
        r4 = r4 + " · " + r11.width + "×" + r11.height;
    L10:
        if (r11.frameRate <= 0.0f) goto L28;
        r4 = r4 + " · " + String.format(Locale.getDefault(), "%.0f fps", new Object[]{Float.valueOf(r11.frameRate)});
    L28:
        String r5 = codec(r11);
        if (r5.isEmpty() == true) goto L32;
        r4 = r4 + " · " + r5;
    L32:
        if (r12 != 1) goto L37;
        if (r11.sampleRate <= 0) goto L37;
        r4 = r4 + " · " + String.format(Locale.getDefault(), "%.1f kHz", new Object[]{Float.valueOf(r11.sampleRate / 1000.0f)});
    L37:
        if (r11.bitrate <= 0) goto L44;
        StringBuilder r7 = new StringBuilder().append(r4).append(" · ");
        if (r12 != 2) goto L41;
        String r0 = String.format(Locale.getDefault(), "%.1f Mb/s", new Object[]{Float.valueOf(r11.bitrate / 1000000.0f)});
    L42:
        r4 = r7.append(r0).toString();
        goto L44
    L41:
        r0 = Math.round(r11.bitrate / 1000.0f) + " kb/s";
    L44:
        return "Pista " + r13 + " · " + r4;
    L12:
        if (r12 != 1) goto L14;
        r4 = audio(r11);
    L16:
        if (r12 != 3) goto L26;
        if ((r11.roleFlags & 64) == 0) goto L20;
    L21:
        r4 = r4 + " · Para sordos";
    L23:
        if ((r11.selectionFlags & 2) == 0) goto L26;
        r4 = r4 + " · Forzados";
        goto L26
    L20:
        if ((r11.roleFlags & 1024) == 0) goto L23;
    L26:
        if ((r11.roleFlags & 512) == 0) goto L28;
        r4 = r4 + " · Audiodescripción";
        goto L28
    L14:
        r4 = language(r11);
        goto L16
    }

    static /* synthetic */ int lambda$options$0(Option r5, Option r6) {
        Format r0 = r5.group.getTrackFormat(r5.index);
        Format r1 = r6.group.getTrackFormat(r6.index);
        int r2 = Integer.compare(r1.height, r0.height);
        if (r2 == 0) goto L6;
        return r2;
    L6:
        return Integer.compare(r1.bitrate, r0.bitrate);
    }

    static String language(Format r5) {
        String r0 = r5.language;
        if (r0 != null) goto L5;
    L10:
        String r2 = "Idioma sin indicar";
    L12:
        if (r5.label == null) goto L22;
        if (r5.label.isEmpty() == true) goto L22;
        StringBuilder r3 = new StringBuilder().append(r5.label);
        if (r2.equals("Idioma sin indicar") == false) goto L19;
        String r1 = "";
    L23:
        return r3.append(r1).toString();
    L19:
        r1 = " · " + r2;
    L22:
        return r2;
    L5:
        if (r0.isEmpty() == true) goto L10;
        if (r0.equals(C0180C.LANGUAGE_UNDETERMINED) == true) goto L10;
        r2 = Locale.forLanguageTag(r0.replace('_', '-')).getDisplayLanguage(new Locale("es"));
        goto L12
    }

    static List<Option> options(Tracks r10, int r11) {
        ArrayList r0 = new ArrayList();
        UnmodifiableIterator<Tracks.Group> r1 = r10.getGroups().iterator();
    L4:
        if (r1.hasNext() == false) goto L16;
        Tracks.Group r2 = r1.next();
        if (r2.getType() != r11) goto L4;
        int r3 = 0;
    L9:
        if (r3 >= r2.length) goto L4;
        Format r4 = r2.getTrackFormat(r3);
        boolean r5 = r2.isTrackSupported(r3);
        String r6 = describe(r4, r11, r0.size() + 1);
        StringBuilder r8 = new StringBuilder().append(r6);
        if (r5 == false) goto L13;
        String r9 = "";
    L14:
        r0.add(new Option(r2, r3, r8.append(r9).toString(), r5));
        r3 = r3 + 1;
        goto L9
    L13:
        r9 = " · No compatible";
        goto L14
    L16:
        if (r11 != 2) goto L18;
        Collections.sort(r0, new PlaybackTracks$$ExternalSyntheticLambda0());
    L18:
        return r0;
    }

    static String resolution(Format r2) {
        if (r2.height <= 0) goto L7;
        StringBuilder r0 = new StringBuilder().append(r2.height);
        String r1 = TtmlNode.TAG_P;
    L11:
        return r0.append(r1).toString();
    L7:
        if (r2.width <= 0) goto L9;
        r0 = new StringBuilder().append(r2.width);
        r1 = " px";
        goto L11
    L9:
        return "Resolución sin indicar";
    }
}
