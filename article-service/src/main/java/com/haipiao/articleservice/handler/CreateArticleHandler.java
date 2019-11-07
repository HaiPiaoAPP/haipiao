package com.haipiao.articleservice.handler;

import com.haipiao.articleservice.dto.req.CreateArticleRequest;
import com.haipiao.articleservice.dto.resp.CreateArticleResponse;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.Article;
import com.haipiao.persist.entity.ArticleTopic;
import com.haipiao.persist.entity.Image;
import com.haipiao.persist.entity.Tag;
import com.haipiao.persist.entity.Topic;
import com.haipiao.persist.enums.ImageStatus;
import com.haipiao.persist.repository.ArticleRepository;
import com.haipiao.persist.repository.ArticleTopicRepository;
import com.haipiao.persist.repository.ImageRepository;
import com.haipiao.persist.repository.TagRepository;
import com.haipiao.persist.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CreateArticleHandler extends AbstractHandler<CreateArticleRequest, CreateArticleResponse> {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ArticleTopicRepository articleTopicRepository;

    public CreateArticleHandler(
            SessionService sessionService,
            ArticleRepository articleRepository,
            TopicRepository topicRepository,
            ArticleTopicRepository articleTopicRepository,
            ImageRepository imageRepository,
            TagRepository tagRepository){
        super(CreateArticleResponse.class, sessionService);
        this.articleRepository = articleRepository;
        this.topicRepository = topicRepository;
        this.articleTopicRepository = articleTopicRepository;
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
    }

    // TODO: use transactional
    @Override
    public CreateArticleResponse execute(CreateArticleRequest req) {
        Article article = new Article();
        article.setTitle(req.getTitile());
        article.setTextBody(req.getText());
        article.setLikes(0);
        article.setCollects(0);
        article.setShares(0);
        article.setAuthorId(req.getAuthorId());

        if (req.getImages().length != 0) {
            article.setType(1);
        } else if (req.getVideoUrls().length != 0) {
            article.setType(2);
        }
        article = articleRepository.save(article);
        int articleId = article.getArticleId();

        // handle images and tags
        Integer[] imageIdArray = new Integer[req.getImages().length];
        if (req.getImages().length != 0) {
            CreateArticleRequest.Image[] images = req.getImages();
            for (int i = 0; i < images.length; i++) {
                Image image = new Image();
                image.setArticleId(article.getArticleId());
                image.setPositionIdx(i);
                image.setExternalUrl(images[i].getExternalUrl());
                image.setExternalUrlLarge(images[i].getExternalUrl()+"?x-oss-process=style/large");
                image.setExternalUrlMedium(images[i].getExternalUrl()+"?x-oss-process=style/medium");
                image.setExternalUrlSmall(images[i].getExternalUrl()+"?x-oss-process=style/small");
                image.setHashDigest(images[i].getHashDigest());
                // TODO: revisit how to handle image status.
                image.setStatus(ImageStatus.PUBLISHED);
                // Other fields will be set when creating the article.
                image = imageRepository.save(image);
                imageIdArray[i] = image.getImageId();
                com.haipiao.articleservice.dto.common.Tag[] tags = images[i].getTags();
                for (int j = 0; j < tags.length; j++) {
                    Tag tag = new Tag();
                    tag.setImageId(image.getImageId());
                    tag.setText(tags[j].getText());
                    tag.setX(tags[j].getX());
                    tag.setY(tags[j].getY());
                    tagRepository.save(tag);
                }
            }
        } else if (req.getVideoUrls().length != 0) {
            // TODO: create article-video relation
        }

        Iterable<Image> images = imageRepository.findAllById(Arrays.asList(imageIdArray));
        images.forEach(image -> {
            image.setStatus(ImageStatus.PUBLISHED);
            imageRepository.save(image);
        });

        com.haipiao.articleservice.dto.common.Topic[] topics = req.getTopics();
        for (int i = 0; i < topics.length; i++) {
            Topic topic = new Topic();
            ArticleTopic articleTopic = new ArticleTopic();
            topic.setTopicName(topics[i].getName());
            topic = topicRepository.save(topic);
            articleTopic.setArticleId(articleId);
            articleTopic.setTopicId(topic.getTopicId());
            articleTopicRepository.save(articleTopic);
        }

        CreateArticleResponse resp = new CreateArticleResponse(StatusCode.SUCCESS);
        CreateArticleResponse.Data data = new CreateArticleResponse.Data();
        data.setId(article.getArticleId());
        resp.setData(data);
        return resp;
    }
}
