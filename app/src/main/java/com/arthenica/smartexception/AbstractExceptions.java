package com.arthenica.smartexception;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractExceptions {
    public static final boolean DEFAULT_IGNORE_ALL_CAUSES = false;
    public static final int DEFAULT_MAX_DEPTH = 10;
    public static final boolean DEFAULT_PRINT_MODULE_NAME = true;
    public static final boolean DEFAULT_PRINT_PACKAGE_INFORMATION = false;
    public static final boolean DEFAULT_PRINT_SUPPRESSED_EXCEPTIONS = true;
    static final Set<String> groupPackageSet = null;
    static boolean ignoreAllCauses;
    static final Set<String> ignoreCausePackageSet = null;
    static final Set<String> ignorePackageSet = null;
    static boolean printModuleName;
    static boolean printPackageInformation;
    static boolean printSuppressedExceptions;
    static final Set<String> rootPackageSet = null;
    static StackTraceElementSerializer stackTraceElementSerializer;

    static {
        rootPackageSet = Collections.synchronizedSet(new HashSet());
        groupPackageSet = Collections.synchronizedSet(new HashSet());
        ignorePackageSet = Collections.synchronizedSet(new HashSet());
        ignoreCausePackageSet = Collections.synchronizedSet(new HashSet());
        ignoreAllCauses = false;
        printPackageInformation = false;
        printModuleName = true;
        printSuppressedExceptions = true;
    }

    public AbstractExceptions() {
    }

    public static int appendStackTraceGroupElement(StringBuilder r5, String r6, int r7, StackTraceElement r8, boolean r9, boolean r10, String r11) {
        if (r7 > 0) goto L5;
    L14:
        return 0;
    L5:
        if (stackTraceElementSerializer == null) goto L13;
        r5.append(r11);
        if (r7 != 1) goto L9;
        r5.append(stackTraceElementSerializer.toString(r8, r9, r10));
        goto L14
    L9:
        r5.append(String.format("%s%s ... %d more", new Object[]{stackTraceElementSerializer.getModuleName(r8), r6, Integer.valueOf(r7 - 1)}));
        if (r10 == false) goto L14;
        r5.append(stackTraceElementSerializer.getPackageInformation(r8));
        goto L14
    L13:
        throw new IllegalArgumentException("Stack trace element serializer not initialized.");
    }

    public static void clearGroupPackages() {
        groupPackageSet.clear();
    }

    public static void clearIgnorePackages() {
        ignorePackageSet.clear();
        ignoreCausePackageSet.clear();
    }

    public static void clearRootPackages() {
        rootPackageSet.clear();
    }

    public static boolean containsCause(Throwable r1, Class<?> r2) {
        return containsCause(r1, r2, null);
    }

    public static boolean containsCause(Throwable r1, Class<?> r2, String r3) {
        if (searchCause(r1, r2, r3, 10) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public static boolean containsPackage(String r1, Set<String> r2) {
        if (getContainingPackage(r1, r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public static String getAllMessages(Throwable r2) {
        StringBuilder r0 = new StringBuilder();
        getAllMessages(r2, r0);
        return r0.toString();
    }

    public static void getAllMessages(Throwable r2, StringBuilder r3) {
        if (r2 == null) goto L11;
        String r0 = r2.getMessage();
        if (isEmpty(r0) == false) goto L6;
    L9:
        getAllMessages(r2.getCause(), r3);
        return;
    L6:
        if (r3.length() == 0) goto L8;
        r3.append(System.lineSeparator());
        r3.append(" - Caused by: ");
    L8:
        r3.append(r0);
        goto L9
    }

    public static Throwable getCause(Throwable r1) {
        return getCause(r1, 10);
    }

    public static Throwable getCause(Throwable r2, int r3) {
        if (r2 != null) goto L5;
        return null;
    L5:
        if (r3 > 0) goto L7;
        return r2;
    L7:
        Throwable r0 = r2.getCause();
        if (r0 != null) goto L11;
        return r2;
    L11:
        return getCause(r0, r3 - 1);
    }

    public static String getContainingPackage(String r3, Set<String> r4) {
        Iterator<String> r0 = r4.iterator();
    L4:
        if (r0.hasNext() == false) goto L9;
        String r1 = r0.next();
        if (r3.startsWith(r1) == false) goto L4;
        return r1;
    L9:
        return null;
    }

    public static boolean getIgnoreAllCauses() {
        return ignoreAllCauses;
    }

    public static boolean getPrintModuleName() {
        return printModuleName;
    }

    public static boolean getPrintSuppressedExceptions() {
        return printSuppressedExceptions;
    }

    public static StackTraceElement[] getStackTrace(ThrowableWrapper r4, int r5) {
        ArrayList r0 = new ArrayList();
        if (r4 == null) goto L10;
        StackTraceElementWrapper[] r1 = r4.getStackTrace();
        int r2 = 0;
    L6:
        if (r2 >= r1.length) goto L10;
        if (r2 >= r5) goto L10;
        r0.add(r1[r2].getStackTraceElement());
        r2 = r2 + 1;
    L10:
        return (StackTraceElement[]) r0.toArray(new StackTraceElement[0]);
    }

    public static StackTraceElement[] getStackTrace(ThrowableWrapper r9, Set<String> r10, Set<String> r11) {
        ArrayList r0 = new ArrayList();
        ArrayList r1 = new ArrayList();
        if (r9 == null) goto L16;
        StackTraceElementWrapper[] r3 = r9.getStackTrace();
        int r4 = r3.length;
        int r5 = 0;
    L5:
        if (r5 >= r4) goto L16;
        StackTraceElementWrapper r6 = r3[r5];
        String r7 = r6.getStackTraceElement().getClassName();
        if (isEmpty(r7) == true) goto L14;
        if (containsPackage(r7, r10) == false) goto L12;
        r0.addAll(r1);
        r0.add(r6.getStackTraceElement());
        goto L14
    L12:
        if (containsPackage(r7, r11) == true) goto L14;
        r1.add(r6.getStackTraceElement());
    L14:
        r5 = r5 + 1;
    L16:
        if (r0.isEmpty() == false) goto L19;
        r0.addAll(r1);
    L19:
        return (StackTraceElement[]) r0.toArray(new StackTraceElement[0]);
    }

    public static StackTraceElementSerializer getStackTraceElementSerializer() {
        return stackTraceElementSerializer;
    }

    public static String getStackTraceString(ThrowableWrapper r7) {
        return getStackTraceString(r7, rootPackageSet, groupPackageSet, ignorePackageSet, 0, ignoreAllCauses, printPackageInformation);
    }

    public static String getStackTraceString(ThrowableWrapper r7, int r8) {
        return getStackTraceString(r7, new HashSet(), new HashSet(), new HashSet(), r8, ignoreAllCauses, printPackageInformation);
    }

    public static String getStackTraceString(ThrowableWrapper r7, int r8, boolean r9) {
        return getStackTraceString(r7, new HashSet(), new HashSet(), new HashSet(), r8, r9, printPackageInformation);
    }

    public static String getStackTraceString(ThrowableWrapper r7, int r8, boolean r9, boolean r10) {
        return getStackTraceString(r7, new HashSet(), new HashSet(), new HashSet(), r8, r9, r10);
    }

    public static String getStackTraceString(ThrowableWrapper r9, int r10, boolean r11, boolean r12, boolean r13) {
        return getStackTraceString(r9, new HashSet(), new HashSet(), new HashSet(), r10, r11, r12, printModuleName, r13);
    }

    public static String getStackTraceString(ThrowableWrapper r7, String r8) {
        return getStackTraceString(r7, Collections.singleton(r8), new HashSet(), new HashSet(), 0, ignoreAllCauses, printPackageInformation);
    }

    public static String getStackTraceString(ThrowableWrapper r7, String r8, String r9) {
        return getStackTraceString(r7, Collections.singleton(r8), Collections.singleton(r9), new HashSet(), 0, ignoreAllCauses, printPackageInformation);
    }

    public static String getStackTraceString(ThrowableWrapper r22, String r23, boolean r24, boolean r25, Set<String> r26, Set<String> r27, Set<String> r28, int r29, boolean r30, boolean r31, boolean r32, boolean r33) {
        StringBuilder r1 = new StringBuilder();
        if (r22 != null) goto L6;
        return "";
    L6:
        String r13 = r22.getClassName();
        if (r29 <= 0) goto L9;
        Set<String> r9 = r26;
        Set<String> r10 = r28;
        StackTraceElement[] r14 = getStackTrace(r22, r29);
    L10:
        String r2 = r22.getMessage();
        if (isEmpty(r2) == false) goto L13;
        String r15 = r22.getMessage();
    L14:
        if (r24 == false) goto L16;
        r1.append(System.lineSeparator());
        r1.append(r23);
        r1.append("Caused by: ");
    L18:
        r1.append(r13);
        if (isEmpty(r15) == true) goto L21;
        r1.append(": ");
        r1.append(r15);
    L21:
        String r3 = null;
        int r11 = r14.length;
        StackTraceElement r4 = null;
        int r5 = 0;
        int r6 = 0;
    L22:
        if (r6 >= r11) goto L35;
        StackTraceElement r7 = r14[r6];
        String r8 = getContainingPackage(r7.getClassName(), r27);
        if (r8 != null) goto L26;
        r5 = appendStackTraceGroupElement(r1, r3, r5, r4, r32, r31, r23);
        r1.append(System.lineSeparator());
        r1.append(r23);
        r1.append("\tat ");
        if (stackTraceElementSerializer == null) goto L34;
        r1.append(r23);
        r1.append(stackTraceElementSerializer.toString(r7, r32, r31));
        r3 = null;
    L32:
        r6 = r6 + 1;
        goto L22
    L34:
        throw new IllegalArgumentException("Stack trace element serializer not initialized.");
    L26:
        if (r8.equals(r3) == true) goto L28;
        appendStackTraceGroupElement(r1, r3, r5, r4, r32, r31, r23);
        r1.append(System.lineSeparator());
        r1.append(r23);
        r1.append("\tat ");
        r5 = 1;
        r4 = r7;
        r3 = r8;
        goto L32
    L28:
        r5 = r5 + 1;
        goto L32
    L35:
        appendStackTraceGroupElement(r1, r3, r5, r4, r32, r31, r23);
        String r0 = r23;
        ThrowableWrapper[] r12 = r22.getSuppressed();
        if (r12 != null) goto L38;
    L44:
        StringBuilder r16 = r1;
    L45:
        ThrowableWrapper r17 = r22.getCause();
        if (r17 == null) goto L52;
        if (containsPackage(r13, ignoreCausePackageSet) == true) goto L52;
        if (r30 == true) goto L52;
        r16.append(getStackTraceString(r17, r23, true, false, r26, r27, r28, r29, r30, r31, r32, r33));
    L52:
        return r16.toString();
    L38:
        if (r12.length <= 0) goto L44;
        if (r33 == false) goto L44;
        int r18 = r12.length;
        int r19 = 0;
    L41:
        if (r19 >= r18) goto L43;
        StringBuilder r20 = r1;
        r20.append(getStackTraceString(r12[r19], r0 + "\t", false, true, r9, r27, r10, r29, r30, r31, r32, r33));
        r19 = r19 + 1;
        r9 = r26;
        r10 = r28;
        r1 = r20;
        r12 = r12;
        r18 = r18;
        r0 = r23;
        goto L41
    L43:
        r16 = r1;
        goto L45
    L16:
        if (r25 == false) goto L18;
        r1.append(System.lineSeparator());
        r1.append(r23);
        r1.append("Suppressed: ");
        goto L18
    L13:
        r15 = r2;
        goto L14
    L9:
        r9 = r26;
        r10 = r28;
        r14 = getStackTrace(r22, r9, r10);
        goto L10
    }

    public static String getStackTraceString(ThrowableWrapper r7, Set<String> r8, Set<String> r9, Set<String> r10) {
        return getStackTraceString(r7, r8, r9, r10, 0, ignoreAllCauses, printPackageInformation);
    }

    public static String getStackTraceString(ThrowableWrapper r8, Set<String> r9, Set<String> r10, Set<String> r11, int r12, boolean r13, boolean r14) {
        return getStackTraceString(r8, r9, r10, r11, r12, r13, r14, printModuleName);
    }

    public static String getStackTraceString(ThrowableWrapper r11, Set<String> r12, Set<String> r13, Set<String> r14, int r15, boolean r16, boolean r17, boolean r18) {
        return getStackTraceString(r11, false, false, r12, r13, r14, r15, r16, r17, r18, printSuppressedExceptions);
    }

    public static String getStackTraceString(ThrowableWrapper r11, Set<String> r12, Set<String> r13, Set<String> r14, int r15, boolean r16, boolean r17, boolean r18, boolean r19) {
        return getStackTraceString(r11, false, false, r12, r13, r14, r15, r16, r17, r18, r19);
    }

    public static String getStackTraceString(ThrowableWrapper r7, Set<String> r8, Set<String> r9, Set<String> r10, boolean r11) {
        return getStackTraceString(r7, r8, r9, r10, 0, r11, printPackageInformation);
    }

    public static String getStackTraceString(ThrowableWrapper r7, Set<String> r8, Set<String> r9, Set<String> r10, boolean r11, boolean r12) {
        return getStackTraceString(r7, r8, r9, r10, 0, r11, r12);
    }

    public static String getStackTraceString(ThrowableWrapper r9, Set<String> r10, Set<String> r11, Set<String> r12, boolean r13, boolean r14, boolean r15) {
        return getStackTraceString(r9, r10, r11, r12, 0, r13, r14, printModuleName, r15);
    }

    public static String getStackTraceString(ThrowableWrapper r7, boolean r8) {
        return getStackTraceString(r7, rootPackageSet, groupPackageSet, ignorePackageSet, 0, r8, printPackageInformation);
    }

    public static String getStackTraceString(ThrowableWrapper r12, boolean r13, boolean r14, Set<String> r15, Set<String> r16, Set<String> r17, int r18, boolean r19, boolean r20, boolean r21, boolean r22) {
        return getStackTraceString(r12, "", r13, r14, r15, r16, r17, r18, r19, r20, r21, r22);
    }

    public static boolean isEmpty(String r2) {
        if (r2 != null) goto L6;
        return true;
    L6:
        if (r2.trim().length() == 0) goto L10;
        return false;
    L10:
        return true;
    }

    public static boolean isPrintPackageInformation() {
        return printPackageInformation;
    }

    public static String libraryName(Class<?> r5) {
        if (r5 == null) goto L23;
        URL r0 = r5.getClassLoader().getResource(r5.getName().replace('.', '/') + ".class");     // Catch: Exception -> L15
        if (r0 == null) goto L24;
        String r1 = r0.toString();     // Catch: Exception -> L15
        int r2 = r1.lastIndexOf(33);     // Catch: Exception -> L15
        if (r2 <= 0) goto L25;
        String r3 = r1.substring(0, r2);     // Catch: Exception -> L15
        int r4 = r3.lastIndexOf(47);     // Catch: Exception -> L15
        if (r4 <= 0) goto L10;
        r3 = r3.substring(r4 + 1);     // Catch: Exception -> L15
    L10:
        int r6 = r3.lastIndexOf(92);     // Catch: Exception -> L15
        if (r6 <= 0) goto L22;
        return r3.substring(r6 + 1);
    L22:
        return r3;
    L25:
        return null;
    L24:
        return null;
    L18:
        return null;
    L23:
        return null;
    }

    public static String packageInformation(String r4, String r5) {
        boolean r0 = true;
        if (r4 == null) goto L5;
        boolean r2 = true;
    L6:
        if (r5 != null) goto L9;
        r0 = false;
    L9:
        if (r2 == true) goto L14;
        if (r0 == true) goto L14;
        return "";
    L14:
        StringBuilder r1 = new StringBuilder();
        r1.append(" [");
        if (r2 == false) goto L17;
        r1.append(r4);
    L17:
        if (r0 == false) goto L23;
        if (r2 == true) goto L20;
        r1.append(r5);
        goto L23
    L20:
        if (r4.contains(r5) == true) goto L23;
        r1.append(":");
        r1.append(r5);
    L23:
        r1.append("]");
        return r1.toString();
    L5:
        r2 = false;
        goto L6
    }

    public static String packageName(String r2) {
        if (r2 != null) goto L5;
        return "";
    L5:
        int r1 = r2.lastIndexOf(".");
        if (r1 >= 0) goto L8;
        return "";
    L8:
        return r2.substring(0, r1);
    }

    public static void registerGroupPackage(String r1) {
        groupPackageSet.add(r1);
    }

    public static void registerIgnorePackage(String r1, boolean r2) {
        ignorePackageSet.add(r1);
        if (r2 == false) goto L6;
        ignoreCausePackageSet.add(r1);
        return;
    }

    public static void registerRootPackage(String r1) {
        rootPackageSet.add(r1);
    }

    public static Throwable searchCause(Throwable r2, Class<?> r3) {
        return searchCause(r2, r3, null, 10);
    }

    public static Throwable searchCause(Throwable r2, Class<?> r3, int r4) {
        if (r2 != null) goto L6;
        return null;
    L6:
        if (r2.getClass().equals(r3) == false) goto L8;
        return r2;
    L8:
        if (r4 > 0) goto L10;
        return null;
    L10:
        Throwable r1 = r2.getCause();
        if (r1 != null) goto L14;
        return null;
    L14:
        return searchCause(r1, r3, r4 - 1);
    }

    public static Throwable searchCause(Throwable r1, Class<?> r2, String r3) {
        return searchCause(r1, r2, r3, 10);
    }

    public static Throwable searchCause(Throwable r3, Class<?> r4, String r5, int r6) {
        if (r3 != null) goto L6;
        return null;
    L6:
        if (isEmpty(r5) == false) goto L11;
        if (r3.getClass().equals(r4) == false) goto L15;
        return r3;
    L15:
        if (r6 > 0) goto L17;
        return null;
    L17:
        Throwable r1 = r3.getCause();
        if (r1 != null) goto L21;
        return null;
    L21:
        return searchCause(r1, r4, r5, r6 - 1);
    L11:
        if (r3.getClass().equals(r4) == false) goto L15;
        if (getAllMessages(r3).toLowerCase().contains(r5.toLowerCase()) == false) goto L15;
        return r3;
    }

    public static void setIgnoreAllCauses(boolean r0) {
        ignoreAllCauses = r0;
    }

    public static void setPrintModuleName(boolean r0) {
        printModuleName = r0;
    }

    public static void setPrintPackageInformation(boolean r0) {
        printPackageInformation = r0;
    }

    public static void setPrintSuppressedExceptions(boolean r0) {
        printSuppressedExceptions = r0;
    }

    public static void setStackTraceElementSerializer(StackTraceElementSerializer r0) {
        stackTraceElementSerializer = r0;
    }

    public static String version(PackageLoader r2, Class<?> r3, String r4) {
        Package r0 = r3.getPackage();     // Catch: Exception -> L11
        if (r0 != null) goto L5;
        Package r1 = r2.getPackage(r3.getClassLoader(), r4);     // Catch: Exception -> L11
        if (r1 != null) goto L9;
        return null;
    L9:
        return r1.getImplementationVersion();
    L5:
        return r0.getImplementationVersion();
    L12:
        return null;
    }
}
