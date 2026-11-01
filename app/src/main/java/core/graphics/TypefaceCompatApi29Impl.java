package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
    public TypefaceCompatApi29Impl() {
    }

    private Font findBaseFont(FontFamily r7, int r8) {
        if ((r8 & 1) == 0) goto L5;
        int r1 = 700;
    L7:
        if ((r8 & 2) == 0) goto L9;
        int r2 = 1;
    L10:
        FontStyle r0 = new FontStyle(r1, r2);
        Font r3 = r7.getFont(0);
        int r4 = getMatchScore(r0, r3.getStyle());
        int r5 = 1;
    L12:
        if (r5 >= r7.getSize()) goto L17;
        Font r6 = r7.getFont(r5);
        int r9 = getMatchScore(r0, r6.getStyle());
        if (r9 >= r4) goto L16;
        r3 = r6;
        r4 = r9;
    L16:
        r5 = r5 + 1;
        goto L12
    L17:
        return r3;
    L9:
        r2 = 0;
        goto L10
    L5:
        r1 = 400;
        goto L7
    }

    private static int getMatchScore(FontStyle r3, FontStyle r4) {
        int r0 = Math.abs(r3.getWeight() - r4.getWeight()) / 100;
        if (r3.getSlant() != r4.getSlant()) goto L5;
        int r1 = 0;
    L7:
        return r0 + r1;
    L5:
        r1 = 2;
        goto L7
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontFamilyFilesResourceEntry(Context r10, FontResourcesParserCompat.FontFamilyFilesResourceEntry r11, Resources r12, int r13) {
        FontFamily.Builder r0 = null;
        FontResourcesParserCompat.FontFileResourceEntry[] r2 = r11.getEntries();     // Catch: Exception -> L21
        int r3 = r2.length;     // Catch: Exception -> L21
        int r5 = 0;
    L4:
        if (r5 >= r3) goto L17;
        FontResourcesParserCompat.FontFileResourceEntry r6 = r2[r5];     // Catch: Exception -> L21
        Font.Builder r7 = new Font.Builder(r12, r6.getResourceId()).setWeight(r6.getWeight());     // Catch: IOException -> L15 Exception -> L21
        if (r6.isItalic() == false) goto L9;
        int r8 = 1;
    L10:
        Font r9 = r7.setSlant(r8).setTtcIndex(r6.getTtcIndex()).setFontVariationSettings(r6.getVariationSettings()).build();     // Catch: IOException -> L15 Exception -> L21
        if (r0 != null) goto L13;
        r0 = new FontFamily.Builder(r9);     // Catch: IOException -> L15 Exception -> L21
        goto L16
    L13:
        r0.addFont(r9);     // Catch: IOException -> L15 Exception -> L21
        goto L16
    L9:
        r8 = 0;
    L16:
        r5 = r5 + 1;
        goto L4
    L17:
        if (r0 != null) goto L19;
        return null;
    L19:
        FontFamily r4 = r0.build();     // Catch: Exception -> L21
        return new Typeface.CustomFallbackBuilder(r4).setStyle(findBaseFont(r4, r13).getStyle()).build();
    L22:
        return null;
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontInfo(Context r11, CancellationSignal r12, FontsContractCompat.FontInfo[] r13, int r14) {
        FontFamily.Builder r0 = null;
        ContentResolver r1 = r11.getContentResolver();
        int r3 = r13.length;     // Catch: Exception -> L35
        int r5 = 0;
    L4:
        if (r5 >= r3) goto L31;
        FontsContractCompat.FontInfo r6 = r13[r5];     // Catch: Exception -> L35
        ParcelFileDescriptor r7 = r1.openFileDescriptor(r6.getUri(), "r", r12);     // Catch: IOException -> L29 Exception -> L35
        if (r7 != null) goto L39;
        if (r7 == null) goto L30;
        r7.close();     // Catch: IOException -> L29 Exception -> L35
        goto L30
    L39:
        Font.Builder r8 = new Font.Builder(r7).setWeight(r6.getWeight());     // Catch: Throwable -> L22
        if (r6.isItalic() == false) goto L14;
        int r9 = 1;
    L15:
        Font r10 = r8.setSlant(r9).setTtcIndex(r6.getTtcIndex()).build();     // Catch: Throwable -> L22
        if (r0 != null) goto L18;
        r0 = new FontFamily.Builder(r10);     // Catch: Throwable -> L22
    L19:
        if (r7 == null) goto L30;
        r7.close();     // Catch: IOException -> L29 Exception -> L35
        goto L30
    L18:
        r0.addFont(r10);     // Catch: Throwable -> L22
        goto L19
    L14:
        r9 = 0;
    L22:
        th = move-exception;
        if (r7 != null) goto L43;
    L28:
        throw th;     // Catch: IOException -> L29 Exception -> L35
    L43:
        r7.close();     // Catch: Throwable -> L26
    L26:
        th = move-exception;
        th.addSuppressed(th);     // Catch: IOException -> L29 Exception -> L35
    L30:
        r5 = r5 + 1;
        goto L4
    L31:
        if (r0 != null) goto L33;
        return null;
    L33:
        FontFamily r4 = r0.build();     // Catch: Exception -> L35
        return new Typeface.CustomFallbackBuilder(r4).setStyle(findBaseFont(r4, r14).getStyle()).build();
    L36:
        return null;
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    protected Typeface createFromInputStream(Context r3, InputStream r4) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromResourcesFontFile(Context r5, Resources r6, int r7, String r8, int r9) {
        Font r2 = new Font.Builder(r6, r7).build();     // Catch: Exception -> L5
        return new Typeface.CustomFallbackBuilder(new FontFamily.Builder(r2).build()).setStyle(r2.getStyle()).build();
    L6:
        return null;
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    Typeface createWeightStyle(Context r2, Typeface r3, int r4, boolean r5) {
        return Typeface.create(r3, r4, r5);
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] r3, int r4) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }
}
