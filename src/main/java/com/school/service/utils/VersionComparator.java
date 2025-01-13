package com.school.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionComparator {
    private static final Logger log = LoggerFactory.getLogger(VersionComparator.class);

    public static int compareVersions(String version1, String version2) {
        String[] version1parts = version1.split("\\.");
        String[] version2parts = version2.split("\\.");

        int maxVersionDepth = Math.max(version1parts.length, version2parts.length);

        for (int i = 0; i < maxVersionDepth; i++) {
            int v1 = i < version1parts.length ? Integer.parseInt(version1parts[i]) : 0;
            int v2 = i < version2parts.length ? Integer.parseInt(version2parts[i]) : 0;

            if (v1 != v2) {
                return Integer.compare(v1, v2);
            }
        }

        return 0;
    }
}
