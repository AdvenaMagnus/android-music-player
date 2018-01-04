package com.sogoodlabs.silvia.musicplayer;

import com.sogoodlabs.silvia.musicplayer.utils.FileUtils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Alexander on 23.09.2017.
 */

public class FileUtilsTest {

    @Test
    public void gettingFileExtensionTest(){
        String file1 = "file.exe";
        String file2 = "dir1/file.exe";
        String file3 = "/dir1/file.exe";
        String file4 = "dir2/dir1/file.exe";
        String file5 = "dir.o/dir1/file.exe";
        String file6 = "file";

        assertEquals("exe", FileUtils.getExtension(file1));
        assertEquals("exe", FileUtils.getExtension(file2));
        assertEquals("exe", FileUtils.getExtension(file3));
        assertEquals("exe", FileUtils.getExtension(file4));
        assertEquals("exe", FileUtils.getExtension(file5));
        assertEquals("", FileUtils.getExtension(file6));
    }

}
