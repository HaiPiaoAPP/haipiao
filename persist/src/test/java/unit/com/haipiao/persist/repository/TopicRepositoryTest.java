package com.haipiao.persist.repository;

import com.haipiao.persist.config.PersistConfig;
import com.haipiao.persist.entity.Topic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PersistConfig.class})
public class TopicRepositoryTest {

    @Autowired
    TopicRepository topicRepository;

    @Test
    public void testSaveAndRead() {
        Topic topic = new Topic();
        topic.setTopicName("我的话题");
        Topic expected = topicRepository.save(topic);
        Optional<Topic> actual = topicRepository.findById(topic.getTopicId());
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

}
