package androidx.core.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import androidx.core.content.res.FontResourcesParserCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class FontProvider {
    private static final Comparator<byte[]> sByteArrayComparator = null;

    static class Api16Impl {
        private Api16Impl() {
        }

        static Cursor query(ContentResolver r7, Uri r8, String[] r9, String r10, String[] r11, String r12, Object r13) {
            return r7.query(r8, r9, r10, r11, r12, (CancellationSignal) r13);
        }
    }

    static {
        sByteArrayComparator = new FontProvider$$ExternalSyntheticLambda0();
    }

    private FontProvider() {
    }

    private static List<byte[]> convertToByteArrayList(Signature[] r5) {
        ArrayList r0 = new ArrayList();
        int r1 = r5.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0.add(r5[r2].toByteArray());
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static boolean equalsByteArrayList(List<byte[]> r4, List<byte[]> r5) {
        if (r4.size() == r5.size()) goto L5;
        return false;
    L5:
        int r0 = 0;
    L7:
        if (r0 >= r4.size()) goto L12;
        if (Arrays.equals(r4.get(r0), r5.get(r0)) == false) goto L10;
        r0 = r0 + 1;
        goto L7
    L10:
        return false;
    L12:
        return true;
    }

    private static List<List<byte[]>> getCertificates(FontRequest r2, Resources r3) {
        if (r2.getCertificates() == null) goto L7;
        return r2.getCertificates();
    L7:
        return FontResourcesParserCompat.readCerts(r3, r2.getCertificatesArrayResId());
    }

    static FontsContractCompat.FontFamilyResult getFontFamilyResult(Context r3, FontRequest r4, CancellationSignal r5) throws PackageManager.NameNotFoundException {
        ProviderInfo r0 = getProvider(r3.getPackageManager(), r4, r3.getResources());
        if (r0 != null) goto L7;
        return FontsContractCompat.FontFamilyResult.create(1, null);
    L7:
        return FontsContractCompat.FontFamilyResult.create(0, query(r3, r4, r0.authority, r5));
    }

    static ProviderInfo getProvider(PackageManager r8, FontRequest r9, Resources r10) throws PackageManager.NameNotFoundException {
        String r0 = r9.getProviderAuthority();
        ProviderInfo r1 = r8.resolveContentProvider(r0, 0);
        if (r1 == null) goto L18;
        if (r1.packageName.equals(r9.getProviderPackage()) == false) goto L16;
        List<byte[]> r3 = convertToByteArrayList(r8.getPackageInfo(r1.packageName, 64).signatures);
        Collections.sort(r3, sByteArrayComparator);
        List<List<byte[]>> r4 = getCertificates(r9, r10);
        int r5 = 0;
    L8:
        if (r5 >= r4.size()) goto L13;
        ArrayList r6 = new ArrayList(r4.get(r5));
        Collections.sort(r6, sByteArrayComparator);
        if (equalsByteArrayList(r3, r6) == true) goto L11;
        r5 = r5 + 1;
        goto L8
    L11:
        return r1;
    L13:
        return null;
    L16:
        throw new PackageManager.NameNotFoundException("Found content provider " + r0 + ", but package was not " + r9.getProviderPackage());
    L18:
        throw new PackageManager.NameNotFoundException("No package found for authority: " + r0);
    }

    static /* synthetic */ int lambda$static$0(byte[] r3, byte[] r4) {
        if (r3.length != r4.length) goto L5;
        int r0 = 0;
    L8:
        if (r0 >= r3.length) goto L14;
        if (r3[r0] != r4[r0]) goto L12;
        r0 = r0 + 1;
        goto L8
    L12:
        return r3[r0] - r4[r0];
    L14:
        return 0;
    L5:
        return r3.length - r4.length;
    }

    static FontsContractCompat.FontInfo[] query(Context r22, FontRequest r23, String r24, CancellationSignal r25) {
        ArrayList r2 = new ArrayList();
        Uri r5 = new Uri.Builder().scheme("content").authority(r24).build();
        Uri r3 = new Uri.Builder().scheme("content").authority(r24).appendPath("file").build();
        Cursor r11 = null;
        String[] r6 = {"_id", FontsContractCompat.Columns.FILE_ID, FontsContractCompat.Columns.TTC_INDEX, FontsContractCompat.Columns.VARIATION_SETTINGS, FontsContractCompat.Columns.WEIGHT, FontsContractCompat.Columns.ITALIC, FontsContractCompat.Columns.RESULT_CODE};     // Catch: Throwable -> L48
        ContentResolver r4 = r22.getContentResolver();     // Catch: Throwable -> L48
        r11 = Api16Impl.query(r4, r5, r6, "query = ?", new String[]{r23.getQuery()}, null, r25);     // Catch: Throwable -> L48
        if (r11 == null) goto L44;
        if (r11.getCount() <= 0) goto L44;
        int r7 = r11.getColumnIndex(FontsContractCompat.Columns.RESULT_CODE);     // Catch: Throwable -> L48
        r2 = new ArrayList();     // Catch: Throwable -> L48
        int r8 = r11.getColumnIndex("_id");     // Catch: Throwable -> L48
        int r9 = r11.getColumnIndex(FontsContractCompat.Columns.FILE_ID);     // Catch: Throwable -> L48
        int r10 = r11.getColumnIndex(FontsContractCompat.Columns.TTC_INDEX);     // Catch: Throwable -> L48
        int r12 = r11.getColumnIndex(FontsContractCompat.Columns.WEIGHT);     // Catch: Throwable -> L48
        int r13 = r11.getColumnIndex(FontsContractCompat.Columns.ITALIC);     // Catch: Throwable -> L48
    L8:
        if (r11.moveToNext() == false) goto L44;
        if (r7 != (-1)) goto L55;
        int r15 = 0;
    L17:
        if (r10 == (-1)) goto L19;
        int r16 = r11.getInt(r10);     // Catch: Throwable -> L13
    L20:
        int r17 = r16;
        if (r9 != (-1)) goto L25;
        Uri r0 = ContentUris.withAppendedId(r5, r11.getLong(r8));     // Catch: Throwable -> L13
    L26:
        if (r12 == (-1)) goto L29;
        int r1 = r11.getInt(r12);     // Catch: Throwable -> L13
    L30:
        if (r13 != (-1)) goto L32;
        Uri r18 = r3;
    L36:
        boolean r14 = false;
    L37:
        ContentResolver r19 = r4;
        r2.add(FontsContractCompat.FontInfo.create(r0, r17, r1, r14, r15));     // Catch: Throwable -> L40
        r4 = r19;
        r3 = r18;
    L40:
        th = th;
    L50:
        if (r11 == null) goto L52;
        r11.close();
    L52:
        throw th;
    L32:
        r18 = r3;
        r14 = true;
        if (r11.getInt(r13) != 1) goto L36;
    L29:
        r1 = 400;
        goto L30
    L25:
        r0 = ContentUris.withAppendedId(r3, r11.getLong(r9));     // Catch: Throwable -> L48
        goto L26
    L19:
        r16 = 0;
        goto L20
    L55:
        r15 = r11.getInt(r7);     // Catch: Throwable -> L13
    L13:
        th = th;
    L44:
        if (r11 == null) goto L47;
        r11.close();
    L47:
        return (FontsContractCompat.FontInfo[]) r2.toArray(new FontsContractCompat.FontInfo[0]);
    L48:
        th = th;
        goto L50
    }
}
