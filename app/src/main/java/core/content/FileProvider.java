package androidx.core.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class FileProvider extends ContentProvider {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS = null;
    private static final File DEVICE_ROOT = null;
    private static final String DISPLAYNAME_FIELD = "displayName";
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    private static final HashMap<String, PathStrategy> sCache = null;
    private int mResourceId;
    private PathStrategy mStrategy;

    static class Api21Impl {
        private Api21Impl() {
        }

        static File[] getExternalMediaDirs(Context r1) {
            return r1.getExternalMediaDirs();
        }
    }

    interface PathStrategy {
        File getFileForUri(Uri r1);

        Uri getUriForFile(File r1);
    }

    static class SimplePathStrategy implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots;

        SimplePathStrategy(String r2) {
            this.mRoots = new HashMap();
            this.mAuthority = r2;
        }

        void addRoot(String r5, File r6) {
            if (TextUtils.isEmpty(r5) == true) goto L11;
            this.mRoots.put(r5, r6.getCanonicalFile());
            return;
        L7:
            e = move-exception;
            throw new IllegalArgumentException("Failed to resolve canonical path for " + r6, e);
        L11:
            throw new IllegalArgumentException("Name must not be empty");
        }

        @Override // androidx.core.content.FileProvider.PathStrategy
        public File getFileForUri(Uri r10) {
            String r0 = r10.getEncodedPath();
            int r1 = r0.indexOf(47, 1);
            String r2 = Uri.decode(r0.substring(1, r1));
            String r3 = Uri.decode(r0.substring(r1 + 1));
            File r4 = this.mRoots.get(r2);
            if (r4 == null) goto L15;
            File r5 = new File(r4, r3);
            File r6 = r5.getCanonicalFile();     // Catch: IOException -> L11
            if (r6.getPath().startsWith(r4.getPath()) == false) goto L10;
            return r6;
        L10:
            throw new SecurityException("Resolved path jumped beyond configured root");
        L13:
            throw new IllegalArgumentException("Failed to resolve canonical path for " + r5);
        L15:
            throw new IllegalArgumentException("Unable to find configured root for " + r10);
        }

        @Override // androidx.core.content.FileProvider.PathStrategy
        public Uri getUriForFile(File r8) {
            String r0 = r8.getCanonicalPath();     // Catch: IOException -> L22
            Map.Entry<String, File> r1 = null;
            Iterator<Map.Entry<String, File>> r2 = this.mRoots.entrySet().iterator();
        L5:
            if (r2.hasNext() == false) goto L13;
            Map.Entry<String, File> r3 = r2.next();
            String r4 = r3.getValue().getPath();
            if (r0.startsWith(r4) == false) goto L5;
            if (r1 == null) goto L11;
            if (r4.length() <= r1.getValue().getPath().length()) goto L5;
        L11:
            r1 = r3;
            goto L5
        L13:
            if (r1 == null) goto L21;
            String r5 = r1.getValue().getPath();
            if (r5.endsWith("/") == false) goto L17;
            String r6 = r0.substring(r5.length());
        L19:
            return new Uri.Builder().scheme("content").authority(this.mAuthority).encodedPath(Uri.encode(r1.getKey()) + '/' + Uri.encode(r6, "/")).build();
        L17:
            r6 = r0.substring(r5.length() + 1);
            goto L19
        L21:
            throw new IllegalArgumentException("Failed to find configured root that contains " + r0);
        L24:
            throw new IllegalArgumentException("Failed to resolve canonical path for " + r8);
        }
    }

    static {
        COLUMNS = new String[]{"_display_name", "_size"};
        DEVICE_ROOT = new File("/");
        sCache = new HashMap();
    }

    public FileProvider() {
        this.mResourceId = 0;
    }

    protected FileProvider(int r1) {
        this.mResourceId = r1;
    }

    private static File buildPath(File r5, String... r6) {
        File r0 = r5;
        int r1 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L8;
        String r3 = r6[r2];
        if (r3 == null) goto L7;
        r0 = new File(r0, r3);
    L7:
        r2 = r2 + 1;
        goto L3
    L8:
        return r0;
    }

    private static Object[] copyOf(Object[] r2, int r3) {
        Object[] r0 = new Object[r3];
        System.arraycopy(r2, 0, r0, 0, r3);
        return r0;
    }

    private static String[] copyOf(String[] r2, int r3) {
        String[] r0 = new String[r3];
        System.arraycopy(r2, 0, r0, 0, r3);
        return r0;
    }

    static XmlResourceParser getFileProviderPathsMetaData(Context r3, String r4, ProviderInfo r5, int r6) {
        if (r5 == null) goto L13;
        if (r5.metaData != null) goto L7;
        if (r6 == 0) goto L7;
        r5.metaData = new Bundle(1);
        r5.metaData.putInt(META_DATA_FILE_PROVIDER_PATHS, r6);
    L7:
        XmlResourceParser r0 = r5.loadXmlMetaData(r3.getPackageManager(), META_DATA_FILE_PROVIDER_PATHS);
        if (r0 == null) goto L11;
        return r0;
    L11:
        throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
    L13:
        throw new IllegalArgumentException("Couldn't find meta-data for provider with authority " + r4);
    }

    private static PathStrategy getPathStrategy(Context r5, String r6, int r7) {
        HashMap<String, PathStrategy> r0 = sCache;
        monitor-enter(r0);
        PathStrategy r1 = sCache.get(r6);     // Catch: Throwable -> L17
        if (r1 == null) goto L21;
    L15:
        monitor-exit(r0);     // Catch: Throwable -> L17
        return r1;
    L21:
        r1 = parsePathStrategy(r5, r6, r7);     // Catch: XmlPullParserException -> L9 IOException -> L12 Throwable -> L17
        sCache.put(r6, r1);     // Catch: Throwable -> L17
        goto L15
    L12:
        e = move-exception;
        throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e);     // Catch: Throwable -> L17
    L9:
        e = move-exception;
        throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e);     // Catch: Throwable -> L17
    L17:
        th = move-exception;
        throw th;
    }

    public static Uri getUriForFile(Context r2, String r3, File r4) {
        return getPathStrategy(r2, r3, 0).getUriForFile(r4);
    }

    public static Uri getUriForFile(Context r3, String r4, File r5, String r6) {
        return getUriForFile(r3, r4, r5).buildUpon().appendQueryParameter(DISPLAYNAME_FIELD, r6).build();
    }

    private static int modeToMode(String r3) {
        if ("r".equals(r3) == false) goto L6;
        return 268435456;
    L6:
        if ("w".equals(r3) == false) goto L8;
    L21:
        return 738197504;
    L8:
        if ("wt".equals(r3) == true) goto L21;
        if ("wa".equals(r3) == false) goto L14;
        return 704643072;
    L14:
        if ("rw".equals(r3) == false) goto L17;
        return 939524096;
    L17:
        if ("rwt".equals(r3) == false) goto L20;
        return 1006632960;
    L20:
        throw new IllegalArgumentException("Invalid mode: " + r3);
    }

    private static PathStrategy parsePathStrategy(Context r11, String r12, int r13) throws IOException, XmlPullParserException {
        SimplePathStrategy r0 = new SimplePathStrategy(r12);
        XmlResourceParser r2 = getFileProviderPathsMetaData(r11, r12, r11.getPackageManager().resolveContentProvider(r12, 128), r13);
    L3:
        int r3 = r2.next();
        if (r3 == 1) goto L39;
        if (r3 != 2) goto L3;
        String r4 = r2.getName();
        String r5 = r2.getAttributeValue(null, ATTR_NAME);
        String r7 = r2.getAttributeValue(null, ATTR_PATH);
        File r8 = null;
        if (TAG_ROOT_PATH.equals(r4) == false) goto L11;
        r8 = DEVICE_ROOT;
    L36:
        if (r8 == null) goto L3;
        r0.addRoot(r5, buildPath(r8, new String[]{r7}));
        goto L3
    L11:
        if (TAG_FILES_PATH.equals(r4) == false) goto L14;
        r8 = r11.getFilesDir();
        goto L36
    L14:
        if (TAG_CACHE_PATH.equals(r4) == false) goto L17;
        r8 = r11.getCacheDir();
        goto L36
    L17:
        if (TAG_EXTERNAL.equals(r4) == false) goto L20;
        r8 = Environment.getExternalStorageDirectory();
        goto L36
    L20:
        if (TAG_EXTERNAL_FILES.equals(r4) == false) goto L26;
        File[] r6 = ContextCompat.getExternalFilesDirs(r11, null);
        if (r6.length <= 0) goto L36;
        r8 = r6[0];
        goto L36
    L26:
        if (TAG_EXTERNAL_CACHE.equals(r4) == false) goto L32;
        File[] r9 = ContextCompat.getExternalCacheDirs(r11);
        if (r9.length <= 0) goto L36;
        r8 = r9[0];
        goto L36
    L32:
        if (TAG_EXTERNAL_MEDIA.equals(r4) == false) goto L36;
        File[] r10 = Api21Impl.getExternalMediaDirs(r11);
        if (r10.length <= 0) goto L36;
        r8 = r10[0];
        goto L36
    L39:
        return r0;
    }

    @Override // android.content.ContentProvider
    public void attachInfo(Context r4, ProviderInfo r5) {
        super.attachInfo(r4, r5);
        if (r5.exported == true) goto L18;
        if (r5.grantUriPermissions == false) goto L16;
        String r0 = r5.authority.split(";")[0];
        HashMap<String, PathStrategy> r1 = sCache;
        monitor-enter(r1);
        sCache.remove(r0);     // Catch: Throwable -> L12
        monitor-exit(r1);     // Catch: Throwable -> L12
        this.mStrategy = getPathStrategy(r4, r0, this.mResourceId);
        return;
    L12:
        th = move-exception;
        throw th;
    L16:
        throw new SecurityException("Provider must grant uri permissions");
    L18:
        throw new SecurityException("Provider must not be exported");
    }

    @Override // android.content.ContentProvider
    public int delete(Uri r3, String r4, String[] r5) {
        return this.mStrategy.getFileForUri(r3).delete() ? 1 : 0;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri r5) {
        File r0 = this.mStrategy.getFileForUri(r5);
        int r1 = r0.getName().lastIndexOf(46);
        if (r1 < 0) goto L7;
        String r2 = r0.getName().substring(r1 + 1);
        String r3 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(r2);
        if (r3 == null) goto L9;
        return r3;
    L9:
        return "application/octet-stream";
    L7:
        return "application/octet-stream";
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri r3, ContentValues r4) {
        throw new UnsupportedOperationException("No external inserts");
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri r4, String r5) throws FileNotFoundException {
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(r4), modeToMode(r5));
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri r12, String[] r13, String r14, String[] r15, String r16) {
        File r0 = this.mStrategy.getFileForUri(r12);
        String r1 = r12.getQueryParameter(DISPLAYNAME_FIELD);
        if (r13 != null) goto L5;
        r13 = COLUMNS;
    L5:
        String[] r2 = new String[r13.length];
        Object[] r3 = new Object[r13.length];
        int r4 = 0;
        int r5 = r13.length;
        int r6 = 0;
    L6:
        if (r6 >= r5) goto L18;
        String r7 = r13[r6];
        if ("_display_name".equals(r7) == false) goto L15;
        r2[r4] = "_display_name";
        int r8 = r4 + 1;
        if (r1 != null) goto L12;
        String r9 = r0.getName();
    L13:
        r3[r4] = r9;
        r4 = r8;
    L17:
        r6 = r6 + 1;
        goto L6
    L12:
        r9 = r1;
        goto L13
    L15:
        if ("_size".equals(r7) == false) goto L17;
        r2[r4] = "_size";
        r3[r4] = Long.valueOf(r0.length());
        r4 = r4 + 1;
        goto L17
    L18:
        String[] r10 = copyOf(r2, r4);
        Object[] r11 = copyOf(r3, r4);
        MatrixCursor r17 = new MatrixCursor(r10, 1);
        r17.addRow(r11);
        return r17;
    }

    @Override // android.content.ContentProvider
    public int update(Uri r3, ContentValues r4, String r5, String[] r6) {
        throw new UnsupportedOperationException("No external updates");
    }
}
