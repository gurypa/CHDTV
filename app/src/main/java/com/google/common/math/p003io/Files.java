package com.google.common.p003io;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.SuccessorsFunction;
import com.google.common.graph.Traverser;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Files {
    private static final SuccessorsFunction<File> FILE_TREE = null;

    private static final class FileByteSink extends ByteSink {
        private final File file;
        private final ImmutableSet<FileWriteMode> modes;

        private FileByteSink(File r2, FileWriteMode... r3) {
            this.file = (File) Preconditions.checkNotNull(r2);
            this.modes = ImmutableSet.copyOf(r3);
        }

        /* synthetic */ FileByteSink(File r1, FileWriteMode[] r2, C08501 r3) {
            this(r1, r2);
        }

        @Override // com.google.common.p003io.ByteSink
        public FileOutputStream openStream() throws IOException {
            return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
        }

        @Override // com.google.common.p003io.ByteSink
        public /* bridge */ /* synthetic */ OutputStream openStream() throws IOException {
            return openStream();
        }

        public String toString() {
            return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
        }
    }

    private static final class FileByteSource extends ByteSource {
        private final File file;

        private FileByteSource(File r2) {
            this.file = (File) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ FileByteSource(File r1, C08501 r2) {
            this(r1);
        }

        @Override // com.google.common.p003io.ByteSource
        public FileInputStream openStream() throws IOException {
            return new FileInputStream(this.file);
        }

        @Override // com.google.common.p003io.ByteSource
        public /* bridge */ /* synthetic */ InputStream openStream() throws IOException {
            return openStream();
        }

        @Override // com.google.common.p003io.ByteSource
        public byte[] read() throws IOException {
            Closer r0 = Closer.create();
            FileInputStream r1 = (FileInputStream) r0.register(openStream());     // Catch: Throwable -> L6
            byte[] r2 = ByteStreams.toByteArray(r1, r1.getChannel().size());     // Catch: Throwable -> L6
            r0.close();
            return r2;
        L6:
            th = move-exception;
        L9:
            th = move-exception;
            r0.close();
            throw th;
        L8:
            throw r0.rethrow(th);     // Catch: Throwable -> L9
        }

        @Override // com.google.common.p003io.ByteSource
        public long size() throws IOException {
            if (this.file.isFile() == false) goto L7;
            return this.file.length();
        L7:
            throw new FileNotFoundException(this.file.toString());
        }

        @Override // com.google.common.p003io.ByteSource
        public Optional<Long> sizeIfKnown() {
            if (this.file.isFile() == false) goto L7;
            return Optional.m185of(Long.valueOf(this.file.length()));
        L7:
            return Optional.absent();
        }

        public String toString() {
            return "Files.asByteSource(" + this.file + ")";
        }
    }

    private static abstract enum FilePredicate extends Enum<FilePredicate> implements Predicate<File> {
        private static final /* synthetic */ FilePredicate[] $VALUES = null;
        public static final FilePredicate IS_DIRECTORY = null;
        public static final FilePredicate IS_FILE = null;

        private static /* synthetic */ FilePredicate[] $values() {
            return new FilePredicate[]{IS_DIRECTORY, IS_FILE};
        }

        static {
            final String r1 = "IS_DIRECTORY";
            final int r2 = 0;
            IS_DIRECTORY = new C08521(r1, r2);
            final String r3 = "IS_FILE";
            final int r4 = 1;
            IS_FILE = new C08532(r3, r4);
            $VALUES = $values();
        }

        private FilePredicate(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ FilePredicate(String r1, int r2, C08501 r3) {
            this(r1, r2);
        }

        public static FilePredicate valueOf(String r1) {
            return (FilePredicate) Enum.valueOf(FilePredicate.class, r1);
        }

        public static FilePredicate[] values() {
            return (FilePredicate[]) $VALUES.clone();
        }
    }

    static {
        FILE_TREE = new C08512();
    }

    private Files() {
    }

    @Deprecated
    public static void append(CharSequence r3, File r4, Charset r5) throws IOException {
        asCharSink(r4, r5, new FileWriteMode[]{FileWriteMode.APPEND}).write(r3);
    }

    public static ByteSink asByteSink(File r2, FileWriteMode... r3) {
        return new FileByteSink(r2, r3, null);
    }

    public static ByteSource asByteSource(File r2) {
        return new FileByteSource(r2, null);
    }

    public static CharSink asCharSink(File r1, Charset r2, FileWriteMode... r3) {
        return asByteSink(r1, r3).asCharSink(r2);
    }

    public static CharSource asCharSource(File r1, Charset r2) {
        return asByteSource(r1).asCharSource(r2);
    }

    public static void copy(File r2, File r3) throws IOException {
        Preconditions.checkArgument(!r2.equals(r3), "Source %s and destination %s must be different", r2, r3);
        asByteSource(r2).copyTo(asByteSink(r3, new FileWriteMode[0]));
    }

    public static void copy(File r1, OutputStream r2) throws IOException {
        asByteSource(r1).copyTo(r2);
    }

    @Deprecated
    public static void copy(File r1, Charset r2, Appendable r3) throws IOException {
        asCharSource(r1, r2).copyTo(r3);
    }

    public static void createParentDirs(File r4) throws IOException {
        Preconditions.checkNotNull(r4);
        File r0 = r4.getCanonicalFile().getParentFile();
        if (r0 != null) goto L5;
        return;
    L5:
        r0.mkdirs();
        if (r0.isDirectory() == false) goto L9;
        return;
    L9:
        throw new IOException("Unable to create parent directories of " + r4);
    }

    @Deprecated
    public static File createTempDir() {
        return TempFileCreator.INSTANCE.createTempDir();
    }

    public static boolean equal(File r7, File r8) throws IOException {
        Preconditions.checkNotNull(r7);
        Preconditions.checkNotNull(r8);
        if (r7 != r8) goto L5;
        return true;
    L5:
        if (r7.equals(r8) == true) goto L19;
        long r0 = r7.length();
        long r2 = r8.length();
        if (r0 == 0) goto L16;
        if (r2 == 0) goto L16;
        if (r0 == r2) goto L16;
        return false;
    L16:
        return asByteSource(r7).contentEquals(asByteSource(r8));
    L19:
        return true;
    }

    public static Traverser<File> fileTraverser() {
        return Traverser.forTree(FILE_TREE);
    }

    public static String getFileExtension(String r3) {
        Preconditions.checkNotNull(r3);
        String r0 = new File(r3).getName();
        int r1 = r0.lastIndexOf(46);
        if (r1 != (-1)) goto L6;
        return "";
    L6:
        return r0.substring(r1 + 1);
    }

    public static String getNameWithoutExtension(String r3) {
        Preconditions.checkNotNull(r3);
        String r0 = new File(r3).getName();
        int r1 = r0.lastIndexOf(46);
        if (r1 != (-1)) goto L6;
        return r0;
    L6:
        return r0.substring(0, r1);
    }

    @Deprecated
    public static HashCode hash(File r1, HashFunction r2) throws IOException {
        return asByteSource(r1).hash(r2);
    }

    public static Predicate<File> isDirectory() {
        return FilePredicate.IS_DIRECTORY;
    }

    public static Predicate<File> isFile() {
        return FilePredicate.IS_FILE;
    }

    public static MappedByteBuffer map(File r1) throws IOException {
        Preconditions.checkNotNull(r1);
        return map(r1, FileChannel.MapMode.READ_ONLY);
    }

    public static MappedByteBuffer map(File r2, FileChannel.MapMode r3) throws IOException {
        return mapInternal(r2, r3, -1);
    }

    public static MappedByteBuffer map(File r2, FileChannel.MapMode r3, long r4) throws IOException {
        if (r4 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "size (%s) may not be negative", r4);
        return mapInternal(r2, r3, r4);
    L5:
        r0 = false;
        goto L6
    }

    private static MappedByteBuffer mapInternal(File r9, FileChannel.MapMode r10, long r11) throws IOException {
        Preconditions.checkNotNull(r9);
        Preconditions.checkNotNull(r10);
        Closer r1 = Closer.create();
    L22:
        th = th;
    L35:
    L26:
        th = move-exception;
        r1.close();
        throw th;
    L25:
        throw r1.rethrow(th);     // Catch: Throwable -> L26
    L4:
        if (r10 != FileChannel.MapMode.READ_ONLY) goto L9;
        String r2 = "r";
    L10:
        FileChannel r3 = (FileChannel) r1.register(((RandomAccessFile) r1.register(new RandomAccessFile(r9, r2))).getChannel());     // Catch: Throwable -> L22
        if (r11 == (-1)) goto L29;
        long r7 = r11;
    L31:
        MappedByteBuffer r12 = r3.map(r10, 0, r7);     // Catch: Throwable -> L20
        r1.close();
        return r12;
    L20:
        th = th;
        goto L35
    L29:
        r7 = r3.size();     // Catch: Throwable -> L7
    L7:
        th = th;
        goto L35
    L9:
        r2 = "rw";
        goto L10
    }

    public static void move(File r3, File r4) throws IOException {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        Preconditions.checkArgument(!r3.equals(r4), "Source %s and destination %s must be different", r3, r4);
        if (r3.renameTo(r4) == true) goto L12;
        copy(r3, r4);
        if (r3.delete() == false) goto L7;
        return;
    L7:
        if (r4.delete() == true) goto L11;
        throw new IOException("Unable to delete " + r4);
    L11:
        throw new IOException("Unable to delete " + r3);
    }

    public static BufferedReader newReader(File r3, Charset r4) throws FileNotFoundException {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        return new BufferedReader(new InputStreamReader(new FileInputStream(r3), r4));
    }

    public static BufferedWriter newWriter(File r3, Charset r4) throws FileNotFoundException {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(r3), r4));
    }

    @Deprecated
    @ParametricNullness
    public static <T> T readBytes(File r1, ByteProcessor<T> r2) throws IOException {
        return (T) asByteSource(r1).read(r2);
    }

    @CheckForNull
    @Deprecated
    public static String readFirstLine(File r1, Charset r2) throws IOException {
        return asCharSource(r1, r2).readFirstLine();
    }

    @Deprecated
    @ParametricNullness
    public static <T> T readLines(File r1, Charset r2, LineProcessor<T> r3) throws IOException {
        return (T) asCharSource(r1, r2).readLines(r3);
    }

    public static List<String> readLines(File r2, Charset r3) throws IOException {
        return (List) asCharSource(r2, r3).readLines(new C08501());
    }

    public static String simplifyPath(String r10) {
        Preconditions.checkNotNull(r10);
        if (r10.length() != 0) goto L5;
        return ".";
    L5:
        Iterable<String> r2 = Splitter.m190on('/').omitEmptyStrings().split(r10);
        ArrayList r3 = new ArrayList();
        Iterator<String> r4 = r2.iterator();
    L6:
        byte r6 = 0;
        if (r4.hasNext() == false) goto L28;
        String r5 = r4.next();
        switch(r5.hashCode()) {
            case 46: goto L15;
            case 1472: goto L12;
            default: goto L17;
        };
    L17:
        r6 = -1;
    L18:
        switch(r6) {
            case 0: goto L6;
            case 1: goto L21;
            default: goto L19;
        };
    L19:
        r3.add(r5);
        goto L6
    L21:
        if (r3.size() <= 0) goto L25;
        if (((String) r3.get(r3.size() - 1)).equals("..") == true) goto L25;
        r3.remove(r3.size() - 1);
    L25:
        r3.add("..");
        goto L6
    L12:
        if (r5.equals("..") == false) goto L17;
        r6 = 1;
        goto L18
    L15:
        if (r5.equals(".") == false) goto L17;
    L28:
        String r1 = Joiner.m183on('/').join(r3);
        if (r10.charAt(0) != '/') goto L32;
        r1 = "/" + r1;
    L32:
        if (r1.startsWith("/../") == false) goto L35;
        r1 = r1.substring(3);
        goto L32
    L35:
        if (r1.equals("/..") == false) goto L38;
        return "/";
    L38:
        if ("".equals(r1) == false) goto L55;
        return ".";
    L55:
        return r1;
    }

    public static byte[] toByteArray(File r1) throws IOException {
        return asByteSource(r1).read();
    }

    @Deprecated
    public static String toString(File r1, Charset r2) throws IOException {
        return asCharSource(r1, r2).read();
    }

    public static void touch(File r3) throws IOException {
        Preconditions.checkNotNull(r3);
        if (r3.createNewFile() == false) goto L5;
        return;
    L5:
        if (r3.setLastModified(System.currentTimeMillis()) == false) goto L8;
        return;
    L8:
        throw new IOException("Unable to update modification time of " + r3);
    }

    @Deprecated
    public static void write(CharSequence r1, File r2, Charset r3) throws IOException {
        asCharSink(r2, r3, new FileWriteMode[0]).write(r1);
    }

    public static void write(byte[] r1, File r2) throws IOException {
        asByteSink(r2, new FileWriteMode[0]).write(r1);
    }
}
