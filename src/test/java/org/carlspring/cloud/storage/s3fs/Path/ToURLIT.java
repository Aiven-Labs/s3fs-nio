package org.carlspring.cloud.storage.s3fs.Path;

import org.carlspring.cloud.storage.s3fs.S3FileSystemProvider;
import org.carlspring.cloud.storage.s3fs.S3Path;
import org.carlspring.cloud.storage.s3fs.util.EnvironmentBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.carlspring.cloud.storage.s3fs.AmazonS3Factory.PATH_STYLE_ACCESS;
import static org.carlspring.cloud.storage.s3fs.util.S3EndpointConstant.S3_GLOBAL_URI_IT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ToURLIT
{

    private static final URI uriGlobal = EnvironmentBuilder.getS3URI(S3_GLOBAL_URI_IT);

    public FileSystem getS3FileSystem(Entry... props)
            throws IOException
    {
        System.clearProperty(S3FileSystemProvider.AMAZON_S3_FACTORY_CLASS);

        Map<String, Object> env = new HashMap<>(EnvironmentBuilder.getRealEnv());
        if (props != null)
        {
            for (Entry entry : props)
            {
                env.put(entry.getKey(), entry.getValue());
            }
        }

        try
        {
            return FileSystems.newFileSystem(uriGlobal, env);
        }
        catch (FileSystemAlreadyExistsException e)
        {
            FileSystems.getFileSystem(uriGlobal).close();

            return FileSystems.newFileSystem(uriGlobal, env);
        }
    }

    @Test
    public void toURLDefault()
            throws IOException
    {
        FileSystem fs = getS3FileSystem();

        S3Path s3Path = (S3Path) fs.getPath("/bucket.with.dots").resolve("index.html");

        assertEquals(new URL("https://bucket.with.dots." + S3_GLOBAL_URI_IT.toString().replace("s3://", "") +
                             "index.html"),
                     s3Path.toURL());
    }

    @Test
    public void toURLWithPathStyle()
            throws IOException
    {
        FileSystem fs = getS3FileSystem(new Entry(PATH_STYLE_ACCESS, "true"));

        S3Path s3Path = (S3Path) fs.getPath("/bucket.with.dots").resolve("index.html");

        assertEquals(new URL("https://" + S3_GLOBAL_URI_IT.toString().replace("s3://", "") +
                             "bucket.with.dots/index.html"),
                     s3Path.toURL());
    }

    @Test
    public void toURLNull()
            throws IOException
    {
        FileSystem fs = getS3FileSystem(new Entry(PATH_STYLE_ACCESS, "true"));

        S3Path s3Path = (S3Path) fs.getPath("directory").resolve("index.html");

        assertNull(s3Path.toURL());
    }

    public static class Entry
    {

        private String key;

        private String value;


        public Entry(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        public String getKey()
        {
            return key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }
    }

}
