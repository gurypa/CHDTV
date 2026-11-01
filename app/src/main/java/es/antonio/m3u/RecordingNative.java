package es.antonio.m3u;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import androidx.core.os.EnvironmentCompat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* JADX INFO: loaded from: classes7.dex */
public final class RecordingNative {
    private static final String[] DEPENDENCIES = null;
    static final Loader SYSTEM = null;
    private static String abi;
    private static Context app;
    private static final Set<String> loaded = null;
    private static Loader loader;
    private static File root;

    interface Loader {
        void absolute(String r1);

        void named(String r1);
    }

    static {
        SYSTEM = new C10291();
        loader = SYSTEM;
        loaded = new HashSet();
        DEPENDENCIES = new String[]{"c++_shared", "avutil", "swresample", "swscale", "avcodec", "avformat", "avfilter", "avdevice"};
    }

    public RecordingNative() {
    }

    static String choose(ZipFile r6, String[] r7, boolean r8) {
        int r0 = r7.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L4;
        String r3 = r7[r2];
        if (r3.equals("arm64-v8a") == false) goto L9;
    L11:
        boolean r4 = true;
    L12:
        if (r4 != r8) goto L19;
        if (r3.matches("[a-zA-Z0-9_-]+") == false) goto L19;
        if (r6.getEntry("lib/" + r3 + "/libffmpegkit_abidetect.so") == null) goto L19;
        return r3;
    L19:
        r2 = r2 + 1;
        goto L3
    L9:
        if (r3.equals("x86_64") == true) goto L11;
        r4 = false;
        goto L12
    L4:
        return null;
    }

    public static synchronized void configure(Context r1) {
        monitor-enter(RecordingNative.class);
        app = r1.getApplicationContext();     // Catch: Throwable -> L7
        monitor-exit(RecordingNative.class);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    static void failure(String r2, Throwable r3) {
        BlackBox.event("RECORDING_NATIVE_ERROR", "stage=" + token(r2) + " type=" + r3.getClass().getSimpleName() + " reason=" + reason(r3));
    }

    static boolean fatal(Throwable r1) {
        if ((r1 instanceof VirtualMachineError) == false) goto L5;
        return true;
    L5:
        if ((r1 instanceof ThreadDeath) == true) goto L10;
        return false;
    L10:
        return true;
    }

    private static void loadAbsolute(ZipFile r2, String r3) throws IOException {
        if (loaded.contains(r3) == false) goto L5;
        return;
    L5:
        File r4 = unpack(r2, root, abi, r3);
        loader.absolute(r4.getAbsolutePath());
        loaded.add(r3);
    }

    private static void loadDependency(ZipFile r1, String r2) throws IOException {
        if (loaded.contains(r2) == false) goto L10;
        return;
    L10:
        loader.named(r2);     // Catch: UnsatisfiedLinkError -> L7
        loaded.add(r2);     // Catch: UnsatisfiedLinkError -> L7
        return;
    L7:
        e = move-exception;
        failure(r2, e);
        loadAbsolute(r1, r2);
    }

    public static synchronized void loadLibrary(String r9) {
        monitor-enter(RecordingNative.class);
        loader.named(r9);     // Catch: Throwable -> L7 UnsatisfiedLinkError -> L9
    L5:
        monitor-exit(RecordingNative.class);
        return;
    L7:
        th = move-exception;
        throw th;
    L9:
        e = move-exception;
        failure(r9, e);     // Catch: Throwable -> L7
        if (app != null) goto L61;
        throw e;     // Catch: Throwable -> L7
    L61:
    L51:
        e = move-exception;
        failure(r9, e);     // Catch: Throwable -> L7
        UnsatisfiedLinkError r10 = new UnsatisfiedLinkError("Recording engine load failed");     // Catch: Throwable -> L7
        r10.initCause(e);     // Catch: Throwable -> L7
        throw r10;     // Catch: Throwable -> L7
    L13:
        Throwable r2 = null;
        ZipFile r3 = new ZipFile(new File(app.getApplicationInfo().sourceDir));     // Catch: Throwable -> L43
    L39:
        th = move-exception;
        r3.close();     // Catch: Throwable -> L43
        throw th;     // Catch: Throwable -> L43
    L16:
        if (root != null) goto L23;
        abi = choose(r3, Build.SUPPORTED_ABIS, Process.is64Bit());     // Catch: Throwable -> L39
        if (abi == null) goto L21;
        root = new File(app.getFilesDir(), "recording-native-v189/" + abi);     // Catch: Throwable -> L39
        BlackBox.event("RECORDING_NATIVE_FALLBACK", "abi=" + abi + " apk_local=true");     // Catch: Throwable -> L39
        goto L23
    L21:
        throw new IOException("No packaged ABI for recording process");     // Catch: Throwable -> L39
    L23:
        if (r9.startsWith("ffmpegkit") == true) goto L25;
    L35:
        loadAbsolute(r3, r9);     // Catch: Throwable -> L39
        r3.close();     // Catch: Throwable -> L43
        BlackBox.event("RECORDING_NATIVE_RECOVERED", "library=" + token(r9));     // Catch: Throwable -> L7 UnsatisfiedLinkError -> L49 Throwable -> L51
        goto L5
    L25:
        if (r9.equals("ffmpegkit_abidetect") == true) goto L35;
        String[] r1 = DEPENDENCIES;     // Catch: Throwable -> L39
        int r4 = r1.length;     // Catch: Throwable -> L39
        int r5 = 0;
    L27:
        if (r5 >= r4) goto L35;
        String r6 = r1[r5];     // Catch: Throwable -> L39
        if (abi.equals("armeabi-v7a") == false) goto L34;
        if (r6.equals("c++_shared") == true) goto L34;
        r6 = String.valueOf(r6) + "_neon";     // Catch: Throwable -> L39
    L34:
        loadDependency(r3, r6);     // Catch: Throwable -> L39
        r5 = r5 + 1;     // Catch: Throwable -> L39
    L43:
        th = move-exception;
        if (0 == 0) goto L66;
        if (null == th) goto L48;
        r2.addSuppressed(th);     // Catch: Throwable -> L7 UnsatisfiedLinkError -> L49 Throwable -> L51
    L48:
        throw null;     // Catch: Throwable -> L7 UnsatisfiedLinkError -> L49 Throwable -> L51
    }

    static String reason(Throwable r5) {
        Set r0 = Collections.newSetFromMap(new IdentityHashMap());
    L3:
        if (r5 == null) goto L37;
        if (r0.add(r5) == false) goto L52;
        String r1 = r5.getMessage();
        if (r1 == null) goto L32;
        Matcher r2 = Pattern.compile("cannot locate symbol [\"']([^\"']+)").matcher(r1);
        if (r2.find() == true) goto L13;
        Matcher r3 = Pattern.compile("library [\"']([^\"']+)[\"'] not found").matcher(r1);
        if (r3.find() == true) goto L17;
        String r4 = r1.toLowerCase(Locale.ROOT);
        if (r4.contains("wrong elf") == true) goto L35;
        if (r4.contains("bad elf") == true) goto L51;
        if (r4.contains("align") == true) goto L33;
        if (r4.contains("page size") == true) goto L50;
        if (r4.contains("jni") == false) goto L32;
        return "jni_initialization";
    L50:
        return "alignment";
    L33:
        return "alignment";
    L51:
        return "bad_elf";
    L35:
        return "bad_elf";
    L17:
        return "missing_library dependency=" + token(r3.group(1));
    L13:
        return "missing_symbol symbol=" + token(r2.group(1));
    L32:
        r5 = r5.getCause();
        goto L3
    L52:
        return "native_load_failure";
    L37:
        return "native_load_failure";
    }

    static String token(String r1) {
        if (r1 != null) goto L4;
    L6:
        return EnvironmentCompat.MEDIA_UNKNOWN;
    L4:
        if (r1.matches("[A-Za-z0-9_+.$@-]{1,200}") == false) goto L6;
        return r1;
    }

    static File unpack(ZipFile r15, File r16, String r17, String r18) throws IOException {
        if (r18.matches("[A-Za-z0-9_+.-]+") == false) goto L131;
        ZipEntry r3 = r15.getEntry("lib/" + r17 + "/lib" + r18 + ".so");
        if (r3 == null) goto L129;
        if (r3.getSize() < 1) goto L127;
        if (r3.getSize() > 134217728) goto L127;
        if (r16.isDirectory() == false) goto L13;
    L17:
        File r5 = new File(r16, "lib" + r18 + ".so");
        File r4 = new File(r16, "lib" + r18 + ".crc");
        String r2 = String.valueOf(r3.getSize()) + ":" + r3.getCrc();
        if (r5.isFile() == true) goto L20;
    L43:
        File r1 = File.createTempFile("native-", ".tmp", r16);
        CRC32 r6 = new CRC32();     // Catch: Throwable -> L123
        InputStream r8 = r15.getInputStream(r3);     // Catch: Throwable -> L116
        FileOutputStream r9 = new FileOutputStream(r1);     // Catch: Throwable -> L105
        byte[] r0 = new byte[65536];     // Catch: Throwable -> L98
        long r10 = 0;
    L49:
        int r12 = r8.read(r0);     // Catch: Throwable -> L98
        if (r12 == (-1)) goto L140;
        InputStream r19 = r8;
        r10 = r10 + ((long) r12);
    L96:
        th = th;
    L100:
        Throwable r7 = th;
    L149:
        r9.close();     // Catch: Throwable -> L103
        throw r7;     // Catch: Throwable -> L103
    L103:
        th = th;
    L107:
        if (r7 != null) goto L109;
        r7 = th;
    L113:
        if (r19 == null) goto L168;
        r19.close();     // Catch: Throwable -> L111
        throw r7;     // Catch: Throwable -> L111
    L111:
        th = th;
    L118:
        if (r7 == null) goto L164;
        if (r7 == th) goto L163;
        r7.addSuppressed(th);     // Catch: Throwable -> L123
        throw r7;     // Catch: Throwable -> L123
    L163:
        throw r7;     // Catch: Throwable -> L123
    L164:
        throw th;     // Catch: Throwable -> L123
    L168:
        throw r7;     // Catch: Throwable -> L111
    L109:
        if (r7 == th) goto L113;
        r7.addSuppressed(th);     // Catch: Throwable -> L111
        goto L113
    L92:
        if (r10 > r3.getSize()) goto L95;
        r6.update(r0, 0, r12);     // Catch: Throwable -> L96
        r9.write(r0, 0, r12);     // Catch: Throwable -> L96
        r8 = r19;
        goto L49
    L95:
        throw new IOException("Native size mismatch");     // Catch: Throwable -> L96
    L140:
        r9.getFD().sync();     // Catch: Throwable -> L88
        r9.close();     // Catch: Throwable -> L105
        if (r8 == null) goto L57;
        r8.close();     // Catch: Throwable -> L116
    L57:
        if (r10 != r3.getSize()) goto L167;
        if (r6.getValue() != r3.getCrc()) goto L167;
        if (r1.setReadable(true, true) == false) goto L166;
        if (r1.setWritable(false, false) == false) goto L166;
        if (r1.renameTo(r5) == false) goto L84;
        FileWriter r11 = new FileWriter(r4);     // Catch: Throwable -> L76
        r11.write(r2);     // Catch: Throwable -> L70
        r11.close();     // Catch: Throwable -> L76
        return r5;
    L70:
        th = move-exception;
        Throwable r13 = th;
        r11.close();     // Catch: Throwable -> L74
        throw r13;     // Catch: Throwable -> L74
    L74:
        th = th;
    L78:
        if (r13 == null) goto L82;
        if (r13 == th) goto L161;
        r13.addSuppressed(th);     // Catch: Throwable -> L123
        throw r13;     // Catch: Throwable -> L123
    L161:
        throw r13;     // Catch: Throwable -> L123
    L82:
        throw th;     // Catch: Throwable -> L123
    L76:
        th = th;
        r13 = null;
        goto L78
    L84:
        throw new IOException("Native commit failed");     // Catch: Throwable -> L123
    L166:
        throw new IOException("Native permissions");     // Catch: Throwable -> L123
    L167:
        throw new IOException("Native checksum mismatch");     // Catch: Throwable -> L123
    L88:
        th = move-exception;
        r7 = th;
        r19 = r8;
    L98:
        th = th;
        r19 = r8;
    L105:
        th = th;
        r19 = r8;
        r7 = null;
    L116:
        th = th;
        r7 = null;
    L123:
        th = move-exception;
        r1.delete();
        throw th;
    L20:
        if (r5.length() != r3.getSize()) goto L43;
        if (r4.isFile() == false) goto L43;
        BufferedReader r14 = new BufferedReader(new FileReader(r4));     // Catch: Throwable -> L36
        if (r2.equals(r14.readLine()) == false) goto L28;
        r14.close();     // Catch: Throwable -> L36
        return r5;
    L28:
        r14.close();     // Catch: Throwable -> L36
    L30:
        th = move-exception;
        Throwable r20 = th;
        r14.close();     // Catch: Throwable -> L34
        throw r20;     // Catch: Throwable -> L34
    L34:
        th = th;
    L38:
        if (r20 == null) goto L42;
        if (r20 == th) goto L159;
        r20.addSuppressed(th);
        throw r20;
    L159:
        throw r20;
    L42:
        throw th;
    L36:
        th = th;
        r20 = null;
        goto L38
    L13:
        if (r16.mkdirs() == true) goto L17;
        throw new IOException("Native directory unavailable");
    L127:
        throw new IOException("Native library size");
    L129:
        throw new FileNotFoundException("Packaged recording library missing");
    L131:
        throw new IOException("Invalid native name");
    }
}
