package com.jt.core.pipeline;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.jt.core.PageContext;
import com.jt.core.Take;
import okhttp3.MediaType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * Created by he on 2017/8/1.
 */
public class SinkFileNode implements Node {

    private static final Logger LOGGER = LoggerFactory.getLogger(SinkFileNode.class);

    private static Set<String> supportMedia = ImmutableSet.of("html", "jpeg");

    @Override
    public void process(PageContext pageContext) {

        MediaType mediaType = pageContext.getMediaType();
        String subtype = mediaType.subtype();
        if (!supportMedia.contains(subtype)) {
            return;
        }
        byte[] body = pageContext.getBody();
        String url = pageContext.getRequest().url().toString();
        String name = DigestUtils.md5Hex(url);
        String path = Take.path;

        try {
            if (StringUtils.equalsIgnoreCase(subtype, "html")) {
                LOGGER.info("保存html文件 {} {}", url, name);
                Files.write(pageContext.getHtml(), new File(path, name+".html"), Charset.forName(pageContext.getCharset()));
            } else if (StringUtils.equalsIgnoreCase(subtype, "jpeg")) {
                LOGGER.info("保存图片 {} {}", url, name);
                Files.write(body, new File(path, name+".jpeg"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
