package androidx.media3.exoplayer.upstream.experimental;

import android.content.Context;
import android.os.Handler;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.NetworkTypeObserver;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.audio.SilenceSkippingAudioProcessor;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.SignedBytes;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.p005io.encoding.Base64;

/* JADX INFO: loaded from: classes.dex */
public final class ExperimentalBandwidthMeter implements BandwidthMeter, TransferListener {
    private static final int COUNTRY_GROUP_INDEX_2G = 1;
    private static final int COUNTRY_GROUP_INDEX_3G = 2;
    private static final int COUNTRY_GROUP_INDEX_4G = 3;
    private static final int COUNTRY_GROUP_INDEX_5G_NSA = 4;
    private static final int COUNTRY_GROUP_INDEX_5G_SA = 5;
    private static final int COUNTRY_GROUP_INDEX_WIFI = 0;
    public static final long DEFAULT_INITIAL_BITRATE_ESTIMATE = 1000000;
    public static final ImmutableList<Long> DEFAULT_INITIAL_BITRATE_ESTIMATES_2G = null;
    public static final ImmutableList<Long> DEFAULT_INITIAL_BITRATE_ESTIMATES_3G = null;
    public static final ImmutableList<Long> DEFAULT_INITIAL_BITRATE_ESTIMATES_4G = null;
    public static final ImmutableList<Long> DEFAULT_INITIAL_BITRATE_ESTIMATES_5G_NSA = null;
    public static final ImmutableList<Long> DEFAULT_INITIAL_BITRATE_ESTIMATES_5G_SA = null;
    public static final ImmutableList<Long> DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI = null;
    public static final float DEFAULT_TIME_TO_FIRST_BYTE_PERCENTILE = 0.5f;
    public static final int DEFAULT_TIME_TO_FIRST_BYTE_SAMPLES = 20;
    private final BandwidthEstimator bandwidthEstimator;
    private long initialBitrateEstimate;
    private final ImmutableMap<Integer, Long> initialBitrateEstimates;
    private int networkType;
    private int networkTypeOverride;
    private boolean networkTypeOverrideSet;
    private final boolean resetOnNetworkTypeChange;
    private final TimeToFirstByteEstimator timeToFirstByteEstimator;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.upstream.experimental.ExperimentalBandwidthMeter$1 */
    static /* synthetic */ class C03741 {
    }

    public static final class Builder {
        private BandwidthEstimator bandwidthEstimator;
        private final Context context;
        private Map<Integer, Long> initialBitrateEstimates;
        private boolean resetOnNetworkTypeChange;
        private TimeToFirstByteEstimator timeToFirstByteEstimator;

        public Builder(Context r4) {
            this.context = r4.getApplicationContext();
            this.initialBitrateEstimates = getInitialBitrateEstimatesForCountry(Util.getCountryCode(r4));
            this.timeToFirstByteEstimator = new PercentileTimeToFirstByteEstimator(20, 0.5f);
            this.bandwidthEstimator = new SplitParallelSampleBandwidthEstimator.Builder().build();
            this.resetOnNetworkTypeChange = true;
        }

        private static Map<Integer, Long> getInitialBitrateEstimatesForCountry(String r8) {
            int[] r0 = ExperimentalBandwidthMeter.access$100(r8);
            HashMap r1 = new HashMap(8);
            r1.put(0, 1000000L);
            r1.put(2, ExperimentalBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI.get(r0[0]));
            r1.put(3, ExperimentalBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_2G.get(r0[1]));
            r1.put(4, ExperimentalBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_3G.get(r0[2]));
            r1.put(5, ExperimentalBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_4G.get(r0[3]));
            r1.put(10, ExperimentalBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_5G_NSA.get(r0[4]));
            r1.put(9, ExperimentalBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_5G_SA.get(r0[5]));
            r1.put(7, ExperimentalBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI.get(r0[0]));
            return r1;
        }

        public ExperimentalBandwidthMeter build() {
            return new ExperimentalBandwidthMeter(this.context, this.initialBitrateEstimates, this.timeToFirstByteEstimator, this.bandwidthEstimator, this.resetOnNetworkTypeChange, null);
        }

        public Builder setBandwidthEstimator(BandwidthEstimator r1) {
            this.bandwidthEstimator = r1;
            return this;
        }

        public Builder setInitialBitrateEstimate(int r4, long r5) {
            this.initialBitrateEstimates.put(Integer.valueOf(r4), Long.valueOf(r5));
            return this;
        }

        public Builder setInitialBitrateEstimate(long r4) {
            Iterator<Integer> r0 = this.initialBitrateEstimates.keySet().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            setInitialBitrateEstimate(r0.next().intValue(), r4);
            goto L4
        L6:
            return this;
        }

        public Builder setInitialBitrateEstimate(String r2) {
            this.initialBitrateEstimates = getInitialBitrateEstimatesForCountry(Ascii.toUpperCase(r2));
            return this;
        }

        public Builder setResetOnNetworkTypeChange(boolean r1) {
            this.resetOnNetworkTypeChange = r1;
            return this;
        }

        public Builder setTimeToFirstByteEstimator(TimeToFirstByteEstimator r1) {
            this.timeToFirstByteEstimator = r1;
            return this;
        }
    }

    public static /* synthetic */ void $r8$lambda$YrEKJbnnl_09BSuKbNym0QzEVoI(ExperimentalBandwidthMeter r0, int r1) {
        r0.onNetworkTypeChanged(r1);
    }

    static {
        DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI = ImmutableList.m217of(4300000L, 3200000L, 2400000L, 1700000L, 860000L);
        DEFAULT_INITIAL_BITRATE_ESTIMATES_2G = ImmutableList.m217of(1500000L, 980000L, 750000L, 520000L, 290000L);
        Long r0 = Long.valueOf(SilenceSkippingAudioProcessor.DEFAULT_MAX_SILENCE_TO_KEEP_DURATION_US);
        DEFAULT_INITIAL_BITRATE_ESTIMATES_3G = ImmutableList.m217of(r0, 1300000L, 1000000L, 860000L, 610000L);
        DEFAULT_INITIAL_BITRATE_ESTIMATES_4G = ImmutableList.m217of(2500000L, 1700000L, 1200000L, 970000L, 680000L);
        DEFAULT_INITIAL_BITRATE_ESTIMATES_5G_NSA = ImmutableList.m217of(4700000L, 2800000L, 2100000L, 1700000L, 980000L);
        DEFAULT_INITIAL_BITRATE_ESTIMATES_5G_SA = ImmutableList.m217of(2700000L, r0, 1600000L, 1300000L, 1000000L);
    }

    private ExperimentalBandwidthMeter(Context r4, Map<Integer, Long> r5, TimeToFirstByteEstimator r6, BandwidthEstimator r7, boolean r8) {
        this.initialBitrateEstimates = ImmutableMap.copyOf(r5);
        this.timeToFirstByteEstimator = r6;
        this.bandwidthEstimator = r7;
        this.resetOnNetworkTypeChange = r8;
        NetworkTypeObserver r0 = NetworkTypeObserver.getInstance(r4);
        this.networkType = r0.getNetworkType();
        this.initialBitrateEstimate = getInitialBitrateEstimateForNetworkType(this.networkType);
        r0.register(new ExperimentalBandwidthMeter$$ExternalSyntheticLambda0(this));
    }

    /* synthetic */ ExperimentalBandwidthMeter(Context r1, Map r2, TimeToFirstByteEstimator r3, BandwidthEstimator r4, boolean r5, C03741 r6) {
        this(r1, r2, r3, r4, r5);
    }

    static /* synthetic */ int[] access$100(String r1) {
        return getInitialBitrateCountryGroupAssignment(r1);
    }

    private static int[] getInitialBitrateCountryGroupAssignment(String r2) {
        switch(r2.hashCode()) {
            case 2083: goto L720;
            case 2084: goto L717;
            case 2085: goto L714;
            case 2086: goto L711;
            case 2088: goto L708;
            case 2091: goto L705;
            case 2092: goto L702;
            case 2094: goto L699;
            case 2096: goto L696;
            case 2097: goto L693;
            case 2098: goto L690;
            case 2099: goto L687;
            case 2100: goto L684;
            case 2102: goto L681;
            case 2103: goto L678;
            case 2105: goto L675;
            case 2111: goto L672;
            case 2112: goto L669;
            case 2114: goto L666;
            case 2115: goto L663;
            case 2116: goto L660;
            case 2117: goto L657;
            case 2118: goto L654;
            case 2119: goto L651;
            case 2120: goto L648;
            case 2122: goto L645;
            case 2123: goto L642;
            case 2124: goto L639;
            case 2125: goto L636;
            case 2127: goto L633;
            case 2128: goto L630;
            case 2129: goto L627;
            case 2130: goto L624;
            case 2133: goto L621;
            case 2135: goto L618;
            case 2136: goto L615;
            case 2142: goto L612;
            case 2145: goto L609;
            case 2147: goto L606;
            case 2148: goto L603;
            case 2149: goto L600;
            case 2150: goto L597;
            case 2152: goto L594;
            case 2153: goto L591;
            case 2154: goto L588;
            case 2155: goto L585;
            case 2156: goto L582;
            case 2159: goto L579;
            case 2162: goto L576;
            case 2163: goto L573;
            case 2164: goto L570;
            case 2165: goto L567;
            case 2166: goto L564;
            case 2167: goto L561;
            case 2177: goto L558;
            case 2182: goto L555;
            case 2183: goto L552;
            case 2185: goto L549;
            case 2187: goto L546;
            case 2198: goto L543;
            case 2206: goto L540;
            case 2208: goto L537;
            case 2210: goto L534;
            case 2221: goto L531;
            case 2222: goto L528;
            case 2223: goto L525;
            case 2243: goto L522;
            case 2244: goto L519;
            case 2245: goto L516;
            case 2247: goto L513;
            case 2249: goto L510;
            case 2252: goto L507;
            case 2266: goto L504;
            case 2267: goto L501;
            case 2269: goto L498;
            case 2270: goto L495;
            case 2271: goto L492;
            case 2272: goto L489;
            case 2273: goto L486;
            case 2274: goto L483;
            case 2277: goto L480;
            case 2278: goto L477;
            case 2279: goto L474;
            case 2281: goto L471;
            case 2282: goto L468;
            case 2283: goto L465;
            case 2285: goto L462;
            case 2286: goto L459;
            case 2288: goto L456;
            case 2290: goto L453;
            case 2307: goto L450;
            case 2314: goto L447;
            case 2316: goto L444;
            case 2317: goto L441;
            case 2331: goto L438;
            case 2332: goto L435;
            case 2339: goto L432;
            case 2340: goto L429;
            case 2341: goto L426;
            case 2342: goto L423;
            case 2344: goto L420;
            case 2345: goto L417;
            case 2346: goto L414;
            case 2347: goto L411;
            case 2363: goto L408;
            case 2371: goto L405;
            case 2373: goto L402;
            case 2374: goto L399;
            case 2394: goto L396;
            case 2396: goto L393;
            case 2397: goto L390;
            case 2398: goto L387;
            case 2402: goto L384;
            case 2403: goto L381;
            case 2407: goto L378;
            case 2412: goto L375;
            case 2414: goto L372;
            case 2415: goto L369;
            case 2421: goto L366;
            case 2422: goto L363;
            case 2423: goto L360;
            case 2429: goto L357;
            case 2431: goto L354;
            case 2438: goto L351;
            case 2439: goto L348;
            case 2440: goto L345;
            case 2441: goto L342;
            case 2442: goto L339;
            case 2445: goto L336;
            case 2452: goto L333;
            case 2454: goto L330;
            case 2455: goto L327;
            case 2456: goto L324;
            case 2457: goto L321;
            case 2458: goto L318;
            case 2459: goto L315;
            case 2462: goto L312;
            case 2463: goto L309;
            case 2464: goto L306;
            case 2465: goto L303;
            case 2466: goto L300;
            case 2467: goto L297;
            case 2468: goto L294;
            case 2469: goto L291;
            case 2470: goto L288;
            case 2471: goto L285;
            case 2472: goto L282;
            case 2473: goto L279;
            case 2474: goto L276;
            case 2475: goto L273;
            case 2476: goto L270;
            case 2477: goto L267;
            case 2483: goto L264;
            case 2485: goto L261;
            case 2487: goto L258;
            case 2488: goto L255;
            case 2489: goto L252;
            case 2491: goto L249;
            case 2494: goto L246;
            case 2497: goto L243;
            case 2498: goto L240;
            case 2500: goto L237;
            case 2503: goto L234;
            case 2508: goto L231;
            case 2526: goto L228;
            case 2545: goto L225;
            case 2549: goto L222;
            case 2550: goto L219;
            case 2551: goto L216;
            case 2552: goto L213;
            case 2555: goto L210;
            case 2556: goto L207;
            case 2557: goto L204;
            case 2562: goto L201;
            case 2563: goto L198;
            case 2564: goto L195;
            case 2567: goto L192;
            case 2569: goto L189;
            case 2576: goto L186;
            case 2611: goto L183;
            case 2621: goto L180;
            case 2625: goto L177;
            case 2627: goto L174;
            case 2629: goto L171;
            case 2638: goto L168;
            case 2639: goto L165;
            case 2640: goto L162;
            case 2641: goto L159;
            case 2642: goto L156;
            case 2644: goto L153;
            case 2645: goto L150;
            case 2646: goto L147;
            case 2647: goto L144;
            case 2648: goto L141;
            case 2649: goto L138;
            case 2650: goto L135;
            case 2651: goto L132;
            case 2652: goto L129;
            case 2655: goto L126;
            case 2656: goto L123;
            case 2657: goto L120;
            case 2659: goto L117;
            case 2661: goto L114;
            case 2662: goto L111;
            case 2663: goto L108;
            case 2671: goto L105;
            case 2672: goto L102;
            case 2675: goto L99;
            case 2676: goto L96;
            case 2678: goto L93;
            case 2680: goto L90;
            case 2681: goto L87;
            case 2682: goto L84;
            case 2683: goto L81;
            case 2686: goto L78;
            case 2688: goto L75;
            case 2690: goto L72;
            case 2691: goto L69;
            case 2694: goto L66;
            case 2700: goto L63;
            case 2706: goto L60;
            case 2718: goto L57;
            case 2724: goto L54;
            case 2725: goto L51;
            case 2731: goto L48;
            case 2733: goto L45;
            case 2735: goto L42;
            case 2737: goto L39;
            case 2739: goto L36;
            case 2744: goto L33;
            case 2751: goto L30;
            case 2767: goto L27;
            case 2780: goto L24;
            case 2803: goto L21;
            case 2828: goto L18;
            case 2843: goto L15;
            case 2855: goto L12;
            case 2867: goto L9;
            case 2877: goto L6;
            default: goto L722;
        };
    L722:
        byte r0 = -1;
    L723:
        switch(r0) {
            case 0: goto L1055;
            case 1: goto L1053;
            case 2: goto L1051;
            case 3: goto L1049;
            case 4: goto L1047;
            case 5: goto L1045;
            case 6: goto L1043;
            case 7: goto L1041;
            case 8: goto L1039;
            case 9: goto L1037;
            case 10: goto L1035;
            case 11: goto L1033;
            case 12: goto L1031;
            case 13: goto L1029;
            case 14: goto L1027;
            case 15: goto L1025;
            case 16: goto L1023;
            case 17: goto L1021;
            case 18: goto L1019;
            case 19: goto L1017;
            case 20: goto L1015;
            case 21: goto L1013;
            case 22: goto L1013;
            case 23: goto L1011;
            case 24: goto L1011;
            case 25: goto L1009;
            case 26: goto L1007;
            case 27: goto L1005;
            case 28: goto L1003;
            case 29: goto L1001;
            case 30: goto L999;
            case 31: goto L997;
            case 32: goto L995;
            case 33: goto L993;
            case 34: goto L991;
            case 35: goto L989;
            case 36: goto L987;
            case 37: goto L985;
            case 38: goto L983;
            case 39: goto L981;
            case 40: goto L979;
            case 41: goto L977;
            case 42: goto L975;
            case 43: goto L973;
            case 44: goto L971;
            case 45: goto L969;
            case 46: goto L967;
            case 47: goto L965;
            case 48: goto L963;
            case 49: goto L961;
            case 50: goto L959;
            case 51: goto L957;
            case 52: goto L955;
            case 53: goto L953;
            case 54: goto L951;
            case 55: goto L949;
            case 56: goto L947;
            case 57: goto L945;
            case 58: goto L943;
            case 59: goto L941;
            case 60: goto L939;
            case 61: goto L937;
            case 62: goto L935;
            case 63: goto L933;
            case 64: goto L933;
            case 65: goto L933;
            case 66: goto L931;
            case 67: goto L929;
            case 68: goto L927;
            case 69: goto L925;
            case 70: goto L923;
            case 71: goto L921;
            case 72: goto L919;
            case 73: goto L919;
            case 74: goto L917;
            case 75: goto L915;
            case 76: goto L913;
            case 77: goto L911;
            case 78: goto L909;
            case 79: goto L909;
            case 80: goto L907;
            case 81: goto L905;
            case 82: goto L903;
            case 83: goto L901;
            case 84: goto L901;
            case 85: goto L899;
            case 86: goto L897;
            case 87: goto L895;
            case 88: goto L893;
            case 89: goto L893;
            case 90: goto L893;
            case 91: goto L891;
            case 92: goto L889;
            case 93: goto L889;
            case 94: goto L887;
            case 95: goto L887;
            case 96: goto L885;
            case 97: goto L883;
            case 98: goto L881;
            case 99: goto L879;
            case 100: goto L879;
            case 101: goto L877;
            case 102: goto L875;
            case 103: goto L873;
            case 104: goto L871;
            case 105: goto L869;
            case 106: goto L867;
            case 107: goto L865;
            case 108: goto L863;
            case 109: goto L863;
            case 110: goto L861;
            case 111: goto L859;
            case 112: goto L857;
            case 113: goto L855;
            case 114: goto L853;
            case 115: goto L851;
            case 116: goto L851;
            case 117: goto L849;
            case 118: goto L847;
            case 119: goto L845;
            case 120: goto L845;
            case 121: goto L843;
            case 122: goto L841;
            case 123: goto L839;
            case 124: goto L837;
            case 125: goto L835;
            case 126: goto L833;
            case 127: goto L831;
            case 128: goto L831;
            case 129: goto L831;
            case 130: goto L829;
            case 131: goto L827;
            case 132: goto L825;
            case 133: goto L823;
            case 134: goto L821;
            case 135: goto L819;
            case 136: goto L817;
            case 137: goto L815;
            case 138: goto L815;
            case 139: goto L815;
            case 140: goto L815;
            case 141: goto L815;
            case 142: goto L815;
            case 143: goto L815;
            case 144: goto L813;
            case 145: goto L811;
            case 146: goto L811;
            case 147: goto L811;
            case 148: goto L811;
            case 149: goto L811;
            case 150: goto L809;
            case 151: goto L809;
            case 152: goto L809;
            case 153: goto L807;
            case 154: goto L807;
            case 155: goto L807;
            case 156: goto L805;
            case 157: goto L803;
            case 158: goto L803;
            case 159: goto L803;
            case 160: goto L803;
            case 161: goto L801;
            case 162: goto L799;
            case 163: goto L797;
            case 164: goto L795;
            case 165: goto L795;
            case 166: goto L793;
            case 167: goto L791;
            case 168: goto L789;
            case 169: goto L789;
            case 170: goto L787;
            case 171: goto L785;
            case 172: goto L785;
            case 173: goto L785;
            case 174: goto L785;
            case 175: goto L783;
            case 176: goto L781;
            case 177: goto L781;
            case 178: goto L779;
            case 179: goto L779;
            case 180: goto L777;
            case 181: goto L777;
            case 182: goto L777;
            case 183: goto L777;
            case 184: goto L775;
            case 185: goto L773;
            case 186: goto L771;
            case 187: goto L771;
            case 188: goto L771;
            case 189: goto L769;
            case 190: goto L767;
            case 191: goto L765;
            case 192: goto L763;
            case 193: goto L763;
            case 194: goto L761;
            case 195: goto L759;
            case 196: goto L757;
            case 197: goto L755;
            case 198: goto L753;
            case 199: goto L753;
            case 200: goto L753;
            case 201: goto L753;
            case 202: goto L753;
            case 203: goto L753;
            case 204: goto L753;
            case 205: goto L751;
            case 206: goto L751;
            case 207: goto L751;
            case 208: goto L751;
            case 209: goto L751;
            case 210: goto L751;
            case 211: goto L751;
            case 212: goto L751;
            case 213: goto L751;
            case 214: goto L751;
            case 215: goto L749;
            case 216: goto L747;
            case 217: goto L745;
            case 218: goto L743;
            case 219: goto L743;
            case 220: goto L741;
            case 221: goto L741;
            case 222: goto L741;
            case 223: goto L741;
            case 224: goto L739;
            case 225: goto L739;
            case 226: goto L737;
            case 227: goto L735;
            case 228: goto L735;
            case 229: goto L735;
            case 230: goto L735;
            case 231: goto L735;
            case 232: goto L735;
            case 233: goto L733;
            case 234: goto L733;
            case 235: goto L731;
            case 236: goto L729;
            case 237: goto L727;
            case 238: goto L727;
            default: goto L725;
        };
    L725:
        return new int[]{2, 2, 2, 2, 2, 2};
    L727:
        return new int[]{4, 2, 4, 3, 2, 2};
    L729:
        return new int[]{4, 4, 4, 3, 2, 2};
    L731:
        return new int[]{2, 4, 2, 1, 1, 2};
    L733:
        return new int[]{2, 3, 3, 4, 2, 2};
    L735:
        return new int[]{4, 4, 4, 4, 2, 2};
    L737:
        return new int[]{1, 2, 1, 1, 2, 2};
    L739:
        return new int[]{3, 1, 2, 2, 2, 2};
    L741:
        return new int[]{4, 2, 2, 4, 2, 2};
    L743:
        return new int[]{4, 3, 3, 2, 2, 2};
    L745:
        return new int[]{0, 0, 1, 2, 2, 2};
    L747:
        return new int[]{0, 2, 1, 2, 2, 2};
    L749:
        return new int[]{2, 2, 1, 1, 2, 4};
    L751:
        return new int[]{1, 2, 0, 0, 2, 2};
    L753:
        return new int[]{0, 2, 2, 2, 2, 2};
    L755:
        return new int[]{1, 2, 3, 4, 3, 2};
    L757:
        return new int[]{2, 1, 1, 2, 1, 2};
    L759:
        return new int[]{2, 2, 4, 1, 3, 1};
    L761:
        return new int[]{3, 3, 2, 3, 4, 2};
    L763:
        return new int[]{0, 2, 1, 2, 3, 3};
    L765:
        return new int[]{3, 4, 2, 1, 3, 2};
    L767:
        return new int[]{0, 0, 0, 0, 0, 0};
    L769:
        return new int[]{2, 4, 1, 0, 2, 2};
    L771:
        return new int[]{1, 1, 1, 1, 2, 2};
    L773:
        return new int[]{3, 2, 4, 3, 2, 2};
    L775:
        return new int[]{3, 1, 1, 1, 2, 2};
    L777:
        return new int[]{4, 2, 4, 4, 2, 2};
    L779:
        return new int[]{3, 3, 4, 4, 2, 2};
    L781:
        return new int[]{0, 1, 2, 2, 2, 2};
    L783:
        return new int[]{3, 4, 1, 0, 2, 2};
    L785:
        return new int[]{4, 3, 4, 4, 2, 2};
    L787:
        return new int[]{3, 2, 1, 2, 2, 2};
    L789:
        return new int[]{4, 4, 3, 4, 2, 2};
    L791:
        return new int[]{2, 3, 2, 1, 2, 2};
    L793:
        return new int[]{2, 2, 1, 2, 2, 2};
    L795:
        return new int[]{4, 3, 2, 4, 2, 2};
    L797:
        return new int[]{2, 4, 4, 1, 2, 2};
    L799:
        return new int[]{2, 2, 3, 4, 4, 2};
    L801:
        return new int[]{4, 4, 3, 2, 2, 2};
    L803:
        return new int[]{4, 2, 3, 3, 2, 2};
    L805:
        return new int[]{0, 1, 1, 1, 2, 2};
    L807:
        return new int[]{3, 2, 2, 2, 2, 2};
    L809:
        return new int[]{0, 0, 0, 0, 1, 2};
    L811:
        return new int[]{4, 2, 2, 2, 2, 2};
    L813:
        return new int[]{2, 3, 3, 3, 1, 1};
    L815:
        return new int[]{0, 0, 0, 0, 0, 2};
    L817:
        return new int[]{3, 1, 1, 2, 2, 0};
    L819:
        return new int[]{3, 3, 2, 0, 2, 2};
    L821:
        return new int[]{1, 0, 0, 1, 3, 3};
    L823:
        return new int[]{1, 0, 0, 1, 2, 2};
    L825:
        return new int[]{0, 0, 1, 1, 3, 2};
    L827:
        return new int[]{0, 3, 2, 3, 1, 2};
    L829:
        return new int[]{1, 4, 4, 4, 4, 2};
    L831:
        return new int[]{1, 2, 2, 2, 2, 2};
    L833:
        return new int[]{2, 2, 4, 1, 2, 2};
    L835:
        return new int[]{3, 4, 1, 3, 2, 2};
    L837:
        return new int[]{2, 0, 2, 1, 2, 0};
    L839:
        return new int[]{1, 0, 2, 2, 4, 4};
    L841:
        return new int[]{3, 3, 3, 3, 2, 2};
    L843:
        return new int[]{2, 1, 2, 3, 2, 1};
    L845:
        return new int[]{4, 3, 3, 3, 2, 2};
    L847:
        return new int[]{2, 2, 3, 1, 2, 2};
    L849:
        return new int[]{1, 2, 4, 4, 3, 2};
    L851:
        return new int[]{2, 3, 2, 3, 2, 2};
    L853:
        return new int[]{2, 3, 1, 2, 4, 2};
    L855:
        return new int[]{0, 0, 1, 2, 4, 2};
    L857:
        return new int[]{2, 2, 4, 3, 2, 2};
    L859:
        return new int[]{0, 0, 3, 0, 0, 2};
    L861:
        return new int[]{2, 1, 4, 3, 0, 4};
    L863:
        return new int[]{2, 4, 4, 4, 2, 2};
    L865:
        return new int[]{3, 4, 2, 1, 2, 2};
    L867:
        return new int[]{3, 4, 3, 2, 2, 2};
    L869:
        return new int[]{1, 0, 4, 1, 1, 0};
    L871:
        return new int[]{2, 4, 4, 4, 3, 2};
    L873:
        return new int[]{3, 2, 2, 1, 2, 2};
    L875:
        return new int[]{3, 2, 1, 3, 4, 2};
    L877:
        return new int[]{3, 1, 0, 2, 2, 2};
    L879:
        return new int[]{4, 3, 3, 4, 2, 2};
    L881:
        return new int[]{2, 1, 2, 3, 2, 2};
    L883:
        return new int[]{0, 2, 4, 4, 3, 1};
    L885:
        return new int[]{2, 0, 2, 2, 2, 2};
    L887:
        return new int[]{3, 2, 3, 3, 4, 2};
    L889:
        return new int[]{3, 3, 2, 2, 2, 2};
    L891:
        return new int[]{1, 0, 0, 1, 3, 2};
    L893:
        return new int[]{3, 4, 3, 3, 2, 2};
    L895:
        return new int[]{1, 2, 2, 3, 2, 2};
    L897:
        return new int[]{2, 0, 0, 1, 3, 2};
    L899:
        return new int[]{1, 0, 0, 0, 2, 2};
    L901:
        return new int[]{1, 2, 2, 0, 2, 2};
    L903:
        return new int[]{3, 3, 1, 1, 2, 2};
    L905:
        return new int[]{4, 0, 3, 2, 1, 3};
    L907:
        return new int[]{0, 1, 0, 1, 0, 2};
    L909:
        return new int[]{3, 4, 4, 4, 2, 2};
    L911:
        return new int[]{2, 2, 1, 1, 2, 2};
    L913:
        return new int[]{3, 1, 1, 2, 2, 2};
    L915:
        return new int[]{1, 2, 1, 3, 2, 2};
    L917:
        return new int[]{2, 1, 2, 2, 3, 2};
    L919:
        return new int[]{1, 0, 0, 0, 0, 2};
    L921:
        return new int[]{0, 2, 2, 4, 4, 4};
    L923:
        return new int[]{1, 0, 4, 2, 2, 2};
    L925:
        return new int[]{2, 1, 1, 2, 2, 2};
    L927:
        return new int[]{3, 2, 1, 1, 1, 2};
    L929:
        return new int[]{0, 3, 2, 3, 4, 2};
    L931:
        return new int[]{2, 4, 3, 1, 2, 2};
    L933:
        return new int[]{0, 2, 0, 1, 2, 2};
    L935:
        return new int[]{0, 1, 1, 2, 1, 2};
    L937:
        return new int[]{4, 2, 3, 3, 4, 3};
    L939:
        return new int[]{3, 2, 3, 2, 2, 2};
    L941:
        return new int[]{3, 2, 2, 0, 2, 2};
    L943:
        return new int[]{1, 1, 3, 2, 2, 3};
    L945:
        return new int[]{1, 2, 2, 3, 4, 2};
    L947:
        return new int[]{1, 1, 1, 1, 1, 2};
    L949:
        return new int[]{3, 1, 3, 3, 2, 4};
    L951:
        return new int[]{0, 1, 0, 1, 1, 0};
    L953:
        return new int[]{3, 1, 1, 3, 2, 2};
    L955:
        return new int[]{4, 4, 1, 2, 2, 2};
    L957:
        return new int[]{2, 2, 4, 3, 3, 2};
    L959:
        return new int[]{2, 1, 2, 1, 2, 2};
    L961:
        return new int[]{1, 0, 0, 0, 1, 2};
    L963:
        return new int[]{2, 1, 1, 3, 2, 2};
    L965:
        return new int[]{3, 4, 4, 2, 2, 2};
    L967:
        return new int[]{3, 3, 3, 2, 2, 2};
    L969:
        return new int[]{0, 2, 1, 1, 2, 2};
    L971:
        return new int[]{3, 2, 3, 3, 2, 2};
    L973:
        return new int[]{1, 1, 0, 2, 2, 2};
    L975:
        return new int[]{2, 2, 0, 0, 2, 2};
    L977:
        return new int[]{1, 1, 3, 2, 2, 2};
    L979:
        return new int[]{3, 4, 0, 0, 2, 2};
    L981:
        return new int[]{1, 1, 1, 1, 0, 2};
    L983:
        return new int[]{0, 2, 2, 0, 2, 2};
    L985:
        return new int[]{4, 2, 4, 0, 2, 2};
    L987:
        return new int[]{3, 2, 2, 3, 2, 2};
    L989:
        return new int[]{0, 0, 0, 1, 0, 2};
    L991:
        return new int[]{4, 3, 4, 4, 4, 2};
    L993:
        return new int[]{0, 0, 0, 0, 1, 0};
    L995:
        return new int[]{1, 3, 2, 1, 2, 2};
    L997:
        return new int[]{0, 0, 2, 0, 0, 2};
    L999:
        return new int[]{0, 1, 4, 2, 2, 1};
    L1001:
        return new int[]{0, 0, 2, 0, 1, 2};
    L1003:
        return new int[]{1, 0, 1, 0, 0, 2};
    L1005:
        return new int[]{2, 3, 0, 1, 2, 2};
    L1007:
        return new int[]{2, 3, 3, 2, 2, 2};
    L1009:
        return new int[]{2, 0, 1, 1, 3, 1};
    L1011:
        return new int[]{2, 2, 2, 1, 2, 2};
    L1013:
        return new int[]{2, 4, 3, 4, 2, 2};
    L1015:
        return new int[]{0, 1, 0, 0, 0, 2};
    L1017:
        return new int[]{4, 2, 4, 2, 2, 2};
    L1019:
        return new int[]{1, 2, 3, 3, 2, 2};
    L1021:
        return new int[]{3, 2, 1, 0, 2, 2};
    L1023:
        return new int[]{3, 1, 2, 2, 3, 2};
    L1025:
        return new int[]{3, 2, 1, 1, 2, 2};
    L1027:
        return new int[]{1, 1, 1, 1, 2, 4};
    L1029:
        return new int[]{1, 2, 4, 4, 2, 2};
    L1031:
        return new int[]{3, 2, 0, 0, 2, 2};
    L1033:
        return new int[]{0, 2, 0, 0, 2, 2};
    L1035:
        return new int[]{4, 4, 2, 3, 2, 2};
    L1037:
        return new int[]{1, 3, 1, 3, 4, 2};
    L1039:
        return new int[]{0, 0, 1, 0, 1, 2};
    L1041:
        return new int[]{2, 1, 3, 2, 4, 2};
    L1043:
        return new int[]{2, 2, 3, 4, 2, 2};
    L1045:
        return new int[]{0, 3, 1, 1, 3, 0};
    L1047:
        return new int[]{2, 2, 3, 3, 2, 2};
    L1049:
        return new int[]{2, 2, 2, 2, 1, 2};
    L1051:
        return new int[]{3, 4, 4, 3, 2, 2};
    L1053:
        return new int[]{1, 1, 1, 2, 2, 2};
    L1055:
        return new int[]{1, 4, 2, 3, 4, 1};
    L6:
        if (r2.equals("ZW") == false) goto L722;
        r0 = 238;
        goto L723
    L9:
        if (r2.equals("ZM") == false) goto L722;
        r0 = 236;
        goto L723
    L12:
        if (r2.equals("ZA") == false) goto L722;
        r0 = 235;
        goto L723
    L15:
        if (r2.equals("YT") == false) goto L722;
        r0 = 234;
        goto L723
    L18:
        if (r2.equals("YE") == false) goto L722;
        r0 = 232;
        goto L723
    L21:
        if (r2.equals("XK") == false) goto L722;
        r0 = 226;
        goto L723
    L24:
        if (r2.equals("WS") == false) goto L722;
        r0 = 225;
        goto L723
    L27:
        if (r2.equals("WF") == false) goto L722;
        r0 = 223;
        goto L723
    L30:
        if (r2.equals("VU") == false) goto L722;
        r0 = 219;
        goto L723
    L33:
        if (r2.equals("VN") == false) goto L722;
        r0 = 217;
        goto L723
    L36:
        if (r2.equals("VI") == false) goto L722;
        r0 = 216;
        goto L723
    L39:
        if (r2.equals("VG") == false) goto L722;
        r0 = 215;
        goto L723
    L42:
        if (r2.equals("VE") == false) goto L722;
        r0 = 231;
        goto L723
    L45:
        if (r2.equals("VC") == false) goto L722;
        r0 = 214;
        goto L723
    L48:
        if (r2.equals("VA") == false) goto L722;
        r0 = 204;
        goto L723
    L51:
        if (r2.equals("UZ") == false) goto L722;
        r0 = 197;
        goto L723
    L54:
        if (r2.equals("UY") == false) goto L722;
        r0 = 196;
        goto L723
    L57:
        if (r2.equals("US") == false) goto L722;
        r0 = 195;
        goto L723
    L60:
        if (r2.equals("UG") == false) goto L722;
        r0 = 194;
        goto L723
    L63:
        if (r2.equals("UA") == false) goto L722;
        r0 = 193;
        goto L723
    L66:
        if (r2.equals("TZ") == false) goto L722;
        r0 = 191;
        goto L723
    L69:
        if (r2.equals("TW") == false) goto L722;
        r0 = 190;
        goto L723
    L72:
        if (r2.equals("TV") == false) goto L722;
        r0 = 222;
        goto L723
    L75:
        if (r2.equals("TT") == false) goto L722;
        r0 = 189;
        goto L723
    L78:
        if (r2.equals("TR") == false) goto L722;
        r0 = 188;
        goto L723
    L81:
        if (r2.equals("TO") == false) goto L722;
        r0 = 185;
        goto L723
    L84:
        if (r2.equals("TN") == false) goto L722;
        r0 = 184;
        goto L723
    L87:
        if (r2.equals("TM") == false) goto L722;
        r0 = 221;
        goto L723
    L90:
        if (r2.equals("TL") == false) goto L722;
        r0 = 183;
        goto L723
    L93:
        if (r2.equals("TJ") == false) goto L722;
        r0 = 179;
        goto L723
    L96:
        if (r2.equals("TH") == false) goto L722;
        r0 = 177;
        goto L723
    L99:
        if (r2.equals("TG") == false) goto L722;
        r0 = 175;
        goto L723
    L102:
        if (r2.equals("TD") == false) goto L722;
        r0 = 174;
        goto L723
    L105:
        if (r2.equals("TC") == false) goto L722;
        r0 = 170;
        goto L723
    L108:
        if (r2.equals("SZ") == false) goto L722;
        r0 = 169;
        goto L723
    L111:
        if (r2.equals("SY") == false) goto L722;
        r0 = 173;
        goto L723
    L114:
        if (r2.equals("SX") == false) goto L722;
        r0 = 213;
        goto L723
    L117:
        if (r2.equals("SV") == false) goto L722;
        r0 = 167;
        goto L723
    L120:
        if (r2.equals("ST") == false) goto L722;
        r0 = 166;
        goto L723
    L123:
        if (r2.equals("SS") == false) goto L722;
        r0 = 165;
        goto L723
    L126:
        if (r2.equals("SR") == false) goto L722;
        r0 = 163;
        goto L723
    L129:
        if (r2.equals("SO") == false) goto L722;
        r0 = 162;
        goto L723
    L132:
        if (r2.equals("SN") == false) goto L722;
        r0 = 161;
        goto L723
    L135:
        if (r2.equals("SM") == false) goto L722;
        r0 = 203;
        goto L723
    L138:
        if (r2.equals("SL") == false) goto L722;
        r0 = 160;
        goto L723
    L141:
        if (r2.equals("SK") == false) goto L722;
        r0 = 156;
        goto L723
    L144:
        if (r2.equals("SJ") == false) goto L722;
        r0 = 155;
        goto L723
    L147:
        if (r2.equals("SI") == false) goto L722;
        r0 = 152;
        goto L723
    L150:
        if (r2.equals("SH") == false) goto L722;
        r0 = 149;
        goto L723
    L153:
        if (r2.equals("SG") == false) goto L722;
        r0 = 144;
        goto L723
    L156:
        if (r2.equals("SE") == false) goto L722;
        r0 = 143;
        goto L723
    L159:
        if (r2.equals("SD") == false) goto L722;
        r0 = 172;
        goto L723
    L162:
        if (r2.equals("SC") == false) goto L722;
        r0 = 148;
        goto L723
    L165:
        if (r2.equals("SB") == false) goto L722;
        r0 = 237;
        goto L723
    L168:
        if (r2.equals("SA") == false) goto L722;
        r0 = 136;
        goto L723
    L171:
        if (r2.equals("RW") == false) goto L722;
        r0 = 135;
        goto L723
    L174:
        if (r2.equals("RU") == false) goto L722;
        r0 = 134;
        goto L723
    L177:
        if (r2.equals("RS") == false) goto L722;
        r0 = 133;
        goto L723
    L180:
        if (r2.equals("RO") == false) goto L722;
        r0 = 132;
        goto L723
    L183:
        if (r2.equals("RE") == false) goto L722;
        r0 = 131;
        goto L723
    L186:
        if (r2.equals("QA") == false) goto L722;
        r0 = 130;
        goto L723
    L189:
        if (r2.equals("PY") == false) goto L722;
        r0 = 129;
        goto L723
    L192:
        if (r2.equals("PW") == false) goto L722;
        r0 = 126;
        goto L723
    L195:
        if (r2.equals("PT") == false) goto L722;
        r0 = 151;
        goto L723
    L198:
        if (r2.equals("PS") == false) goto L722;
        r0 = 125;
        goto L723
    L201:
        if (r2.equals("PR") == false) goto L722;
        r0 = 124;
        goto L723
    L204:
        if (r2.equals("PM") == false) goto L722;
        r0 = 202;
        goto L723
    L207:
        if (r2.equals("PL") == false) goto L722;
        r0 = 123;
        goto L723
    L210:
        if (r2.equals("PK") == false) goto L722;
        r0 = 122;
        goto L723
    L213:
        if (r2.equals("PH") == false) goto L722;
        r0 = 121;
        goto L723
    L216:
        if (r2.equals("PG") == false) goto L722;
        r0 = 120;
        goto L723
    L219:
        if (r2.equals("PF") == false) goto L722;
        r0 = 118;
        goto L723
    L222:
        if (r2.equals("PE") == false) goto L722;
        r0 = 117;
        goto L723
    L225:
        if (r2.equals("PA") == false) goto L722;
        r0 = 116;
        goto L723
    L228:
        if (r2.equals("OM") == false) goto L722;
        r0 = 114;
        goto L723
    L231:
        if (r2.equals("NZ") == false) goto L722;
        r0 = 113;
        goto L723
    L234:
        if (r2.equals("NU") == false) goto L722;
        r0 = 147;
        goto L723
    L237:
        if (r2.equals("NR") == false) goto L722;
        r0 = 182;
        goto L723
    L240:
        if (r2.equals("NP") == false) goto L722;
        r0 = 112;
        goto L723
    L243:
        if (r2.equals("NO") == false) goto L722;
        r0 = 111;
        goto L723
    L246:
        if (r2.equals("NL") == false) goto L722;
        r0 = 110;
        goto L723
    L249:
        if (r2.equals("NI") == false) goto L722;
        r0 = 109;
        goto L723
    L252:
        if (r2.equals("NG") == false) goto L722;
        r0 = 107;
        goto L723
    L255:
        if (r2.equals("NF") == false) goto L722;
        r0 = 154;
        goto L723
    L258:
        if (r2.equals("NE") == false) goto L722;
        r0 = 230;
        goto L723
    L261:
        if (r2.equals("NC") == false) goto L722;
        r0 = 233;
        goto L723
    L264:
        if (r2.equals("NA") == false) goto L722;
        r0 = 106;
        goto L723
    L267:
        if (r2.equals("MZ") == false) goto L722;
        r0 = 224;
        goto L723
    L270:
        if (r2.equals("MY") == false) goto L722;
        r0 = 105;
        goto L723
    L273:
        if (r2.equals("MX") == false) goto L722;
        r0 = 104;
        goto L723
    L276:
        if (r2.equals("MW") == false) goto L722;
        r0 = 103;
        goto L723
    L279:
        if (r2.equals("MV") == false) goto L722;
        r0 = 102;
        goto L723
    L282:
        if (r2.equals("MU") == false) goto L722;
        r0 = 101;
        goto L723
    L285:
        if (r2.equals("MT") == false) goto L722;
        r0 = 142;
        goto L723
    L288:
        if (r2.equals("MS") == false) goto L722;
        r0 = 201;
        goto L723
    L291:
        if (r2.equals("MR") == false) goto L722;
        r0 = 100;
        goto L723
    L294:
        if (r2.equals("MQ") == false) goto L722;
        r0 = 98;
        goto L723
    L297:
        if (r2.equals("MP") == false) goto L722;
        r0 = 128;
        goto L723
    L300:
        if (r2.equals("MO") == false) goto L722;
        r0 = 97;
        goto L723
    L303:
        if (r2.equals("MN") == false) goto L722;
        r0 = 96;
        goto L723
    L306:
        if (r2.equals("MM") == false) goto L722;
        r0 = 95;
        goto L723
    L309:
        if (r2.equals("ML") == false) goto L722;
        r0 = 93;
        goto L723
    L312:
        if (r2.equals("MK") == false) goto L722;
        r0 = 91;
        goto L723
    L315:
        if (r2.equals("MH") == false) goto L722;
        r0 = 220;
        goto L723
    L318:
        if (r2.equals("MG") == false) goto L722;
        r0 = 90;
        goto L723
    L321:
        if (r2.equals("MF") == false) goto L722;
        r0 = 87;
        goto L723
    L324:
        if (r2.equals("ME") == false) goto L722;
        r0 = 86;
        goto L723
    L327:
        if (r2.equals("MD") == false) goto L722;
        r0 = 85;
        goto L723
    L330:
        if (r2.equals("MC") == false) goto L722;
        r0 = 84;
        goto L723
    L333:
        if (r2.equals("MA") == false) goto L722;
        r0 = 82;
        goto L723
    L336:
        if (r2.equals("LY") == false) goto L722;
        r0 = 159;
        goto L723
    L339:
        if (r2.equals("LV") == false) goto L722;
        r0 = 141;
        goto L723
    L342:
        if (r2.equals("LU") == false) goto L722;
        r0 = 81;
        goto L723
    L345:
        if (r2.equals("LT") == false) goto L722;
        r0 = 80;
        goto L723
    L348:
        if (r2.equals("LS") == false) goto L722;
        r0 = 119;
        goto L723
    L351:
        if (r2.equals("LR") == false) goto L722;
        r0 = 79;
        goto L723
    L354:
        if (r2.equals("LK") == false) goto L722;
        r0 = 94;
        goto L723
    L357:
        if (r2.equals("LI") == false) goto L722;
        r0 = 200;
        goto L723
    L360:
        if (r2.equals("LC") == false) goto L722;
        r0 = 77;
        goto L723
    L363:
        if (r2.equals("LB") == false) goto L722;
        r0 = 76;
        goto L723
    L366:
        if (r2.equals("LA") == false) goto L722;
        r0 = 75;
        goto L723
    L369:
        if (r2.equals("KZ") == false) goto L722;
        r0 = 74;
        goto L723
    L372:
        if (r2.equals("KY") == false) goto L722;
        r0 = 212;
        goto L723
    L375:
        if (r2.equals("KW") == false) goto L722;
        r0 = 73;
        goto L723
    L378:
        if (r2.equals("KR") == false) goto L722;
        r0 = 71;
        goto L723
    L381:
        if (r2.equals("KN") == false) goto L722;
        r0 = 211;
        goto L723
    L384:
        if (r2.equals("KM") == false) goto L722;
        r0 = 218;
        goto L723
    L387:
        if (r2.equals("KI") == false) goto L722;
        r0 = 181;
        goto L723
    L390:
        if (r2.equals("KH") == false) goto L722;
        r0 = 70;
        goto L723
    L393:
        if (r2.equals("KG") == false) goto L722;
        r0 = 69;
        goto L723
    L396:
        if (r2.equals("KE") == false) goto L722;
        r0 = 68;
        goto L723
    L399:
        if (r2.equals("JP") == false) goto L722;
        r0 = 67;
        goto L723
    L402:
        if (r2.equals("JO") == false) goto L722;
        r0 = 187;
        goto L723
    L405:
        if (r2.equals("JM") == false) goto L722;
        r0 = 66;
        goto L723
    L408:
        if (r2.equals("JE") == false) goto L722;
        r0 = 65;
        goto L723
    L411:
        if (r2.equals("IT") == false) goto L722;
        r0 = 62;
        goto L723
    L414:
        if (r2.equals("IS") == false) goto L722;
        r0 = 140;
        goto L723
    L417:
        if (r2.equals("IR") == false) goto L722;
        r0 = Base64.padSymbol;
        goto L723
    L420:
        if (r2.equals("IQ") == false) goto L722;
        r0 = 60;
        goto L723
    L423:
        if (r2.equals("IO") == false) goto L722;
        r0 = 59;
        goto L723
    L426:
        if (r2.equals("IN") == false) goto L722;
        r0 = 58;
        goto L723
    L429:
        if (r2.equals("IM") == false) goto L722;
        r0 = SignedBytes.MAX_POWER_OF_TWO;
        goto L723
    L432:
        if (r2.equals("IL") == false) goto L722;
        r0 = 57;
        goto L723
    L435:
        if (r2.equals("IE") == false) goto L722;
        r0 = 56;
        goto L723
    L438:
        if (r2.equals("ID") == false) goto L722;
        r0 = 55;
        goto L723
    L441:
        if (r2.equals("HU") == false) goto L722;
        r0 = 139;
        goto L723
    L444:
        if (r2.equals("HT") == false) goto L722;
        r0 = 229;
        goto L723
    L447:
        if (r2.equals("HR") == false) goto L722;
        r0 = 72;
        goto L723
    L450:
        if (r2.equals("HK") == false) goto L722;
        r0 = 54;
        goto L723
    L453:
        if (r2.equals("GY") == false) goto L722;
        r0 = 53;
        goto L723
    L456:
        if (r2.equals("GW") == false) goto L722;
        r0 = 52;
        goto L723
    L459:
        if (r2.equals("GU") == false) goto L722;
        r0 = 51;
        goto L723
    L462:
        if (r2.equals("GT") == false) goto L722;
        r0 = 50;
        goto L723
    L465:
        if (r2.equals("GR") == false) goto L722;
        r0 = 49;
        goto L723
    L468:
        if (r2.equals("GQ") == false) goto L722;
        r0 = 228;
        goto L723
    L471:
        if (r2.equals("GP") == false) goto L722;
        r0 = 48;
        goto L723
    L474:
        if (r2.equals("GN") == false) goto L722;
        r0 = 47;
        goto L723
    L477:
        if (r2.equals("GM") == false) goto L722;
        r0 = 164;
        goto L723
    L480:
        if (r2.equals("GL") == false) goto L722;
        r0 = 83;
        goto L723
    L483:
        if (r2.equals("GI") == false) goto L722;
        r0 = 63;
        goto L723
    L486:
        if (r2.equals("GH") == false) goto L722;
        r0 = 46;
        goto L723
    L489:
        if (r2.equals("GG") == false) goto L722;
        r0 = 45;
        goto L723
    L492:
        if (r2.equals("GF") == false) goto L722;
        r0 = 44;
        goto L723
    L495:
        if (r2.equals("GE") == false) goto L722;
        r0 = 43;
        goto L723
    L498:
        if (r2.equals("GD") == false) goto L722;
        r0 = 42;
        goto L723
    L501:
        if (r2.equals("GB") == false) goto L722;
        r0 = 41;
        goto L723
    L504:
        if (r2.equals("GA") == false) goto L722;
        r0 = 40;
        goto L723
    L507:
        if (r2.equals("FR") == false) goto L722;
        r0 = 39;
        goto L723
    L510:
        if (r2.equals("FO") == false) goto L722;
        r0 = 38;
        goto L723
    L513:
        if (r2.equals("FM") == false) goto L722;
        r0 = 37;
        goto L723
    L516:
        if (r2.equals("FK") == false) goto L722;
        r0 = 153;
        goto L723
    L519:
        if (r2.equals("FJ") == false) goto L722;
        r0 = 36;
        goto L723
    L522:
        if (r2.equals("FI") == false) goto L722;
        r0 = 35;
        goto L723
    L525:
        if (r2.equals("ET") == false) goto L722;
        r0 = 34;
        goto L723
    L528:
        if (r2.equals("ES") == false) goto L722;
        r0 = 33;
        goto L723
    L531:
        if (r2.equals("ER") == false) goto L722;
        r0 = 146;
        goto L723
    L534:
        if (r2.equals("EG") == false) goto L722;
        r0 = 89;
        goto L723
    L537:
        if (r2.equals("EE") == false) goto L722;
        r0 = 138;
        goto L723
    L540:
        if (r2.equals("EC") == false) goto L722;
        r0 = 32;
        goto L723
    L543:
        if (r2.equals("DZ") == false) goto L722;
        r0 = 178;
        goto L723
    L546:
        if (r2.equals("DO") == false) goto L722;
        r0 = 78;
        goto L723
    L549:
        if (r2.equals("DM") == false) goto L722;
        r0 = 210;
        goto L723
    L552:
        if (r2.equals("DK") == false) goto L722;
        r0 = Ascii.f91US;
        goto L723
    L555:
        if (r2.equals("DJ") == false) goto L722;
        r0 = 158;
        goto L723
    L558:
        if (r2.equals("DE") == false) goto L722;
        r0 = Ascii.f87RS;
        goto L723
    L561:
        if (r2.equals("CZ") == false) goto L722;
        r0 = Ascii.f83GS;
        goto L723
    L564:
        if (r2.equals("CY") == false) goto L722;
        r0 = Ascii.f82FS;
        goto L723
    L567:
        if (r2.equals("CX") == false) goto L722;
        r0 = 199;
        goto L723
    L570:
        if (r2.equals("CW") == false) goto L722;
        r0 = 209;
        goto L723
    L573:
        if (r2.equals("CV") == false) goto L722;
        r0 = Ascii.ESC;
        goto L723
    L576:
        if (r2.equals("CU") == false) goto L722;
        r0 = 180;
        goto L723
    L579:
        if (r2.equals("CR") == false) goto L722;
        r0 = 108;
        goto L723
    L582:
        if (r2.equals("CO") == false) goto L722;
        r0 = Ascii.SUB;
        goto L723
    L585:
        if (r2.equals("CN") == false) goto L722;
        r0 = Ascii.f80EM;
        goto L723
    L588:
        if (r2.equals("CM") == false) goto L722;
        r0 = 99;
        goto L723
    L591:
        if (r2.equals("CL") == false) goto L722;
        r0 = 176;
        goto L723
    L594:
        if (r2.equals("CK") == false) goto L722;
        r0 = Ascii.CAN;
        goto L723
    L597:
        if (r2.equals("CI") == false) goto L722;
        r0 = Ascii.SYN;
        goto L723
    L600:
        if (r2.equals("CH") == false) goto L722;
        r0 = Ascii.DC4;
        goto L723
    L603:
        if (r2.equals("CG") == false) goto L722;
        r0 = 88;
        goto L723
    L606:
        if (r2.equals("CF") == false) goto L722;
        r0 = 19;
        goto L723
    L609:
        if (r2.equals("CD") == false) goto L722;
        r0 = 92;
        goto L723
    L612:
        if (r2.equals("CA") == false) goto L722;
        r0 = 192;
        goto L723
    L615:
        if (r2.equals("BZ") == false) goto L722;
        r0 = Ascii.ETB;
        goto L723
    L618:
        if (r2.equals("BY") == false) goto L722;
        r0 = Ascii.DC2;
        goto L723
    L621:
        if (r2.equals("BW") == false) goto L722;
        r0 = 17;
        goto L723
    L624:
        if (r2.equals("BT") == false) goto L722;
        r0 = Ascii.DLE;
        goto L723
    L627:
        if (r2.equals("BS") == false) goto L722;
        r0 = Ascii.f88SI;
        goto L723
    L630:
        if (r2.equals("BR") == false) goto L722;
        r0 = Ascii.f89SO;
        goto L723
    L633:
        if (r2.equals("BQ") == false) goto L722;
        r0 = 208;
        goto L723
    L636:
        if (r2.equals("BO") == false) goto L722;
        r0 = Ascii.f79CR;
        goto L723
    L639:
        if (r2.equals("BN") == false) goto L722;
        r0 = Ascii.f81FF;
        goto L723
    L642:
        if (r2.equals("BM") == false) goto L722;
        r0 = Ascii.f92VT;
        goto L723
    L645:
        if (r2.equals("BL") == false) goto L722;
        r0 = 127;
        goto L723
    L648:
        if (r2.equals("BJ") == false) goto L722;
        r0 = 10;
        goto L723
    L651:
        if (r2.equals("BI") == false) goto L722;
        r0 = 227;
        goto L723
    L654:
        if (r2.equals("BH") == false) goto L722;
        r0 = 9;
        goto L723
    L657:
        if (r2.equals("BG") == false) goto L722;
        r0 = 150;
        goto L723
    L660:
        if (r2.equals("BF") == false) goto L722;
        r0 = 171;
        goto L723
    L663:
        if (r2.equals("BE") == false) goto L722;
        r0 = 8;
        goto L723
    L666:
        if (r2.equals("BD") == false) goto L722;
        r0 = 7;
        goto L723
    L669:
        if (r2.equals("BB") == false) goto L722;
        r0 = 207;
        goto L723
    L672:
        if (r2.equals("BA") == false) goto L722;
        r0 = 186;
        goto L723
    L675:
        if (r2.equals("AZ") == false) goto L722;
        r0 = 157;
        goto L723
    L678:
        if (r2.equals("AX") == false) goto L722;
        r0 = 198;
        goto L723
    L681:
        if (r2.equals("AW") == false) goto L722;
        r0 = 6;
        goto L723
    L684:
        if (r2.equals("AU") == false) goto L722;
        r0 = 5;
        goto L723
    L687:
        if (r2.equals("AT") == false) goto L722;
        r0 = 137;
        goto L723
    L690:
        if (r2.equals("AS") == false) goto L722;
        r0 = 4;
        goto L723
    L693:
        if (r2.equals("AR") == false) goto L722;
        r0 = 3;
        goto L723
    L696:
        if (r2.equals("AQ") == false) goto L722;
        r0 = 145;
        goto L723
    L699:
        if (r2.equals("AO") == false) goto L722;
        r0 = 2;
        goto L723
    L702:
        if (r2.equals("AM") == false) goto L722;
        r0 = 115;
        goto L723
    L705:
        if (r2.equals("AL") == false) goto L722;
        r0 = 1;
        goto L723
    L708:
        if (r2.equals("AI") == false) goto L722;
        r0 = 206;
        goto L723
    L711:
        if (r2.equals("AG") == false) goto L722;
        r0 = Ascii.NAK;
        goto L723
    L714:
        if (r2.equals("AF") == false) goto L722;
        r0 = 168;
        goto L723
    L717:
        if (r2.equals("AE") == false) goto L722;
        r0 = 0;
        goto L723
    L720:
        if (r2.equals("AD") == false) goto L722;
        r0 = 205;
        goto L723
    }

    private long getInitialBitrateEstimateForNetworkType(int r4) {
        Long r0 = this.initialBitrateEstimates.get(Integer.valueOf(r4));
        if (r0 != null) goto L5;
        r0 = this.initialBitrateEstimates.get(0);
    L5:
        if (r0 != null) goto L8;
        r0 = 1000000L;
    L8:
        return r0.longValue();
    }

    private static boolean isTransferAtFullNetworkSpeed(DataSpec r1, boolean r2) {
        if (r2 == true) goto L4;
    L6:
        return false;
    L4:
        if (r1.isFlagSet(8) == true) goto L6;
        return true;
    }

    private synchronized void onNetworkTypeChanged(int r4) {
        monitor-enter(this);
    L27:
        th = move-exception;
        throw th;
    L4:
        if (this.networkType == 0) goto L10;
        if (this.resetOnNetworkTypeChange == true) goto L10;
        monitor-exit(this);
        return;
    L10:
        if (this.networkTypeOverrideSet == false) goto L13;
        r4 = this.networkTypeOverride;     // Catch: Throwable -> L27
    L13:
        if (this.networkType != r4) goto L16;
        monitor-exit(this);
        return;
    L16:
        this.networkType = r4;     // Catch: Throwable -> L27
        if (r4 == 1) goto L25;
        if (r4 == 0) goto L25;
        if (r4 == 8) goto L25;
        this.initialBitrateEstimate = getInitialBitrateEstimateForNetworkType(r4);     // Catch: Throwable -> L27
        this.bandwidthEstimator.onNetworkTypeChange(this.initialBitrateEstimate);     // Catch: Throwable -> L27
        this.timeToFirstByteEstimator.reset();     // Catch: Throwable -> L27
        monitor-exit(this);
        return;
    L25:
        monitor-exit(this);
    }

    @Override // androidx.media3.exoplayer.upstream.BandwidthMeter
    public synchronized void addEventListener(Handler r2, BandwidthMeter.EventListener r3) {
        monitor-enter(this);
        Assertions.checkNotNull(r2);     // Catch: Throwable -> L6
        Assertions.checkNotNull(r3);     // Catch: Throwable -> L6
        this.bandwidthEstimator.addEventListener(r2, r3);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.upstream.BandwidthMeter
    public synchronized long getBitrateEstimate() {
        monitor-enter(this);
        long r0 = this.bandwidthEstimator.getBandwidthEstimate();     // Catch: Throwable -> L9
        if (r0 == Long.MIN_VALUE) goto L6;
        long r2 = r0;
    L7:
        monitor-exit(this);
        return r2;
    L6:
        r2 = this.initialBitrateEstimate;     // Catch: Throwable -> L9
    L9:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.upstream.BandwidthMeter
    public synchronized long getTimeToFirstByteEstimateUs() {
        monitor-enter(this);
        long r0 = this.timeToFirstByteEstimator.getTimeToFirstByteEstimateUs();     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.upstream.BandwidthMeter
    public TransferListener getTransferListener() {
        return this;
    }

    @Override // androidx.media3.datasource.TransferListener
    public synchronized void onBytesTransferred(DataSource r2, DataSpec r3, boolean r4, int r5) {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (isTransferAtFullNetworkSpeed(r3, r4) == true) goto L7;
        monitor-exit(this);
        return;
    L7:
        this.bandwidthEstimator.onBytesTransferred(r2, r5);     // Catch: Throwable -> L10
        monitor-exit(this);
    }

    @Override // androidx.media3.datasource.TransferListener
    public synchronized void onTransferEnd(DataSource r2, DataSpec r3, boolean r4) {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (isTransferAtFullNetworkSpeed(r3, r4) == true) goto L7;
        monitor-exit(this);
        return;
    L7:
        this.bandwidthEstimator.onTransferEnd(r2);     // Catch: Throwable -> L10
        monitor-exit(this);
    }

    @Override // androidx.media3.datasource.TransferListener
    public synchronized void onTransferInitializing(DataSource r2, DataSpec r3, boolean r4) {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (isTransferAtFullNetworkSpeed(r3, r4) == true) goto L7;
        monitor-exit(this);
        return;
    L7:
        this.timeToFirstByteEstimator.onTransferInitializing(r3);     // Catch: Throwable -> L10
        this.bandwidthEstimator.onTransferInitializing(r2);     // Catch: Throwable -> L10
        monitor-exit(this);
    }

    @Override // androidx.media3.datasource.TransferListener
    public synchronized void onTransferStart(DataSource r2, DataSpec r3, boolean r4) {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (isTransferAtFullNetworkSpeed(r3, r4) == true) goto L7;
        monitor-exit(this);
        return;
    L7:
        this.timeToFirstByteEstimator.onTransferStart(r3);     // Catch: Throwable -> L10
        this.bandwidthEstimator.onTransferStart(r2);     // Catch: Throwable -> L10
        monitor-exit(this);
    }

    @Override // androidx.media3.exoplayer.upstream.BandwidthMeter
    public synchronized void removeEventListener(BandwidthMeter.EventListener r2) {
        monitor-enter(this);
        this.bandwidthEstimator.removeEventListener(r2);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void setNetworkTypeOverride(int r2) {
        monitor-enter(this);
        this.networkTypeOverride = r2;     // Catch: Throwable -> L6
        this.networkTypeOverrideSet = true;     // Catch: Throwable -> L6
        onNetworkTypeChanged(r2);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }
}
