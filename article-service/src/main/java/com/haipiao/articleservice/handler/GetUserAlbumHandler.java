package com.haipiao.articleservice.handler;

import com.haipiao.articleservice.dto.req.GetArticleCommentsRequest;
import com.haipiao.articleservice.dto.resp.AlbumResponse;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.Album;
import com.haipiao.persist.repository.*;
import com.haipiao.persist.vo.AlbumData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangshun
 */
@Component
public class GetUserAlbumHandler extends AbstractHandler<GetArticleCommentsRequest, AlbumResponse> {

    public static final Logger LOG = LoggerFactory.getLogger(GetUserAlbumHandler.class);

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserAlbumRelationRepository userAlbumRelationRepository;

    protected GetUserAlbumHandler(SessionService sessionService,
                                  AlbumRepository albumRepository,
                                  ImageRepository imageRepository,
                                  ArticleRepository articleRepository,
                                  UserAlbumRelationRepository userAlbumRelationRepository) {
        super(AlbumResponse.class, sessionService);
        this.albumRepository = albumRepository;
        this.imageRepository = imageRepository;
        this.articleRepository = articleRepository;
        this.userAlbumRelationRepository = userAlbumRelationRepository;
    }

    /**
     * 获取用户收藏的专辑
     * @param request
     * @return
     * @throws AppException
     */
    @Override
    public AlbumResponse execute(GetArticleCommentsRequest request) throws AppException {
        AlbumResponse resp = new AlbumResponse(StatusCode.SUCCESS);
        Integer userId = request.getId();
        Pageable pageable = PageRequest.of(Integer.parseInt(request.getCursor()), request.getLimit());
        Page<Album> albumPage = albumRepository.findArticlesByFollowerIdForPage(userId, pageable);
        List<Album> albums = albumPage.getContent();
        if (CollectionUtils.isEmpty(albums)) {
            resp.setErrorMessage(String.format("user %s not have albums", userId));
            return resp;
        }

        List<AlbumData> albumList = albums.stream()
                .filter(Objects::nonNull)
                .map(a -> new AlbumData(a.getAlbumId(),a.getAlbumName(),
                        imageRepository.findExternalUrlsByAlbumId(a.getAlbumId(),0,4),
                        articleRepository.countByAlbumId(a.getAlbumId()),
                        userAlbumRelationRepository.countByAlbumId(a.getAlbumId())))
                .collect(Collectors.toList());

        resp.setData(new AlbumResponse.Data(albumList, (int) albumPage.getTotalElements(),
                request.getCursor()+albums.size(), albumPage.getTotalPages() > Integer.valueOf(request.getCursor())));
        return resp;
    }
}
