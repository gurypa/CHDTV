package androidx.core.text.util;

import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
class FindAddress {
    private static final String HOUSE_COMPONENT = "(?:one|[0-9]+([a-z](?=[^a-z]|$)|st|nd|rd|th)?)";
    private static final String HOUSE_END = "(?=[,\"'\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)";
    private static final String HOUSE_POST_DELIM = ",\"'\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final String HOUSE_PRE_DELIM = ":,\"'\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final int MAX_ADDRESS_LINES = 5;
    private static final int MAX_ADDRESS_WORDS = 14;
    private static final int MAX_LOCATION_NAME_DISTANCE = 5;
    private static final int MIN_ADDRESS_WORDS = 4;

    /* JADX INFO: renamed from: NL */
    private static final String f17NL = "\n\u000b\f\r\u0085\u2028\u2029";

    /* JADX INFO: renamed from: SP */
    private static final String f18SP = "\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000";
    private static final String WORD_DELIM = ",*•\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final String WORD_END = "(?=[,*•\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)";

    /* JADX INFO: renamed from: WS */
    private static final String f19WS = "\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final int kMaxAddressNameWordLength = 25;
    private static final Pattern sHouseNumberRe = null;
    private static final Pattern sLocationNameRe = null;
    private static final Pattern sStateRe = null;
    private static final ZipRange[] sStateZipCodeRanges = null;
    private static final Pattern sSuffixedNumberRe = null;
    private static final Pattern sWordRe = null;
    private static final Pattern sZipCodeRe = null;

    private static class ZipRange {
        int mException1;
        int mException2;
        int mHigh;
        int mLow;

        ZipRange(int r1, int r2, int r3, int r4) {
            this.mLow = r1;
            this.mHigh = r2;
            this.mException1 = r3;
            this.mException2 = r4;
        }

        boolean matches(String r4) {
            int r0 = Integer.parseInt(r4.substring(0, 2));
            if (this.mLow > r0) goto L7;
            if (r0 > this.mHigh) goto L7;
        L10:
            return true;
        L7:
            if (r0 == this.mException1) goto L10;
            if (r0 == this.mException2) goto L10;
            return false;
        }
    }

    static {
        sStateZipCodeRanges = new ZipRange[]{new ZipRange(99, 99, -1, -1), new ZipRange(35, 36, -1, -1), new ZipRange(71, 72, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(85, 86, -1, -1), new ZipRange(90, 96, -1, -1), new ZipRange(80, 81, -1, -1), new ZipRange(6, 6, -1, -1), new ZipRange(20, 20, -1, -1), new ZipRange(19, 19, -1, -1), new ZipRange(32, 34, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(30, 31, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(50, 52, -1, -1), new ZipRange(83, 83, -1, -1), new ZipRange(60, 62, -1, -1), new ZipRange(46, 47, -1, -1), new ZipRange(66, 67, 73, -1), new ZipRange(40, 42, -1, -1), new ZipRange(70, 71, -1, -1), new ZipRange(1, 2, -1, -1), new ZipRange(20, 21, -1, -1), new ZipRange(3, 4, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(48, 49, -1, -1), new ZipRange(55, 56, -1, -1), new ZipRange(63, 65, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(38, 39, -1, -1), new ZipRange(55, 56, -1, -1), new ZipRange(27, 28, -1, -1), new ZipRange(58, 58, -1, -1), new ZipRange(68, 69, -1, -1), new ZipRange(3, 4, -1, -1), new ZipRange(7, 8, -1, -1), new ZipRange(87, 88, 86, -1), new ZipRange(88, 89, 96, -1), new ZipRange(10, 14, 0, 6), new ZipRange(43, 45, -1, -1), new ZipRange(73, 74, -1, -1), new ZipRange(97, 97, -1, -1), new ZipRange(15, 19, -1, -1), new ZipRange(6, 6, 0, 9), new ZipRange(96, 96, -1, -1), new ZipRange(2, 2, -1, -1), new ZipRange(29, 29, -1, -1), new ZipRange(57, 57, -1, -1), new ZipRange(37, 38, -1, -1), new ZipRange(75, 79, 87, 88), new ZipRange(84, 84, -1, -1), new ZipRange(22, 24, 20, -1), new ZipRange(6, 9, -1, -1), new ZipRange(5, 5, -1, -1), new ZipRange(98, 99, -1, -1), new ZipRange(53, 54, -1, -1), new ZipRange(24, 26, -1, -1), new ZipRange(82, 83, -1, -1)};
        sWordRe = Pattern.compile("[^,*•\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]+(?=[,*•\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sHouseNumberRe = Pattern.compile("(?:one|[0-9]+([a-z](?=[^a-z]|$)|st|nd|rd|th)?)(?:-(?:one|[0-9]+([a-z](?=[^a-z]|$)|st|nd|rd|th)?))*(?=[,\"'\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sStateRe = Pattern.compile("(?:(ak|alaska)|(al|alabama)|(ar|arkansas)|(as|american[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+samoa)|(az|arizona)|(ca|california)|(co|colorado)|(ct|connecticut)|(dc|district[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+of[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+columbia)|(de|delaware)|(fl|florida)|(fm|federated[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+states[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+of[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+micronesia)|(ga|georgia)|(gu|guam)|(hi|hawaii)|(ia|iowa)|(id|idaho)|(il|illinois)|(in|indiana)|(ks|kansas)|(ky|kentucky)|(la|louisiana)|(ma|massachusetts)|(md|maryland)|(me|maine)|(mh|marshall[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+islands)|(mi|michigan)|(mn|minnesota)|(mo|missouri)|(mp|northern[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+mariana[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+islands)|(ms|mississippi)|(mt|montana)|(nc|north[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+carolina)|(nd|north[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+dakota)|(ne|nebraska)|(nh|new[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+hampshire)|(nj|new[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+jersey)|(nm|new[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+mexico)|(nv|nevada)|(ny|new[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+york)|(oh|ohio)|(ok|oklahoma)|(or|oregon)|(pa|pennsylvania)|(pr|puerto[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+rico)|(pw|palau)|(ri|rhode[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+island)|(sc|south[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+carolina)|(sd|south[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+dakota)|(tn|tennessee)|(tx|texas)|(ut|utah)|(va|virginia)|(vi|virgin[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+islands)|(vt|vermont)|(wa|washington)|(wi|wisconsin)|(wv|west[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000]+virginia)|(wy|wyoming))(?=[,*•\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sLocationNameRe = Pattern.compile("(?:alley|annex|arcade|ave[.]?|avenue|alameda|bayou|beach|bend|bluffs?|bottom|boulevard|branch|bridge|brooks?|burgs?|bypass|broadway|camino|camp|canyon|cape|causeway|centers?|circles?|cliffs?|club|common|corners?|course|courts?|coves?|creek|crescent|crest|crossing|crossroad|curve|circulo|dale|dam|divide|drives?|estates?|expressway|extensions?|falls?|ferry|fields?|flats?|fords?|forest|forges?|forks?|fort|freeway|gardens?|gateway|glens?|greens?|groves?|harbors?|haven|heights|highway|hills?|hollow|inlet|islands?|isle|junctions?|keys?|knolls?|lakes?|land|landing|lane|lights?|loaf|locks?|lodge|loop|mall|manors?|meadows?|mews|mills?|mission|motorway|mount|mountains?|neck|orchard|oval|overpass|parks?|parkways?|pass|passage|path|pike|pines?|plains?|plaza|points?|ports?|prairie|privada|radial|ramp|ranch|rapids?|rd[.]?|rest|ridges?|river|roads?|route|row|rue|run|shoals?|shores?|skyway|springs?|spurs?|squares?|station|stravenue|stream|st[.]?|streets?|summit|speedway|terrace|throughway|trace|track|trafficway|trail|tunnel|turnpike|underpass|unions?|valleys?|viaduct|views?|villages?|ville|vista|walks?|wall|ways?|wells?|xing|xrd)(?=[,*•\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sSuffixedNumberRe = Pattern.compile("([0-9]+)(st|nd|rd|th)", 2);
        sZipCodeRe = Pattern.compile("(?:[0-9]{5}(?:-[0-9]{4})?)(?=[,*•\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006 \u2008\u2009\u200a \u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
    }

    private FindAddress() {
    }

    private static int attemptMatch(String r13, MatchResult r14) {
        int r0 = -1;
        int r1 = -1;
        int r2 = r14.end();
        int r3 = 1;
        boolean r4 = true;
        boolean r5 = false;
        int r6 = 1;
        String r7 = "";
        Matcher r8 = sWordRe.matcher(r13);
    L4:
        if (r2 >= r13.length()) goto L58;
        if (r8.find(r2) == false) goto L8;
        if ((r8.end() - r8.start()) > 25) goto L12;
    L14:
        if (r2 >= r8.start()) goto L20;
        int r9 = r2 + 1;
        if (f17NL.indexOf(r13.charAt(r2)) == (-1)) goto L18;
        r3 = r3 + 1;
    L18:
        r2 = r9;
        goto L14
    L20:
        if (r3 > 5) goto L58;
        r6 = r6 + 1;
        if (r6 > 14) goto L58;
        if (matchHouseNumber(r13, r2) == null) goto L34;
        if (r4 == true) goto L29;
    L32:
        if (r0 != (-1)) goto L57;
        r0 = r2;
    L57:
        r7 = r8.group(0);
        r2 = r8.end();
        goto L4
    L29:
        if (r3 <= 1) goto L32;
        return -r2;
    L34:
        r4 = false;
        if (isValidLocationName(r8.group(0)) == false) goto L37;
        r5 = true;
        goto L57
    L37:
        if (r6 != 5) goto L40;
        if (r5 == true) goto L40;
        r2 = r8.end();
    L40:
        if (r5 == false) goto L57;
        if (r6 <= 4) goto L57;
        MatchResult r10 = matchState(r13, r2);
        if (r10 == null) goto L57;
        if (r7.equals("et") == true) goto L48;
    L50:
        Matcher r11 = sWordRe.matcher(r13);
        if (r11.find(r10.end()) == true) goto L53;
        r1 = r10.end();
        goto L57
    L53:
        if (isValidZipCode(r11.group(0), r10) == false) goto L57;
        return r11.end();
    L48:
        if (r10.group(0).equals("al") == false) goto L50;
        r2 = r10.end();
        goto L58
    L12:
        return -r8.end();
    L8:
        return -r13.length();
    L58:
        if (r1 <= 0) goto L60;
        return r1;
    L60:
        if (r0 <= 0) goto L62;
        int r12 = r0;
    L64:
        return -r12;
    L62:
        r12 = r2;
        goto L64
    }

    private static boolean checkHouseNumber(String r7) {
        int r0 = 0;
        int r1 = 0;
    L4:
        if (r1 >= r7.length()) goto L10;
        if (Character.isDigit(r7.charAt(r1)) == false) goto L8;
        r0 = r0 + 1;
    L8:
        r1 = r1 + 1;
        goto L4
    L10:
        if (r0 <= 5) goto L12;
        return false;
    L12:
        Matcher r2 = sSuffixedNumberRe.matcher(r7);
        if (r2.find() == false) goto L39;
        int r3 = Integer.parseInt(r2.group(1));
        if (r3 != 0) goto L17;
        return false;
    L17:
        String r4 = r2.group(2).toLowerCase(Locale.getDefault());
        String r5 = "th";
        switch((r3 % 10)) {
            case 1: goto L34;
            case 2: goto L28;
            case 3: goto L22;
            default: goto L20;
        };
    L20:
        return r4.equals("th");
    L22:
        if ((r3 % 100) == 13) goto L26;
        r5 = "rd";
    L26:
        return r4.equals(r5);
    L28:
        if ((r3 % 100) == 12) goto L32;
        r5 = "nd";
    L32:
        return r4.equals(r5);
    L34:
        if ((r3 % 100) == 11) goto L38;
        r5 = CmcdConfiguration.KEY_STREAM_TYPE;
    L38:
        return r4.equals(r5);
    L39:
        return true;
    }

    static String findAddress(String r4) {
        Matcher r0 = sHouseNumberRe.matcher(r4);
        int r1 = 0;
    L4:
        if (r0.find(r1) == false) goto L13;
        if (checkHouseNumber(r0.group(0)) == true) goto L7;
        r1 = r0.end();
        goto L4
    L7:
        int r2 = r0.start();
        int r3 = attemptMatch(r4, r0);
        if (r3 > 0) goto L10;
        r1 = -r3;
        goto L4
    L10:
        return r4.substring(r2, r3);
    L13:
        return null;
    }

    public static boolean isValidLocationName(String r1) {
        return sLocationNameRe.matcher(r1).matches();
    }

    public static boolean isValidZipCode(String r1) {
        return sZipCodeRe.matcher(r1).matches();
    }

    public static boolean isValidZipCode(String r1, String r2) {
        return isValidZipCode(r1, matchState(r2, 0));
    }

    private static boolean isValidZipCode(String r3, MatchResult r4) {
        if (r4 != null) goto L5;
        return false;
    L5:
        int r1 = r4.groupCount();
    L6:
        if (r1 <= 0) goto L12;
        int r2 = r1 - 1;
        if (r4.group(r1) != null) goto L9;
        r1 = r2;
        goto L6
    L9:
        r1 = r2;
    L12:
        if (sZipCodeRe.matcher(r3).matches() == true) goto L14;
        return false;
    L14:
        if (sStateZipCodeRanges[r1].matches(r3) == false) goto L21;
        return true;
    L21:
        return false;
    }

    public static MatchResult matchHouseNumber(String r4, int r5) {
        if (r5 > 0) goto L5;
    L7:
        Matcher r1 = sHouseNumberRe.matcher(r4).region(r5, r4.length());
        if (r1.lookingAt() == false) goto L12;
        MatchResult r2 = r1.toMatchResult();
        if (checkHouseNumber(r2.group(0)) == false) goto L12;
        return r2;
    L12:
        return null;
    L5:
        if (HOUSE_PRE_DELIM.indexOf(r4.charAt(r5 - 1)) != (-1)) goto L7;
        return null;
    }

    public static MatchResult matchState(String r3, int r4) {
        if (r4 > 0) goto L5;
    L7:
        Matcher r1 = sStateRe.matcher(r3).region(r4, r3.length());
        if (r1.lookingAt() == true) goto L10;
        return null;
    L10:
        return r1.toMatchResult();
    L5:
        if (WORD_DELIM.indexOf(r3.charAt(r4 - 1)) != (-1)) goto L7;
        return null;
    }
}
