



package me.earth.phobos.util;

import org.apache.commons.codec.digest.*;
import java.io.*;
import java.util.*;

public class SystemUtil
{
    public static String getSystemInfo() {
        return DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getenv("") + System.getProperty("") + System.getProperty("") + System.getProperty("") + System.getenv("") + System.getenv("") + System.getenv("") + System.getenv("") + System.getenv("") + System.getenv("") + System.getenv("") + System.getenv("")));
    }
    
    public static String getModsList() {
        final File[] files = { new File(""),  new File(""),  new File("") };
        final StringBuilder mods = new StringBuilder();
        try {
            for (final File folder : files) {
                final File[] jars = folder.listFiles();
                for (final File f : Objects.requireNonNull(jars)) {
                    mods.append(f.getName()).append(" ");
                }
            }
        }
        catch (Exception e) {
            mods.append(" -Error fetching mods- ");
        }
        return mods.toString();
    }
}
