package androidx.core.text.util;

import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.core.net.MailTo;
import androidx.core.util.PatternsCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class LinkifyCompat {
    private static final Comparator<LinkSpec> COMPARATOR = null;
    private static final String[] EMPTY_STRING = null;

    static class Api24Impl {
        private Api24Impl() {
        }

        static void addLinks(TextView r0, Pattern r1, String r2, String[] r3, Linkify.MatchFilter r4, Linkify.TransformFilter r5) {
            Linkify.addLinks(r0, r1, r2, r3, r4, r5);
        }

        static boolean addLinks(Spannable r1, Pattern r2, String r3, String[] r4, Linkify.MatchFilter r5, Linkify.TransformFilter r6) {
            return Linkify.addLinks(r1, r2, r3, r4, r5, r6);
        }
    }

    private static class LinkSpec {
        int end;
        URLSpan frameworkAddedSpan;
        int start;
        String url;

        LinkSpec() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LinkifyMask {
    }

    static {
        EMPTY_STRING = new String[0];
        COMPARATOR = new LinkifyCompat$$ExternalSyntheticLambda0();
    }

    private LinkifyCompat() {
    }

    private static void addLinkMovementMethod(TextView r2) {
        if ((r2.getMovementMethod() instanceof LinkMovementMethod) == false) goto L5;
        return;
    L5:
        if (r2.getLinksClickable() == false) goto L9;
        r2.setMovementMethod(LinkMovementMethod.getInstance());
        return;
    }

    public static void addLinks(TextView r7, Pattern r8, String r9) {
        if (shouldAddLinksFallbackToFramework() == false) goto L6;
        Linkify.addLinks(r7, r8, r9);
        return;
    L6:
        addLinks(r7, r8, r9, null, null, null);
    }

    public static void addLinks(TextView r7, Pattern r8, String r9, Linkify.MatchFilter r10, Linkify.TransformFilter r11) {
        if (shouldAddLinksFallbackToFramework() == false) goto L6;
        Linkify.addLinks(r7, r8, r9, r10, r11);
        return;
    L6:
        addLinks(r7, r8, r9, null, r10, r11);
    }

    public static void addLinks(TextView r6, Pattern r7, String r8, String[] r9, Linkify.MatchFilter r10, Linkify.TransformFilter r11) {
        if (shouldAddLinksFallbackToFramework() == false) goto L6;
        Api24Impl.addLinks(r6, r7, r8, r9, r10, r11);
        return;
    L6:
        SpannableString r0 = SpannableString.valueOf(r6.getText());
        if (addLinks(r0, r7, r8, r9, r10, r11) == false) goto L10;
        r6.setText(r0);
        addLinkMovementMethod(r6);
        return;
    }

    public static boolean addLinks(Spannable r10, int r11) {
        if (shouldAddLinksFallbackToFramework() == false) goto L7;
        return Linkify.addLinks(r10, r11);
    L7:
        if (r11 != 0) goto L9;
        return false;
    L9:
        URLSpan[] r1 = (URLSpan[]) r10.getSpans(0, r10.length(), URLSpan.class);
        int r2 = r1.length - 1;
    L10:
        if (r2 < 0) goto L13;
        r10.removeSpan(r1[r2]);
        r2 = r2 - 1;
        goto L10
    L13:
        if ((r11 & 4) == 0) goto L15;
        Linkify.addLinks(r10, 4);
    L15:
        ArrayList r3 = new ArrayList();
        if ((r11 & 1) == 0) goto L18;
        Spannable r5 = r10;
        gatherLinks(r3, r5, PatternsCompat.AUTOLINK_WEB_URL, new String[]{"http://", "https://", "rtsp://"}, Linkify.sUrlMatchFilter, null);
    L20:
        if ((r11 & 2) == 0) goto L23;
        gatherLinks(r3, r5, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[]{MailTo.MAILTO_SCHEME}, null, null);
    L23:
        if ((r11 & 8) == 0) goto L25;
        gatherMapLinks(r3, r5);
    L25:
        pruneOverlaps(r3, r5);
        if (r3.size() != 0) goto L28;
        return false;
    L28:
        Iterator r12 = r3.iterator();
    L30:
        if (r12.hasNext() == false) goto L35;
        LinkSpec r0 = (LinkSpec) r12.next();
        if (r0.frameworkAddedSpan != null) goto L30;
        applyLink(r0.url, r0.start, r0.end, r5);
        goto L30
    L35:
        return true;
    L18:
        r5 = r10;
        goto L20
    }

    public static boolean addLinks(Spannable r7, Pattern r8, String r9) {
        if (shouldAddLinksFallbackToFramework() == false) goto L7;
        return Linkify.addLinks(r7, r8, r9);
    L7:
        return addLinks(r7, r8, r9, null, null, null);
    }

    public static boolean addLinks(Spannable r7, Pattern r8, String r9, Linkify.MatchFilter r10, Linkify.TransformFilter r11) {
        if (shouldAddLinksFallbackToFramework() == false) goto L7;
        return Linkify.addLinks(r7, r8, r9, r10, r11);
    L7:
        return addLinks(r7, r8, r9, null, r10, r11);
    }

    public static boolean addLinks(Spannable r9, Pattern r10, String r11, String[] r12, Linkify.MatchFilter r13, Linkify.TransformFilter r14) {
        if (shouldAddLinksFallbackToFramework() == true) goto L5;
        if (r11 != null) goto L9;
        r11 = "";
    L9:
        if (r12 != null) goto L11;
    L12:
        r12 = EMPTY_STRING;
    L13:
        String[] r0 = new String[r12.length + 1];
        r0[0] = r11.toLowerCase(Locale.ROOT);
        int r1 = 0;
    L15:
        if (r1 >= r12.length) goto L21;
        String r3 = r12[r1];
        int r4 = r1 + 1;
        if (r3 != null) goto L19;
        String r5 = "";
    L20:
        r0[r4] = r5;
        r1 = r1 + 1;
        goto L15
    L19:
        r5 = r3.toLowerCase(Locale.ROOT);
        goto L20
    L21:
        boolean r2 = false;
        Matcher r6 = r10.matcher(r9);
    L23:
        if (r6.find() == false) goto L31;
        int r7 = r6.start();
        int r8 = r6.end();
        String r15 = r6.group(0);
        boolean r16 = true;
        if (r13 == null) goto L27;
        r16 = r13.acceptMatch(r9, r7, r8);
    L27:
        if (r16 == false) goto L23;
        if (r15 == null) goto L23;
        applyLink(makeUrl(r15, r0, r6, r14), r7, r8, r9);
        r2 = true;
        goto L23
    L31:
        return r2;
    L11:
        if (r12.length >= 1) goto L13;
    L5:
        return Api24Impl.addLinks(r9, r10, r11, r12, r13, r14);
    }

    public static boolean addLinks(TextView r5, int r6) {
        if (shouldAddLinksFallbackToFramework() == false) goto L7;
        return Linkify.addLinks(r5, r6);
    L7:
        if (r6 != 0) goto L9;
        return false;
    L9:
        CharSequence r1 = r5.getText();
        if ((r1 instanceof Spannable) == true) goto L12;
        SpannableString r2 = SpannableString.valueOf(r1);
        if (addLinks(r2, r6) == false) goto L19;
        addLinkMovementMethod(r5);
        r5.setText(r2);
        return true;
    L19:
        return false;
    L12:
        if (addLinks((Spannable) r1, r6) == false) goto L19;
        addLinkMovementMethod(r5);
        return true;
    }

    private static void applyLink(String r2, int r3, int r4, Spannable r5) {
        r5.setSpan(new URLSpan(r2), r3, r4, 33);
    }

    private static String findAddress(String r2) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return WebView.findAddress(r2);
    L7:
        return FindAddress.findAddress(r2);
    }

    private static void gatherLinks(ArrayList<LinkSpec> r6, Spannable r7, Pattern r8, String[] r9, Linkify.MatchFilter r10, Linkify.TransformFilter r11) {
        Matcher r0 = r8.matcher(r7);
    L4:
        if (r0.find() == false) goto L12;
        int r1 = r0.start();
        int r2 = r0.end();
        String r3 = r0.group(0);
        if (r10 == null) goto L9;
        if (r10.acceptMatch(r7, r1, r2) == false) goto L4;
    L9:
        if (r3 == null) goto L4;
        LinkSpec r4 = new LinkSpec();
        r4.url = makeUrl(r3, r9, r0, r11);
        r4.start = r1;
        r4.end = r2;
        r6.add(r4);
        goto L4
    }

    private static void gatherMapLinks(ArrayList<LinkSpec> r10, Spannable r11) {
        String r0 = r11.toString();
        int r1 = 0;
    L18:
        String r2 = findAddress(r0);     // Catch: UnsupportedOperationException -> L16
        if (r2 == null) goto L29;
        int r3 = r0.indexOf(r2);     // Catch: UnsupportedOperationException -> L16
        if (r3 < 0) goto L28;
        LinkSpec r4 = new LinkSpec();     // Catch: UnsupportedOperationException -> L16
        int r6 = r3 + r2.length();     // Catch: UnsupportedOperationException -> L16
        r4.start = r1 + r3;     // Catch: UnsupportedOperationException -> L16
        r4.end = r1 + r6;     // Catch: UnsupportedOperationException -> L16
        r0 = r0.substring(r6);     // Catch: UnsupportedOperationException -> L16
        r1 = r1 + r6;
        r4.url = "geo:0,0?q=" + URLEncoder.encode(r2, "UTF-8");     // Catch: UnsupportedOperationException -> L16
        r10.add(r4);     // Catch: UnsupportedOperationException -> L16
    L28:
        return;
    L29:
        return;
    }

    static /* synthetic */ int lambda$static$0(LinkSpec r2, LinkSpec r3) {
        if (r2.start >= r3.start) goto L7;
        return -1;
    L7:
        if (r2.start <= r3.start) goto L11;
        return 1;
    L11:
        return Integer.compare(r3.end, r2.end);
    }

    private static String makeUrl(String r9, String[] r10, Matcher r11, Linkify.TransformFilter r12) {
        if (r12 == null) goto L4;
        String r0 = r12.transformUrl(r11, r9);
    L5:
        boolean r13 = false;
        int r6 = r10.length;
        int r8 = 0;
    L6:
        if (r8 >= r6) goto L13;
        String r3 = r10[r8];
        if (r0.regionMatches(true, 0, r3, 0, r3.length()) == true) goto L9;
        r8 = r8 + 1;
        goto L6
    L9:
        r13 = true;
        if (r0.regionMatches(false, 0, r3, 0, r3.length()) == true) goto L13;
        r0 = r3 + r0.substring(r3.length());
    L13:
        if (r13 == false) goto L15;
        return r0;
    L15:
        if (r10.length > 0) goto L17;
        return r0;
    L17:
        return r10[0] + r0;
    L4:
        r0 = r9;
        goto L5
    }

    private static void pruneOverlaps(ArrayList<LinkSpec> r9, Spannable r10) {
        int r2 = 0;
        URLSpan[] r0 = (URLSpan[]) r10.getSpans(0, r10.length(), URLSpan.class);
        int r1 = r0.length;
    L3:
        if (r2 >= r1) goto L5;
        URLSpan r3 = r0[r2];
        LinkSpec r4 = new LinkSpec();
        r4.frameworkAddedSpan = r3;
        r4.start = r10.getSpanStart(r3);
        r4.end = r10.getSpanEnd(r3);
        r9.add(r4);
        r2 = r2 + 1;
        goto L3
    L5:
        Collections.sort(r9, COMPARATOR);
        int r5 = r9.size();
        int r6 = 0;
    L7:
        if (r6 >= (r5 - 1)) goto L28;
        LinkSpec r7 = r9.get(r6);
        LinkSpec r8 = r9.get(r6 + 1);
        int r11 = -1;
        if (r7.start > r8.start) goto L27;
        if (r7.end <= r8.start) goto L27;
        if (r8.end > r7.end) goto L16;
        r11 = r6 + 1;
    L22:
        if (r11 == (-1)) goto L27;
        Object r12 = r9.get(r11).frameworkAddedSpan;
        if (r12 == null) goto L26;
        r10.removeSpan(r12);
    L26:
        r9.remove(r11);
        r5 = r5 - 1;
        goto L7
    L16:
        if ((r7.end - r7.start) <= (r8.end - r8.start)) goto L19;
        r11 = r6 + 1;
        goto L22
    L19:
        if ((r7.end - r7.start) >= (r8.end - r8.start)) goto L22;
        r11 = r6;
    L27:
        r6 = r6 + 1;
        goto L7
    }

    private static boolean shouldAddLinksFallbackToFramework() {
        if (Build.VERSION.SDK_INT < 28) goto L5;
        return true;
    L5:
        return false;
    }
}
