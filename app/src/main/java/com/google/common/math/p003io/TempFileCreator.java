package com.google.common.p003io;

import com.google.common.base.StandardSystemProperty;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryFlag;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class TempFileCreator {
    static final TempFileCreator INSTANCE = null;

    /* JADX INFO: renamed from: com.google.common.io.TempFileCreator$1 */
    static /* synthetic */ class C08561 {
    }

    private static final class JavaIoCreator extends TempFileCreator {
        private static final int TEMP_DIR_ATTEMPTS = 10000;

        private JavaIoCreator() {
            super(null);
        }

        /* synthetic */ JavaIoCreator(C08561 r1) {
            this();
        }

        @Override // com.google.common.p003io.TempFileCreator
        File createTempDir() {
            File r0 = new File(StandardSystemProperty.JAVA_IO_TMPDIR.value());
            String r1 = System.currentTimeMillis() + "-";
            int r2 = 0;
        L4:
            if (r2 >= 10000) goto L10;
            File r3 = new File(r0, r1 + r2);
            if (r3.mkdir() == true) goto L7;
            r2 = r2 + 1;
            goto L4
        L7:
            return r3;
        L10:
            throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + r1 + "0 to " + r1 + "9999)");
        }

        @Override // com.google.common.p003io.TempFileCreator
        File createTempFile(String r2) throws IOException {
            return File.createTempFile(r2, null, null);
        }
    }

    private static final class JavaNioCreator extends TempFileCreator {
        private static final PermissionSupplier directoryPermissions = null;
        private static final PermissionSupplier filePermissions = null;

        private interface PermissionSupplier {
            FileAttribute<?> get() throws IOException;
        }

        static {
            Set<String> r0 = FileSystems.getDefault().supportedFileAttributeViews();
            if (r0.contains("posix") == false) goto L6;
            filePermissions = new TempFileCreator$JavaNioCreator$$ExternalSyntheticLambda0();
            directoryPermissions = new TempFileCreator$JavaNioCreator$$ExternalSyntheticLambda1();
            return;
        L6:
            if (r0.contains("acl") == false) goto L8;
            PermissionSupplier r1 = userPermissions();
            directoryPermissions = r1;
            filePermissions = r1;
            return;
        L8:
            PermissionSupplier r2 = new TempFileCreator$JavaNioCreator$$ExternalSyntheticLambda2();
            directoryPermissions = r2;
            filePermissions = r2;
        }

        private JavaNioCreator() {
            super(null);
        }

        /* synthetic */ JavaNioCreator(C08561 r1) {
            this();
        }

        static /* synthetic */ PermissionSupplier access$300() {
            return userPermissions();
        }

        private static String getUsername() {
            String r0 = (String) Objects.requireNonNull(StandardSystemProperty.USER_NAME.value());
            Class<?> r1 = Class.forName("java.lang.ProcessHandle");     // Catch: IllegalAccessException -> L5 NoSuchMethodException -> L7 InvocationTargetException -> L9 ClassNotFoundException -> L12
            Class<?> r2 = Class.forName("java.lang.ProcessHandle$Info");     // Catch: IllegalAccessException -> L5 NoSuchMethodException -> L7 InvocationTargetException -> L9 ClassNotFoundException -> L12
            Class<?> r3 = Class.forName("java.util.Optional");     // Catch: IllegalAccessException -> L5 NoSuchMethodException -> L7 InvocationTargetException -> L9 ClassNotFoundException -> L12
            Method r4 = r1.getMethod("current", new Class[0]);     // Catch: IllegalAccessException -> L5 NoSuchMethodException -> L7 InvocationTargetException -> L9 ClassNotFoundException -> L12
            Method r6 = r1.getMethod("info", new Class[0]);     // Catch: IllegalAccessException -> L5 NoSuchMethodException -> L7 InvocationTargetException -> L9 ClassNotFoundException -> L12
            Method r7 = r2.getMethod("user", new Class[0]);     // Catch: IllegalAccessException -> L5 NoSuchMethodException -> L7 InvocationTargetException -> L9 ClassNotFoundException -> L12
            return (String) Objects.requireNonNull(r3.getMethod("orElse", new Class[]{Object.class}).invoke(r7.invoke(r6.invoke(r4.invoke(null, new Object[0]), new Object[0]), new Object[0]), new Object[]{r0}));
        L9:
            e = move-exception;
            Throwables.throwIfUnchecked(e.getCause());
            return r0;
        L13:
            return r0;
        L6:
            return r0;
        L8:
            return r0;
        }

        static /* synthetic */ FileAttribute lambda$static$0() throws IOException {
            return PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-------"));
        }

        static /* synthetic */ FileAttribute lambda$static$1() throws IOException {
            return PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));
        }

        static /* synthetic */ FileAttribute lambda$static$2() throws IOException {
            throw new IOException("unrecognized FileSystem type " + FileSystems.getDefault());
        }

        static /* synthetic */ FileAttribute lambda$userPermissions$3(FileAttribute r0) throws IOException {
            return r0;
        }

        static /* synthetic */ FileAttribute lambda$userPermissions$4(IOException r2) throws IOException {
            throw new IOException("Could not find user", r2);
        }

        private static PermissionSupplier userPermissions() {
            final ImmutableList r1 = ImmutableList.m213of(AclEntry.newBuilder().setType(AclEntryType.ALLOW).setPrincipal(FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName(getUsername())).setPermissions(EnumSet.allOf(AclEntryPermission.class)).setFlags(new AclEntryFlag[]{AclEntryFlag.DIRECTORY_INHERIT, AclEntryFlag.FILE_INHERIT}).build());     // Catch: IOException -> L4
            final FileAttribute<ImmutableList<AclEntry>> r2 = new C08571(r1);     // Catch: IOException -> L4
            return new TempFileCreator$JavaNioCreator$$ExternalSyntheticLambda3(r2);
        L4:
            e = move-exception;
            return new TempFileCreator$JavaNioCreator$$ExternalSyntheticLambda4(e);
        }

        @Override // com.google.common.p003io.TempFileCreator
        File createTempDir() {
            return Files.createTempDirectory(Paths.get(StandardSystemProperty.JAVA_IO_TMPDIR.value(), new String[0]), null, new FileAttribute[]{directoryPermissions.get()}).toFile();
        L4:
            e = move-exception;
            throw new IllegalStateException("Failed to create directory", e);
        }

        @Override // com.google.common.p003io.TempFileCreator
        File createTempFile(String r5) throws IOException {
            return Files.createTempFile(Paths.get(StandardSystemProperty.JAVA_IO_TMPDIR.value(), new String[0]), r5, null, new FileAttribute[]{filePermissions.get()}).toFile();
        }
    }

    private static final class ThrowingCreator extends TempFileCreator {
        private static final String MESSAGE = "Guava cannot securely create temporary files or directories under SDK versions before Jelly Bean. You can create one yourself, either in the insecure default directory or in a more secure directory, such as context.getCacheDir(). For more information, see the Javadoc for Files.createTempDir().";

        private ThrowingCreator() {
            super(null);
        }

        /* synthetic */ ThrowingCreator(C08561 r1) {
            this();
        }

        @Override // com.google.common.p003io.TempFileCreator
        File createTempDir() {
            throw new IllegalStateException(MESSAGE);
        }

        @Override // com.google.common.p003io.TempFileCreator
        File createTempFile(String r3) throws IOException {
            throw new IOException(MESSAGE);
        }
    }

    static {
        INSTANCE = pickSecureCreator();
    }

    private TempFileCreator() {
    }

    /* synthetic */ TempFileCreator(C08561 r1) {
        this();
    }

    private static TempFileCreator pickSecureCreator() {
        C08561 r0 = null;
        Class.forName("java.nio.file.Path");     // Catch: ClassNotFoundException -> L5
        return new JavaNioCreator(r0);
    L23:
        if (((Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null)).intValue() >= ((Integer) Class.forName("android.os.Build$VERSION_CODES").getField("JELLY_BEAN").get(null)).intValue()) goto L11;
        return new ThrowingCreator(r0);
    L11:
        return new JavaIoCreator(r0);
    L17:
        return new ThrowingCreator(r0);
    L14:
        return new ThrowingCreator(r0);
    L20:
        return new ThrowingCreator(r0);
    }

    static void testMakingUserPermissionsFromScratch() throws IOException {
        JavaNioCreator.access$300().get();
    }

    abstract File createTempDir();

    abstract File createTempFile(String r1) throws IOException;
}
