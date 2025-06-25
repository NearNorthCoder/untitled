package com.closeddoor.client;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class InjectionEngine {

    private final Map<String, ClassNode> classMap;

    public InjectionEngine(Map<String, ClassNode> classMap) {
        this.classMap = classMap;
    }

    public void inject() {
        System.out.println("[InjectionEngine] Starting injection process...");

        for (Map.Entry<String, ClassNode> entry : classMap.entrySet()) {
            String className = entry.getKey();
            ClassNode node = entry.getValue();

            // Modify or inject as needed here – currently pass-through
            try {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
                node.accept(writer);
                byte[] modifiedBytes = writer.toByteArray();

                // Write the modified class file back to disk
                File outFile = new File("injected/" + className + ".class");
                outFile.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(outFile)) {
                    fos.write(modifiedBytes);
                }

                System.out.println("[InjectionEngine] Injected and saved: " + outFile.getPath());

            } catch (Exception e) {
                System.err.println("[InjectionEngine] Failed to inject " + className + ": " + e.getMessage());
            }
        }

        System.out.println("[InjectionEngine] Injection process complete.");
    }

    public static Map<String, ClassNode> loadDeobClasses(Path dir) throws Exception {
        System.out.println("[InjectionEngine] Loading deobfuscated classes from: " + dir);
        return Deobfuscator.readClassMapFromDirectory(dir);
    }
}
