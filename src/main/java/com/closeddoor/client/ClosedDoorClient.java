package com.closeddoor.client;

import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.util.Map;

public class ClosedDoorClient {

    public static void main(String[] args) {
        System.out.println("[ClosedDoor] Starting client...");

        // Define input and output directories
        File inputJar = new File("input/classes"); // Adjust as needed
        File outputDir = new File("output/deob");

        // Run Deobfuscator
        Deobfuscator deobfuscator = new Deobfuscator(inputJar, outputDir);
        deobfuscator.run();

        try {
            // Read classes from output directory
            Map<String, ClassNode> classMap = Deobfuscator.readClassMapFromDirectory(outputDir.toPath());

            // Resolve Hooks
            HookResolver hookResolver = new HookResolver(classMap);
            hookResolver.resolve();

            // Inject Code
            InjectionEngine injectionEngine = new InjectionEngine(classMap);
            injectionEngine.inject();

            System.out.println("[ClosedDoor] Injection complete. Shutting down.");
        } catch (Exception e) {
            System.err.println("[ClosedDoor] Critical error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
