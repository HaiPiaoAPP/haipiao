package com.haipiao.userservice.handler;

import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.common.util.session.UserSessionInfo;
import com.haipiao.persist.entity.Category;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.repository.CategoryRepository;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.userservice.enums.GetCategoryEnum;
import com.haipiao.userservice.req.GetCategoryRequest;
import com.haipiao.userservice.resp.GetCategoryResponse;
import com.haipiao.userservice.resp.dto.CategoryInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author wangjipeng
 */
@Component
public class GetCategoryHandler extends AbstractHandler<GetCategoryRequest, GetCategoryResponse> {

    @Autowired
    private CategoryRepository categoryRepository;

    public GetCategoryHandler(SessionService sessionService,
                              CategoryRepository categoryRepository) {
        super(GetCategoryResponse.class, sessionService);
        this.categoryRepository = categoryRepository;
    }

    /**
     * 结合之前用户设置的个人基本信息生成感兴趣的主题列表
     - `BAD_REQUEST`: query parameter的类型不合法。
     - `INTERNAL_SERVER_ERROR`: 未知服务器错误。
     * @param request
     * @return
     */
    @Override
    protected GetCategoryResponse execute(GetCategoryRequest request) throws AppException {
        if (!GetCategoryEnum.checkType(request.getType())){
            GetCategoryResponse response = new GetCategoryResponse(StatusCode.BAD_REQUEST);
            response.setErrorMessage(String.format("%s: 无此分类Type", request.getType()));
            return response;
        }
        GetCategoryResponse.Data data = new GetCategoryResponse.Data();
        Iterable<Category> all = categoryRepository.findByType(request.getType());
        List<CategoryInfoDto> list = StreamSupport.stream(all.spliterator(), false)
                .filter(Objects::nonNull)
                .map(c -> new CategoryInfoDto(c.getCategoryId(), c.getCategoryName(), ""))
                .collect(Collectors.toList());
        data.setCategories(list);
        GetCategoryResponse response = new GetCategoryResponse(StatusCode.SUCCESS);
        response.setData(data);
        return response;
    }
}
