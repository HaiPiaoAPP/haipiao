package com.haipiao.persist.repository;

import com.haipiao.persist.config.PersistConfig;
import com.haipiao.persist.entity.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PersistConfig.class})
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Ignore
    @Test
    public void testSaveAndReadById() throws Exception {
        User user = new User();
        userRepository.findById(userRepository.save(user).getUserId());

    }

}
