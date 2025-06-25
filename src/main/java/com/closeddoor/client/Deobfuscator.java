package com.closeddoor.client;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class Deobfuscator {

    private final File inputJar;
    private final File outputDir;

    public Deobfuscator(File inputJar, File outputDir) {
        this.inputJar = inputJar;
        this.outputDir = outputDir;
    }

    public void run() {
        try {
            Map<String, ClassNode> classMap = new HashMap<>();

            Files.walk(inputJar.toPath())
                    .filter(path -> path.toString().endsWith(".class"))
                    .forEach(path -> {
                        try {
                            byte[] bytes = Files.readAllBytes(path);
                            ClassReader reader = new ClassReader(bytes);
                            ClassNode classNode = new ClassNode();
                            reader.accept(classNode, 0);
                            classMap.put(classNode.name, classNode);
                        } catch (Exception e) {
                            System.err.println("[Deobfuscator] Failed to read class: " + path);
                        }
                    });

            if (!outputDir.exists()) outputDir.mkdirs();

            File outJar = new File(outputDir, "deobfuscated.jar");
            try (JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(outJar))) {
                for (ClassNode node : classMap.values()) {
                    JarEntry entry = new JarEntry(node.name + ".class");
                    jarOut.putNextEntry(entry);
                    // You would normally re-write the classNode here.
                    jarOut.closeEntry();
                }
            }

            System.out.println("[Deobfuscator] Wrote deobfuscated jar to: " + outJar.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("[Deobfuscator] Critical failure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Map<String, ClassNode> readClassMapFromDirectory(Path dir) throws Exception {
        Map<String, ClassNode> map = new HashMap<>();

        Files.walk(dir)
                .filter(path -> path.toString().endsWith(".class"))
                .forEach(path -> {
                    try {
                        byte[] bytes = Files.readAllBytes(path);
                        ClassReader reader = new ClassReader(bytes);
                        ClassNode node = new ClassNode();
                        reader.accept(node, 0);
                        map.put(node.name, node);
                    } catch (Exception e) {
                        System.err.println("[Deobfuscator] Failed to load class: " + path.getFileName());
                    }
                });

        return map;
    }
}
