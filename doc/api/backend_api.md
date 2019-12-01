# HaiPiao API 文档 

## 文档规范

### API标题 (H3)

一段对API的文字描述 （正文）

**URL**： `/example`  
**Method**: GET  
**Required headers**: `Cookie`

**Parameters**:  

| Name | Type        | Required | Description                         |
| ---- | ----------- | -------- | ----------------------------------- |
| example_para | String  | Yes      | 必要的文字描述 |

**Request body**:  
必要的文字描述。（请将代码块的语法设置为`javascript`.）

```javascript
{
  ... 
}
```

**Response body** :  
必要的文字描述。（请将代码块的语法设置为`javascript`.）

**Success**

```javascript
{
  "status_code": "SUCCESS",
  "data": {
    ...
  }
}
```

**Fail**

```javascript
{
  "status_code": <String>,
  "error_message": <String>
}
```

**Possible error codes**: 

- `SOME_ERROR`: 必要的文字描述。


## API 

### 1. 获取验证码

客户端为当前用户设备请求一个验证码，请求中需包含当前设备（手机）号码。服务器端返回一个6位验证码，一分钟内有效。
需要根据电话号码和请求的IP做限流保护。

蓝湖图2。

**URL**： `/security-code`  
**Method**: GET

**Parameters**:

| Name | Type        | Required | Description                         |
| ---- | ----------- | -------- | ----------------------------------- |
| cell | String  | Yes      | 请求验证码的手机号 |
| country_code | String  | Yes      | 请求验证码的手机号的国家号（86：中国） |

**Response body**:

请求成功将返回一个security code。

**Success**

```javascript
{
  "status_code": "SUCCESS",
  "security_code": "098320" // 测试使用，未来会删除
}
```

**Fail**

```javascript
{
  "status_code": <String>,
  "error_message": <String>
}
```

**Possible error codes**: 

- `INTERNAL_SERVER_ERROR`: 未知服务器端错误。
- `BAD_REQUEST`: 请求不合规（例如：中国电话号码不满足位数要求）
- `THROTTLED`: 同一手机号请求过多
- `EXTERNAL_SERVICE_ERROR`: 无法请求短信服务

### 2. 验证验证码并登陆/注册

客户端请求服务器验证 1. 用户登陆或注册（`login`）或 2. 用户修改手机号（`update_cell`）  
如果验证类型为`login`（登陆或注册）：
- 当用户（手机号）已存在，直接登陆, 服务器需返回session token(`"type"="SESSION_TOKEN"`)。
- 当用户（手机号）不存在，服务器端需返回nonce, nonce在使用一次后即失效(`"type"="NONCE"`)。 

如果验证目的为`update_cell` (更新手机号)：
- 服务器端需返回nonce, nonce在使用一次后即失效(`"type"="NONCE"`)

**URL**： `/security-code/verification`  
**Method**: POST

**Request body**:  
purpose可以是`login`或`update_cell`。

```javascript
{
  "country_code": "86",
  "cell": "12345678900",
  "purpose": "login",
  "code": "314489"
}
```

**Response body** :  
请求成功将会返回一个token，token类型和token签发的时间。

**Success**

```javascript
{ 
  "status_code": "SUCCESS",
  "data": {
    "token": "euMA3jBRShPn/K935B9e0A==:T4p4tBPdDrgD70UbbgGNoQ==",
    "issued_time": 1571641196070,
    "type": "SESSION_TOKEN" 
  }
}
```


**Fail**

```javascript
{
  "status_code": <String>,
  "error_message": <String>
}
```

**Possible error codes**: 

- `UNAUTHORIZED`: 短信验证码错误
- `INTERNAL_SERVER_ERROR`: 未知服务器错误

### 3. 请求上传阿里云OSS的token

App请求一个拥有阿里云OSS写权限的token。然后用这个token来上传图片到OSS，上传完成后记录下图片的URL，以便在后续的API中使用。
请参考此文档（https://help.aliyun.com/document_detail/100624.html?spm=a2c4g.11186623.6.656.1a6c44fdyhPHYR）中实现原理部分。
需用对同一个用户进行限流。

**URL**： `/image/securitytoken`  
**Method**: GET  
**Required headers**:
- `Cookie`: 必须包含seesion_token。不支持visitor mode(游客模式)

**Response body** :  
返回调用阿里云OSS所必须的信息。

**Success**

endpoint为目前使用的阿里云OSS的可用区域的endpoint URL。
access key id, access key secret，security token和expire time, app需要缓存。
前三个字段需要在写阿里云OSS操作中提供，阿里云提供了相关SDK。
前端需要根据expire time来决定是否需要重新请求。

```javascript
{
  "status_code": "SUCCESS",
  "data": {
    "endpoint": "http://oss-cn-hangzhou.aliyuncs.com",
    "access_key_id": "tempaccesskey",
    "access_key_secret": "someaccesskeysecret",
    "security_token": "sometoken",
    "expire_time": 1573113573
  }
}
```

**Fail**

```javascript
{
  "status_code": <String>,
  "error_message": <String>
}
```

**Possible error codes**: 

- `UNAUTHORIZED`: 没有session token或者session token不合法。
- `THROTTLED`: 请求频率过高。

### 4. 创建用户

客户端上传用户设置的个人基本信息：名字，性别，生日，头像等，请求中需包含session token。

**URL**： `/user`  
**Method**: POST   
**Required headers**: `Cookie`

**Request body**:  

`profile_image_url`为可缺省，即不上传头像。
`name`，`gender`和`birthday`是必需的。
`gender`可以是`M`(男),`W`(女),`U`(未设置)。
`nonce`由验证手机验证码获得，只能使用一次。

```javascript
{
  "nonce": "euMA3jBRShPn/K935B9e0A==",  
  "name": "王小明",
  "gender": "M",
  "birthday": "1992/01/31",
  "profile_image_url": "aliyun-abc.oss-cn-hangzhou.aliyuncs.com/abc/myphoto.jpg"
}
```

**Response body**:

返回用户ID和session token。
App需要缓存用户ID和session token。

**Success**

```javascript
{
  "status_code": "SUCCESS",
  "data": {
    "id": 1234,
    "session_token": "euMA3jBRShPn/K935B9e0A==:T4p4tBPdDrgD70UbbgGNoQ=="
  } 
}
```

**Fail**

```javascript
{
  "status_code": <String>,
  "error_message": <String>
}
```

**Possible error codes**: 

- `BAD_REQUEST`: 缺少必需的字段。
- `INTERNAL_SERVER_ERROR`: 未知服务器错误。

### 4. 获取感兴趣的主题

客户端向服务器端请求一个的备选“感兴趣的主题”列表。
该列表应结合之前用户设置的个人基本信息生成。
请求中需包含session token。
category列表和对应图片建议缓存。

蓝湖图7和8为需要调用`/category?type=all`。

**URL**: `/category?type=[hot|misc|all]`  
**Method**: GET  

**Required headers**: `Cookie: session-token=<token>`

**Parameters**:  

| Name | Type        | Required | Description                         |
| ---- | ----------- | -------- | ----------------------------------- |
| type | String Enum | Yes      | `hot`: 热门 `misc`: 其他 `all`: 所有 |

**Response body** :

data字段下有且只有`all`, `hot`和`misc`中的一个。

**Success**


`all`: 获取全部的推荐分类：

```javascript
{
  "status_code": "SUCCESS",
  "data": {
    "all": [
      {
        "id": 11,
        "name": "工作",
        "cover_image_url": "aliyun-abc.oss-cn-hangzhou.aliyuncs.com/abc/work.jpg"
      },
      {
        "id": 12,
        "name": "旅游",
        "cover_image_url": "aliyun-abc.oss-cn-hangzhou.aliyuncs.com/abc/travel.jpg"
      }
    ]
  }
}
```

`hot`: 获取热门分类：

```javascript
{
  "status_code": "SUCCESS",
  "data": {
    "hot": [
      {
        "id": 11,
        "name": "工作",
        "cover_image_url": "aliyun-abc.oss-cn-hangzhou.aliyuncs.com/abc/work.jpg"
      }
    ]
  }
}
```

`misc`: 获取其他分类：

```javascript
{
  "status_code": "SUCCESS",
  "data": {
    "misc": [
      {
        "id": 12,
        "name": "旅游",
        "cover_image_url": "aliyun-abc.oss-cn-hangzhou.aliyuncs.com/abc/travel.jpg"
      }
    ]
  }
}
```

**Fail**

```javascript
{
  "status_code": <String>,
  "error_message": <String>
}
```

**Possible error codes**: 

- `BAD_REQUEST`: query parameter的类型不合法。
- `INTERNAL_SERVER_ERROR`: 未知服务器错误。

### 5. 设置感兴趣的主题 

客户端上传用户设置的5+ 个感兴趣主题，请求中需包含session token。

**URL**: `/user/category`

**Method**: POST

**Required headers**:
`Cookie: session-token=<token>`： 必须包含seesion_token。不支持visitor mode(游客模式)。

**Request body**:

用户选中的主题ID的列表。

```javascript
{
  "categories": [
    11,
    12
  ]
}
```

**Response body**:

**Success**:

```javascript
{
  "status_code": "SUCCESS"
}
```

**Fail**

```javascript
{
  "status_code": <String>,
  "error_message": <String>
}
```

**Possible error codes**: 

- `BAD_REQUEST`: query parameter的类型不合法。
- `UNAUTHORIZED`: 用户未登录或者session token不合法。
- `INTERNAL_SERVER_ERROR`: 未知服务器错误。

### 6. 推荐值得关注的用户

客户端上传用户设置的5+ 个感兴趣主题，请求中需包含session token。

**URL**: `/recommendation/user?context=<string>[&limit=<integer>][&cursor=<string>][&article=<integer>][&user=<integer>]`

**Method**: GET

**Parameters**:  

| Name | Type        | Required | Description                         |
| ---- | ----------- | -------- | ----------------------------------- |
| context | string | true |推荐情景: `default`: 默认, `article`: 文章内, `user_profile`:用户简介  |
| article | int | true when context=article | article的ID |
| user | int | true when context=user_profile | user的ID |
| limit | int | false | 推荐的个数，默认6个 |
| cursor | string | false | 当前一次响应中`more_to_follow`为`true`时，如果想要继续请求列表中的后续内容，需要带上前一次返回的cursor。 |

**Required headers**:

`Cookie: session-token=<token>`： 必须包含seesion_token。不支持visitor mode(游客模式)。

**Response body**:

返回一个用户列表。`cursor`为当前分页位置，`limit`为每页大小，`more_to_follow`为是否有后续内容。

**Success**:

```javascript
{
  "status_code": "SUCCESS",
  "data": {
    users: [
      {
        id : 1234,
        name : "张三",
        profile_image_url : "aliyun-abc.oss-cn-hangzhou.aliyuncs.com/user/1234.jpg",
        followers_count : 345,
        description : "留学申请达人"
      },
      {
        id : 234,
        name : "李四",
        profile_image_url : "aliyun-abc.oss-cn-hangzhou.aliyuncs.com/user/234.jpg",
        followers_count : 900,
        description : "聊聊斯坦福那些事"
      }
    ],
    cursor: "arandomstring",
    more_to_follow: false
  }
}
```

**Fail**

```javascript
{
  "status_code": <String>,
  "error_message": <String>
}
```

**Possible error codes**: 

- `BAD_REQUEST`: query parameter名称或组合不合法。
- `UNAUTHORIZED`: 用户未登录或者session token不合法。
- `INTERNAL_SERVER_ERROR`: 未知服务器错误。

### 7. 关注用户

设置当前用户关注的单个用户。请求中需包含session token。
group_id为0表示"全部"（默认）分组。
蓝湖图9中关注用户时，加入"全部"分组。
followee_id为被关注的用户的id。

**URL**: `/group/{group_id}/user/{followee_id}`

**Method**: POST

**Required headers**: `Cookie: session-token=<token>`

**Response body**:

**Success**:

```javascript
{
  "status_code": "SUCCESS",
}
```

**Fail**

```javascript
{
  "status_code": <String>,
  "error_message": <String>
}
```

**Possible error codes**: 

- `BAD_REQUEST`: group_id或followee_id不存在。
- `UNAUTHORIZED`: 用户未登录或者session token不合法。
- `INTERNAL_SERVER_ERROR`: 未知服务器错误。

TODO: complete APIs below

### 10. 获取推荐文章列表

客户端向服务器端请求一个“推荐文章列表”，请求中需包含session token。服务端交由后端推荐算法生成推荐文章列表。

discover = 发现

nearby = 附近

article_related = 与当前文章相关

topic_related = 与当前话题相关

topic_related_latest = 与当前话题相关且最新 

### 11.  我的分类列表

客户端向服务器段请求当前用户所选择的“感兴趣主题”列表，请求中需包含session token。

### 12. 点赞

当前用户为一篇文章点赞，请求中需包含session token。

### 13. 取消点赞

当前用户取消之前的点赞， 请求中需包含session token。

### 14. 不感兴趣

当前用户标记一篇文章为“不感兴趣”，请求中需包含session token。

### 15. 获取关注用户的更新

检查当前用户所关注的用户是否有更新

check：检查是否有更新

pull： 获取所有更新信息

### 16. 通知是否有更新

检查当前用户是否有未读的系统通知

check：检查是否有更新

pull： 获取所有更新信息

### 17. 加载文章

包含是否本人已点赞和已收藏

### 18.  获取用户分组

获取当前用户所创建的所有“分组”

### 19. 获取默认分组

获取系统的默认分组

### 20. 删除分组

### 21. 新建分组

### 22. 获取热搜列表（前N）（16）

### 23. 自动补全 （17）

### 24. 搜索文章 

### 25. 搜索话题

### 26. 搜索用户 (21)

### 27. 举报违规文章 (21)

### 28. 设置关注用户的分组

### 30. 获取用户信息

### 31. 获取用户所有文章

###  32. 更改以关注用户的分组

### 33. 取消关注

### 34. 获取用户的收藏文章列表

### 35. 获取用户专辑列表

一段对API的文字描述 （正文）

**URL**： `/user/{user_id}/album?limit=<integer>&cursor=<string>`
TODO: decide how to handle user itself

**Method**: GET  

**Required headers**: `Cookie`

**Parameters**:  

| Name | Type        | Required | Description                         |
| ---- | ----------- | -------- | ----------------------------------- |
| limit | Integer  | No      | 请求的列表长度(默认为6) |
| cursor | string | false | 当前一次响应`more_to_follow`为`true`时，如果想要继续请求列表中的后续内容，需要带上前一次返回的cursor。 |

**Response body** :  

返回一个专辑列表。如果请求的limit比较大，服务器会返回`more_to_follow: true`以及一个cursor同时还有一个不完整的列表（个数<limit）。

**Success**

```json
{
  "status_code": "SUCCESS",
  "data": {
    "albums": [
      {
         "id": 1234,
         "title": "有意思的事儿",
         "cover_image_urls": ["aliyun-abc.oss-cn-hangzhou.aliyuncs.com/cover/1234.jpg"],
         "articles_count": 1,
         "followers_count": 15,
         "followered": false           
      }
    ],
    "cursor": "thisisacursor",
    "more_to_follow": true
  }
}
```

**Fail**

```json
{
  "status_code": <string>,
  "error_message": <string>
}
```

**Possible error codes**: 

- UNAUTHORIZED: session token不存在或不合法
- INTERNAL_SERVER_ERROR: 未知服务器端错误。

### 36. 获取用户粉丝列表

### 37. 举报用户

被举报用户ID，原因，图片

### 38. 加载评论列表

评论者昵称，评论时间，前2条回复，点赞数，是否已为评论点赞，回复总数

### 39. 加载一条评论和回复 

回复内容，回复者昵称，回复时间，回复的点赞数，被回复的人

### 40. 收藏文章到专辑

 参数：文章ID，专辑ID，

### 41. 添加专辑

### 42. 获取专辑中文章列表

### 43. 移动文章到新的专辑

### 44. 删除文章

### 45. 编辑专辑

 参数：标题，简介，可见性

### 46.  删除专辑

### 47. 获取已关注用户列表

### 48. 获取用户关注的专辑列表

### 49. 获取用户关注的话题列表

### 50. 获取推荐当前用户关注的话题列表

标签名称，参与人数

### 51. 获取话题信息

### 52. 创建新文章

### 53. APP报错

### 54. 登出

### 55. 获取用户全部信息

头像，海漂ID，性别，是否已完成实名认证，常住地，组织，生日，个性签名，鲸鱼等级

### 56. 修改用户信息

name，gender， location， birthday， signature

### 57. 获取账号相关信息

### 58. 获取用户隐私设置

### 59. 修改用户隐私设置

### 60. 获取最新用户协议

### 61. 获取N条热门文章列表

### 62. 获取达人列表

### 63. 获取赞的N条通知

### 64. 获取收藏的N条通知

### 65. 获取评论的N条通知

### 66. 获取@的N条通知

### 67. 获取新增关注的N条通知

### 68. 获取系统通知的N条通知

### 69. 获取私信的N条通知


