package org.carlspring.cloud.storage.s3fs.Path;

import org.carlspring.cloud.storage.s3fs.S3FileSystem;
import org.carlspring.cloud.storage.s3fs.S3FileSystemProvider;
import org.carlspring.cloud.storage.s3fs.S3Path;
import org.carlspring.cloud.storage.s3fs.S3UnitTestBase;
import org.carlspring.cloud.storage.s3fs.util.S3EndpointConstant;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.carlspring.cloud.storage.s3fs.util.S3EndpointConstant.S3_GLOBAL_URI_TEST;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartsWithTest
        extends S3UnitTestBase
{


    @BeforeEach
    public void setup()
            throws IOException
    {
        s3fsProvider = getS3fsProvider();
        fileSystem = s3fsProvider.newFileSystem(S3EndpointConstant.S3_GLOBAL_URI_TEST, null);
    }

    @AfterEach
    public void tearDown()
            throws IOException
    {
        s3fsProvider.close((S3FileSystem) fileSystem);
        fileSystem.close();
    }

    private S3Path getPath(String path)
    {
        return s3fsProvider.getFileSystem(S3_GLOBAL_URI_TEST).getPath(path);
    }

    @Test
    public void startsWithBucket()
    {
        assertTrue(getPath("/bucket/file1").startsWith(getPath("/bucket/")));
        assertFalse(getPath("/bucket/file1").startsWith(getPath("/bucket")));
    }

    @Test
    public void startsWithBlank()
    {
        assertFalse(getPath("/bucket/file1").startsWith(getPath("")));
    }

    @Test
    public void startsWithBlankRelative()
    {
        assertFalse(getPath("file1").startsWith(getPath("")));
    }

    @Test
    public void startsWithSlash()
    {
        assertTrue(getPath("/bucket/file").startsWith(getPath("/bucket/")));
    }

    @Test
    public void startsWithBlankBlank()
    {
        assertTrue(getPath("").startsWith(getPath("")));
    }

    @Test
    public void startsWithOnlyBuckets()
    {
        assertTrue(getPath("/bucket").startsWith(getPath("/bucket")));
    }

    @Test
    public void startsWithRelativeVsAbsolute()
    {
        assertFalse(getPath("/bucket/file1").startsWith(getPath("file1")));
    }

    @Test
    public void startsWithRelativeVsAbsoluteInBucket()
    {
        assertFalse(getPath("/bucket/file1").startsWith(getPath("bucket")));
    }

    @Test
    public void startsWithFalse()
    {
        assertFalse(getPath("/bucket/file1").startsWith(getPath("/bucket/file1/file2")));
        assertTrue(getPath("/bucket/file1/file2").startsWith(getPath("/bucket/file1")));
    }

    @Test
    public void startsWithNotNormalize()
    {
        assertFalse(getPath("/bucket/file1/file2").startsWith(getPath("/bucket/file1/../")));
    }

    @Test
    public void startsWithNormalize()
    {
        // in this implementation not exists .. or . special paths
        assertFalse(getPath("/bucket/file1/file2").startsWith(getPath("/bucket/file1/../").normalize()));
    }

    @Test
    public void startsWithRelative()
    {
        assertTrue(getPath("file/file1").startsWith(getPath("file")));
    }

    @Test
    public void startsWithDifferentProvider()
    {
        assertFalse(getPath("/bucket/hello").startsWith(Paths.get("/bucket")));
    }

    @Test
    public void startsWithString()
    {
        assertTrue(getPath("/bucket/hello").startsWith("/bucket/hello"));
    }

    @Test
    public void startsWithStringRelative()
    {
        assertTrue(getPath("subkey1/hello").startsWith("subkey1/hello"));
    }

    @Test
    public void startsWithStringOnlyBuckets()
    {
        assertTrue(getPath("/bucket").startsWith("/bucket"));
    }

    @Test
    public void startsWithStringRelativeVsAbsolute()
    {
        assertFalse(getPath("/bucket/file1").startsWith("file1"));
    }

    @Test
    public void startsWithStringFalse()
    {
        assertFalse(getPath("/bucket/file1").startsWith("/bucket/file1/file2"));
        assertTrue(getPath("/bucket/file1/file2").startsWith("/bucket/file1"));
    }

    @Test
    public void startsWithStringRelativeVsAbsoluteInBucket()
    {
        assertFalse(getPath("/bucket/file1").startsWith("bucket"));
    }

}
