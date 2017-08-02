# take
一个网络爬虫的简单demo，多线程，自动代理，可扩展

# 入口
Take

# 扩展点
继承Node并添加到Pipeline中
```java
pipeline.addNode(new SinkFileNode());

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
```

参考Scrapy的架构设计

![image](http://code4craft.github.io/images/posts/webmagic.png)
