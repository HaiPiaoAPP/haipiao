package com.haipiao.articleservice.handler;

import com.haipiao.articleservice.dto.req.GetArticleRequest;
import com.haipiao.articleservice.dto.resp.GetArticleResponse;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.Article;
import com.haipiao.persist.entity.ArticleTopic;
import com.haipiao.persist.entity.Image;
import com.haipiao.persist.entity.Tag;
import com.haipiao.persist.entity.Topic;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.repository.ArticleRepository;
import com.haipiao.persist.repository.ArticleTopicRepository;
import com.haipiao.persist.repository.ImageRepository;
import com.haipiao.persist.repository.TagRepository;
import com.haipiao.persist.repository.TopicRepository;
import com.haipiao.persist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GetArticleHandler extends AbstractHandler<GetArticleRequest, GetArticleResponse> {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ArticleTopicRepository articleTopicRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TagRepository tagRepository;

    public GetArticleHandler(
        SessionService sessionService,
        ArticleRepository articleRepository,
        UserRepository userRepository,
        TopicRepository topicRepository,
        ArticleTopicRepository articleTopicRepository,
        ImageRepository imageRepository,
        TagRepository tagRepository) {
        super(GetArticleResponse.class, sessionService);
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.articleTopicRepository = articleTopicRepository;
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public GetArticleResponse execute(GetArticleRequest req) {

        Article article;
        int articleID = req.getId();
        Optional<Article> optionalArticle = articleRepository.findById(articleID);
        if(optionalArticle.isPresent()) {
            article = optionalArticle.get();
        } else {
            GetArticleResponse resp = new GetArticleResponse(StatusCode.NOT_FOUND);
            resp.setErrorMessage(String.format("article %s not found in DB", articleID));
            return resp;
        }

        GetArticleResponse.ArticleData data = new GetArticleResponse.ArticleData();
        data.setId(articleID);
        data.setTitle(article.getTitle());
        data.setText(article.getTextBody());
        data.setCollects(article.getCollects());
        data.setLikes(article.getLikes());
        data.setShares(article.getShares());

        // Author
        int authorID = article.getAuthorId();
        Optional<User> optionalUser = userRepository.findById(authorID);
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            GetArticleResponse resp = new GetArticleResponse(StatusCode.NOT_FOUND);
            resp.setErrorMessage(String.format("user %s not found in DB", authorID));
            return resp;
        }
        GetArticleResponse.ArticleData.Author author = new GetArticleResponse.ArticleData.Author();
        author.setId(user.getUserId());
        author.setName(user.getUserName());
        author.setProfileImageUrl(user.getProfileImgUrl());
        author.setProfileImageUrlSmall(user.getProfileImgUrlSmall());
        data.setAuthor(author);

        // Topic
        List<ArticleTopic> articleTopicList = articleTopicRepository.findAllByArticleId(articleID);
        ArticleTopic[] articleTopicArray = new ArticleTopic[articleTopicList.size()];
        articleTopicArray = articleTopicList.toArray(articleTopicArray);
        com.haipiao.articleservice.dto.common.Topic[] topics = new com.haipiao.articleservice.dto.common.Topic[articleTopicArray.length];
        for (int i = 0; i < articleTopicArray.length; i++) {
            topics[i] = new com.haipiao.articleservice.dto.common.Topic();
            int topicID = articleTopicArray[i].getId();
            topics[i].setId(topicID);
            Optional<Topic> optionalTopic = topicRepository.findById(topicID);
            Topic topic;
            if (optionalTopic.isPresent()) {
                topic = optionalTopic.get();
            } else {
                GetArticleResponse resp = new GetArticleResponse(StatusCode.NOT_FOUND);
                resp.setErrorMessage(String.format("topic %s not found in DB", topicID));
                return resp;
            }
            topics[i].setName(topic.getTopicName());
        }
        data.setTopics(topics);

        if (article.getType() == 1) { // Images and tags
            List<Image> imageList = imageRepository.findByArticleIdOrderByPositionIdxAsc(articleID);
            Image[] imageArray = new Image[imageList.size()];
            imageArray = imageList.toArray(imageArray);
            GetArticleResponse.ArticleData.Image[] images = new GetArticleResponse.ArticleData.Image[imageArray.length];
            for (int i = 0; i < imageArray.length; i++) {
                int positionId = imageArray[i].getPositionIdx();
                images[positionId] = new GetArticleResponse.ArticleData.Image();
                images[positionId].setUrl(imageArray[i].getExternalUrl());
                images[positionId].setUrlSmall(imageArray[i].getExternalUrlSmall());
                List<Tag> tagList = tagRepository.findAllByImageId(imageArray[i].getImageId());
                Tag[] tagArray = new Tag[tagList.size()];
                tagArray = tagList.toArray(tagArray);
                com.haipiao.articleservice.dto.common.Tag[] tags = new com.haipiao.articleservice.dto.common.Tag[tagArray.length];
                for (int j = 0; j < tagArray.length; j++) {
                    com.haipiao.articleservice.dto.common.Tag tag = new com.haipiao.articleservice.dto.common.Tag();
                    tag.setText(tagArray[j].getText());
                    tag.setX(tagArray[j].getX());
                    tag.setY(tagArray[j].getY());
                    tags[j] = tag;
                }
                images[positionId].setTags(tags);
            }
            data.setImages(images);
        } else if (article.getType() == 2) {
            // TODO: handle videos
        }

        // TODO: set user related stuff
        data.setCollected(true);
        data.setLiked(true);

        GetArticleResponse resp = new GetArticleResponse(StatusCode.SUCCESS);
        resp.setData(data);
        return resp;
    }
}
