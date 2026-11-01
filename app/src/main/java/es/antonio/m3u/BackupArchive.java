package es.antonio.m3u;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class BackupArchive {
    static final Set<String> FILES = null;
    static final long LIMIT = 1073741824;
    static final byte[] MAGIC = null;

    static final class EncryptedInput extends InputStream {

        /* JADX INFO: renamed from: at */
        int f172at;
        byte[] buffer;
        int count;
        boolean end;

        /* JADX INFO: renamed from: in */
        final DataInputStream f173in;

        /* JADX INFO: renamed from: iv */
        final byte[] f174iv;
        final SecretKey key;

        EncryptedInput(InputStream r2, SecretKey r3, byte[] r4) {
            this.buffer = new byte[0];
            this.f173in = new DataInputStream(r2);
            this.key = r3;
            this.f174iv = r4;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.f173in.close();
        }

        boolean next() throws IOException {
            if (this.end == false) goto L5;
            return false;
        L5:
            int r7 = this.f173in.readInt();
            if (r7 < 16) goto L18;
            if (r7 > 65552) goto L18;
            byte[] r6 = new byte[r7];
            this.f173in.readFully(r6);
            SecretKey r3 = this.key;
            byte[] r4 = this.f174iv;
            int r5 = this.count;
            this.count = r5 + 1;
            this.buffer = BackupArchive.chunk(2, r3, r4, r5, r6, r7);
            this.f172at = 0;
            if (this.buffer.length != 0) goto L16;
            this.end = true;
            if (this.f173in.read() != (-1)) goto L15;
            return false;
        L15:
            throw new IOException("Copia dañada");
        L16:
            return true;
        L18:
            throw new IOException("Copia dañada");
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.f172at == this.buffer.length) goto L5;
        L8:
            byte[] r0 = this.buffer;
            int r1 = this.f172at;
            this.f172at = r1 + 1;
            return r0[r1] & 255;
        L5:
            if (next() == true) goto L8;
            return -1;
        }

        @Override // java.io.InputStream
        public int read(byte[] r4, int r5, int r6) throws IOException {
            if (r6 != 0) goto L6;
            return 0;
        L6:
            if (this.f172at == this.buffer.length) goto L8;
        L11:
            int r0 = Math.min(r6, this.buffer.length - this.f172at);
            System.arraycopy(this.buffer, this.f172at, r4, r5, r0);
            this.f172at += r0;
            return r0;
        L8:
            if (next() == true) goto L11;
            return -1;
        }
    }

    static final class EncryptedOutput extends OutputStream {
        final byte[] buffer;
        boolean closed;
        int count;

        /* JADX INFO: renamed from: iv */
        final byte[] f175iv;
        final SecretKey key;
        final DataOutputStream out;
        int used;

        EncryptedOutput(OutputStream r2, SecretKey r3, byte[] r4) {
            this.buffer = new byte[65536];
            this.out = new DataOutputStream(r2);
            this.key = r3;
            this.f175iv = r4;
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.closed == false) goto L5;
            return;
        L5:
            this.closed = true;
        L12:
            th = move-exception;
            this.out.close();
            throw th;
        L7:
            if (this.used <= 0) goto L9;
            emit();     // Catch: Throwable -> L12
        L9:
            emit();     // Catch: Throwable -> L12
            this.out.flush();     // Catch: Throwable -> L12
            this.out.close();
        }

        void emit() throws IOException {
            SecretKey r1 = this.key;
            byte[] r2 = this.f175iv;
            int r3 = this.count;
            this.count = r3 + 1;
            byte[] r0 = BackupArchive.chunk(1, r1, r2, r3, this.buffer, this.used);
            this.out.writeInt(r0.length);
            this.out.write(r0);
            this.used = 0;
        }

        @Override // java.io.OutputStream
        public void write(int r4) throws IOException {
            byte[] r0 = this.buffer;
            int r1 = this.used;
            this.used = r1 + 1;
            r0[r1] = (byte) r4;
            if (this.used != this.buffer.length) goto L6;
            emit();
            return;
        }

        @Override // java.io.OutputStream
        public void write(byte[] r4, int r5, int r6) throws IOException {
        L2:
            if (r6 <= 0) goto L6;
            int r0 = Math.min(r6, this.buffer.length - this.used);
            System.arraycopy(r4, r5, this.buffer, this.used, r0);
            this.used += r0;
            r5 = r5 + r0;
            r6 = r6 - r0;
            if (this.used != this.buffer.length) goto L2;
            emit();
            goto L2
        }
    }

    static {
        FILES = new HashSet(Arrays.asList(new String[]{"catalog.dat", "epg.dat", "plex-import.dat"}));
        MAGIC = new byte[]{67, 72, 68, 66, 65, 75, 2};
    }

    BackupArchive() {
    }

    static void apply(Context r2, String r3, JSONObject r4) throws Exception {
        if (editor(r2, r3, r4).commit() == false) goto L6;
        return;
    L6:
        throw new IOException("No se pudieron restaurar los ajustes");
    }

    static byte[] chunk(int r4, SecretKey r5, byte[] r6, int r7, byte[] r8, int r9) throws IOException {
        Cipher r0 = Cipher.getInstance("AES/GCM/NoPadding");     // Catch: Exception -> L4
        r0.init(r4, r5, new GCMParameterSpec(128, nonce(r6, r7)));     // Catch: Exception -> L4
        r0.updateAAD(MAGIC);     // Catch: Exception -> L4
        return r0.doFinal(r8, 0, r9);
    L4:
        e = move-exception;
        throw new IOException("Contraseña incorrecta o copia dañada", e);
    }

    static Cipher cipher(int r4, String r5, byte[] r6, byte[] r7) throws Exception {
        Cipher r0 = Cipher.getInstance("AES/GCM/NoPadding");
        r0.init(r4, key(r5, r6), new GCMParameterSpec(128, r7));
        r0.updateAAD(MAGIC);
        return r0;
    }

    static void copy(InputStream r7, OutputStream r8, long r9) throws IOException {
        byte[] r0 = new byte[65536];
        long r1 = 0;
    L3:
        int r3 = r7.read(r0);
        if (r3 == (-1)) goto L10;
        r1 = r1 + ((long) r3);
        if (r1 > r9) goto L9;
        r8.write(r0, 0, r3);
        goto L3
    L9:
        throw new IOException("La copia supera el límite de 1 GB");
    }

    static void delete(File r4) {
        File[] r0 = r4.listFiles();
        if (r0 == null) goto L7;
        int r1 = r0.length;
        int r2 = 0;
    L5:
        if (r2 >= r1) goto L7;
        delete(r0[r2]);
        r2 = r2 + 1;
    L7:
        r4.delete();
    }

    static SharedPreferences.Editor editor(Context r9, String r10, JSONObject r11) throws Exception {
        SharedPreferences.Editor r1 = r9.getSharedPreferences(r10, 0).edit().clear();
        Iterator<String> r2 = r11.keys();
    L4:
        if (r2.hasNext() == false) goto L41;
        String r3 = r2.next();
        JSONObject r4 = r11.getJSONObject(r3);
        String r5 = r4.getString("type");
        switch(r5.hashCode()) {
            case -891985903: goto L24;
            case 104431: goto L21;
            case 113762: goto L18;
            case 3029738: goto L15;
            case 3327612: goto L12;
            case 97526364: goto L9;
            default: goto L26;
        };
    L26:
        byte r6 = -1;
    L28:
        switch(r6) {
            case 0: goto L40;
            case 1: goto L39;
            case 2: goto L38;
            case 3: goto L37;
            case 4: goto L36;
            case 5: goto L31;
            default: goto L30;
        };
    L30:
        throw new IOException("Ajustes incompatibles");
    L36:
        r1.putFloat(r3, (float) r4.getDouble("value"));
        goto L4
    L37:
        r1.putLong(r3, r4.getLong("value"));
        goto L4
    L38:
        r1.putInt(r3, r4.getInt("value"));
        goto L4
    L39:
        r1.putBoolean(r3, r4.getBoolean("value"));
        goto L4
    L40:
        r1.putString(r3, r4.getString("value"));
        goto L4
    L31:
        HashSet r7 = new HashSet();
        JSONArray r8 = r4.getJSONArray("value");
        int r12 = 0;
    L33:
        if (r12 >= r8.length()) goto L35;
        r7.add(r8.getString(r12));
        r12 = r12 + 1;
        goto L33
    L35:
        r1.putStringSet(r3, r7);
        goto L4
    L9:
        if (r5.equals("float") == false) goto L26;
        r6 = 4;
        goto L28
    L12:
        if (r5.equals("long") == false) goto L26;
        r6 = 3;
        goto L28
    L15:
        if (r5.equals("bool") == false) goto L26;
        r6 = 1;
        goto L28
    L18:
        if (r5.equals("set") == false) goto L26;
        r6 = 5;
        goto L28
    L21:
        if (r5.equals("int") == false) goto L26;
        r6 = 2;
        goto L28
    L24:
        if (r5.equals("string") == false) goto L26;
        r6 = 0;
        goto L28
    L41:
        return r1;
    }

    static SecretKey key(String r4, byte[] r5) throws Exception {
        PBEKeySpec r0 = new PBEKeySpec(r4.toCharArray(), r5, 150000, 256);
        SecretKeySpec r1 = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(r0).getEncoded(), "AES");     // Catch: Throwable -> L6
        r0.clearPassword();
        return r1;
    L6:
        th = move-exception;
        r0.clearPassword();
        throw th;
    }

    static List<String> names(Context r9) {
        ArrayList r0 = new ArrayList();
        File[] r2 = new File(r9.getApplicationInfo().dataDir, "shared_prefs").listFiles();
        if (r2 == null) goto L10;
        int r3 = r2.length;
        int r5 = 0;
    L5:
        if (r5 >= r3) goto L10;
        File r6 = r2[r5];
        if (r6.getName().endsWith(".xml") == false) goto L9;
        r0.add(r6.getName().substring(0, r6.getName().length() - 4));
    L9:
        r5 = r5 + 1;
    L10:
        return r0;
    }

    static byte[] nonce(byte[] r4, int r5) {
        byte[] r0 = (byte[]) r4.clone();
        int r1 = 0;
    L4:
        if (r1 >= 4) goto L6;
        r0[r1 + 8] = (byte) (r5 >>> (24 - (r1 * 8)));
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    static JSONObject preference(Context r9, String r10) throws Exception {
        JSONObject r0 = new JSONObject();
        Iterator<Map.Entry<String, ?>> r1 = r9.getSharedPreferences(r10, 0).getAll().entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L26;
        Map.Entry<String, ?> r2 = r1.next();
        Object r3 = r2.getValue();
        if ((r3 instanceof String) == false) goto L9;
        String r4 = "string";
    L21:
        String r5 = r2.getKey();
        JSONObject r6 = new JSONObject().put("type", r4);
        if ((r3 instanceof Set) == false) goto L24;
        Object r7 = new JSONArray((Set) r3);
    L25:
        r0.put(r5, r6.put("value", r7));
        goto L4
    L24:
        r7 = r3;
        goto L25
    L9:
        if ((r3 instanceof Boolean) == false) goto L12;
        r4 = "bool";
        goto L21
    L12:
        if ((r3 instanceof Integer) == false) goto L15;
        r4 = "int";
        goto L21
    L15:
        if ((r3 instanceof Long) == false) goto L18;
        r4 = "long";
        goto L21
    L18:
        if ((r3 instanceof Float) == false) goto L20;
        r4 = "float";
        goto L21
    L20:
        r4 = "set";
        goto L21
    L26:
        return r0;
    }

    static void restore(Context r27, InputStream r28, String r29) throws Exception {
        File r18 = new File(r27.getCacheDir(), "restore-" + UUID.randomUUID());
        if (r18.mkdirs() == false) goto L186;
        DataInputStream r5 = new DataInputStream(r28);     // Catch: Throwable -> L180
        byte[] r7 = new byte[MAGIC.length];     // Catch: Throwable -> L180
        byte[] r8 = new byte[16];     // Catch: Throwable -> L180
        byte[] r9 = new byte[12];     // Catch: Throwable -> L180
        r5.readFully(r7);     // Catch: Throwable -> L180
        if (Arrays.equals(r7, MAGIC) == false) goto L177;
        r5.readFully(r8);     // Catch: Throwable -> L180
        r5.readFully(r9);     // Catch: Throwable -> L180
        File r10 = new File(r18, "archive.zip");     // Catch: Throwable -> L180
        EncryptedInput r11 = new EncryptedInput(r5, key(r29, r8), r9);     // Catch: Throwable -> L174
        FileOutputStream r13 = new FileOutputStream(r10);     // Catch: Throwable -> L167
        copy(r11, r13, LIMIT);     // Catch: Throwable -> L158
        r13.close();     // Catch: Throwable -> L167
        r11.close();     // Catch: Throwable -> L174
        HashSet r12 = new HashSet();     // Catch: Throwable -> L174
        long r16 = 0;
        JSONObject r14 = null;
        ZipInputStream r15 = new ZipInputStream(new FileInputStream(r10));     // Catch: Throwable -> L174
    L201:
        ZipEntry r17 = r15.getNextEntry();     // Catch: Throwable -> L151
        DataInputStream r21 = r5;
        if (r17 == null) goto L67;
        String r19 = r17.getName();     // Catch: Throwable -> L64
        if (r12.add(r19) == false) goto L61;
        if (r19.equals("manifest.json") == false) goto L212;
    L27:
        File r6 = new File(r18, r19);     // Catch: Throwable -> L64
        FileOutputStream r20 = new FileOutputStream(r6);     // Catch: Throwable -> L64
        byte[] r22 = r7;
        byte[] r23 = r8;
        copy(r15, r20, LIMIT - r16);     // Catch: Throwable -> L52
    L220:
        r20.close();     // Catch: Throwable -> L50
        r16 = r16 + r6.length();     // Catch: Throwable -> L50
        if (r19.equals("manifest.json") == false) goto L49;
        if (r6.length() > 16777216) goto L47;
        ByteArrayOutputStream r24 = new ByteArrayOutputStream();     // Catch: Throwable -> L50
        FileInputStream r25 = new FileInputStream(r6);     // Catch: Throwable -> L50
        copy(r25, r24, 16777216);     // Catch: Throwable -> L39
        r25.close();     // Catch: Throwable -> L62
        r14 = new JSONObject(r24.toString("UTF-8"));     // Catch: Throwable -> L62
        goto L49
    L39:
        th = move-exception;
        r25.close();     // Catch: Throwable -> L43
    L261:
        throw th;     // Catch: Throwable -> L62
    L43:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L62
        throw th;     // Catch: Throwable -> L62
    L47:
        throw new IOException("Ajustes demasiado grandes");     // Catch: Throwable -> L62
    L49:
        r9 = r9;
        r5 = r21;
        r7 = r22;
        r8 = r23;
        r10 = r10;
    L50:
        th = move-exception;
        Throwable r2 = th;
    L222:
        r15.close();     // Catch: Throwable -> L155
    L263:
        throw r2;     // Catch: Throwable -> L178
    L155:
        th = move-exception;
        r2.addSuppressed(th);     // Catch: Throwable -> L178
        throw r2;     // Catch: Throwable -> L178
    L52:
        th = move-exception;
        r20.close();     // Catch: Throwable -> L56
    L262:
        throw th;     // Catch: Throwable -> L62
    L56:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L62
        throw th;     // Catch: Throwable -> L62
    L62:
        th = move-exception;
        r2 = th;
        goto L222
    L212:
        if (FILES.contains(r19) == false) goto L61;
    L25:
        th = move-exception;
        r2 = th;
    L61:
        throw new IOException("Copia incompatible");     // Catch: Throwable -> L62
    L64:
        th = move-exception;
        r2 = th;
        goto L222
    L67:
        r15.close();     // Catch: Throwable -> L174
        if (r14 == null) goto L150;
        if (r14.getString("app").equals("es.antonio.m3u") == false) goto L150;
        if (r14.getInt("version") != 1) goto L150;
        JSONObject r3 = r14.getJSONObject("preferences");     // Catch: Throwable -> L174
        r3.remove("cdn_token_state");     // Catch: Throwable -> L174
        r3.remove("dazn_token_state");     // Catch: Throwable -> L174
        r3.remove("timed-access-local");     // Catch: Throwable -> L174
        r3.remove("timed-issued-codes");     // Catch: Throwable -> L174
        r3.remove("backup-history-local");     // Catch: Throwable -> L174
        Iterator<String> r26 = r3.keys();     // Catch: Throwable -> L174
    L75:
        if (r26.hasNext() == false) goto L83;
        String r30 = r26.next();     // Catch: Throwable -> L81
        if (r30.matches("[A-Za-z0-9_.-]{1,160}") == false) goto L80;
        editor(r27, r30, r3.getJSONObject(r30));     // Catch: Throwable -> L81
        goto L75
    L80:
        throw new IOException("Ajustes incompatibles");     // Catch: Throwable -> L81
    L81:
        th = th;
        r18 = r18;
    L183:
        delete(r18);
        throw th;
    L83:
        File r31 = new File(r18, "rollback");     // Catch: Throwable -> L174
        if (r31.mkdir() == false) goto L148;
        JSONObject r32 = new JSONObject();     // Catch: Throwable -> L174
        Iterator<String> r33 = names(r27).iterator();     // Catch: Throwable -> L174
    L87:
        if (r33.hasNext() == false) goto L90;
        String r110 = r33.next();     // Catch: Throwable -> L81
        r32.put(r110, preference(r27, r110));     // Catch: Throwable -> L81
        goto L87
    L90:
        Iterator<String> r34 = FILES.iterator();     // Catch: Throwable -> L174
    L92:
        if (r34.hasNext() == false) goto L190;
        String r111 = r34.next();     // Catch: Throwable -> L81
        File r112 = new File(r27.getFilesDir(), r111);     // Catch: Throwable -> L81
        if (r112.exists() == false) goto L92;
        transfer(r112, new File(r31, r111));     // Catch: Throwable -> L81
        goto L92
    L190:
        Iterator<String> r35 = FILES.iterator();     // Catch: Exception -> L128 Throwable -> L174
    L99:
        if (r35.hasNext() == false) goto L107;
        String r113 = r35.next();     // Catch: Exception -> L128 Throwable -> L174
        new AppStorage(r27).remove(r113);     // Catch: Exception -> L128 Throwable -> L174
        File r114 = new File(r18, r113);     // Catch: Exception -> L128 Throwable -> L174
        if (r114.exists() == false) goto L106;
    L210:
        transfer(r114, new File(r27.getFilesDir(), r113));     // Catch: Exception -> L126 Throwable -> L178
    L126:
        e = e;
    L130:
        Iterator<String> r4 = FILES.iterator();     // Catch: Throwable -> L178
    L132:
        if (r4.hasNext() == false) goto L136;
        String r36 = r4.next();     // Catch: Throwable -> L178
        new AppStorage(r27).remove(r36);     // Catch: Throwable -> L178
        File r37 = new File(r31, r36);     // Catch: Throwable -> L178
        if (r37.exists() == false) goto L132;
        transfer(r37, new File(r27.getFilesDir(), r36));     // Catch: Throwable -> L178
        goto L132
    L136:
        Iterator<String> r38 = names(r27).iterator();     // Catch: Throwable -> L178
    L138:
        if (r38.hasNext() == false) goto L142;
        String r39 = r38.next();     // Catch: Throwable -> L178
        if (r32.has(r39) == true) goto L138;
        apply(r27, r39, new JSONObject());     // Catch: Throwable -> L178
        goto L138
    L142:
        Iterator<String> r40 = r32.keys();     // Catch: Throwable -> L178
    L144:
        if (r40.hasNext() == false) goto L146;
        String r41 = r40.next();     // Catch: Throwable -> L178
        apply(r27, r41, r32.getJSONObject(r41));     // Catch: Throwable -> L178
        goto L144
    L146:
        throw e;     // Catch: Throwable -> L178
    L106:
        r18 = r18;
        goto L99
    L107:
        File r115 = r18;
        Iterator<String> r42 = names(r27).iterator();     // Catch: Exception -> L126 Throwable -> L178
    L109:
        if (r42.hasNext() == false) goto L117;
        String r43 = r42.next();     // Catch: Exception -> L126 Throwable -> L178
        if (r43.equals("timed-access-local") == true) goto L109;
        if (r43.equals("backup-history-local") == true) goto L109;
        if (r3.has(r43) == true) goto L109;
        apply(r27, r43, new JSONObject());     // Catch: Exception -> L126 Throwable -> L178
        goto L109
    L117:
        Iterator<String> r0 = r3.keys();     // Catch: Exception -> L126 Throwable -> L178
    L118:
        if (r0.hasNext() == false) goto L123;
        String r1 = r0.next();     // Catch: Exception -> L126 Throwable -> L178
        apply(r27, r1, r3.getJSONObject(r1));     // Catch: Exception -> L126 Throwable -> L178
        goto L118
    L123:
        CdnTokens.disable(r27);     // Catch: Throwable -> L178
        DaznTokens.disable(r27);     // Catch: Throwable -> L178
        ParentalControl.unlocked = false;     // Catch: Throwable -> L178
        delete(r115);
        return;
    L128:
        e = e;
        goto L130
    L148:
        throw new IOException("No hay espacio");     // Catch: Throwable -> L178
    L150:
        throw new IOException("Copia incompatible");     // Catch: Throwable -> L178
    L151:
        th = move-exception;
        r2 = th;
    L158:
        th = move-exception;
        r13.close();     // Catch: Throwable -> L162
    L264:
        throw th;     // Catch: Throwable -> L165
    L162:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L165
        throw th;     // Catch: Throwable -> L165
    L165:
        th = move-exception;
        Throwable r44 = th;
    L199:
        r11.close();     // Catch: Throwable -> L171
    L265:
        throw r44;     // Catch: Throwable -> L178
    L171:
        th = move-exception;
        r44.addSuppressed(th);     // Catch: Throwable -> L178
        throw r44;     // Catch: Throwable -> L178
    L167:
        th = move-exception;
        r44 = th;
    L174:
        th = th;
    L182:
        r18 = r18;
        goto L183
    L177:
        throw new IOException("No es una copia de Chdtv");     // Catch: Throwable -> L178
    L178:
        th = th;
    L180:
        th = th;
        goto L182
    L186:
        throw new IOException("No hay espacio para restaurar");
    }

    static void transfer(File r4, File r5) throws IOException {
        FileInputStream r0 = new FileInputStream(r4);
        FileOutputStream r1 = new FileOutputStream(r5);     // Catch: Throwable -> L15
        copy(r0, r1, LIMIT);     // Catch: Throwable -> L9
        r1.close();     // Catch: Throwable -> L15
        r0.close();
        return;
    L9:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L12
    L14:
        throw th;     // Catch: Throwable -> L15
    L12:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        r0.close();     // Catch: Throwable -> L18
    L20:
        throw th;
    L18:
        th = move-exception;
        th.addSuppressed(th);
        goto L20
    }

    static void write(Context r12, OutputStream r13, String r14) throws Exception {
        byte[] r0 = new byte[16];
        byte[] r1 = new byte[12];
        SecureRandom r2 = new SecureRandom();
        r2.nextBytes(r0);
        r2.nextBytes(r1);
        r13.write(MAGIC);
        r13.write(r0);
        r13.write(r1);
        JSONObject r3 = new JSONObject();
        Iterator<String> r4 = names(r12).iterator();
    L4:
        if (r4.hasNext() == false) goto L16;
        String r5 = r4.next();
        if (r5.equals("cdn_token_state") == true) goto L4;
        if (r5.equals("dazn_token_state") == true) goto L4;
        if (r5.equals("timed-access-local") == true) goto L4;
        if (r5.equals("timed-issued-codes") == true) goto L4;
        if (r5.equals("backup-history-local") == true) goto L4;
        r3.put(r5, preference(r12, r5));
        goto L4
    L16:
        JSONObject r6 = new JSONObject().put("app", "es.antonio.m3u").put("version", 1).put("created", System.currentTimeMillis()).put("preferences", r3);
        ZipOutputStream r7 = new ZipOutputStream(new EncryptedOutput(r13, key(r14, r0), r1));
        r7.putNextEntry(new ZipEntry("manifest.json"));     // Catch: Throwable -> L36
        r7.write(r6.toString().getBytes(StandardCharsets.UTF_8));     // Catch: Throwable -> L36
        r7.closeEntry();     // Catch: Throwable -> L36
        Iterator<String> r8 = FILES.iterator();     // Catch: Throwable -> L36
    L19:
        if (r8.hasNext() == false) goto L34;
        String r9 = r8.next();     // Catch: Throwable -> L36
        File r10 = new File(r12.getFilesDir(), r9);     // Catch: Throwable -> L36
        if (r10.exists() == false) goto L19;
        r7.putNextEntry(new ZipEntry(r9));     // Catch: Throwable -> L36
        FileInputStream r11 = SourceStore.backupInput(r10);     // Catch: Throwable -> L36
        copy(r11, r7, LIMIT);     // Catch: Throwable -> L28
        r11.close();     // Catch: Throwable -> L36
        r7.closeEntry();     // Catch: Throwable -> L36
        goto L19
    L28:
        th = move-exception;
        r11.close();     // Catch: Throwable -> L31
    L33:
        throw th;     // Catch: Throwable -> L36
    L31:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L36
        goto L33
    L34:
        r7.close();
        return;
    L36:
        th = move-exception;
        r7.close();     // Catch: Throwable -> L39
    L41:
        throw th;
    L39:
        th = move-exception;
        th.addSuppressed(th);
        goto L41
    }
}
