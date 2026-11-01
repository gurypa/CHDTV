package androidx.media3.common.util;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.security.NetworkSecurityPolicy;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import android.util.SparseLongArray;
import android.view.Display;
import android.view.WindowManager;
import androidx.core.location.LocationRequestCompat;
import androidx.core.os.EnvironmentCompat;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.CmcdData;
import androidx.media3.extractor.metadata.dvbsi.AppInfoTableDecoder;
import androidx.media3.extractor.p001ts.PsExtractor;
import androidx.media3.extractor.p001ts.TsExtractor;
import androidx.media3.extractor.text.ttml.TtmlNode;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.google.common.math.DoubleMath;
import com.google.common.math.LongMath;
import com.google.common.p003io.ByteStreams;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedBytes;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import es.antonio.m3u.BuildConfig;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

/* JADX INFO: loaded from: classes.dex */
public final class Util {
    private static final int[] CRC16_BYTES_MSBF = null;
    private static final int[] CRC32_BYTES_MSBF = null;
    private static final int[] CRC8_BYTES_MSBF = null;
    public static final String DEVICE = null;
    public static final String DEVICE_DEBUG_INFO = null;
    public static final byte[] EMPTY_BYTE_ARRAY = null;
    public static final long[] EMPTY_LONG_ARRAY = null;
    private static final Pattern ESCAPED_CHARACTER_PATTERN = null;
    private static final String ISM_DASH_FORMAT_EXTENSION = "format=mpd-time-csf";
    private static final String ISM_HLS_FORMAT_EXTENSION = "format=m3u8-aapl";
    private static final Pattern ISM_PATH_PATTERN = null;
    public static final String MANUFACTURER = null;
    public static final String MODEL = null;
    public static final int SDK_INT = 0;
    private static final String TAG = "Util";
    private static final Pattern XS_DATE_TIME_PATTERN = null;
    private static final Pattern XS_DURATION_PATTERN = null;
    private static final String[] additionalIsoLanguageReplacements = null;
    private static final String[] isoLegacyTagReplacements = null;
    private static HashMap<String, String> languageTagReplacementMap;

    private static class Api29 {
        private Api29() {
        }

        public static void startForeground(Service r3, int r4, Notification r5, int r6, String r7) {
            r3.startForeground(r4, r5, r6);     // Catch: RuntimeException -> L4
            return;
        L4:
            e = move-exception;
            Log.m44e(Util.TAG, "The service must be declared with a foregroundServiceType that includes " + r7);
            throw e;
        }
    }

    static {
        SDK_INT = Build.VERSION.SDK_INT;
        DEVICE = Build.DEVICE;
        MANUFACTURER = Build.MANUFACTURER;
        MODEL = Build.MODEL;
        DEVICE_DEBUG_INFO = DEVICE + ", " + MODEL + ", " + MANUFACTURER + ", " + SDK_INT;
        EMPTY_BYTE_ARRAY = new byte[0];
        EMPTY_LONG_ARRAY = new long[0];
        XS_DATE_TIME_PATTERN = Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt](\\d\\d):(\\d\\d):(\\d\\d)([\\.,](\\d+))?([Zz]|((\\+|\\-)(\\d?\\d):?(\\d\\d)))?");
        XS_DURATION_PATTERN = Pattern.compile("^(-)?P(([0-9]*)Y)?(([0-9]*)M)?(([0-9]*)D)?(T(([0-9]*)H)?(([0-9]*)M)?(([0-9.]*)S)?)?$");
        ESCAPED_CHARACTER_PATTERN = Pattern.compile("%([A-Fa-f0-9]{2})");
        ISM_PATH_PATTERN = Pattern.compile("(?:.*\\.)?isml?(?:/(manifest(.*))?)?", 2);
        additionalIsoLanguageReplacements = new String[]{"alb", "sq", "arm", "hy", "baq", "eu", "bur", "my", "tib", "bo", "chi", "zh", "cze", "cs", "dut", "nl", "ger", "de", "gre", "el", "fre", "fr", "geo", "ka", "ice", "is", "mac", "mk", "mao", "mi", "may", "ms", "per", "fa", "rum", "ro", "scc", "hbs-srp", "slo", "sk", "wel", "cy", "id", "ms-ind", "iw", "he", "heb", "he", "ji", "yi", "arb", "ar-arb", "in", "ms-ind", "ind", "ms-ind", "nb", "no-nob", "nob", "no-nob", "nn", "no-nno", "nno", "no-nno", "tw", "ak-twi", "twi", "ak-twi", CmcdConfiguration.KEY_BUFFER_STARVATION, "hbs-bos", "bos", "hbs-bos", "hr", "hbs-hrv", "hrv", "hbs-hrv", "sr", "hbs-srp", "srp", "hbs-srp", "cmn", "zh-cmn", "hak", "zh-hak", "nan", "zh-nan", "hsn", "zh-hsn"};
        isoLegacyTagReplacements = new String[]{"i-lux", "lb", "i-hak", "zh-hak", "i-navajo", "nv", "no-bok", "no-nob", "no-nyn", "no-nno", "zh-guoyu", "zh-cmn", "zh-hakka", "zh-hak", "zh-min-nan", "zh-nan", "zh-xiang", "zh-hsn"};
        CRC32_BYTES_MSBF = new int[]{0, 79764919, 159529838, 222504665, 319059676, 398814059, 445009330, 507990021, 638119352, 583659535, 797628118, 726387553, 890018660, 835552979, 1015980042, 944750013, 1276238704, 1221641927, 1167319070, 1095957929, 1595256236, 1540665371, 1452775106, 1381403509, 1780037320, 1859660671, 1671105958, 1733955601, 2031960084, 2111593891, 1889500026, 1952343757, -1742489888, -1662866601, -1851683442, -1788833735, -1960329156, -1880695413, -2103051438, -2040207643, -1104454824, -1159051537, -1213636554, -1284997759, -1389417084, -1444007885, -1532160278, -1603531939, -734892656, -789352409, -575645954, -646886583, -952755380, -1007220997, -827056094, -898286187, -231047128, -151282273, -71779514, -8804623, -515967244, -436212925, -390279782, -327299027, 881225847, 809987520, 1023691545, 969234094, 662832811, 591600412, 771767749, 717299826, 311336399, 374308984, 453813921, 533576470, 25881363, 88864420, 134795389, 214552010, 2023205639, 2086057648, 1897238633, 1976864222, 1804852699, 1867694188, 1645340341, 1724971778, 1587496639, 1516133128, 1461550545, 1406951526, 1302016099, 1230646740, 1142491917, 1087903418, -1398421865, -1469785312, -1524105735, -1578704818, -1079922613, -1151291908, -1239184603, -1293773166, -1968362705, -1905510760, -2094067647, -2014441994, -1716953613, -1654112188, -1876203875, -1796572374, -525066777, -462094256, -382327159, -302564546, -206542021, -143559028, -97365931, -17609246, -960696225, -1031934488, -817968335, -872425850, -709327229, -780559564, -600130067, -654598054, 1762451694, 1842216281, 1619975040, 1682949687, 2047383090, 2127137669, 1938468188, 2001449195, 1325665622, 1271206113, 1183200824, 1111960463, 1543535498, 1489069629, 1434599652, 1363369299, 622672798, 568075817, 748617968, 677256519, 907627842, 853037301, 1067152940, 995781531, 51762726, 131386257, 177728840, 240578815, 269590778, 349224269, 429104020, 491947555, -248556018, -168932423, -122852000, -60002089, -500490030, -420856475, -341238852, -278395381, -685261898, -739858943, -559578920, -630940305, -1004286614, -1058877219, -845023740, -916395085, -1119974018, -1174433591, -1262701040, -1333941337, -1371866206, -1426332139, -1481064244, -1552294533, -1690935098, -1611170447, -1833673816, -1770699233, -2009983462, -1930228819, -2119160460, -2056179517, 1569362073, 1498123566, 1409854455, 1355396672, 1317987909, 1246755826, 1192025387, 1137557660, 2072149281, 2135122070, 1912620623, 1992383480, 1753615357, 1816598090, 1627664531, 1707420964, 295390185, 358241886, 404320391, 483945776, 43990325, 106832002, 186451547, 266083308, 932423249, 861060070, 1041341759, 986742920, 613929101, 542559546, 756411363, 701822548, -978770311, -1050133554, -869589737, -924188512, -693284699, -764654318, -550540341, -605129092, -475935807, -413084042, -366743377, -287118056, -257573603, -194731862, -114850189, -35218492, -1984365303, -1921392450, -2143631769, -2063868976, -1698919467, -1635936670, -1824608069, -1744851700, -1347415887, -1418654458, -1506661409, -1561119128, -1129027987, -1200260134, -1254728445, -1309196108};
        CRC16_BYTES_MSBF = new int[]{0, 4129, 8258, 12387, 16516, 20645, 24774, 28903, 33032, 37161, 41290, 45419, 49548, 53677, 57806, 61935};
        CRC8_BYTES_MSBF = new int[]{0, 7, 14, 9, 28, 27, 18, 21, 56, 63, 54, 49, 36, 35, 42, 45, 112, 119, 126, 121, 108, 107, 98, 101, 72, 79, 70, 65, 84, 83, 90, 93, 224, 231, 238, BuildConfig.VERSION_CODE, 252, 251, 242, 245, 216, 223, 214, 209, 196, 195, 202, 205, 144, 151, 158, 153, 140, TsExtractor.TS_STREAM_TYPE_DTS_UHD, TsExtractor.TS_STREAM_TYPE_HDMV_DTS, 133, 168, 175, 166, 161, 180, 179, 186, PsExtractor.PRIVATE_STREAM_1, 199, PsExtractor.AUDIO_STREAM, 201, 206, 219, 220, 213, 210, 255, 248, 241, 246, 227, 228, 237, 234, 183, 176, 185, 190, 171, TsExtractor.TS_STREAM_TYPE_AC4, 165, 162, 143, TsExtractor.TS_STREAM_TYPE_DTS_HD, TsExtractor.TS_STREAM_TYPE_AC3, TsExtractor.TS_STREAM_TYPE_SPLICE_INFO, 147, 148, 157, 154, 39, 32, 41, 46, 59, 60, 53, 50, 31, 24, 17, 22, 3, 4, 13, 10, 87, 80, 89, 94, 75, 76, 69, 66, 111, LocationRequestCompat.QUALITY_LOW_POWER, 97, LocationRequestCompat.QUALITY_BALANCED_POWER_ACCURACY, 115, AppInfoTableDecoder.APPLICATION_INFORMATION_TABLE_ID, 125, 122, 137, 142, TsExtractor.TS_STREAM_TYPE_E_AC3, 128, 149, 146, 155, 156, 177, 182, 191, 184, 173, 170, 163, 164, 249, 254, 247, PsExtractor.VIDEO_STREAM_MASK, 229, 226, 235, 236, 193, 198, 207, 200, 221, 218, 211, 212, 105, 110, 103, 96, 117, 114, 123, 124, 81, 86, 95, 88, 77, 74, 67, 68, 25, 30, 23, 16, 5, 2, 11, 12, 33, 38, 47, 40, 61, 58, 51, 52, 78, 73, 64, 71, 82, 85, 92, 91, 118, 113, 120, 127, 106, 109, 100, 99, 62, 57, 48, 55, 34, 37, 44, 43, 6, 1, 8, 15, 26, 29, 20, 19, 174, 169, 160, 167, 178, 181, TsExtractor.TS_PACKET_SIZE, 187, 150, 145, 152, 159, TsExtractor.TS_STREAM_TYPE_DTS, 141, 132, 131, 222, 217, 208, 215, 194, 197, 204, 203, 230, 225, 232, 239, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 253, 244, 243};
    }

    private Util() {
    }

    public static long addWithOverflowDefault(long r6, long r8, long r10) {
        long r0 = r6 + r8;
        if (((r6 ^ r0) & (r8 ^ r0)) >= 0) goto L5;
        return r10;
    L5:
        return r0;
    }

    @Deprecated
    public static boolean areEqual(Object r1, Object r2) {
        return Objects.equals(r1, r2);
    }

    public static <T extends Comparable<? super T>> int binarySearchCeil(List<? extends Comparable<? super T>> r3, T r4, boolean r5, boolean r6) {
        int r0 = Collections.binarySearch(r3, r4);
        if (r0 >= 0) goto L5;
        r0 = ~r0;
    L13:
        if (r6 == false) goto L16;
        return Math.min(r3.size() - 1, r0);
    L16:
        return r0;
    L5:
        int r1 = r3.size();
    L6:
        r0 = r0 + 1;
        if (r0 >= r1) goto L11;
        if (r3.get(r0).compareTo(r4) == 0) goto L6;
    L11:
        if (r5 == false) goto L13;
        r0 = r0 - 1;
        goto L13
    }

    public static int binarySearchCeil(int[] r2, int r3, boolean r4, boolean r5) {
        int r0 = Arrays.binarySearch(r2, r3);
        if (r0 >= 0) goto L5;
        r0 = ~r0;
    L12:
        if (r5 == false) goto L15;
        return Math.min(r2.length - 1, r0);
    L15:
        return r0;
    L5:
        r0 = r0 + 1;
        if (r0 >= r2.length) goto L10;
        if (r2[r0] == r3) goto L5;
    L10:
        if (r4 == false) goto L12;
        r0 = r0 - 1;
        goto L12
    }

    public static int binarySearchCeil(long[] r3, long r4, boolean r6, boolean r7) {
        int r0 = Arrays.binarySearch(r3, r4);
        if (r0 >= 0) goto L5;
        r0 = ~r0;
    L12:
        if (r7 == false) goto L15;
        return Math.min(r3.length - 1, r0);
    L15:
        return r0;
    L5:
        r0 = r0 + 1;
        if (r0 >= r3.length) goto L10;
        if (r3[r0] == r4) goto L5;
    L10:
        if (r6 == false) goto L12;
        r0 = r0 - 1;
        goto L12
    }

    public static int binarySearchFloor(LongArray r5, long r6, boolean r8, boolean r9) {
        int r0 = 0;
        int r1 = r5.size() - 1;
    L3:
        if (r0 > r1) goto L9;
        int r2 = (r0 + r1) >>> 1;
        if (r5.get(r2) < r6) goto L6;
        r1 = r2 - 1;
        goto L3
    L6:
        r0 = r2 + 1;
        goto L3
    L9:
        if (r8 == true) goto L11;
    L15:
        if (r9 == true) goto L17;
        return r1;
    L17:
        if (r1 != (-1)) goto L28;
        return 0;
    L28:
        return r1;
    L11:
        if ((r1 + 1) >= r5.size()) goto L15;
        if (r5.get(r1 + 1) != r6) goto L15;
        return r1 + 1;
    }

    public static <T extends Comparable<? super T>> int binarySearchFloor(List<? extends Comparable<? super T>> r2, T r3, boolean r4, boolean r5) {
        int r0 = Collections.binarySearch(r2, r3);
        if (r0 >= 0) goto L5;
        r0 = -(r0 + 2);
    L12:
        if (r5 == false) goto L15;
        return Math.max(0, r0);
    L15:
        return r0;
    L5:
        r0 = r0 - 1;
        if (r0 < 0) goto L10;
        if (r2.get(r0).compareTo(r3) == 0) goto L5;
    L10:
        if (r4 == false) goto L12;
        r0 = r0 + 1;
        goto L12
    }

    public static int binarySearchFloor(int[] r2, int r3, boolean r4, boolean r5) {
        int r0 = Arrays.binarySearch(r2, r3);
        if (r0 >= 0) goto L5;
        r0 = -(r0 + 2);
    L12:
        if (r5 == false) goto L15;
        return Math.max(0, r0);
    L15:
        return r0;
    L5:
        r0 = r0 - 1;
        if (r0 < 0) goto L10;
        if (r2[r0] == r3) goto L5;
    L10:
        if (r4 == false) goto L12;
        r0 = r0 + 1;
        goto L12
    }

    public static int binarySearchFloor(long[] r3, long r4, boolean r6, boolean r7) {
        int r0 = Arrays.binarySearch(r3, r4);
        if (r0 >= 0) goto L5;
        r0 = -(r0 + 2);
    L12:
        if (r7 == false) goto L15;
        return Math.max(0, r0);
    L15:
        return r0;
    L5:
        r0 = r0 - 1;
        if (r0 < 0) goto L10;
        if (r3[r0] == r4) goto L5;
    L10:
        if (r6 == false) goto L12;
        r0 = r0 + 1;
        goto L12
    }

    @EnsuresNonNull({"#1"})
    public static <T> T castNonNull(T r0) {
        return r0;
    }

    @EnsuresNonNull({"#1"})
    public static <T> T[] castNonNullTypeArray(T[] r0) {
        return r0;
    }

    public static int ceilDivide(int r1, int r2) {
        return ((r1 + r2) - 1) / r2;
    }

    public static long ceilDivide(long r4, long r6) {
        return ((r4 + r6) - 1) / r6;
    }

    public static boolean checkCleartextTrafficPermitted(MediaItem... r7) {
        if (SDK_INT >= 24) goto L5;
        return true;
    L5:
        int r0 = r7.length;
        int r3 = 0;
    L6:
        if (r3 >= r0) goto L21;
        MediaItem r4 = r7[r3];
        if (r4.localConfiguration == null) goto L20;
        if (isTrafficRestricted(r4.localConfiguration.uri) == true) goto L12;
        int r5 = 0;
    L15:
        if (r5 >= r4.localConfiguration.subtitleConfigurations.size()) goto L20;
        if (isTrafficRestricted(r4.localConfiguration.subtitleConfigurations.get(r5).uri) == true) goto L18;
        r5 = r5 + 1;
        goto L15
    L18:
        return false;
    L12:
        return false;
    L20:
        r3 = r3 + 1;
        goto L6
    L21:
        return true;
    }

    public static void closeQuietly(Closeable r1) {
        if (r1 == null) goto L11;
        r1.close();     // Catch: IOException -> L5
        return;
    L8:
        return;
    }

    public static int compareLong(long r1, long r3) {
        if (r1 >= r3) goto L6;
        return -1;
    L6:
        if (r1 != r3) goto L8;
        return 0;
    L8:
        return 1;
    }

    public static float constrainValue(float r1, float r2, float r3) {
        return Math.max(r2, Math.min(r1, r3));
    }

    public static int constrainValue(int r1, int r2, int r3) {
        return Math.max(r2, Math.min(r1, r3));
    }

    public static long constrainValue(long r2, long r4, long r6) {
        return Math.max(r4, Math.min(r2, r6));
    }

    public static <T> boolean contains(SparseArray<T> r1, int r2) {
        if (r1.indexOfKey(r2) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static boolean contains(Object[] r5, Object r6) {
        int r0 = r5.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L9;
        if (areEqual(r5[r2], r6) == true) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    public static <T> boolean contentEquals(SparseArray<T> r7, SparseArray<T> r8) {
        if (r7 != null) goto L8;
        if (r8 != null) goto L7;
        return true;
    L7:
        return false;
    L8:
        if (r8 != null) goto L11;
        return false;
    L11:
        if (SDK_INT >= 31) goto L13;
        int r2 = r7.size();
        if (r2 == r8.size()) goto L17;
        return false;
    L17:
        int r3 = 0;
    L18:
        if (r3 >= r2) goto L23;
        int r4 = r7.keyAt(r3);
        if (Objects.equals(r7.valueAt(r3), r8.get(r4)) == false) goto L21;
        r3 = r3 + 1;
        goto L18
    L21:
        return false;
    L23:
        return true;
    L13:
        return r7.contentEquals(r8);
    }

    public static <T> int contentHashCode(SparseArray<T> r4) {
        if (SDK_INT >= 31) goto L5;
        int r0 = 17;
        int r1 = 0;
    L8:
        if (r1 >= r4.size()) goto L10;
        r0 = (((r0 * 31) + r4.keyAt(r1)) * 31) + Objects.hashCode(r4.valueAt(r1));
        r1 = r1 + 1;
        goto L8
    L10:
        return r0;
    L5:
        return r4.contentHashCode();
    }

    public static int crc16(byte[] r3, int r4, int r5, int r6) {
        int r0 = r4;
    L3:
        if (r0 >= r5) goto L5;
        int r1 = UnsignedBytes.toInt(r3[r0]);
        int r7 = crc16UpdateFourBits(r1 >> 4, r6);
        r6 = crc16UpdateFourBits(r1 & 15, r7);
        r0 = r0 + 1;
        goto L3
    L5:
        return r6;
    }

    private static int crc16UpdateFourBits(int r3, int r4) {
        int r0 = (((r4 >> 12) & 255) ^ r3) & 255;
        int r5 = (r4 << 4) & 65535;
        return (CRC16_BYTES_MSBF[r0] ^ r5) & 65535;
    }

    public static int crc32(byte[] r5, int r6, int r7, int r8) {
        int r0 = r6;
    L3:
        if (r0 >= r7) goto L5;
        r8 = (r8 << 8) ^ CRC32_BYTES_MSBF[((r8 >>> 24) ^ (r5[r0] & 255)) & 255];
        r0 = r0 + 1;
        goto L3
    L5:
        return r8;
    }

    public static int crc8(byte[] r3, int r4, int r5, int r6) {
        int r0 = r4;
    L3:
        if (r0 >= r5) goto L5;
        r6 = CRC8_BYTES_MSBF[(r3[r0] & 255) ^ r6];
        r0 = r0 + 1;
        goto L3
    L5:
        return r6;
    }

    public static Handler createHandler(Looper r1, Handler.Callback r2) {
        return new Handler(r1, r2);
    }

    public static Handler createHandlerForCurrentLooper() {
        return createHandlerForCurrentLooper(null);
    }

    public static Handler createHandlerForCurrentLooper(Handler.Callback r1) {
        return createHandler((Looper) Assertions.checkStateNotNull(Looper.myLooper()), r1);
    }

    public static Handler createHandlerForCurrentOrMainLooper() {
        return createHandlerForCurrentOrMainLooper(null);
    }

    public static Handler createHandlerForCurrentOrMainLooper(Handler.Callback r1) {
        return createHandler(getCurrentOrMainLooper(), r1);
    }

    private static HashMap<String, String> createIsoLanguageReplacementMap() {
        String[] r0 = Locale.getISOLanguages();
        HashMap<String, String> r1 = new HashMap(r0.length + additionalIsoLanguageReplacements.length);
        int r2 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L11;
        String r4 = r0[r3];
        String r5 = new Locale(r4).getISO3Language();     // Catch: MissingResourceException -> L9
        if (TextUtils.isEmpty(r5) == true) goto L10;
        r1.put(r5, r4);     // Catch: MissingResourceException -> L9
    L10:
        r3 = r3 + 1;
        goto L3
    L11:
        int r6 = 0;
    L13:
        if (r6 >= additionalIsoLanguageReplacements.length) goto L15;
        r1.put(additionalIsoLanguageReplacements[r6], additionalIsoLanguageReplacements[r6 + 1]);
        r6 = r6 + 2;
        goto L13
    L15:
        return r1;
    }

    public static ByteBuffer createReadOnlyByteBuffer(ByteBuffer r2) {
        return r2.asReadOnlyBuffer().order(r2.order());
    }

    public static File createTempDirectory(Context r1, String r2) throws IOException {
        File r0 = createTempFile(r1, r2);
        r0.delete();
        r0.mkdir();
        return r0;
    }

    public static File createTempFile(Context r2, String r3) throws IOException {
        return File.createTempFile(r3, null, (File) Assertions.checkNotNull(r2.getCacheDir()));
    }

    public static long durationUsToSampleCount(long r7, int r9) {
        return scaleLargeValue(r7, r9, 1000000, RoundingMode.UP);
    }

    public static String escapeFileName(String r7) {
        int r0 = r7.length();
        int r1 = 0;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L8;
        if (shouldEscapeCharacter(r7.charAt(r2)) == false) goto L7;
        r1 = r1 + 1;
    L7:
        r2 = r2 + 1;
        goto L3
    L8:
        if (r1 != 0) goto L10;
        return r7;
    L10:
        int r3 = 0;
        StringBuilder r4 = new StringBuilder((r1 * 2) + r0);
    L11:
        if (r1 <= 0) goto L17;
        int r5 = r3 + 1;
        char r6 = r7.charAt(r3);
        if (shouldEscapeCharacter(r6) == false) goto L15;
        r4.append('%').append(Integer.toHexString(r6));
        r1 = r1 - 1;
    L16:
        r3 = r5;
        goto L11
    L15:
        r4.append(r6);
        goto L16
    L17:
        if (r3 >= r0) goto L20;
        r4.append(r7, r3, r0);
    L20:
        return r4.toString();
    }

    public static Uri fixSmoothStreamingIsmManifestUri(Uri r3) {
        String r0 = r3.getPath();
        if (r0 != null) goto L5;
        return r3;
    L5:
        Matcher r1 = ISM_PATH_PATTERN.matcher(r0);
        if (r1.matches() == true) goto L8;
    L11:
        return r3;
    L8:
        if (r1.group(1) != null) goto L11;
        return Uri.withAppendedPath(r3, "Manifest");
    }

    public static String formatInvariant(String r1, Object... r2) {
        return String.format(Locale.US, r1, r2);
    }

    public static String fromUtf8Bytes(byte[] r2) {
        return new String(r2, StandardCharsets.UTF_8);
    }

    public static String fromUtf8Bytes(byte[] r2, int r3, int r4) {
        return new String(r2, r3, r4, StandardCharsets.UTF_8);
    }

    public static int generateAudioSessionIdV21(Context r2) {
        AudioManager r0 = (AudioManager) r2.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        if (r0 != null) goto L6;
        return -1;
    L6:
        return r0.generateAudioSessionId();
    }

    public static String getAdaptiveMimeTypeForContentType(int r1) {
        switch(r1) {
            case 0: goto L9;
            case 1: goto L7;
            case 2: goto L5;
            default: goto L3;
        };
    L3:
        return null;
    L5:
        return MimeTypes.APPLICATION_M3U8;
    L7:
        return MimeTypes.APPLICATION_SS;
    L9:
        return MimeTypes.APPLICATION_MPD;
    }

    public static int getApiLevelThatAudioFormatIntroducedAudioEncoding(int r1) {
        switch(r1) {
            case 2: goto L19;
            case 3: goto L19;
            case 4: goto L17;
            case 5: goto L17;
            case 6: goto L17;
            case 7: goto L15;
            case 8: goto L15;
            case 9: goto L13;
            case 10: goto L13;
            case 11: goto L13;
            case 12: goto L13;
            case 13: goto L3;
            case 14: goto L11;
            case 15: goto L13;
            case 16: goto L13;
            case 17: goto L13;
            case 18: goto L13;
            case 19: goto L3;
            case 20: goto L9;
            case 21: goto L3;
            case 22: goto L7;
            case 23: goto L3;
            case 24: goto L3;
            case 25: goto L3;
            case 26: goto L3;
            case 27: goto L3;
            case 28: goto L3;
            case 29: goto L3;
            case 30: goto L5;
            default: goto L3;
        };
    L3:
        return Integer.MAX_VALUE;
    L5:
        return 34;
    L7:
        return 31;
    L9:
        return 30;
    L11:
        return 25;
    L13:
        return 28;
    L15:
        return 23;
    L17:
        return 21;
    L19:
        return 3;
    }

    @Deprecated
    public static int getAudioContentTypeForStreamType(int r1) {
        switch(r1) {
            case 0: goto L7;
            case 1: goto L5;
            case 2: goto L5;
            case 3: goto L3;
            case 4: goto L5;
            case 5: goto L5;
            case 6: goto L3;
            case 7: goto L3;
            case 8: goto L5;
            default: goto L3;
        };
    L3:
        return 2;
    L5:
        return 4;
    L7:
        return 1;
    }

    public static AudioFormat getAudioFormat(int r1, int r2, int r3) {
        return new AudioFormat.Builder().setSampleRate(r1).setChannelMask(r2).setEncoding(r3).build();
    }

    public static int getAudioTrackChannelConfig(int r3) {
        switch(r3) {
            case 1: goto L30;
            case 2: goto L28;
            case 3: goto L26;
            case 4: goto L24;
            case 5: goto L22;
            case 6: goto L20;
            case 7: goto L18;
            case 8: goto L17;
            case 10: goto L13;
            case 12: goto L10;
            case 24: goto L6;
            default: goto L4;
        };
    L4:
        return 0;
    L10:
        return 743676;
    L17:
        return 6396;
    L18:
        return 1276;
    L20:
        return 252;
    L22:
        return 220;
    L24:
        return 204;
    L26:
        return 28;
    L28:
        return 12;
    L30:
        return 4;
    L6:
        if (SDK_INT < 32) goto L9;
        return 67108860;
    L9:
        return 0;
    L13:
        if (SDK_INT < 32) goto L16;
        return 737532;
    L16:
        return 6396;
    }

    public static int getAudioUsageForStreamType(int r1) {
        switch(r1) {
            case 0: goto L15;
            case 1: goto L13;
            case 2: goto L11;
            case 3: goto L3;
            case 4: goto L9;
            case 5: goto L7;
            case 6: goto L3;
            case 7: goto L3;
            case 8: goto L5;
            default: goto L3;
        };
    L3:
        return 1;
    L5:
        return 3;
    L7:
        return 5;
    L9:
        return 4;
    L11:
        return 6;
    L13:
        return 13;
    L15:
        return 2;
    }

    public static String getAuxiliaryTrackTypeString(int r2) {
        switch(r2) {
            case 0: goto L13;
            case 1: goto L11;
            case 2: goto L9;
            case 3: goto L7;
            case 4: goto L5;
            default: goto L4;
        };
    L5:
        return "depth metadata";
    L7:
        return "depth-inverse";
    L9:
        return "depth-linear";
    L11:
        return "original";
    L13:
        return "undefined";
    L4:
        throw new IllegalStateException("Unsupported auxiliary track type");
    }

    public static Player.Commands getAvailableCommands(Player r12, Player.Commands r13) {
        boolean r0 = r12.isPlayingAd();
        boolean r1 = r12.isCurrentMediaItemSeekable();
        boolean r2 = r12.hasPreviousMediaItem();
        boolean r3 = r12.hasNextMediaItem();
        boolean r4 = r12.isCurrentMediaItemLive();
        boolean r5 = r12.isCurrentMediaItemDynamic();
        boolean r6 = r12.getCurrentTimeline().isEmpty();
        Player.Commands.Builder r7 = new Player.Commands.Builder().addAll(r13).addIf(4, !r0);
        boolean r8 = false;
        if (r1 == false) goto L6;
        if (r0 == true) goto L6;
        boolean r10 = true;
    L7:
        Player.Commands.Builder r9 = r7.addIf(5, r10);
        if (r2 == false) goto L11;
        if (r0 == true) goto L11;
        boolean r11 = true;
    L12:
        Player.Commands.Builder r14 = r9.addIf(6, r11);
        if (r6 == true) goto L19;
        if (r2 == true) goto L17;
        if (r4 == false) goto L17;
        if (r1 == false) goto L19;
    L17:
        if (r0 == true) goto L19;
        boolean r15 = true;
    L20:
        Player.Commands.Builder r16 = r14.addIf(7, r15);
        if (r3 == false) goto L24;
        if (r0 == true) goto L24;
        boolean r17 = true;
    L25:
        Player.Commands.Builder r18 = r16.addIf(8, r17);
        if (r6 == true) goto L32;
        if (r3 == true) goto L30;
        if (r4 == false) goto L32;
        if (r5 == false) goto L32;
    L30:
        if (r0 == true) goto L32;
        boolean r19 = true;
    L33:
        Player.Commands.Builder r20 = r18.addIf(9, r19).addIf(10, !r0);
        if (r1 == false) goto L37;
        if (r0 == true) goto L37;
        boolean r110 = true;
    L38:
        Player.Commands.Builder r21 = r20.addIf(11, r110);
        if (r1 == false) goto L43;
        if (r0 == true) goto L43;
        r8 = true;
    L43:
        return r21.addIf(12, r8).build();
    L37:
        r110 = false;
    L32:
        r19 = false;
    L24:
        r17 = false;
    L19:
        r15 = false;
    L11:
        r11 = false;
    L6:
        r10 = false;
        goto L7
    }

    public static int getBigEndianInt(ByteBuffer r3, int r4) {
        int r0 = r3.getInt(r4);
        if (r3.order() != ByteOrder.BIG_ENDIAN) goto L6;
        return r0;
    L6:
        return Integer.reverseBytes(r0);
    }

    public static int getByteDepth(int r1) {
        switch(r1) {
            case 2: goto L11;
            case 3: goto L9;
            case 4: goto L7;
            case 21: goto L5;
            case 22: goto L7;
            case 268435456: goto L11;
            case 1342177280: goto L5;
            case 1610612736: goto L7;
            default: goto L4;
        };
    L5:
        return 3;
    L7:
        return 4;
    L9:
        return 1;
    L11:
        return 2;
    L4:
        throw new IllegalArgumentException();
    }

    public static byte[] getBytesFromHexString(String r6) {
        byte[] r0 = new byte[r6.length() / 2];
        int r1 = 0;
    L4:
        if (r1 >= r0.length) goto L6;
        int r2 = r1 * 2;
        r0[r1] = (byte) ((Character.digit(r6.charAt(r2), 16) << 4) + Character.digit(r6.charAt(r2 + 1), 16));
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public static int getCodecCountOfType(String r6, int r7) {
        String[] r0 = splitCodecs(r6);
        int r1 = 0;
        int r2 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L8;
        if (r7 != MimeTypes.getTrackTypeOfCodec(r0[r3])) goto L7;
        r1 = r1 + 1;
    L7:
        r3 = r3 + 1;
        goto L3
    L8:
        return r1;
    }

    public static String getCodecsOfType(String r7, int r8) {
        String[] r0 = splitCodecs(r7);
        if (r0.length != 0) goto L5;
        return null;
    L5:
        StringBuilder r1 = new StringBuilder();
        int r3 = r0.length;
        int r4 = 0;
    L6:
        if (r4 >= r3) goto L15;
        String r5 = r0[r4];
        if (r8 != MimeTypes.getTrackTypeOfCodec(r5)) goto L13;
        if (r1.length() <= 0) goto L12;
        r1.append(",");
    L12:
        r1.append(r5);
    L13:
        r4 = r4 + 1;
        goto L6
    L15:
        if (r1.length() > 0) goto L17;
        return null;
    L17:
        return r1.toString();
    }

    public static String getCountryCode(Context r3) {
        if (r3 == null) goto L10;
        TelephonyManager r0 = (TelephonyManager) r3.getSystemService("phone");
        if (r0 == null) goto L10;
        String r1 = r0.getNetworkCountryIso();
        if (TextUtils.isEmpty(r1) == true) goto L10;
        return Ascii.toUpperCase(r1);
    L10:
        return Ascii.toUpperCase(Locale.getDefault().getCountry());
    }

    public static Point getCurrentDisplayModeSize(Context r3) {
        Display r0 = null;
        DisplayManager r1 = (DisplayManager) r3.getSystemService("display");
        if (r1 == null) goto L5;
        r0 = r1.getDisplay(0);
    L5:
        if (r0 != null) goto L8;
        r0 = ((WindowManager) Assertions.checkNotNull((WindowManager) r3.getSystemService("window"))).getDefaultDisplay();
    L8:
        return getCurrentDisplayModeSize(r3, r0);
    }

    public static Point getCurrentDisplayModeSize(Context r5, Display r6) {
        if (r6.getDisplayId() == 0) goto L5;
    L30:
        Point r0 = new Point();
        if (SDK_INT < 23) goto L33;
        getDisplaySizeV23(r6, r0);
    L34:
        return r0;
    L33:
        r6.getRealSize(r0);
        goto L34
    L5:
        if (isTv(r5) == false) goto L30;
        if (SDK_INT >= 28) goto L9;
        String r1 = getSystemProperty("sys.display-size");
    L11:
        if (TextUtils.isEmpty(r1) == true) goto L23;
        String[] r2 = split(r1.trim(), "x");     // Catch: NumberFormatException -> L20
        if (r2.length != 2) goto L21;
        int r3 = Integer.parseInt(r2[0]);     // Catch: NumberFormatException -> L20
        int r4 = Integer.parseInt(r2[1]);     // Catch: NumberFormatException -> L20
        if (r3 <= 0) goto L21;
        if (r4 <= 0) goto L21;
        return new Point(r3, r4);
    L21:
        Log.m44e(TAG, "Invalid display size: " + r1);
    L23:
        if ("Sony".equals(MANUFACTURER) == false) goto L30;
        if (MODEL.startsWith("BRAVIA") == false) goto L30;
        if (r5.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd") == false) goto L30;
        return new Point(3840, 2160);
    L9:
        r1 = getSystemProperty("vendor.display-size");
        goto L11
    }

    public static Looper getCurrentOrMainLooper() {
        Looper r0 = Looper.myLooper();
        if (r0 == null) goto L6;
        return r0;
    L6:
        return Looper.getMainLooper();
    }

    public static Uri getDataUriForString(String r3, String r4) {
        return Uri.parse("data:" + r3 + ";base64," + Base64.encodeToString(r4.getBytes(), 2));
    }

    public static Locale getDefaultDisplayLocale() {
        if (SDK_INT < 24) goto L6;
        return Locale.getDefault(Locale.Category.DISPLAY);
    L6:
        return Locale.getDefault();
    }

    private static void getDisplaySizeV23(Display r2, Point r3) {
        Display.Mode r0 = r2.getMode();
        r3.x = r0.getPhysicalWidth();
        r3.y = r0.getPhysicalHeight();
    }

    public static Drawable getDrawable(Context r1, Resources r2, int r3) {
        return r2.getDrawable(r3, r1.getTheme());
    }

    public static UUID getDrmUuid(String r2) {
        String r0 = Ascii.toLowerCase(r2);
        switch(r0.hashCode()) {
            case -1860423953: goto L12;
            case -1400551171: goto L9;
            case 790309106: goto L6;
            default: goto L14;
        };
    L14:
        byte r1 = -1;
    L15:
        switch(r1) {
            case 0: goto L23;
            case 1: goto L21;
            case 2: goto L19;
            default: goto L28;
        };
    L28:
        return UUID.fromString(r2);
    L26:
        return null;
    L19:
        return C0180C.CLEARKEY_UUID;
    L21:
        return C0180C.PLAYREADY_UUID;
    L23:
        return C0180C.WIDEVINE_UUID;
    L6:
        if (r0.equals("clearkey") == false) goto L14;
        r1 = 2;
        goto L15
    L9:
        if (r0.equals("widevine") == false) goto L14;
        r1 = 0;
        goto L15
    L12:
        if (r0.equals("playready") == false) goto L14;
        r1 = 1;
        goto L15
    }

    public static int getErrorCodeForMediaDrmErrorCode(int r1) {
        switch(r1) {
            case 2: goto L11;
            case 3: goto L3;
            case 4: goto L11;
            case 5: goto L3;
            case 6: goto L3;
            case 7: goto L11;
            case 8: goto L9;
            case 9: goto L3;
            case 10: goto L7;
            case 11: goto L3;
            case 12: goto L3;
            case 13: goto L3;
            case 14: goto L3;
            case 15: goto L9;
            case 16: goto L11;
            case 17: goto L7;
            case 18: goto L11;
            case 19: goto L7;
            case 20: goto L7;
            case 21: goto L7;
            case 22: goto L7;
            case 23: goto L3;
            case 24: goto L5;
            case 25: goto L5;
            case 26: goto L5;
            case 27: goto L5;
            case 28: goto L5;
            default: goto L3;
        };
    L3:
        return PlaybackException.ERROR_CODE_DRM_SYSTEM_ERROR;
    L5:
        return PlaybackException.ERROR_CODE_DRM_PROVISIONING_FAILED;
    L7:
        return PlaybackException.ERROR_CODE_DRM_LICENSE_ACQUISITION_FAILED;
    L9:
        return PlaybackException.ERROR_CODE_DRM_CONTENT_ERROR;
    L11:
        return PlaybackException.ERROR_CODE_DRM_DISALLOWED_OPERATION;
    }

    public static int getErrorCodeFromPlatformDiagnosticsInfo(String r6) {
        if (r6 != null) goto L5;
        return 0;
    L5:
        String[] r1 = split(r6, "_");
        int r2 = r1.length;
        if (r2 >= 2) goto L8;
        return 0;
    L8:
        String r3 = r1[r2 - 1];
        if (r2 >= 3) goto L11;
    L13:
        boolean r4 = false;
    L21:
        int r0 = Integer.parseInt((String) Assertions.checkNotNull(r3));     // Catch: NumberFormatException -> L19
        if (r4 == false) goto L18;
        return -r0;
    L18:
        return r0;
    L20:
        return 0;
    L11:
        if ("neg".equals(r1[r2 - 2]) == false) goto L13;
        r4 = true;
        goto L21
    }

    public static String getFormatSupportString(int r1) {
        switch(r1) {
            case 0: goto L13;
            case 1: goto L11;
            case 2: goto L9;
            case 3: goto L7;
            case 4: goto L5;
            default: goto L4;
        };
    L5:
        return "YES";
    L7:
        return "NO_EXCEEDS_CAPABILITIES";
    L9:
        return "NO_UNSUPPORTED_DRM";
    L11:
        return "NO_UNSUPPORTED_TYPE";
    L13:
        return "NO";
    L4:
        throw new IllegalStateException();
    }

    public static int getIntegerCodeForString(String r4) {
        int r0 = r4.length();
        if (r0 > 4) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkArgument(r1);
        int r2 = 0;
        int r3 = 0;
    L7:
        if (r3 >= r0) goto L9;
        r2 = (r2 << 8) | r4.charAt(r3);
        r3 = r3 + 1;
        goto L7
    L9:
        return r2;
    L5:
        r1 = false;
        goto L6
    }

    public static String getLocaleLanguageTag(Locale r1) {
        return r1.toLanguageTag();
    }

    public static int getMaxPendingFramesCountForMediaCodecDecoders(Context r1) {
        if (isFrameDropAllowedOnSurfaceInput(r1) == false) goto L6;
        return 1;
    L6:
        return 5;
    }

    public static long getMediaDurationForPlayoutDuration(long r4, float r6) {
        if (r6 != 1.0f) goto L6;
        return r4;
    L6:
        return Math.round(r4 * ((double) r6));
    }

    public static long getNowUnixTimeMs(long r2) {
        if (r2 != C0180C.TIME_UNSET) goto L6;
        return System.currentTimeMillis();
    L6:
        return android.os.SystemClock.elapsedRealtime() + r2;
    }

    public static int getPcmEncoding(int r1) {
        switch(r1) {
            case 8: goto L11;
            case 16: goto L9;
            case 24: goto L7;
            case 32: goto L5;
            default: goto L3;
        };
    L3:
        return 0;
    L5:
        return 22;
    L7:
        return 21;
    L9:
        return 2;
    L11:
        return 3;
    }

    public static Format getPcmFormat(int r2, int r3, int r4) {
        return new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_RAW).setChannelCount(r3).setSampleRate(r4).setPcmEncoding(r2).build();
    }

    public static Format getPcmFormat(AudioProcessor.AudioFormat r3) {
        return getPcmFormat(r3.encoding, r3.channelCount, r3.sampleRate);
    }

    public static int getPcmFrameSize(int r1, int r2) {
        return getByteDepth(r1) * r2;
    }

    public static long getPlayoutDurationForMediaDuration(long r4, float r6) {
        if (r6 != 1.0f) goto L6;
        return r4;
    L6:
        return Math.round(r4 / ((double) r6));
    }

    public static List<String> getRoleFlagStrings(int r2) {
        ArrayList r0 = new ArrayList();
        if ((r2 & 1) == 0) goto L6;
        r0.add("main");
    L6:
        if ((r2 & 2) == 0) goto L9;
        r0.add("alt");
    L9:
        if ((r2 & 4) == 0) goto L12;
        r0.add("supplementary");
    L12:
        if ((r2 & 8) == 0) goto L15;
        r0.add("commentary");
    L15:
        if ((r2 & 16) == 0) goto L18;
        r0.add("dub");
    L18:
        if ((r2 & 32) == 0) goto L21;
        r0.add("emergency");
    L21:
        if ((r2 & 64) == 0) goto L24;
        r0.add("caption");
    L24:
        if ((r2 & 128) == 0) goto L27;
        r0.add("subtitle");
    L27:
        if ((r2 & 256) == 0) goto L30;
        r0.add("sign");
    L30:
        if ((r2 & 512) == 0) goto L33;
        r0.add("describes-video");
    L33:
        if ((r2 & 1024) == 0) goto L36;
        r0.add("describes-music");
    L36:
        if ((r2 & 2048) == 0) goto L39;
        r0.add("enhanced-intelligibility");
    L39:
        if ((r2 & 4096) == 0) goto L42;
        r0.add("transcribes-dialog");
    L42:
        if ((r2 & 8192) == 0) goto L45;
        r0.add("easy-read");
    L45:
        if ((r2 & 16384) == 0) goto L48;
        r0.add("trick-play");
    L48:
        if ((32768 & r2) == 0) goto L50;
        r0.add("auxiliary");
    L50:
        return r0;
    }

    public static List<String> getSelectionFlagStrings(int r2) {
        ArrayList r0 = new ArrayList();
        if ((r2 & 4) == 0) goto L6;
        r0.add(TtmlNode.TEXT_EMPHASIS_AUTO);
    L6:
        if ((r2 & 1) == 0) goto L9;
        r0.add("default");
    L9:
        if ((r2 & 2) == 0) goto L11;
        r0.add("forced");
    L11:
        return r0;
    }

    public static int getStreamTypeForAudioUsage(int r1) {
        switch(r1) {
            case 1: goto L17;
            case 2: goto L15;
            case 3: goto L13;
            case 4: goto L11;
            case 5: goto L9;
            case 6: goto L7;
            case 7: goto L9;
            case 8: goto L9;
            case 9: goto L9;
            case 10: goto L9;
            case 11: goto L4;
            case 12: goto L17;
            case 13: goto L5;
            case 14: goto L17;
            default: goto L4;
        };
    L4:
        return 3;
    L5:
        return 1;
    L7:
        return 2;
    L9:
        return 5;
    L11:
        return 4;
    L13:
        return 8;
    L15:
        return 0;
    L17:
        return 3;
    }

    public static String getStringForTime(StringBuilder r19, Formatter r20, long r21) {
        if (r21 != C0180C.TIME_UNSET) goto L5;
        long r1 = 0;
    L7:
        if (r1 >= 0) goto L9;
        String r5 = "-";
    L10:
        long r6 = (500 + Math.abs(r1)) / 1000;
        long r10 = r6 % 60;
        long r12 = (r6 / 60) % 60;
        long r8 = r6 / 3600;
        r19.setLength(0);
        if (r8 <= 0) goto L14;
        return r20.format("%s%d:%02d:%02d", new Object[]{r5, Long.valueOf(r8), Long.valueOf(r12), Long.valueOf(r10)}).toString();
    L14:
        return r20.format("%s%02d:%02d", new Object[]{r5, Long.valueOf(r12), Long.valueOf(r10)}).toString();
    L9:
        r5 = "";
        goto L10
    L5:
        r1 = r21;
        goto L7
    }

    public static String[] getSystemLanguageCodes() {
        String[] r0 = getSystemLocales();
        int r1 = 0;
    L4:
        if (r1 >= r0.length) goto L6;
        r0[r1] = normalizeLanguageCode(r0[r1]);
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    private static String[] getSystemLocales() {
        Configuration r0 = Resources.getSystem().getConfiguration();
        if (SDK_INT < 24) goto L6;
        return getSystemLocalesV24(r0);
    L6:
        return new String[]{getLocaleLanguageTag(r0.locale)};
    }

    private static String[] getSystemLocalesV24(Configuration r2) {
        return split(r2.getLocales().toLanguageTags(), ",");
    }

    private static String getSystemProperty(String r6) {
        Class<?> r0 = Class.forName("android.os.SystemProperties");     // Catch: Exception -> L4
        return (String) r0.getMethod("get", new Class[]{String.class}).invoke(r0, new Object[]{r6});
    L4:
        e = move-exception;
        Log.m45e(TAG, "Failed to read system property " + r6, e);
        return null;
    }

    public static String getTrackTypeString(int r2) {
        switch(r2) {
            case -2: goto L22;
            case -1: goto L20;
            case 0: goto L18;
            case 1: goto L16;
            case 2: goto L14;
            case 3: goto L12;
            case 4: goto L10;
            case 5: goto L8;
            case 6: goto L6;
            default: goto L4;
        };
    L6:
        return "camera motion";
    L8:
        return TtmlNode.TAG_METADATA;
    L10:
        return "image";
    L12:
        return "text";
    L14:
        return MimeTypes.BASE_TYPE_VIDEO;
    L16:
        return MimeTypes.BASE_TYPE_AUDIO;
    L18:
        return "default";
    L20:
        return EnvironmentCompat.MEDIA_UNKNOWN;
    L22:
        return "none";
    L4:
        if (r2 >= 10000) goto L26;
        return "?";
    L26:
        return "custom (" + r2 + ")";
    }

    public static String getUserAgent(Context r3, String r4) {
        String r2 = r3.getPackageManager().getPackageInfo(r3.getPackageName(), 0).versionName;     // Catch: PackageManager.NameNotFoundException -> L4
    L7:
        return r4 + "/" + r2 + " (Linux;Android " + Build.VERSION.RELEASE + ") " + MediaLibraryInfo.VERSION_SLASHY;
    L5:
        r2 = "?";
        goto L7
    }

    public static byte[] getUtf8Bytes(String r1) {
        return r1.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] gzip(byte[] r4) {
        ByteArrayOutputStream r0 = new ByteArrayOutputStream();
        GZIPOutputStream r1 = new GZIPOutputStream(r0);     // Catch: IOException -> L14
        r1.write(r4);     // Catch: Throwable -> L8
        r1.close();     // Catch: IOException -> L14
        return r0.toByteArray();
    L8:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L11
    L13:
        throw th;     // Catch: IOException -> L14
    L11:
        th = move-exception;
        th.addSuppressed(th);     // Catch: IOException -> L14
    L14:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    public static boolean handlePauseButtonAction(Player r2) {
        if (r2 != null) goto L4;
        return false;
    L4:
        if (r2.isCommandAvailable(1) == false) goto L9;
        r2.pause();
        return true;
    L9:
        return false;
    }

    public static boolean handlePlayButtonAction(Player r4) {
        if (r4 != null) goto L5;
        return false;
    L5:
        int r0 = r4.getPlaybackState();
        boolean r1 = false;
        if (r0 != 1) goto L11;
        if (r4.isCommandAvailable(2) == false) goto L11;
        r4.prepare();
        r1 = true;
    L16:
        if (r4.isCommandAvailable(1) == false) goto L19;
        r4.play();
        return true;
    L19:
        return r1;
    L11:
        if (r0 != 4) goto L16;
        if (r4.isCommandAvailable(4) == false) goto L16;
        r4.seekToDefaultPosition();
        r1 = true;
        goto L16
    }

    public static boolean handlePlayPauseButtonAction(Player r1) {
        return handlePlayPauseButtonAction(r1, true);
    }

    public static boolean handlePlayPauseButtonAction(Player r1, boolean r2) {
        if (shouldShowPlayButton(r1, r2) == false) goto L7;
        return handlePlayButtonAction(r1);
    L7:
        return handlePauseButtonAction(r1);
    }

    public static int inferContentType(Uri r7) {
        String r0 = r7.getScheme();
        if (r0 != null) goto L5;
    L8:
        String r1 = r7.getLastPathSegment();
        if (r1 != null) goto L11;
        return 4;
    L11:
        int r3 = r1.lastIndexOf(46);
        if (r3 < 0) goto L16;
        int r4 = inferContentTypeForExtension(r1.substring(r3 + 1));
        if (r4 == 4) goto L16;
        return r4;
    L16:
        Matcher r5 = ISM_PATH_PATTERN.matcher((CharSequence) Assertions.checkNotNull(r7.getPath()));
        if (r5.matches() == false) goto L29;
        String r6 = r5.group(2);
        if (r6 != null) goto L21;
        return 1;
    L21:
        if (r6.contains(ISM_DASH_FORMAT_EXTENSION) == false) goto L25;
        return 0;
    L25:
        if (r6.contains(ISM_HLS_FORMAT_EXTENSION) == false) goto L30;
        return 2;
    L30:
        return 1;
    L29:
        return 4;
    L5:
        if (Ascii.equalsIgnoreCase("rtsp", r0) == false) goto L8;
        return 3;
    }

    @Deprecated
    public static int inferContentType(Uri r1, String r2) {
        if (TextUtils.isEmpty(r2) == false) goto L6;
        return inferContentType(r1);
    L6:
        return inferContentTypeForExtension(r2);
    }

    @Deprecated
    public static int inferContentType(String r2) {
        return inferContentType(Uri.parse("file:///" + r2));
    }

    public static int inferContentTypeForExtension(String r4) {
        String r5 = Ascii.toLowerCase(r4);
        switch(r5.hashCode()) {
            case 104579: goto L15;
            case 108321: goto L12;
            case 3242057: goto L9;
            case 3299913: goto L6;
            default: goto L17;
        };
    L17:
        byte r0 = -1;
    L18:
        switch(r0) {
            case 0: goto L23;
            case 1: goto L22;
            case 2: goto L21;
            case 3: goto L21;
            default: goto L19;
        };
    L19:
        return 4;
    L21:
        return 1;
    L22:
        return 2;
    L23:
        return 0;
    L6:
        if (r5.equals("m3u8") == false) goto L17;
        r0 = 1;
        goto L18
    L9:
        if (r5.equals("isml") == false) goto L17;
        r0 = 3;
        goto L18
    L12:
        if (r5.equals("mpd") == false) goto L17;
        r0 = 0;
        goto L18
    L15:
        if (r5.equals("ism") == false) goto L17;
        r0 = 2;
        goto L18
    }

    public static int inferContentTypeForUriAndMimeType(Uri r5, String r6) {
        if (r6 != null) goto L6;
        return inferContentType(r5);
    L6:
        switch(r6.hashCode()) {
            case -979127466: goto L18;
            case -156749520: goto L15;
            case 64194685: goto L12;
            case 1154777587: goto L9;
            default: goto L20;
        };
    L20:
        byte r0 = -1;
    L21:
        switch(r0) {
            case 0: goto L27;
            case 1: goto L26;
            case 2: goto L25;
            case 3: goto L24;
            default: goto L22;
        };
    L22:
        return 4;
    L24:
        return 3;
    L25:
        return 1;
    L26:
        return 2;
    L27:
        return 0;
    L9:
        if (r6.equals(MimeTypes.APPLICATION_RTSP) == false) goto L20;
        r0 = 3;
        goto L21
    L12:
        if (r6.equals(MimeTypes.APPLICATION_MPD) == false) goto L20;
        r0 = 0;
        goto L21
    L15:
        if (r6.equals(MimeTypes.APPLICATION_SS) == false) goto L20;
        r0 = 2;
        goto L21
    L18:
        if (r6.equals(MimeTypes.APPLICATION_M3U8) == false) goto L20;
        r0 = 1;
        goto L21
    }

    public static boolean inflate(ParsableByteArray r4, ParsableByteArray r5, Inflater r6) {
        if (r4.bytesLeft() > 0) goto L6;
        return false;
    L6:
        if (r5.capacity() >= r4.bytesLeft()) goto L8;
        r5.ensureCapacity(r4.bytesLeft() * 2);
    L8:
        if (r6 != null) goto L10;
        r6 = new Inflater();
    L10:
        r6.setInput(r4.getData(), r4.getPosition(), r4.bytesLeft());
        int r0 = 0;
    L34:
        r0 = r0 + r6.inflate(r5.getData(), r0, r5.capacity() - r0);     // Catch: Throwable -> L28 DataFormatException -> L31
        if (r6.finished() == true) goto L14;
        if (r6.needsDictionary() == true) goto L26;
        if (r6.needsInput() == true) goto L26;
        if (r0 != r5.capacity()) goto L34;
        r5.ensureCapacity(r5.capacity() * 2);     // Catch: Throwable -> L28 DataFormatException -> L31
    L26:
        r6.reset();
        return false;
    L14:
        r5.setLimit(r0);     // Catch: Throwable -> L28 DataFormatException -> L31
        r6.reset();
        return true;
    L28:
        th = move-exception;
        r6.reset();
        throw th;
    L32:
        r6.reset();
        return false;
    }

    public static String intToStringMaxRadix(int r1) {
        return Integer.toString(r1, 36);
    }

    private static boolean isAppSpecificStorageFileUri(Activity r7, Uri r8) {
        String r1 = r8.getPath();     // Catch: IOException -> L19
        if (r1 != null) goto L6;
        return false;
    L6:
        String r2 = new File(r1).getCanonicalPath();     // Catch: IOException -> L19
        String r3 = r7.getFilesDir().getCanonicalPath();     // Catch: IOException -> L19
        String r4 = null;
        File r5 = r7.getExternalFilesDir(null);     // Catch: IOException -> L19
        if (r5 == null) goto L11;
        r4 = r5.getCanonicalPath();     // Catch: IOException -> L19
    L11:
        if (r2.startsWith(r3) == true) goto L17;
        if (r4 == null) goto L23;
        if (r2.startsWith(r4) == true) goto L17;
        return false;
    L23:
        return false;
    L17:
        return true;
    L20:
        return false;
    }

    public static boolean isAutomotive(Context r2) {
        if (SDK_INT >= 23) goto L5;
    L7:
        return false;
    L5:
        if (r2.getPackageManager().hasSystemFeature("android.hardware.type.automotive") == false) goto L7;
        return true;
    }

    public static boolean isBitmapFactorySupportedMimeType(String r4) {
        switch(r4.hashCode()) {
            case -1487656890: goto L24;
            case -1487464693: goto L21;
            case -1487464690: goto L18;
            case -1487394660: goto L15;
            case -1487018032: goto L12;
            case -879272239: goto L9;
            case -879258763: goto L6;
            default: goto L26;
        };
    L26:
        byte r0 = -1;
    L27:
        switch(r0) {
            case 0: goto L37;
            case 1: goto L37;
            case 2: goto L37;
            case 3: goto L37;
            case 4: goto L34;
            case 5: goto L34;
            case 6: goto L30;
            default: goto L28;
        };
    L28:
        return false;
    L37:
        return true;
    L30:
        if (SDK_INT >= 34) goto L32;
        return false;
    L32:
        return true;
    L34:
        if (SDK_INT >= 26) goto L36;
        return false;
    L36:
        return true;
    L6:
        if (r4.equals(MimeTypes.IMAGE_PNG) == false) goto L26;
        r0 = 0;
        goto L27
    L9:
        if (r4.equals(MimeTypes.IMAGE_BMP) == false) goto L26;
        r0 = 2;
        goto L27
    L12:
        if (r4.equals(MimeTypes.IMAGE_WEBP) == false) goto L26;
        r0 = 3;
        goto L27
    L15:
        if (r4.equals(MimeTypes.IMAGE_JPEG) == false) goto L26;
        r0 = 1;
        goto L27
    L18:
        if (r4.equals(MimeTypes.IMAGE_HEIF) == false) goto L26;
        r0 = 4;
        goto L27
    L21:
        if (r4.equals(MimeTypes.IMAGE_HEIC) == false) goto L26;
        r0 = 5;
        goto L27
    L24:
        if (r4.equals(MimeTypes.IMAGE_AVIF) == false) goto L26;
        r0 = 6;
        goto L27
    }

    public static boolean isEncodingHighResolutionPcm(int r1) {
        if (r1 != 21) goto L5;
    L14:
        return true;
    L5:
        if (r1 == 1342177280) goto L14;
        if (r1 == 22) goto L14;
        if (r1 == 1610612736) goto L14;
        if (r1 == 4) goto L14;
        return false;
    }

    public static boolean isEncodingLinearPcm(int r1) {
        if (r1 != 3) goto L5;
    L20:
        return true;
    L5:
        if (r1 == 2) goto L20;
        if (r1 == 268435456) goto L20;
        if (r1 == 21) goto L20;
        if (r1 == 1342177280) goto L20;
        if (r1 == 22) goto L20;
        if (r1 == 1610612736) goto L20;
        if (r1 == 4) goto L20;
        return false;
    }

    public static boolean isFrameDropAllowedOnSurfaceInput(Context r2) {
        if (SDK_INT >= 29) goto L5;
    L18:
        return true;
    L5:
        if (r2.getApplicationInfo().targetSdkVersion < 29) goto L18;
        if (SDK_INT != 30) goto L13;
        if (Ascii.equalsIgnoreCase(MODEL, "moto g(20)") == true) goto L18;
        if (Ascii.equalsIgnoreCase(MODEL, "rmx3231") == true) goto L18;
    L13:
        if (SDK_INT == 34) goto L15;
    L17:
        return false;
    L15:
        if (Ascii.equalsIgnoreCase(MODEL, "sm-x200") == false) goto L17;
        goto L17
    }

    public static boolean isLinebreak(int r1) {
        if (r1 != 10) goto L5;
    L8:
        return true;
    L5:
        if (r1 == 13) goto L8;
        return false;
    }

    public static boolean isLocalFileUri(Uri r2) {
        String r0 = r2.getScheme();
        if (TextUtils.isEmpty(r0) == false) goto L5;
    L8:
        return true;
    L5:
        if ("file".equals(r0) == true) goto L8;
        return false;
    }

    private static boolean isMediaStoreExternalContentUri(Uri r4) {
        if ("content".equals(r4.getScheme()) == true) goto L5;
    L16:
        return false;
    L5:
        if ("media".equals(r4.getAuthority()) == false) goto L16;
        List<String> r0 = r4.getPathSegments();
        if (r0.isEmpty() == false) goto L10;
        return false;
    L10:
        String r2 = r0.get(0);
        if ("external".equals(r2) == false) goto L13;
    L14:
        return true;
    L13:
        if ("external_primary".equals(r2) == true) goto L14;
        return false;
    }

    private static boolean isReadStoragePermissionRequestNeeded(Activity r3, Uri r4) {
        if (SDK_INT >= 23) goto L6;
        return false;
    L6:
        if (isLocalFileUri(r4) == false) goto L10;
        return !isAppSpecificStorageFileUri(r3, r4);
    L10:
        if (isMediaStoreExternalContentUri(r4) == false) goto L12;
        return true;
    L12:
        return false;
    }

    public static boolean isRunningOnEmulator() {
        String r0 = Ascii.toLowerCase(DEVICE);
        if (r0.contains("emulator") == false) goto L5;
    L12:
        return true;
    L5:
        if (r0.contains("emu64a") == true) goto L12;
        if (r0.contains("emu64x") == true) goto L12;
        if (r0.contains("generic") == true) goto L12;
        return false;
    }

    private static boolean isTrafficRestricted(Uri r2) {
        if ("http".equals(r2.getScheme()) == true) goto L5;
    L7:
        return false;
    L5:
        if (NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted((String) Assertions.checkNotNull(r2.getHost())) == true) goto L7;
        return true;
    }

    public static boolean isTv(Context r3) {
        UiModeManager r0 = (UiModeManager) r3.getApplicationContext().getSystemService("uimode");
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.getCurrentModeType() != 4) goto L7;
        return true;
    }

    public static boolean isWear(Context r2) {
        return r2.getPackageManager().hasSystemFeature("android.hardware.type.watch");
    }

    static /* synthetic */ Thread lambda$newSingleThreadExecutor$3(String r1, Runnable r2) {
        return new Thread(r2, r1);
    }

    static /* synthetic */ Thread lambda$newSingleThreadScheduledExecutor$4(String r1, Runnable r2) {
        return new Thread(r2, r1);
    }

    static /* synthetic */ void lambda$postOrRunWithCompletion$0(SettableFuture r1, Runnable r2, Object r3) {
    L7:
        th = move-exception;
        r1.setException(th);
        return;
    L3:
        if (r1.isCancelled() == false) goto L5;
        return;
    L5:
        r2.run();     // Catch: Throwable -> L7
        r1.set(r3);     // Catch: Throwable -> L7
    }

    static /* synthetic */ void lambda$transformFutureAsync$1(SettableFuture r1, ListenableFuture r2) {
        if (r1.isCancelled() == false) goto L6;
        r2.cancel(false);
        return;
    }

    static /* synthetic */ void lambda$transformFutureAsync$2(ListenableFuture r3, SettableFuture r4, AsyncFunction r5) {
        r4.setFuture(r5.apply(Futures.getDone(r3)));     // Catch: Throwable -> L5
        return;
    L5:
        th = move-exception;
        r4.setException(th);
        return;
    L8:
        e = e;
    L11:
        r4.setException(e);
        return;
    L10:
        e = e;
    L13:
        e = move-exception;
        Throwable r1 = e.getCause();
        if (r1 != null) goto L17;
        Throwable r2 = e;
    L18:
        r4.setException(r2);
        return;
    L17:
        r2 = r1;
    L21:
        r4.cancel(false);
    }

    public static int linearSearch(int[] r2, int r3) {
        int r0 = 0;
    L4:
        if (r0 >= r2.length) goto L9;
        if (r2[r0] == r3) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L9:
        return -1;
    }

    public static int linearSearch(long[] r3, long r4) {
        int r0 = 0;
    L4:
        if (r0 >= r3.length) goto L9;
        if (r3[r0] == r4) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L9:
        return -1;
    }

    public static String loadAsset(Context r2, String r3) throws IOException {
        InputStream r0 = null;
        r0 = r2.getAssets().open(r3);     // Catch: Throwable -> L6
        String r1 = fromUtf8Bytes(ByteStreams.toByteArray(r0));     // Catch: Throwable -> L6
        closeQuietly(r0);
        return r1;
    L6:
        th = move-exception;
        closeQuietly(r0);
        throw th;
    }

    public static long maxValue(SparseLongArray r5) {
        if (r5.size() == 0) goto L10;
        long r0 = Long.MIN_VALUE;
        int r2 = 0;
    L6:
        if (r2 >= r5.size()) goto L8;
        r0 = Math.max(r0, r5.valueAt(r2));
        r2 = r2 + 1;
        goto L6
    L8:
        return r0;
    L10:
        throw new NoSuchElementException();
    }

    private static String maybeReplaceLegacyLanguageTags(String r4) {
        int r0 = 0;
    L4:
        if (r0 >= isoLegacyTagReplacements.length) goto L10;
        if (r4.startsWith(isoLegacyTagReplacements[r0]) == true) goto L8;
        r0 = r0 + 2;
        goto L4
    L8:
        return isoLegacyTagReplacements[r0 + 1] + r4.substring(isoLegacyTagReplacements[r0].length());
    L10:
        return r4;
    }

    @Deprecated
    public static boolean maybeRequestReadExternalStoragePermission(Activity r5, Uri... r6) {
        int r0 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L9;
        if (maybeRequestReadStoragePermission(r5, r6[r2]) == true) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    @Deprecated
    public static boolean maybeRequestReadExternalStoragePermission(Activity r1, MediaItem... r2) {
        return maybeRequestReadStoragePermission(r1, r2);
    }

    private static boolean maybeRequestReadStoragePermission(Activity r2, Uri r3) {
        if (isReadStoragePermissionRequestNeeded(r2, r3) == true) goto L7;
        return false;
    L7:
        if (SDK_INT >= 33) goto L11;
        return requestExternalStoragePermission(r2);
    L11:
        return requestReadMediaPermissions(r2);
    }

    public static boolean maybeRequestReadStoragePermission(Activity r8, MediaItem... r9) {
        if (SDK_INT >= 23) goto L5;
        return false;
    L5:
        int r0 = r9.length;
        int r1 = 0;
    L6:
        if (r1 >= r0) goto L21;
        MediaItem r3 = r9[r1];
        if (r3.localConfiguration == null) goto L20;
        if (maybeRequestReadStoragePermission(r8, r3.localConfiguration.uri) == true) goto L12;
        ImmutableList<MediaItem.SubtitleConfiguration> r4 = r3.localConfiguration.subtitleConfigurations;
        int r6 = 0;
    L15:
        if (r6 >= r4.size()) goto L20;
        if (maybeRequestReadStoragePermission(r8, r4.get(r6).uri) == true) goto L18;
        r6 = r6 + 1;
        goto L15
    L18:
        return true;
    L12:
        return true;
    L20:
        r1 = r1 + 1;
        goto L6
    L21:
        return false;
    }

    public static long minValue(SparseLongArray r5) {
        if (r5.size() == 0) goto L10;
        long r0 = Long.MAX_VALUE;
        int r2 = 0;
    L6:
        if (r2 >= r5.size()) goto L8;
        r0 = Math.min(r0, r5.valueAt(r2));
        r2 = r2 + 1;
        goto L6
    L8:
        return r0;
    L10:
        throw new NoSuchElementException();
    }

    public static <T> void moveItems(List<T> r4, int r5, int r6, int r7) {
        ArrayDeque r0 = new ArrayDeque();
        int r2 = (r6 - r5) - 1;
    L3:
        if (r2 < 0) goto L5;
        r0.addFirst(r4.remove(r5 + r2));
        r2 = r2 - 1;
        goto L3
    L5:
        r4.addAll(Math.min(r7, r4.size()), r0);
    }

    public static long msToUs(long r2) {
        if (r2 == C0180C.TIME_UNSET) goto L9;
        if (r2 == Long.MIN_VALUE) goto L9;
        return 1000 * r2;
    L9:
        return r2;
    }

    public static ExecutorService newSingleThreadExecutor(final String r1) {
        return Executors.newSingleThreadExecutor(new Util$$ExternalSyntheticLambda2(r1));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(final String r1) {
        return Executors.newSingleThreadScheduledExecutor(new Util$$ExternalSyntheticLambda4(r1));
    }

    public static String normalizeLanguageCode(String r5) {
        if (r5 != null) goto L5;
        return null;
    L5:
        String r0 = r5.replace('_', '-');
        if (r0.isEmpty() == false) goto L8;
    L9:
        r0 = r5;
    L10:
        String r1 = Ascii.toLowerCase(r0);
        String r2 = splitAtFirst(r1, "-")[0];
        if (languageTagReplacementMap != null) goto L13;
        languageTagReplacementMap = createIsoLanguageReplacementMap();
    L13:
        String r3 = languageTagReplacementMap.get(r2);
        if (r3 == null) goto L17;
        r1 = r3 + r1.substring(r2.length());
        r2 = r3;
    L17:
        if ("no".equals(r2) == true) goto L23;
        if (CmcdData.Factory.OBJECT_TYPE_INIT_SEGMENT.equals(r2) == true) goto L23;
        if ("zh".equals(r2) == true) goto L23;
        return r1;
    L23:
        return maybeReplaceLegacyLanguageTags(r1);
    L8:
        if (r0.equals(C0180C.LANGUAGE_UNDETERMINED) == false) goto L10;
        goto L9
    }

    public static <T> T[] nullSafeArrayAppend(T[] r2, T r3) {
        Object[] r0 = Arrays.copyOf(r2, r2.length + 1);
        r0[r2.length] = r3;
        return (T[]) castNonNullTypeArray(r0);
    }

    public static <T> T[] nullSafeArrayConcatenation(T[] r4, T[] r5) {
        T[] r0 = (T[]) Arrays.copyOf(r4, r4.length + r5.length);
        System.arraycopy(r5, 0, r0, r4.length, r5.length);
        return r0;
    }

    public static <T> T[] nullSafeArrayCopy(T[] r1, int r2) {
        if (r2 > r1.length) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        return (T[]) Arrays.copyOf(r1, r2);
    L5:
        r0 = false;
        goto L6
    }

    public static <T> T[] nullSafeArrayCopyOfRange(T[] r3, int r4, int r5) {
        boolean r0 = true;
        if (r4 < 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2);
        if (r5 <= r3.length) goto L10;
        r0 = false;
    L10:
        Assertions.checkArgument(r0);
        return (T[]) Arrays.copyOfRange(r3, r4, r5);
    L5:
        r2 = false;
        goto L6
    }

    public static <T> void nullSafeListToArray(List<T> r2, T[] r3) {
        if (r2.size() != r3.length) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        r2.toArray(r3);
        return;
    L5:
        r0 = false;
        goto L6
    }

    public static long parseXsDateTime(String r11) throws ParserException {
        Matcher r0 = XS_DATE_TIME_PATTERN.matcher(r11);
        if (r0.matches() == false) goto L21;
        if (r0.group(9) != null) goto L8;
        int r1 = 0;
    L13:
        GregorianCalendar r2 = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        r2.clear();
        r2.set(Integer.parseInt(r0.group(1)), Integer.parseInt(r0.group(2)) - 1, Integer.parseInt(r0.group(3)), Integer.parseInt(r0.group(4)), Integer.parseInt(r0.group(5)), Integer.parseInt(r0.group(6)));
        if (TextUtils.isEmpty(r0.group(8)) == true) goto L16;
        r2.set(14, new BigDecimal("0." + r0.group(8)).movePointRight(3).intValue());
    L16:
        long r3 = r2.getTimeInMillis();
        if (r1 != 0) goto L19;
        return r3;
    L19:
        return r3 - (((long) r1) * 60000);
    L8:
        if (r0.group(9).equalsIgnoreCase("Z") == false) goto L10;
        r1 = 0;
        goto L13
    L10:
        r1 = (Integer.parseInt(r0.group(12)) * 60) + Integer.parseInt(r0.group(13));
        if ("-".equals(r0.group(11)) == false) goto L13;
        r1 = r1 * (-1);
        goto L13
    L21:
        throw ParserException.createForMalformedContainer("Invalid date/time format: " + r11, null);
    }

    public static long parseXsDuration(String r19) {
        Matcher r0 = XS_DURATION_PATTERN.matcher(r19);
        if (r0.matches() == false) goto L33;
        boolean r2 = true ^ TextUtils.isEmpty(r0.group(1));
        String r7 = r0.group(3);
        double r8 = 0.0d;
        if (r7 == null) goto L7;
        double r10 = Double.parseDouble(r7) * 3.1556908E7d;
    L8:
        String r12 = r0.group(5);
        if (r12 == null) goto L11;
        double r13 = Double.parseDouble(r12) * 2629739.0d;
    L12:
        double r11 = r10 + r13;
        String r14 = r0.group(7);
        if (r14 == null) goto L15;
        double r15 = Double.parseDouble(r14) * 86400.0d;
    L16:
        double r16 = r11 + r15;
        String r17 = r0.group(10);
        if (r17 == null) goto L19;
        double r18 = Double.parseDouble(r17) * 3600.0d;
    L20:
        double r110 = r16 + r18;
        String r5 = r0.group(12);
        if (r5 == null) goto L23;
        double r111 = Double.parseDouble(r5) * 60.0d;
    L24:
        double r112 = r110 + r111;
        String r6 = r0.group(14);
        if (r6 == null) goto L27;
        r8 = Double.parseDouble(r6);
    L27:
        long r3 = (long) (1000.0d * (r112 + r8));
        if (r2 == false) goto L31;
        return -r3;
    L31:
        return r3;
    L23:
        r111 = 0.0d;
        goto L24
    L19:
        r18 = 0.0d;
        goto L20
    L15:
        r15 = 0.0d;
        goto L16
    L11:
        r13 = 0.0d;
        goto L12
    L7:
        r10 = 0.0d;
        goto L8
    L33:
        return (long) ((Double.parseDouble(r19) * 3600.0d) * 1000.0d);
    }

    public static boolean postOrRun(Handler r3, Runnable r4) {
        if (r3.getLooper().getThread().isAlive() == true) goto L7;
        return false;
    L7:
        if (r3.getLooper() != Looper.myLooper()) goto L11;
        r4.run();
        return true;
    L11:
        return r3.post(r4);
    }

    public static <T> ListenableFuture<T> postOrRunWithCompletion(Handler r2, final Runnable r3, final T r4) {
        final SettableFuture r0 = SettableFuture.create();
        postOrRun(r2, new Util$$ExternalSyntheticLambda3(r0, r3, r4));
        return r0;
    }

    public static boolean readBoolean(Parcel r1) {
        if (r1.readInt() == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static void recursiveDelete(File r4) {
        File[] r0 = r4.listFiles();
        if (r0 == null) goto L7;
        int r1 = r0.length;
        int r2 = 0;
    L5:
        if (r2 >= r1) goto L7;
        recursiveDelete(r0[r2]);
        r2 = r2 + 1;
    L7:
        r4.delete();
    }

    public static Intent registerReceiverNotExported(Context r2, BroadcastReceiver r3, IntentFilter r4) {
        if (SDK_INT >= 33) goto L7;
        return r2.registerReceiver(r3, r4);
    L7:
        return r2.registerReceiver(r3, r4, 4);
    }

    public static <T> void removeRange(List<T> r1, int r2, int r3) {
        if (r2 < 0) goto L10;
        if (r3 > r1.size()) goto L10;
        if (r2 > r3) goto L10;
        if (r2 == r3) goto L11;
        r1.subList(r2, r3).clear();
        return;
    L11:
        return;
    L10:
        throw new IllegalArgumentException();
    }

    private static boolean requestExternalStoragePermission(Activity r3) {
        if (r3.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) goto L6;
        r3.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 0);
        return true;
    L6:
        return false;
    }

    private static boolean requestReadMediaPermissions(Activity r5) {
        if (r5.checkSelfPermission("android.permission.READ_MEDIA_AUDIO") == 0) goto L5;
    L10:
        r5.requestPermissions(new String[]{"android.permission.READ_MEDIA_AUDIO", "android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO"}, 0);
        return true;
    L5:
        if (r5.checkSelfPermission("android.permission.READ_MEDIA_VIDEO") != 0) goto L10;
        if (r5.checkSelfPermission("android.permission.READ_MEDIA_IMAGES") != 0) goto L10;
        return false;
    }

    public static long sampleCountToDurationUs(long r7, int r9) {
        return scaleLargeValue(r7, 1000000, r9, RoundingMode.DOWN);
    }

    public static long scaleLargeTimestamp(long r7, long r9, long r11) {
        return scaleLargeValue(r7, r9, r11, RoundingMode.DOWN);
    }

    public static long[] scaleLargeTimestamps(List<Long> r6, long r7, long r9) {
        return scaleLargeValues(r6, r7, r9, RoundingMode.DOWN);
    }

    public static void scaleLargeTimestampsInPlace(long[] r6, long r7, long r9) {
        scaleLargeValuesInPlace(r6, r7, r9, RoundingMode.DOWN);
    }

    public static long scaleLargeValue(long r4, long r6, long r8, RoundingMode r10) {
        if (r4 != 0) goto L5;
    L33:
        return 0;
    L5:
        if (r6 == 0) goto L33;
        if (r8 < r6) goto L14;
        if ((r8 % r6) != 0) goto L14;
        return LongMath.divide(r4, LongMath.divide(r8, r6, RoundingMode.UNNECESSARY), r10);
    L14:
        if (r8 >= r6) goto L20;
        if ((r6 % r8) != 0) goto L20;
        return LongMath.saturatedMultiply(r4, LongMath.divide(r6, r8, RoundingMode.UNNECESSARY));
    L20:
        if (r8 < r4) goto L26;
        if ((r8 % r4) != 0) goto L26;
        return LongMath.divide(r6, LongMath.divide(r8, r4, RoundingMode.UNNECESSARY), r10);
    L26:
        if (r8 >= r4) goto L32;
        if ((r4 % r8) != 0) goto L32;
        return LongMath.saturatedMultiply(r6, LongMath.divide(r4, r8, RoundingMode.UNNECESSARY));
    L32:
        return scaleLargeValueFallback(r4, r6, r8, r10);
    }

    private static long scaleLargeValueFallback(long r30, long r32, long r34, RoundingMode r36) {
        long r3 = LongMath.saturatedMultiply(r30, r32);
        if (r3 != Long.MAX_VALUE) goto L5;
    L8:
        long r10 = LongMath.gcd(Math.abs(r32), Math.abs(r34));
        long r14 = LongMath.divide(r32, r10, RoundingMode.UNNECESSARY);
        long r5 = LongMath.divide(r34, r10, RoundingMode.UNNECESSARY);
        long r0 = LongMath.gcd(Math.abs(r30), Math.abs(r5));
        long r4 = LongMath.divide(r30, r0, RoundingMode.UNNECESSARY);
        long r6 = LongMath.divide(r5, r0, RoundingMode.UNNECESSARY);
        long r1 = LongMath.saturatedMultiply(r4, r14);
        if (r1 != Long.MAX_VALUE) goto L11;
    L14:
        double r2 = r4 * (r14 / r6);
        if (r2 <= 9.223372036854776E18d) goto L18;
        return Long.MAX_VALUE;
    L18:
        if (r2 >= (-9.223372036854776E18d)) goto L21;
        return Long.MIN_VALUE;
    L21:
        return DoubleMath.roundToLong(r2, r36);
    L11:
        if (r1 == Long.MIN_VALUE) goto L14;
        return LongMath.divide(r1, r6, r36);
    L5:
        if (r3 == Long.MIN_VALUE) goto L8;
        return LongMath.divide(r3, r34, r36);
    }

    public static long[] scaleLargeValues(List<Long> r16, long r17, long r19, RoundingMode r21) {
        long r3 = r17;
        long r5 = r19;
        RoundingMode r7 = r21;
        long[] r8 = new long[r16.size()];
        if (r3 != 0) goto L6;
        return r8;
    L6:
        if (r5 < r3) goto L15;
        if ((r5 % r3) != 0) goto L15;
        long r1 = LongMath.divide(r5, r3, RoundingMode.UNNECESSARY);
        int r9 = 0;
    L11:
        if (r9 >= r8.length) goto L13;
        r8[r9] = LongMath.divide(r16.get(r9).longValue(), r1, r7);
        r9 = r9 + 1;
        goto L11
    L13:
        return r8;
    L15:
        if (r5 < r3) goto L17;
    L23:
        int r11 = 0;
    L25:
        if (r11 >= r8.length) goto L41;
        long r2 = r16.get(r11).longValue();
        if (r2 == 0) goto L40;
        if (r5 < r2) goto L35;
        if ((r5 % r2) != 0) goto L35;
        r8[r11] = LongMath.divide(r3, LongMath.divide(r5, r2, RoundingMode.UNNECESSARY), r7);
    L35:
        if (r5 < r2) goto L37;
    L39:
        r8[r11] = scaleLargeValueFallback(r2, r3, r5, r7);
        goto L40
    L37:
        if ((r2 % r5) != 0) goto L39;
        r8[r11] = LongMath.saturatedMultiply(r3, LongMath.divide(r2, r5, RoundingMode.UNNECESSARY));
    L40:
        r11 = r11 + 1;
        r3 = r17;
        r5 = r19;
        r7 = r21;
        goto L25
    L41:
        return r8;
    L17:
        if ((r3 % r5) != 0) goto L23;
        long r4 = LongMath.divide(r3, r5, RoundingMode.UNNECESSARY);
        int r10 = 0;
    L20:
        if (r10 >= r8.length) goto L22;
        r8[r10] = LongMath.saturatedMultiply(r16.get(r10).longValue(), r4);
        r10 = r10 + 1;
        goto L20
    L22:
        return r8;
    }

    public static void scaleLargeValuesInPlace(long[] r10, long r11, long r13, RoundingMode r15) {
        if (r11 != 0) goto L7;
        Arrays.fill(r10, 0);
        return;
    L7:
        if (r13 < r11) goto L16;
        if ((r13 % r11) != 0) goto L16;
        long r0 = LongMath.divide(r13, r11, RoundingMode.UNNECESSARY);
        int r2 = 0;
    L12:
        if (r2 >= r10.length) goto L42;
        r10[r2] = LongMath.divide(r10[r2], r0, r15);
        r2 = r2 + 1;
        goto L12
    L42:
        return;
    L16:
        if (r13 < r11) goto L18;
    L24:
        int r9 = 0;
    L26:
        if (r9 >= r10.length) goto L51;
        if (r10[r9] == 0) goto L41;
        if (r13 < r10[r9]) goto L36;
        if ((r13 % r10[r9]) != 0) goto L36;
        r10[r9] = LongMath.divide(r11, LongMath.divide(r13, r10[r9], RoundingMode.UNNECESSARY), r15);
    L36:
        if (r13 < r10[r9]) goto L38;
    L40:
        r10[r9] = scaleLargeValueFallback(r10[r9], r11, r13, r15);
        goto L41
    L38:
        if ((r10[r9] % r13) != 0) goto L40;
        r10[r9] = LongMath.saturatedMultiply(r11, LongMath.divide(r10[r9], r13, RoundingMode.UNNECESSARY));
    L41:
        r9 = r9 + 1;
        goto L26
    L51:
        return;
    L18:
        if ((r11 % r13) != 0) goto L24;
        long r1 = LongMath.divide(r11, r13, RoundingMode.UNNECESSARY);
        int r3 = 0;
    L21:
        if (r3 >= r10.length) goto L50;
        r10[r3] = LongMath.saturatedMultiply(r10[r3], r1);
        r3 = r3 + 1;
        goto L21
    }

    public static void setForegroundServiceNotification(Service r2, int r3, Notification r4, int r5, String r6) {
        if (SDK_INT < 29) goto L5;
        Api29.startForeground(r2, r3, r4, r5, r6);
        return;
    L5:
        r2.startForeground(r3, r4);
    }

    private static boolean shouldEscapeCharacter(char r1) {
        switch(r1) {
            case 34: goto L5;
            case 37: goto L5;
            case 42: goto L5;
            case 47: goto L5;
            case 58: goto L5;
            case 60: goto L5;
            case 62: goto L5;
            case 63: goto L5;
            case 92: goto L5;
            case 124: goto L5;
            default: goto L3;
        };
    L3:
        return false;
    L5:
        return true;
    }

    @EnsuresNonNullIf(expression = {"#1"}, result = false)
    public static boolean shouldShowPlayButton(Player r1) {
        return shouldShowPlayButton(r1, true);
    }

    @EnsuresNonNullIf(expression = {"#1"}, result = false)
    public static boolean shouldShowPlayButton(Player r3, boolean r4) {
        if (r3 != null) goto L5;
        return true;
    L5:
        if (r3.getPlayWhenReady() == true) goto L7;
        return true;
    L7:
        if (r3.getPlaybackState() != 1) goto L9;
        return true;
    L9:
        if (r3.getPlaybackState() == 4) goto L20;
        if (r4 == true) goto L12;
    L14:
        return false;
    L12:
        if (r3.getPlaybackSuppressionReason() == 0) goto L14;
        return true;
    L20:
        return true;
    }

    public static void sneakyThrow(Throwable r0) {
        sneakyThrowInternal(r0);
    }

    private static <T extends Throwable> void sneakyThrowInternal(Throwable r0) throws Throwable {
        throw r0;
    }

    public static String[] split(String r1, String r2) {
        return r1.split(r2, -1);
    }

    public static String[] splitAtFirst(String r1, String r2) {
        return r1.split(r2, 2);
    }

    public static String[] splitCodecs(String r2) {
        if (TextUtils.isEmpty(r2) == false) goto L7;
        return new String[0];
    L7:
        return split(r2.trim(), "(\\s*,\\s*)");
    }

    public static ComponentName startForegroundService(Context r2, Intent r3) {
        if (SDK_INT < 26) goto L7;
        return r2.startForegroundService(r3);
    L7:
        return r2.startService(r3);
    }

    public static long subtractWithOverflowDefault(long r6, long r8, long r10) {
        long r0 = r6 - r8;
        if (((r6 ^ r8) & (r6 ^ r0)) >= 0) goto L5;
        return r10;
    L5:
        return r0;
    }

    public static long sum(long... r6) {
        long r0 = 0;
        int r2 = r6.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = r0 + r6[r3];
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    public static boolean tableExists(SQLiteDatabase r4, String r5) {
        if (DatabaseUtils.queryNumEntries(r4, "sqlite_master", "tbl_name = ?", new String[]{r5}) <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static byte[] toByteArray(float r1) {
        return Ints.toByteArray(Float.floatToIntBits(r1));
    }

    @Deprecated
    public static byte[] toByteArray(InputStream r1) throws IOException {
        return ByteStreams.toByteArray(r1);
    }

    public static byte[] toByteArray(int... r7) {
        byte[] r0 = new byte[r7.length * 4];
        int r1 = 0;
        int r2 = r7.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        int r4 = r7[r3];
        int r5 = r1 + 1;
        r0[r1] = (byte) (r4 >> 24);
        int r6 = r5 + 1;
        r0[r5] = (byte) (r4 >> 16);
        int r8 = r6 + 1;
        r0[r6] = (byte) (r4 >> 8);
        r1 = r8 + 1;
        r0[r8] = (byte) r4;
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    public static String toHexString(byte[] r5) {
        StringBuilder r0 = new StringBuilder(r5.length * 2);
        int r1 = 0;
    L4:
        if (r1 >= r5.length) goto L7;
        r0.append(Character.forDigit((r5[r1] >> 4) & 15, 16)).append(Character.forDigit(r5[r1] & Ascii.f88SI, 16));
        r1 = r1 + 1;
        goto L4
    L7:
        return r0.toString();
    }

    public static long toLong(int r4, int r5) {
        return (toUnsignedLong(r4) << 32) | toUnsignedLong(r5);
    }

    public static long toUnsignedLong(int r4) {
        return ((long) r4) & 4294967295L;
    }

    public static <T, U> ListenableFuture<T> transformFutureAsync(final ListenableFuture<U> r3, final AsyncFunction<U, T> r4) {
        final SettableFuture r0 = SettableFuture.create();
        r0.addListener(new Util$$ExternalSyntheticLambda0(r0, r3), MoreExecutors.directExecutor());
        r3.addListener(new Util$$ExternalSyntheticLambda1(r3, r0, r4), MoreExecutors.directExecutor());
        return r0;
    }

    public static String unescapeFileName(String r8) {
        int r0 = r8.length();
        int r1 = 0;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L8;
        if (r8.charAt(r2) != '%') goto L7;
        r1 = r1 + 1;
    L7:
        r2 = r2 + 1;
        goto L3
    L8:
        if (r1 != 0) goto L10;
        return r8;
    L10:
        int r3 = r0 - (r1 * 2);
        StringBuilder r4 = new StringBuilder(r3);
        Matcher r5 = ESCAPED_CHARACTER_PATTERN.matcher(r8);
        int r6 = 0;
    L11:
        if (r1 <= 0) goto L15;
        if (r5.find() == false) goto L15;
        r4.append(r8, r6, r5.start()).append((char) Integer.parseInt((String) Assertions.checkNotNull(r5.group(1)), 16));
        r6 = r5.end();
        r1 = r1 - 1;
    L15:
        if (r6 >= r0) goto L18;
        r4.append(r8, r6, r0);
    L18:
        if (r4.length() == r3) goto L22;
        return null;
    L22:
        return r4.toString();
    }

    public static long usToMs(long r2) {
        if (r2 == C0180C.TIME_UNSET) goto L9;
        if (r2 == Long.MIN_VALUE) goto L9;
        return r2 / 1000;
    L9:
        return r2;
    }

    public static void writeBoolean(Parcel r0, boolean r1) {
        r0.writeInt(r1 ? 1 : 0);
    }
}
