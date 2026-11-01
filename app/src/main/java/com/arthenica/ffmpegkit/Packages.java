package com.arthenica.ffmpegkit;

import androidx.media3.common.MimeTypes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Packages {
    private static final List<String> supportedExternalLibraries = null;

    static {
        supportedExternalLibraries = new ArrayList();
        supportedExternalLibraries.add("dav1d");
        supportedExternalLibraries.add("fontconfig");
        supportedExternalLibraries.add("freetype");
        supportedExternalLibraries.add("fribidi");
        supportedExternalLibraries.add("gmp");
        supportedExternalLibraries.add("gnutls");
        supportedExternalLibraries.add("kvazaar");
        supportedExternalLibraries.add("mp3lame");
        supportedExternalLibraries.add("libass");
        supportedExternalLibraries.add("iconv");
        supportedExternalLibraries.add("libilbc");
        supportedExternalLibraries.add("libtheora");
        supportedExternalLibraries.add("libvidstab");
        supportedExternalLibraries.add("libvorbis");
        supportedExternalLibraries.add("libvpx");
        supportedExternalLibraries.add("libwebp");
        supportedExternalLibraries.add("libxml2");
        supportedExternalLibraries.add("opencore-amr");
        supportedExternalLibraries.add("openh264");
        supportedExternalLibraries.add("openssl");
        supportedExternalLibraries.add("opus");
        supportedExternalLibraries.add("rubberband");
        supportedExternalLibraries.add("sdl2");
        supportedExternalLibraries.add("shine");
        supportedExternalLibraries.add("snappy");
        supportedExternalLibraries.add("soxr");
        supportedExternalLibraries.add("speex");
        supportedExternalLibraries.add("srt");
        supportedExternalLibraries.add("tesseract");
        supportedExternalLibraries.add("twolame");
        supportedExternalLibraries.add("x264");
        supportedExternalLibraries.add("x265");
        supportedExternalLibraries.add("xvid");
        supportedExternalLibraries.add("zimg");
    }

    public Packages() {
    }

    public static List<String> getExternalLibraries() {
        String r0 = AbiDetect.getNativeBuildConf();
        ArrayList r1 = new ArrayList();
        Iterator<String> r2 = supportedExternalLibraries.iterator();
    L4:
        if (r2.hasNext() == false) goto L11;
        String r3 = r2.next();
        if (r0.contains("enable-" + r3) == true) goto L9;
        if (r0.contains("enable-lib" + r3) == false) goto L4;
    L9:
        r1.add(r3);
        goto L4
    L11:
        Collections.sort(r1);
        return r1;
    }

    public static String getPackageName() {
        List<String> r0 = getExternalLibraries();
        boolean r2 = r0.contains("speex");
        boolean r4 = r0.contains("fribidi");
        boolean r6 = r0.contains("gnutls");
        boolean r8 = r0.contains("xvid");
        boolean r9 = false;
        boolean r10 = false;
        boolean r11 = false;
        boolean r12 = false;
        boolean r13 = false;
        boolean r14 = false;
        boolean r15 = false;
        if (r2 == false) goto L8;
        if (r4 == false) goto L8;
        if (r8 == false) goto L7;
        r15 = true;
    L18:
        boolean r20 = r9;
        boolean r21 = r10;
        boolean r22 = r11;
        boolean r23 = r12;
        boolean r24 = r13;
        boolean r25 = r14;
        if (r15 == false) goto L80;
        if (r0.contains("dav1d") == true) goto L23;
    L78:
        return "custom";
    L23:
        if (r0.contains("fontconfig") == false) goto L78;
        if (r0.contains("freetype") == false) goto L78;
        if (r0.contains("fribidi") == false) goto L78;
        if (r0.contains("gmp") == false) goto L78;
        if (r0.contains("gnutls") == false) goto L78;
        if (r0.contains("kvazaar") == false) goto L78;
        if (r0.contains("mp3lame") == false) goto L78;
        if (r0.contains("libass") == false) goto L78;
        if (r0.contains("iconv") == false) goto L78;
        if (r0.contains("libilbc") == false) goto L78;
        if (r0.contains("libtheora") == false) goto L78;
        if (r0.contains("libvidstab") == false) goto L78;
        if (r0.contains("libvorbis") == false) goto L78;
        if (r0.contains("libvpx") == false) goto L78;
        if (r0.contains("libwebp") == false) goto L78;
        if (r0.contains("libxml2") == false) goto L78;
        if (r0.contains("opencore-amr") == false) goto L78;
        if (r0.contains("opus") == false) goto L78;
        if (r0.contains("shine") == false) goto L78;
        if (r0.contains("snappy") == false) goto L78;
        if (r0.contains("soxr") == false) goto L78;
        if (r0.contains("speex") == false) goto L78;
        if (r0.contains("twolame") == false) goto L78;
        if (r0.contains("x264") == false) goto L78;
        if (r0.contains("x265") == false) goto L78;
        if (r0.contains("xvid") == false) goto L78;
        if (r0.contains("zimg") == false) goto L78;
        return "full-gpl";
    L80:
        if (r25 == false) goto L133;
        if (r0.contains("dav1d") == true) goto L84;
    L131:
        return "custom";
    L84:
        if (r0.contains("fontconfig") == false) goto L131;
        if (r0.contains("freetype") == false) goto L131;
        if (r0.contains("fribidi") == false) goto L131;
        if (r0.contains("gmp") == false) goto L131;
        if (r0.contains("gnutls") == false) goto L131;
        if (r0.contains("kvazaar") == false) goto L131;
        if (r0.contains("mp3lame") == false) goto L131;
        if (r0.contains("libass") == false) goto L131;
        if (r0.contains("iconv") == false) goto L131;
        if (r0.contains("libilbc") == false) goto L131;
        if (r0.contains("libtheora") == false) goto L131;
        if (r0.contains("libvorbis") == false) goto L131;
        if (r0.contains("libvpx") == false) goto L131;
        if (r0.contains("libwebp") == false) goto L131;
        if (r0.contains("libxml2") == false) goto L131;
        if (r0.contains("opencore-amr") == false) goto L131;
        if (r0.contains("opus") == false) goto L131;
        if (r0.contains("shine") == false) goto L131;
        if (r0.contains("snappy") == false) goto L131;
        if (r0.contains("soxr") == false) goto L131;
        if (r0.contains("speex") == false) goto L131;
        if (r0.contains("twolame") == false) goto L131;
        if (r0.contains("zimg") == false) goto L131;
        return "full";
    L133:
        if (r24 == true) goto L135;
        if (r23 == true) goto L163;
        if (r22 == false) goto L200;
        if (r0.contains("gmp") == true) goto L187;
    L198:
        return "custom";
    L187:
        if (r0.contains("gnutls") == false) goto L198;
        if (r0.contains("libvidstab") == false) goto L198;
        if (r0.contains("x264") == false) goto L198;
        if (r0.contains("x265") == false) goto L198;
        if (r0.contains("xvid") == false) goto L198;
        return "https-gpl";
    L200:
        if (r21 == true) goto L202;
        if (r20 == false) goto L221;
        if (r0.contains("libvidstab") == true) goto L212;
    L219:
        return "custom";
    L212:
        if (r0.contains("x264") == false) goto L219;
        if (r0.contains("x265") == false) goto L219;
        if (r0.contains("xvid") == false) goto L219;
        return "min-gpl";
    L221:
        if (r0.size() != 0) goto L224;
        return "min";
    L224:
        return "custom";
    L202:
        if (r0.contains("gmp") == true) goto L204;
    L207:
        return "custom";
    L204:
        if (r0.contains("gnutls") == false) goto L207;
        return "https";
    L163:
        if (r0.contains("mp3lame") == true) goto L165;
    L182:
        return "custom";
    L165:
        if (r0.contains("libilbc") == false) goto L182;
        if (r0.contains("libvorbis") == false) goto L182;
        if (r0.contains("opencore-amr") == false) goto L182;
        if (r0.contains("opus") == false) goto L182;
        if (r0.contains("shine") == false) goto L182;
        if (r0.contains("soxr") == false) goto L182;
        if (r0.contains("speex") == false) goto L182;
        if (r0.contains("twolame") == false) goto L182;
        return MimeTypes.BASE_TYPE_AUDIO;
    L135:
        if (r0.contains("dav1d") == true) goto L137;
    L160:
        return "custom";
    L137:
        if (r0.contains("fontconfig") == false) goto L160;
        if (r0.contains("freetype") == false) goto L160;
        if (r0.contains("fribidi") == false) goto L160;
        if (r0.contains("kvazaar") == false) goto L160;
        if (r0.contains("libass") == false) goto L160;
        if (r0.contains("iconv") == false) goto L160;
        if (r0.contains("libtheora") == false) goto L160;
        if (r0.contains("libvpx") == false) goto L160;
        if (r0.contains("libwebp") == false) goto L160;
        if (r0.contains("snappy") == false) goto L160;
        if (r0.contains("zimg") == false) goto L160;
        return MimeTypes.BASE_TYPE_VIDEO;
    L7:
        r14 = true;
    L8:
        if (r2 == false) goto L10;
        r12 = true;
        goto L18
    L10:
        if (r4 == false) goto L12;
        r13 = true;
        goto L18
    L12:
        if (r8 == false) goto L16;
        if (r6 == false) goto L15;
        r11 = true;
        goto L18
    L15:
        r9 = true;
        goto L18
    L16:
        if (r6 == false) goto L18;
        r10 = true;
        goto L18
    }
}
