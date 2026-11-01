package com.google.common.reflect;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.p003io.ByteSource;
import com.google.common.p003io.CharSource;
import com.google.common.p003io.Resources;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ClassPath {
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = null;
    private static final Logger logger = null;
    private final ImmutableSet<ResourceInfo> resources;

    public static final class ClassInfo extends ResourceInfo {
        private final String className;

        ClassInfo(File r2, String r3, ClassLoader r4) {
            super(r2, r3, r4);
            this.className = ClassPath.getClassName(r3);
        }

        public String getName() {
            return this.className;
        }

        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }

        public String getSimpleName() {
            int r0 = this.className.lastIndexOf(36);
            if (r0 == (-1)) goto L6;
            String r1 = this.className.substring(r0 + 1);
            return CharMatcher.inRange('0', '9').trimLeadingFrom(r1);
        L6:
            String r2 = getPackageName();
            if (r2.isEmpty() == false) goto L11;
            return this.className;
        L11:
            return this.className.substring(r2.length() + 1);
        }

        public boolean isTopLevel() {
            if (this.className.indexOf(36) != (-1)) goto L5;
            return true;
        L5:
            return false;
        }

        public Class<?> load() {
            return this.loader.loadClass(this.className);
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.reflect.ClassPath.ResourceInfo
        public String toString() {
            return this.className;
        }
    }

    static final class LocationInfo {
        private final ClassLoader classloader;
        final File home;

        LocationInfo(File r2, ClassLoader r3) {
            this.home = (File) Preconditions.checkNotNull(r2);
            this.classloader = (ClassLoader) Preconditions.checkNotNull(r3);
        }

        private void scan(File r5, Set<File> r6, ImmutableSet.Builder<ResourceInfo> r7) throws IOException {
            if (r5.exists() == true) goto L6;
            return;
        L6:
            if (r5.isDirectory() == false) goto L8;
            scanDirectory(r5, r7);
            return;
        L8:
            scanJar(r5, r6, r7);
            return;
        L10:
            e = move-exception;
            ClassPath.access$000().warning("Cannot access " + r5 + ": " + e);
        }

        private void scanDirectory(File r3, ImmutableSet.Builder<ResourceInfo> r4) throws IOException {
            HashSet r0 = new HashSet();
            r0.add(r3.getCanonicalFile());
            scanDirectory(r3, "", r0, r4);
        }

        private void scanDirectory(File r9, String r10, Set<File> r11, ImmutableSet.Builder<ResourceInfo> r12) throws IOException {
            File[] r0 = r9.listFiles();
            if (r0 != null) goto L6;
            ClassPath.access$000().warning("Cannot read directory " + r9);
            return;
        L6:
            int r1 = r0.length;
            int r2 = 0;
        L7:
            if (r2 >= r1) goto L18;
            File r3 = r0[r2];
            String r4 = r3.getName();
            if (r3.isDirectory() == false) goto L14;
            File r5 = r3.getCanonicalFile();
            if (r11.add(r5) == false) goto L17;
            scanDirectory(r5, r10 + r4 + "/", r11, r12);
            r11.remove(r5);
        L17:
            r2 = r2 + 1;
            goto L7
        L14:
            String r6 = r10 + r4;
            if (r6.equals("META-INF/MANIFEST.MF") == true) goto L17;
            r12.add(ResourceInfo.m372of(r3, r6, this.classloader));
            goto L17
        }

        private void scanJar(File r5, Set<File> r6, ImmutableSet.Builder<ResourceInfo> r7) throws IOException {
            JarFile r0 = new JarFile(r5);     // Catch: IOException -> L20
            UnmodifiableIterator<File> r1 = ClassPath.getClassPathFromManifest(r5, r0.getManifest()).iterator();     // Catch: Throwable -> L15
        L5:
            if (r1.hasNext() == false) goto L10;
            File r2 = r1.next();     // Catch: Throwable -> L15
            if (r6.add(r2.getCanonicalFile()) == false) goto L5;
            scan(r2, r6, r7);     // Catch: Throwable -> L15
            goto L5
        L10:
            scanJarFile(r0, r7);     // Catch: Throwable -> L15
            r0.close();     // Catch: IOException -> L13
            return;
        L14:
            return;
        L15:
            th = move-exception;
            r0.close();     // Catch: IOException -> L18
        L19:
            throw th;
        }

        private void scanJarFile(JarFile r6, ImmutableSet.Builder<ResourceInfo> r7) {
            Enumeration<JarEntry> r0 = r6.entries();
        L4:
            if (r0.hasMoreElements() == false) goto L11;
            JarEntry r1 = r0.nextElement();
            if (r1.isDirectory() == true) goto L4;
            if (r1.getName().equals("META-INF/MANIFEST.MF") == true) goto L4;
            r7.add(ResourceInfo.m372of(new File(r6.getName()), r1.getName(), this.classloader));
            goto L4
        }

        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof LocationInfo) == false) goto L10;
            LocationInfo r0 = (LocationInfo) r5;
            if (this.home.equals(r0.home) == true) goto L7;
            return false;
        L7:
            if (this.classloader.equals(r0.classloader) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        public final File file() {
            return this.home;
        }

        public int hashCode() {
            return this.home.hashCode();
        }

        public ImmutableSet<ResourceInfo> scanResources() throws IOException {
            return scanResources(new HashSet());
        }

        public ImmutableSet<ResourceInfo> scanResources(Set<File> r3) throws IOException {
            ImmutableSet.Builder<ResourceInfo> r0 = ImmutableSet.builder();
            r3.add(this.home);
            scan(this.home, r3, r0);
            return r0.build();
        }

        public String toString() {
            return this.home.toString();
        }
    }

    public static class ResourceInfo {
        private final File file;
        final ClassLoader loader;
        private final String resourceName;

        ResourceInfo(File r2, String r3, ClassLoader r4) {
            this.file = (File) Preconditions.checkNotNull(r2);
            this.resourceName = (String) Preconditions.checkNotNull(r3);
            this.loader = (ClassLoader) Preconditions.checkNotNull(r4);
        }

        /* JADX INFO: renamed from: of */
        static ResourceInfo m372of(File r1, String r2, ClassLoader r3) {
            if (r2.endsWith(ClassPath.CLASS_FILE_NAME_EXTENSION) == false) goto L7;
            return new ClassInfo(r1, r2, r3);
        L7:
            return new ResourceInfo(r1, r2, r3);
        }

        public final ByteSource asByteSource() {
            return Resources.asByteSource(url());
        }

        public final CharSource asCharSource(Charset r2) {
            return Resources.asCharSource(url(), r2);
        }

        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof ResourceInfo) == false) goto L10;
            ResourceInfo r0 = (ResourceInfo) r5;
            if (this.resourceName.equals(r0.resourceName) == true) goto L7;
            return false;
        L7:
            if (this.loader != r0.loader) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        final File getFile() {
            return this.file;
        }

        public final String getResourceName() {
            return this.resourceName;
        }

        public int hashCode() {
            return this.resourceName.hashCode();
        }

        public String toString() {
            return this.resourceName;
        }

        public final URL url() {
            URL r0 = this.loader.getResource(this.resourceName);
            if (r0 == null) goto L6;
            return r0;
        L6:
            throw new NoSuchElementException(this.resourceName);
        }
    }

    static {
        logger = Logger.getLogger(ClassPath.class.getName());
        CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.m192on(" ").omitEmptyStrings();
    }

    private ClassPath(ImmutableSet<ResourceInfo> r1) {
        this.resources = r1;
    }

    static /* synthetic */ Logger access$000() {
        return logger;
    }

    public static ClassPath from(ClassLoader r6) throws IOException {
        ImmutableSet<LocationInfo> r0 = locationsFrom(r6);
        HashSet r1 = new HashSet();
        UnmodifiableIterator<LocationInfo> r2 = r0.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        r1.add(r2.next().file());
        goto L4
    L6:
        ImmutableSet.Builder r3 = ImmutableSet.builder();
        UnmodifiableIterator<LocationInfo> r4 = r0.iterator();
    L8:
        if (r4.hasNext() == false) goto L11;
        r3.addAll(r4.next().scanResources(r1));
        goto L8
    L11:
        return new ClassPath(r3.build());
    }

    private static ImmutableList<URL> getClassLoaderUrls(ClassLoader r1) {
        if ((r1 instanceof URLClassLoader) == false) goto L7;
        return ImmutableList.copyOf(((URLClassLoader) r1).getURLs());
    L7:
        if (r1.equals(ClassLoader.getSystemClassLoader()) == false) goto L11;
        return parseJavaClassPath();
    L11:
        return ImmutableList.m212of();
    }

    static String getClassName(String r4) {
        return r4.substring(0, r4.length() - CLASS_FILE_NAME_EXTENSION.length()).replace('/', '.');
    }

    static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader r6) {
        LinkedHashMap r0 = Maps.newLinkedHashMap();
        ClassLoader r1 = r6.getParent();
        if (r1 == null) goto L5;
        r0.putAll(getClassPathEntries(r1));
    L5:
        UnmodifiableIterator<URL> r2 = getClassLoaderUrls(r6).iterator();
    L7:
        if (r2.hasNext() == false) goto L15;
        URL r3 = r2.next();
        if (r3.getProtocol().equals("file") == false) goto L7;
        File r4 = toFile(r3);
        if (r0.containsKey(r4) == true) goto L7;
        r0.put(r4, r6);
        goto L7
    L15:
        return ImmutableMap.copyOf(r0);
    }

    static URL getClassPathEntry(File r2, String r3) throws MalformedURLException {
        return new URL(r2.toURI().toURL(), r3);
    }

    static ImmutableSet<File> getClassPathFromManifest(File r8, @CheckForNull Manifest r9) {
        if (r9 == null) goto L4;
        ImmutableSet.Builder r0 = ImmutableSet.builder();
        String r1 = r9.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
        if (r1 == null) goto L19;
        Iterator<String> r2 = CLASS_PATH_ATTRIBUTE_SEPARATOR.split(r1).iterator();
    L9:
        if (r2.hasNext() == false) goto L19;
        String r3 = r2.next();
        URL r4 = getClassPathEntry(r8, r3);     // Catch: MalformedURLException -> L16
        if (r4.getProtocol().equals("file") == false) goto L9;
        r0.add(toFile(r4));
    L17:
        logger.warning("Invalid Class-Path entry: " + r3);
    L19:
        return r0.build();
    L4:
        return ImmutableSet.m259of();
    }

    static ImmutableSet<LocationInfo> locationsFrom(ClassLoader r6) {
        ImmutableSet.Builder r0 = ImmutableSet.builder();
        UnmodifiableIterator<Map.Entry<File, ClassLoader>> r1 = getClassPathEntries(r6).entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L7;
        Map.Entry<File, ClassLoader> r2 = r1.next();
        r0.add(new LocationInfo(r2.getKey(), r2.getValue()));
        goto L4
    L7:
        return r0.build();
    }

    static ImmutableList<URL> parseJavaClassPath() {
        ImmutableList.Builder r0 = ImmutableList.builder();
        Iterator<String> r1 = Splitter.m192on(StandardSystemProperty.PATH_SEPARATOR.value()).split(StandardSystemProperty.JAVA_CLASS_PATH.value()).iterator();
    L4:
        if (r1.hasNext() == false) goto L16;
        String r2 = r1.next();
        r0.add(new File(r2).toURI().toURL());     // Catch: MalformedURLException -> L8 SecurityException -> L10
    L8:
        e = move-exception;
        logger.log(Level.WARNING, "malformed classpath entry: " + r2, e);
    L11:
        r0.add(new URL("file", null, new File(r2).getAbsolutePath()));     // Catch: MalformedURLException -> L8
        goto L4
    L16:
        return r0.build();
    }

    static File toFile(URL r3) {
        Preconditions.checkArgument(r3.getProtocol().equals("file"));
        return new File(r3.toURI());
    L7:
        return new File(r3.getPath());
    }

    public ImmutableSet<ClassInfo> getAllClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).toSet();
    }

    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).filter(new ClassPath$$ExternalSyntheticLambda0()).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses(String r5) {
        Preconditions.checkNotNull(r5);
        ImmutableSet.Builder r0 = ImmutableSet.builder();
        UnmodifiableIterator<ClassInfo> r1 = getTopLevelClasses().iterator();
    L4:
        if (r1.hasNext() == false) goto L10;
        ClassInfo r2 = r1.next();
        if (r2.getPackageName().equals(r5) == false) goto L4;
        r0.add(r2);
        goto L4
    L10:
        return r0.build();
    }

    public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String r6) {
        Preconditions.checkNotNull(r6);
        String r0 = r6 + '.';
        ImmutableSet.Builder r1 = ImmutableSet.builder();
        UnmodifiableIterator<ClassInfo> r2 = getTopLevelClasses().iterator();
    L4:
        if (r2.hasNext() == false) goto L10;
        ClassInfo r3 = r2.next();
        if (r3.getName().startsWith(r0) == false) goto L4;
        r1.add(r3);
        goto L4
    L10:
        return r1.build();
    }
}
