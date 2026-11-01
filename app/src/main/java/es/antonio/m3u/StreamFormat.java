package es.antonio.m3u;

import androidx.media3.common.MimeTypes;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes3.dex */
public final class StreamFormat {
    private StreamFormat() {
    }

    public static String label(Entry r2) {
        String r0 = mime(r2);
        if (MimeTypes.APPLICATION_MPD.equals(r0) == false) goto L7;
        return "DASH";
    L7:
        if (MimeTypes.APPLICATION_M3U8.equals(r0) == false) goto L11;
        return "HLS";
    L11:
        if (MimeTypes.APPLICATION_SS.equals(r0) == false) goto L14;
        return "ISML";
    L14:
        return "Directo";
    }

    public static String mime(Entry r6) {
        if (r6.manifest != null) goto L5;
        String r0 = "";
    L7:
        if (r0.equals("mpd") == false) goto L9;
    L40:
        return MimeTypes.APPLICATION_MPD;
    L9:
        if (r0.equals("dash") == true) goto L40;
        if (r0.equals("hls") == false) goto L14;
    L39:
        return MimeTypes.APPLICATION_M3U8;
    L14:
        if (r0.equals("m3u8") == true) goto L39;
        if (r0.equals("ism") == false) goto L19;
    L38:
        return MimeTypes.APPLICATION_SS;
    L19:
        if (r0.equals("isml") == true) goto L38;
        if (r0.equals("ss") == true) goto L38;
        if (r0.equals("smoothstreaming") == true) goto L38;
        String r1 = new URI(Entry.details(r6).url).getPath().toLowerCase(Locale.ROOT);     // Catch: Exception -> L35
        if (r1.endsWith(".mpd") == false) goto L29;
        return MimeTypes.APPLICATION_MPD;
    L29:
        if (r1.endsWith(".m3u8") == false) goto L31;
        return MimeTypes.APPLICATION_M3U8;
    L31:
        if (r1.matches(".*\\.ism[l]?(?:/manifest(?:\\([^)]*\\))?)?/?") == false) goto L43;
        return MimeTypes.APPLICATION_SS;
    L43:
        return null;
    L36:
        return null;
    L5:
        r0 = r6.manifest.trim().toLowerCase(Locale.ROOT);
        goto L7
    }

    public static String summary(List<Entry> r5) {
        TreeSet r0 = new TreeSet();
        Iterator<Entry> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0.add(label(r1.next()));
        goto L4
    L6:
        StringBuilder r2 = new StringBuilder();
        Iterator r3 = r0.iterator();
    L8:
        if (r3.hasNext() == false) goto L14;
        String r4 = (String) r3.next();
        if (r2.length() <= 0) goto L12;
        r2.append(" / ");
    L12:
        r2.append(r4);
        goto L8
    L14:
        return r2.toString();
    }
}
