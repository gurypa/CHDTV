package com.google.common.base;

import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final enum StandardSystemProperty extends Enum<StandardSystemProperty> {
    private static final /* synthetic */ StandardSystemProperty[] $VALUES = null;
    public static final StandardSystemProperty FILE_SEPARATOR = null;
    public static final StandardSystemProperty JAVA_CLASS_PATH = null;
    public static final StandardSystemProperty JAVA_CLASS_VERSION = null;
    public static final StandardSystemProperty JAVA_COMPILER = null;

    @Deprecated
    public static final StandardSystemProperty JAVA_EXT_DIRS = null;
    public static final StandardSystemProperty JAVA_HOME = null;
    public static final StandardSystemProperty JAVA_IO_TMPDIR = null;
    public static final StandardSystemProperty JAVA_LIBRARY_PATH = null;
    public static final StandardSystemProperty JAVA_SPECIFICATION_NAME = null;
    public static final StandardSystemProperty JAVA_SPECIFICATION_VENDOR = null;
    public static final StandardSystemProperty JAVA_SPECIFICATION_VERSION = null;
    public static final StandardSystemProperty JAVA_VENDOR = null;
    public static final StandardSystemProperty JAVA_VENDOR_URL = null;
    public static final StandardSystemProperty JAVA_VERSION = null;
    public static final StandardSystemProperty JAVA_VM_NAME = null;
    public static final StandardSystemProperty JAVA_VM_SPECIFICATION_NAME = null;
    public static final StandardSystemProperty JAVA_VM_SPECIFICATION_VENDOR = null;
    public static final StandardSystemProperty JAVA_VM_SPECIFICATION_VERSION = null;
    public static final StandardSystemProperty JAVA_VM_VENDOR = null;
    public static final StandardSystemProperty JAVA_VM_VERSION = null;
    public static final StandardSystemProperty LINE_SEPARATOR = null;
    public static final StandardSystemProperty OS_ARCH = null;
    public static final StandardSystemProperty OS_NAME = null;
    public static final StandardSystemProperty OS_VERSION = null;
    public static final StandardSystemProperty PATH_SEPARATOR = null;
    public static final StandardSystemProperty USER_DIR = null;
    public static final StandardSystemProperty USER_HOME = null;
    public static final StandardSystemProperty USER_NAME = null;
    private final String key;

    private static /* synthetic */ StandardSystemProperty[] $values() {
        return new StandardSystemProperty[]{JAVA_VERSION, JAVA_VENDOR, JAVA_VENDOR_URL, JAVA_HOME, JAVA_VM_SPECIFICATION_VERSION, JAVA_VM_SPECIFICATION_VENDOR, JAVA_VM_SPECIFICATION_NAME, JAVA_VM_VERSION, JAVA_VM_VENDOR, JAVA_VM_NAME, JAVA_SPECIFICATION_VERSION, JAVA_SPECIFICATION_VENDOR, JAVA_SPECIFICATION_NAME, JAVA_CLASS_VERSION, JAVA_CLASS_PATH, JAVA_LIBRARY_PATH, JAVA_IO_TMPDIR, JAVA_COMPILER, JAVA_EXT_DIRS, OS_NAME, OS_ARCH, OS_VERSION, FILE_SEPARATOR, PATH_SEPARATOR, LINE_SEPARATOR, USER_NAME, USER_HOME, USER_DIR};
    }

    static {
        JAVA_VERSION = new StandardSystemProperty("JAVA_VERSION", 0, "java.version");
        JAVA_VENDOR = new StandardSystemProperty("JAVA_VENDOR", 1, "java.vendor");
        JAVA_VENDOR_URL = new StandardSystemProperty("JAVA_VENDOR_URL", 2, "java.vendor.url");
        JAVA_HOME = new StandardSystemProperty("JAVA_HOME", 3, "java.home");
        JAVA_VM_SPECIFICATION_VERSION = new StandardSystemProperty("JAVA_VM_SPECIFICATION_VERSION", 4, "java.vm.specification.version");
        JAVA_VM_SPECIFICATION_VENDOR = new StandardSystemProperty("JAVA_VM_SPECIFICATION_VENDOR", 5, "java.vm.specification.vendor");
        JAVA_VM_SPECIFICATION_NAME = new StandardSystemProperty("JAVA_VM_SPECIFICATION_NAME", 6, "java.vm.specification.name");
        JAVA_VM_VERSION = new StandardSystemProperty("JAVA_VM_VERSION", 7, "java.vm.version");
        JAVA_VM_VENDOR = new StandardSystemProperty("JAVA_VM_VENDOR", 8, "java.vm.vendor");
        JAVA_VM_NAME = new StandardSystemProperty("JAVA_VM_NAME", 9, "java.vm.name");
        JAVA_SPECIFICATION_VERSION = new StandardSystemProperty("JAVA_SPECIFICATION_VERSION", 10, "java.specification.version");
        JAVA_SPECIFICATION_VENDOR = new StandardSystemProperty("JAVA_SPECIFICATION_VENDOR", 11, "java.specification.vendor");
        JAVA_SPECIFICATION_NAME = new StandardSystemProperty("JAVA_SPECIFICATION_NAME", 12, "java.specification.name");
        JAVA_CLASS_VERSION = new StandardSystemProperty("JAVA_CLASS_VERSION", 13, "java.class.version");
        JAVA_CLASS_PATH = new StandardSystemProperty("JAVA_CLASS_PATH", 14, "java.class.path");
        JAVA_LIBRARY_PATH = new StandardSystemProperty("JAVA_LIBRARY_PATH", 15, "java.library.path");
        JAVA_IO_TMPDIR = new StandardSystemProperty("JAVA_IO_TMPDIR", 16, "java.io.tmpdir");
        JAVA_COMPILER = new StandardSystemProperty("JAVA_COMPILER", 17, "java.compiler");
        JAVA_EXT_DIRS = new StandardSystemProperty("JAVA_EXT_DIRS", 18, "java.ext.dirs");
        OS_NAME = new StandardSystemProperty("OS_NAME", 19, "os.name");
        OS_ARCH = new StandardSystemProperty("OS_ARCH", 20, "os.arch");
        OS_VERSION = new StandardSystemProperty("OS_VERSION", 21, "os.version");
        FILE_SEPARATOR = new StandardSystemProperty("FILE_SEPARATOR", 22, "file.separator");
        PATH_SEPARATOR = new StandardSystemProperty("PATH_SEPARATOR", 23, "path.separator");
        LINE_SEPARATOR = new StandardSystemProperty("LINE_SEPARATOR", 24, "line.separator");
        USER_NAME = new StandardSystemProperty("USER_NAME", 25, "user.name");
        USER_HOME = new StandardSystemProperty("USER_HOME", 26, "user.home");
        USER_DIR = new StandardSystemProperty("USER_DIR", 27, "user.dir");
        $VALUES = $values();
    }

    private StandardSystemProperty(String r1, int r2, String r3) {
        super(r1, r2);
        this.key = r3;
    }

    public static StandardSystemProperty valueOf(String r1) {
        return (StandardSystemProperty) Enum.valueOf(StandardSystemProperty.class, r1);
    }

    public static StandardSystemProperty[] values() {
        return (StandardSystemProperty[]) $VALUES.clone();
    }

    public String key() {
        return this.key;
    }

    @Override // java.lang.Enum
    public String toString() {
        return key() + "=" + value();
    }

    @CheckForNull
    public String value() {
        return System.getProperty(this.key);
    }
}
