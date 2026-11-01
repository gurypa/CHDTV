package com.arthenica.smartexception.java;

import com.arthenica.smartexception.AbstractExceptions;
import com.arthenica.smartexception.ClassLoader;
import com.arthenica.smartexception.PackageLoader;
import com.arthenica.smartexception.StackTraceElementSerializer;
import com.arthenica.smartexception.ThrowableWrapper;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class Exceptions {
    static ClassLoader classLoader;
    static PackageLoader packageLoader;

    static {
        packageLoader = new JavaPackageLoader();
        classLoader = new JavaClassLoader();
        AbstractExceptions.setStackTraceElementSerializer(new JavaStackTraceElementSerializer());
    }

    public Exceptions() {
    }

    public static void clearGroupPackages() {
        AbstractExceptions.clearGroupPackages();
    }

    public static void clearIgnorePackages() {
        AbstractExceptions.clearIgnorePackages();
    }

    public static void clearRootPackages() {
        AbstractExceptions.clearRootPackages();
    }

    public static boolean containsCause(Throwable r1, Class<?> r2) {
        return AbstractExceptions.containsCause(r1, r2);
    }

    public static boolean containsCause(Throwable r1, Class<?> r2, String r3) {
        return AbstractExceptions.containsCause(r1, r2, r3);
    }

    public static String getAllMessages(Throwable r1) {
        return AbstractExceptions.getAllMessages(r1);
    }

    public static Throwable getCause(Throwable r1) {
        return AbstractExceptions.getCause(r1);
    }

    public static Throwable getCause(Throwable r1, int r2) {
        return AbstractExceptions.getCause(r1, r2);
    }

    public static boolean getIgnoreAllCauses() {
        return AbstractExceptions.getIgnoreAllCauses();
    }

    public static boolean getPrintSuppressedExceptions() {
        return AbstractExceptions.getPrintSuppressedExceptions();
    }

    public static StackTraceElementSerializer getStackTraceElementSerializer() {
        return AbstractExceptions.getStackTraceElementSerializer();
    }

    public static String getStackTraceString(Throwable r1) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r1));
    }

    public static String getStackTraceString(Throwable r1, int r2) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r1), r2);
    }

    public static String getStackTraceString(Throwable r1, int r2, boolean r3) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r1), r2, r3);
    }

    public static String getStackTraceString(Throwable r1, int r2, boolean r3, boolean r4) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r1), r2, r3, r4);
    }

    public static String getStackTraceString(Throwable r1, int r2, boolean r3, boolean r4, boolean r5) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r1), r2, r3, r4, r5);
    }

    public static String getStackTraceString(Throwable r1, String r2) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r1), r2);
    }

    public static String getStackTraceString(Throwable r1, String r2, String r3) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r1), r2, r3);
    }

    public static String getStackTraceString(Throwable r1, Set<String> r2, Set<String> r3, Set<String> r4) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r1), r2, r3, r4);
    }

    public static String getStackTraceString(Throwable r1, Set<String> r2, Set<String> r3, Set<String> r4, boolean r5) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r1), r2, r3, r4, r5);
    }

    public static String getStackTraceString(Throwable r6, Set<String> r7, Set<String> r8, Set<String> r9, boolean r10, boolean r11) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r6), r7, r8, r9, r10, r11);
    }

    public static String getStackTraceString(Throwable r7, Set<String> r8, Set<String> r9, Set<String> r10, boolean r11, boolean r12, boolean r13) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r7), r8, r9, r10, r11, r12, r13);
    }

    public static String getStackTraceString(Throwable r1, boolean r2) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(r1), r2);
    }

    public static boolean isPrintPackageInformation() {
        return AbstractExceptions.isPrintPackageInformation();
    }

    public static void registerGroupPackage(String r0) {
        AbstractExceptions.registerGroupPackage(r0);
    }

    public static void registerIgnorePackage(String r0, boolean r1) {
        AbstractExceptions.registerIgnorePackage(r0, r1);
    }

    public static void registerRootPackage(String r0) {
        AbstractExceptions.registerRootPackage(r0);
    }

    public static Throwable searchCause(Throwable r1, Class<?> r2) {
        return AbstractExceptions.searchCause(r1, r2);
    }

    public static Throwable searchCause(Throwable r1, Class<?> r2, int r3) {
        return AbstractExceptions.searchCause(r1, r2, r3);
    }

    public static Throwable searchCause(Throwable r1, Class<?> r2, String r3) {
        return AbstractExceptions.searchCause(r1, r2, r3);
    }

    public static Throwable searchCause(Throwable r1, Class<?> r2, String r3, int r4) {
        return AbstractExceptions.searchCause(r1, r2, r3, r4);
    }

    public static void setIgnoreAllCauses(boolean r0) {
        AbstractExceptions.setIgnoreAllCauses(r0);
    }

    public static void setPrintPackageInformation(boolean r0) {
        AbstractExceptions.setPrintPackageInformation(r0);
    }

    public static void setPrintSuppressedExceptions(boolean r0) {
        AbstractExceptions.setPrintSuppressedExceptions(r0);
    }

    public static void setStackTraceElementSerializer(StackTraceElementSerializer r0) {
        AbstractExceptions.setStackTraceElementSerializer(r0);
    }
}
