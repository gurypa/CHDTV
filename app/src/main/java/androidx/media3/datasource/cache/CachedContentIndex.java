package androidx.media3.datasource.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.AtomicFile;
import androidx.media3.common.util.Util;
import androidx.media3.database.DatabaseIOException;
import androidx.media3.database.DatabaseProvider;
import androidx.media3.database.VersionTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
class CachedContentIndex {
    static final String FILE_NAME_ATOMIC = "cached_content_index.exi";
    private static final int INCREMENTAL_METADATA_READ_LENGTH = 10485760;
    private final SparseArray<String> idToKey;
    private final HashMap<String, CachedContent> keyToContent;
    private final SparseBooleanArray newIds;
    private Storage previousStorage;
    private final SparseBooleanArray removedIds;
    private Storage storage;

    private static final class DatabaseStorage implements Storage {
        private static final String[] COLUMNS = null;
        private static final String COLUMN_ID = "id";
        private static final int COLUMN_INDEX_ID = 0;
        private static final int COLUMN_INDEX_KEY = 1;
        private static final int COLUMN_INDEX_METADATA = 2;
        private static final String COLUMN_KEY = "key";
        private static final String COLUMN_METADATA = "metadata";
        private static final String TABLE_PREFIX = "ExoPlayerCacheIndex";
        private static final String TABLE_SCHEMA = "(id INTEGER PRIMARY KEY NOT NULL,key TEXT NOT NULL,metadata BLOB NOT NULL)";
        private static final int TABLE_VERSION = 1;
        private static final String WHERE_ID_EQUALS = "id = ?";
        private final DatabaseProvider databaseProvider;
        private String hexUid;
        private final SparseArray<CachedContent> pendingUpdates;
        private String tableName;

        static {
            COLUMNS = new String[]{"id", COLUMN_KEY, "metadata"};
        }

        public DatabaseStorage(DatabaseProvider r2) {
            this.databaseProvider = r2;
            this.pendingUpdates = new SparseArray();
        }

        private void addOrUpdateRow(SQLiteDatabase r6, CachedContent r7) throws IOException {
            ByteArrayOutputStream r0 = new ByteArrayOutputStream();
            CachedContentIndex.access$100(r7.getMetadata(), new DataOutputStream(r0));
            byte[] r1 = r0.toByteArray();
            ContentValues r2 = new ContentValues();
            r2.put("id", Integer.valueOf(r7.f33id));
            r2.put(COLUMN_KEY, r7.key);
            r2.put("metadata", r1);
            r6.replaceOrThrow((String) Assertions.checkNotNull(this.tableName), null, r2);
        }

        public static void delete(DatabaseProvider r1, long r2) throws DatabaseIOException {
            delete(r1, Long.toHexString(r2));
        }

        private static void delete(DatabaseProvider r3, String r4) throws DatabaseIOException {
            String r0 = getTableName(r4);     // Catch: SQLException -> L10
            SQLiteDatabase r1 = r3.getWritableDatabase();     // Catch: SQLException -> L10
            r1.beginTransactionNonExclusive();     // Catch: SQLException -> L10
            VersionTable.removeVersion(r1, 1, r4);     // Catch: Throwable -> L7
            dropTable(r1, r0);     // Catch: Throwable -> L7
            r1.setTransactionSuccessful();     // Catch: Throwable -> L7
            r1.endTransaction();     // Catch: SQLException -> L10
            return;
        L7:
            th = move-exception;
            r1.endTransaction();     // Catch: SQLException -> L10
            throw th;     // Catch: SQLException -> L10
        L10:
            e = move-exception;
            throw new DatabaseIOException(e);
        }

        private void deleteRow(SQLiteDatabase r4, int r5) {
            r4.delete((String) Assertions.checkNotNull(this.tableName), WHERE_ID_EQUALS, new String[]{Integer.toString(r5)});
        }

        private static void dropTable(SQLiteDatabase r2, String r3) {
            r2.execSQL("DROP TABLE IF EXISTS " + r3);
        }

        private Cursor getCursor() {
            return this.databaseProvider.getReadableDatabase().query((String) Assertions.checkNotNull(this.tableName), COLUMNS, null, null, null, null, null);
        }

        private static String getTableName(String r2) {
            return TABLE_PREFIX + r2;
        }

        private void initializeTable(SQLiteDatabase r3) throws DatabaseIOException {
            VersionTable.setVersion(r3, 1, (String) Assertions.checkNotNull(this.hexUid), 1);
            dropTable(r3, (String) Assertions.checkNotNull(this.tableName));
            r3.execSQL("CREATE TABLE " + this.tableName + " " + TABLE_SCHEMA);
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void delete() throws DatabaseIOException {
            delete(this.databaseProvider, (String) Assertions.checkNotNull(this.hexUid));
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public boolean exists() throws DatabaseIOException {
            if (VersionTable.getVersion(this.databaseProvider.getReadableDatabase(), 1, (String) Assertions.checkNotNull(this.hexUid)) != (-1)) goto L13;
            return false;
        L13:
            return true;
        L8:
            e = move-exception;
            throw new DatabaseIOException(e);
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void initialize(long r2) {
            this.hexUid = Long.toHexString(r2);
            this.tableName = getTableName(this.hexUid);
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void load(HashMap<String, CachedContent> r14, SparseArray<String> r15) throws IOException {
            if (this.pendingUpdates.size() != 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
        L31:
            e = move-exception;
            r14.clear();
            r15.clear();
            throw new DatabaseIOException(e);
        L8:
            if (VersionTable.getVersion(this.databaseProvider.getReadableDatabase(), 1, (String) Assertions.checkNotNull(this.hexUid)) == 1) goto L15;
            SQLiteDatabase r3 = this.databaseProvider.getWritableDatabase();     // Catch: SQLiteException -> L31
            r3.beginTransactionNonExclusive();     // Catch: SQLiteException -> L31
            initializeTable(r3);     // Catch: Throwable -> L12
            r3.setTransactionSuccessful();     // Catch: Throwable -> L12
            r3.endTransaction();     // Catch: SQLiteException -> L31
            goto L15
        L12:
            th = move-exception;
            r3.endTransaction();     // Catch: SQLiteException -> L31
            throw th;     // Catch: SQLiteException -> L31
        L15:
            Cursor r4 = getCursor();     // Catch: SQLiteException -> L31
        L34:
        L24:
            th = move-exception;
            if (r4 != null) goto L38;
        L30:
            throw th;     // Catch: SQLiteException -> L31
        L38:
            r4.close();     // Catch: Throwable -> L28
        L28:
            th = move-exception;
            th.addSuppressed(th);     // Catch: SQLiteException -> L31
            goto L30
        L17:
            if (r4.moveToNext() == false) goto L20;
            CachedContent r10 = new CachedContent(r4.getInt(0), (String) Assertions.checkNotNull(r4.getString(1)), CachedContentIndex.access$000(new DataInputStream(new ByteArrayInputStream(r4.getBlob(2)))));     // Catch: Throwable -> L24
            r14.put(r10.key, r10);     // Catch: Throwable -> L24
            r15.put(r10.f33id, r10.key);     // Catch: Throwable -> L24
        L20:
            if (r4 == null) goto L43;
            r4.close();     // Catch: SQLiteException -> L31
            return;
        L43:
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void onRemove(CachedContent r4, boolean r5) {
            if (r5 == false) goto L4;
            this.pendingUpdates.delete(r4.f33id);
            return;
        L4:
            this.pendingUpdates.put(r4.f33id, null);
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void onUpdate(CachedContent r3) {
            this.pendingUpdates.put(r3.f33id, r3);
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void storeFully(HashMap<String, CachedContent> r4) throws IOException {
            SQLiteDatabase r0 = this.databaseProvider.getWritableDatabase();     // Catch: SQLException -> L13
            r0.beginTransactionNonExclusive();     // Catch: SQLException -> L13
            initializeTable(r0);     // Catch: Throwable -> L10
            Iterator<CachedContent> r1 = r4.values().iterator();     // Catch: Throwable -> L10
        L5:
            if (r1.hasNext() == false) goto L7;
            addOrUpdateRow(r0, r1.next());     // Catch: Throwable -> L10
            goto L5
        L7:
            r0.setTransactionSuccessful();     // Catch: Throwable -> L10
            this.pendingUpdates.clear();     // Catch: Throwable -> L10
            r0.endTransaction();     // Catch: SQLException -> L13
            return;
        L10:
            th = move-exception;
            r0.endTransaction();     // Catch: SQLException -> L13
            throw th;     // Catch: SQLException -> L13
        L13:
            e = move-exception;
            throw new DatabaseIOException(e);
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void storeIncremental(HashMap<String, CachedContent> r5) throws IOException {
            if (this.pendingUpdates.size() != 0) goto L25;
            return;
        L25:
            SQLiteDatabase r0 = this.databaseProvider.getWritableDatabase();     // Catch: SQLException -> L20
            r0.beginTransactionNonExclusive();     // Catch: SQLException -> L20
            int r1 = 0;
        L23:
        L17:
            th = move-exception;
            r0.endTransaction();     // Catch: SQLException -> L20
            throw th;     // Catch: SQLException -> L20
        L8:
            if (r1 >= this.pendingUpdates.size()) goto L14;
            CachedContent r2 = this.pendingUpdates.valueAt(r1);     // Catch: Throwable -> L17
            if (r2 != null) goto L12;
            deleteRow(r0, this.pendingUpdates.keyAt(r1));     // Catch: Throwable -> L17
        L13:
            r1 = r1 + 1;     // Catch: Throwable -> L17
            goto L23
        L12:
            addOrUpdateRow(r0, r2);     // Catch: Throwable -> L17
            goto L13
        L14:
            r0.setTransactionSuccessful();     // Catch: Throwable -> L17
            this.pendingUpdates.clear();     // Catch: Throwable -> L17
            r0.endTransaction();     // Catch: SQLException -> L20
            return;
        L20:
            e = move-exception;
            throw new DatabaseIOException(e);
        }
    }

    private static class LegacyStorage implements Storage {
        private static final int FLAG_ENCRYPTED_INDEX = 1;
        private static final int VERSION = 2;
        private static final int VERSION_METADATA_INTRODUCED = 2;
        private final AtomicFile atomicFile;
        private ReusableBufferedOutputStream bufferedOutputStream;
        private boolean changed;
        private final Cipher cipher;
        private final boolean encrypt;
        private final SecureRandom random;
        private final SecretKeySpec secretKeySpec;

        public LegacyStorage(File r7, byte[] r8, boolean r9) {
            boolean r0 = false;
            if (r8 != null) goto L7;
            if (r9 == false) goto L7;
            boolean r2 = false;
        L8:
            Assertions.checkState(r2);
            Cipher r3 = null;
            SecretKeySpec r4 = null;
            if (r8 != null) goto L11;
            Assertions.checkArgument(!r9);
        L22:
            this.encrypt = r9;
            this.cipher = r3;
            this.secretKeySpec = r4;
            if (r9 == false) goto L25;
            SecureRandom r1 = new SecureRandom();
        L26:
            this.random = r1;
            this.atomicFile = new AtomicFile(r7);
            return;
        L25:
            r1 = null;
            goto L26
        L11:
            if (r8.length != 16) goto L13;
            r0 = true;
        L13:
            Assertions.checkArgument(r0);
            r3 = Cipher.getInstance("AES/CBC/PKCS5PADDING");     // Catch: Throwable -> L16 NoSuchAlgorithmException -> L18
            r4 = new SecretKeySpec(r8, "AES");     // Catch: Throwable -> L16 NoSuchAlgorithmException -> L18
        L16:
            e = move-exception;
            throw new IllegalStateException(e);
        L7:
            r2 = true;
            goto L8
        }

        private int hashCachedContent(CachedContent r7, int r8) {
            int r1 = (r7.f33id * 31) + r7.key.hashCode();
            if (r8 >= 2) goto L6;
            long r2 = ContentMetadata.CC.getContentLength(r7.getMetadata());
            return (r1 * 31) + ((int) ((r2 >>> 32) ^ r2));
        L6:
            return (r1 * 31) + r7.getMetadata().hashCode();
        }

        private CachedContent readCachedContent(int r7, DataInputStream r8) throws IOException {
            int r0 = r8.readInt();
            String r1 = r8.readUTF();
            if (r7 >= 2) goto L5;
            long r2 = r8.readLong();
            ContentMetadataMutations r4 = new ContentMetadataMutations();
            ContentMetadataMutations.setContentLength(r4, r2);
            DefaultContentMetadata r3 = DefaultContentMetadata.EMPTY.copyWithMutationsApplied(r4);
        L7:
            return new CachedContent(r0, r1, r3);
        L5:
            r3 = CachedContentIndex.access$000(r8);
            goto L7
        }

        private boolean readFile(HashMap<String, CachedContent> r13, SparseArray<String> r14) {
            if (this.atomicFile.exists() == true) goto L56;
            return true;
        L56:
            BufferedInputStream r3 = new BufferedInputStream(this.atomicFile.openRead());     // Catch: Throwable -> L46 IOException -> L50
            DataInputStream r0 = new DataInputStream(r3);     // Catch: Throwable -> L46 IOException -> L50
            int r4 = r0.readInt();     // Catch: Throwable -> L46 IOException -> L50
            if (r4 >= 0) goto L9;
        L44:
            Util.closeQuietly(r0);
            return false;
        L9:
            if (r4 > 2) goto L44;
            if ((r0.readInt() & 1) == 0) goto L27;
            if (this.cipher != null) goto L18;
            Util.closeQuietly(r0);
            return false;
        L18:
            byte[] r7 = new byte[16];     // Catch: Throwable -> L46 IOException -> L50
            r0.readFully(r7);     // Catch: Throwable -> L46 IOException -> L50
            this.cipher.init(2, (Key) Util.castNonNull(this.secretKeySpec), new IvParameterSpec(r7));     // Catch: InvalidAlgorithmParameterException -> L21 InvalidKeyException -> L23 Throwable -> L46 IOException -> L50
            r0 = new DataInputStream(new CipherInputStream(r3, this.cipher));     // Catch: Throwable -> L46 IOException -> L50
        L29:
            int r5 = r0.readInt();     // Catch: Throwable -> L46 IOException -> L50
            int r8 = 0;
            int r9 = 0;
        L30:
            if (r9 >= r5) goto L32;
            CachedContent r10 = readCachedContent(r4, r0);     // Catch: Throwable -> L46 IOException -> L50
            r13.put(r10.key, r10);     // Catch: Throwable -> L46 IOException -> L50
            r14.put(r10.f33id, r10.key);     // Catch: Throwable -> L46 IOException -> L50
            r8 = r8 + hashCachedContent(r10, r4);     // Catch: Throwable -> L46 IOException -> L50
            r9 = r9 + 1;     // Catch: Throwable -> L46 IOException -> L50
            goto L30
        L32:
            int r11 = r0.readInt();     // Catch: Throwable -> L46 IOException -> L50
            if (r0.read() != (-1)) goto L36;
            boolean r12 = true;
        L37:
            if (r11 != r8) goto L42;
            if (r12 == false) goto L42;
            Util.closeQuietly(r0);
            return true;
        L42:
            Util.closeQuietly(r0);
            return false;
        L36:
            r12 = false;
            goto L37
        L21:
            e = e;
        L25:
            throw new IllegalStateException(e);     // Catch: Throwable -> L46 IOException -> L50
        L23:
            e = e;
            goto L25
        L27:
            if (this.encrypt == false) goto L29;
            this.changed = true;     // Catch: Throwable -> L46 IOException -> L50
        L46:
            th = move-exception;
            if (0 == 0) goto L49;
            Util.closeQuietly(null);
        L49:
            throw th;
        L51:
            if (0 == 0) goto L53;
            Util.closeQuietly(null);
        L53:
            return false;
        }

        private void writeCachedContent(CachedContent r2, DataOutputStream r3) throws IOException {
            r3.writeInt(r2.f33id);
            r3.writeUTF(r2.key);
            CachedContentIndex.access$100(r2.getMetadata(), r3);
        }

        private void writeFile(HashMap<String, CachedContent> r11) throws IOException {
            OutputStream r1 = this.atomicFile.startWrite();     // Catch: Throwable -> L28
            if (this.bufferedOutputStream != null) goto L6;
            this.bufferedOutputStream = new ReusableBufferedOutputStream(r1);     // Catch: Throwable -> L28
        L7:
            ReusableBufferedOutputStream r2 = this.bufferedOutputStream;     // Catch: Throwable -> L28
            DataOutputStream r0 = new DataOutputStream(r2);     // Catch: Throwable -> L28
            r0.writeInt(2);     // Catch: Throwable -> L28
            if (this.encrypt == false) goto L10;
            int r4 = 1;
        L11:
            r0.writeInt(r4);     // Catch: Throwable -> L28
            if (this.encrypt == false) goto L21;
            byte[] r6 = new byte[16];     // Catch: Throwable -> L28
            ((SecureRandom) Util.castNonNull(this.random)).nextBytes(r6);     // Catch: Throwable -> L28
            r0.write(r6);     // Catch: Throwable -> L28
            ((Cipher) Util.castNonNull(this.cipher)).init(1, (Key) Util.castNonNull(this.secretKeySpec), new IvParameterSpec(r6));     // Catch: InvalidAlgorithmParameterException -> L16 InvalidKeyException -> L18 Throwable -> L28
            r0.flush();     // Catch: Throwable -> L28
            r0 = new DataOutputStream(new CipherOutputStream(r2, this.cipher));     // Catch: Throwable -> L28
            goto L21
        L16:
            e = e;
        L20:
            throw new IllegalStateException(e);     // Catch: Throwable -> L28
        L18:
            e = e;
        L21:
            r0.writeInt(r11.size());     // Catch: Throwable -> L28
            int r5 = 0;
            Iterator<CachedContent> r7 = r11.values().iterator();     // Catch: Throwable -> L28
        L23:
            if (r7.hasNext() == false) goto L25;
            CachedContent r8 = r7.next();     // Catch: Throwable -> L28
            writeCachedContent(r8, r0);     // Catch: Throwable -> L28
            r5 = r5 + hashCachedContent(r8, 2);     // Catch: Throwable -> L28
            goto L23
        L25:
            r0.writeInt(r5);     // Catch: Throwable -> L28
            this.atomicFile.endWrite(r0);     // Catch: Throwable -> L28
            Util.closeQuietly(null);
            return;
        L10:
            r4 = 0;
            goto L11
        L6:
            this.bufferedOutputStream.reset(r1);     // Catch: Throwable -> L28
        L28:
            th = move-exception;
            Util.closeQuietly(null);
            throw th;
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void delete() {
            this.atomicFile.delete();
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public boolean exists() {
            return this.atomicFile.exists();
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void initialize(long r1) {
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void load(HashMap<String, CachedContent> r2, SparseArray<String> r3) {
            Assertions.checkState(!this.changed);
            if (readFile(r2, r3) == true) goto L6;
            r2.clear();
            r3.clear();
            this.atomicFile.delete();
            return;
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void onRemove(CachedContent r2, boolean r3) {
            this.changed = true;
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void onUpdate(CachedContent r2) {
            this.changed = true;
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void storeFully(HashMap<String, CachedContent> r2) throws IOException {
            writeFile(r2);
            this.changed = false;
        }

        @Override // androidx.media3.datasource.cache.CachedContentIndex.Storage
        public void storeIncremental(HashMap<String, CachedContent> r2) throws IOException {
            if (this.changed == true) goto L5;
            return;
        L5:
            storeFully(r2);
        }
    }

    private interface Storage {
        void delete() throws IOException;

        boolean exists() throws IOException;

        void initialize(long r1);

        void load(HashMap<String, CachedContent> r1, SparseArray<String> r2) throws IOException;

        void onRemove(CachedContent r1, boolean r2);

        void onUpdate(CachedContent r1);

        void storeFully(HashMap<String, CachedContent> r1) throws IOException;

        void storeIncremental(HashMap<String, CachedContent> r1) throws IOException;
    }

    public CachedContentIndex(DatabaseProvider r7) {
        this(r7, null, null, false, false);
    }

    public CachedContentIndex(DatabaseProvider r5, File r6, byte[] r7, boolean r8, boolean r9) {
        if (r5 != null) goto L7;
        if (r6 != null) goto L7;
        boolean r0 = false;
    L8:
        Assertions.checkState(r0);
        this.keyToContent = new HashMap();
        this.idToKey = new SparseArray();
        this.removedIds = new SparseBooleanArray();
        this.newIds = new SparseBooleanArray();
        LegacyStorage r1 = null;
        if (r5 == null) goto L11;
        DatabaseStorage r2 = new DatabaseStorage(r5);
    L12:
        if (r6 == null) goto L16;
        r1 = new LegacyStorage(new File(r6, FILE_NAME_ATOMIC), r7, r8);
    L16:
        if (r2 == null) goto L21;
        if (r1 == null) goto L20;
        if (r9 == true) goto L21;
    L20:
        this.storage = r2;
        this.previousStorage = r1;
        return;
    L21:
        this.storage = (Storage) Util.castNonNull(r1);
        this.previousStorage = r2;
        return;
    L11:
        r2 = null;
    L7:
        r0 = true;
        goto L8
    }

    static /* synthetic */ DefaultContentMetadata access$000(DataInputStream r1) throws IOException {
        return readContentMetadata(r1);
    }

    static /* synthetic */ void access$100(DefaultContentMetadata r0, DataOutputStream r1) throws IOException {
        writeContentMetadata(r0, r1);
    }

    private CachedContent addNew(String r5) {
        int r0 = getNewId(this.idToKey);
        CachedContent r1 = new CachedContent(r0, r5);
        this.keyToContent.put(r5, r1);
        this.idToKey.put(r0, r5);
        this.newIds.put(r0, true);
        this.storage.onUpdate(r1);
        return r1;
    }

    public static void delete(DatabaseProvider r0, long r1) throws DatabaseIOException {
        DatabaseStorage.delete(r0, r1);
    }

    static int getNewId(SparseArray<String> r3) {
        int r0 = r3.size();
        if (r0 != 0) goto L5;
        int r1 = 0;
    L6:
        if (r1 >= 0) goto L13;
        r1 = 0;
    L8:
        if (r1 >= r0) goto L13;
        if (r1 != r3.keyAt(r1)) goto L13;
        r1 = r1 + 1;
    L13:
        return r1;
    L5:
        r1 = r3.keyAt(r0 - 1) + 1;
        goto L6
    }

    public static boolean isIndexFile(String r1) {
        return r1.startsWith(FILE_NAME_ATOMIC);
    }

    private static DefaultContentMetadata readContentMetadata(DataInputStream r10) throws IOException {
        int r0 = r10.readInt();
        HashMap r1 = new HashMap();
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L13;
        String r3 = r10.readUTF();
        int r4 = r10.readInt();
        if (r4 < 0) goto L11;
        int r5 = 0;
        int r7 = Math.min(r4, INCREMENTAL_METADATA_READ_LENGTH);
        byte[] r8 = Util.EMPTY_BYTE_ARRAY;
    L7:
        if (r5 == r4) goto L9;
        r8 = Arrays.copyOf(r8, r5 + r7);
        r10.readFully(r8, r5, r7);
        r5 = r5 + r7;
        r7 = Math.min(r4 - r5, INCREMENTAL_METADATA_READ_LENGTH);
        goto L7
    L9:
        r1.put(r3, r8);
        r2 = r2 + 1;
        goto L3
    L11:
        throw new IOException("Invalid value size: " + r4);
    L13:
        return new DefaultContentMetadata(r1);
    }

    private static void writeContentMetadata(DefaultContentMetadata r5, DataOutputStream r6) throws IOException {
        Set<Map.Entry<String, byte[]>> r0 = r5.entrySet();
        r6.writeInt(r0.size());
        Iterator<Map.Entry<String, byte[]>> r1 = r0.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        Map.Entry<String, byte[]> r2 = r1.next();
        r6.writeUTF(r2.getKey());
        byte[] r3 = r2.getValue();
        r6.writeInt(r3.length);
        r6.write(r3);
        goto L4
    }

    public void applyContentMetadataMutations(String r3, ContentMetadataMutations r4) {
        CachedContent r0 = getOrAdd(r3);
        if (r0.applyMetadataMutations(r4) == false) goto L6;
        this.storage.onUpdate(r0);
        return;
    }

    public int assignIdForKey(String r2) {
        return getOrAdd(r2).f33id;
    }

    public CachedContent get(String r2) {
        return this.keyToContent.get(r2);
    }

    public Collection<CachedContent> getAll() {
        return Collections.unmodifiableCollection(this.keyToContent.values());
    }

    public ContentMetadata getContentMetadata(String r3) {
        CachedContent r0 = get(r3);
        if (r0 == null) goto L6;
        return r0.getMetadata();
    L6:
        return DefaultContentMetadata.EMPTY;
    }

    public String getKeyForId(int r2) {
        return this.idToKey.get(r2);
    }

    public Set<String> getKeys() {
        return this.keyToContent.keySet();
    }

    public CachedContent getOrAdd(String r3) {
        CachedContent r0 = this.keyToContent.get(r3);
        if (r0 != null) goto L6;
        return addNew(r3);
    L6:
        return r0;
    }

    public void initialize(long r4) throws IOException {
        this.storage.initialize(r4);
        if (this.previousStorage == null) goto L6;
        this.previousStorage.initialize(r4);
    L6:
        if (this.storage.exists() == false) goto L8;
    L12:
        this.storage.load(this.keyToContent, this.idToKey);
    L14:
        if (this.previousStorage == null) goto L17;
        this.previousStorage.delete();
        this.previousStorage = null;
        return;
    L17:
        return;
    L8:
        if (this.previousStorage == null) goto L12;
        if (this.previousStorage.exists() == false) goto L12;
        this.previousStorage.load(this.keyToContent, this.idToKey);
        this.storage.storeFully(this.keyToContent);
        goto L14
    }

    public void maybeRemove(String r6) {
        CachedContent r0 = this.keyToContent.get(r6);
        if (r0 != null) goto L5;
        return;
    L5:
        if (r0.isEmpty() == true) goto L7;
        return;
    L7:
        if (r0.isFullyUnlocked() == false) goto L15;
        this.keyToContent.remove(r6);
        int r1 = r0.f33id;
        boolean r2 = this.newIds.get(r1);
        this.storage.onRemove(r0, r2);
        if (r2 == false) goto L11;
        this.idToKey.remove(r1);
        this.newIds.delete(r1);
        return;
    L11:
        this.idToKey.put(r1, null);
        this.removedIds.put(r1, true);
        return;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void removeEmpty() {
        UnmodifiableIterator r0 = ImmutableSet.copyOf(this.keyToContent.keySet()).iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        maybeRemove((String) r0.next());
        goto L4
    }

    public void store() throws IOException {
        this.storage.storeIncremental(this.keyToContent);
        int r0 = this.removedIds.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        this.idToKey.remove(this.removedIds.keyAt(r1));
        r1 = r1 + 1;
        goto L3
    L5:
        this.removedIds.clear();
        this.newIds.clear();
    }
}
