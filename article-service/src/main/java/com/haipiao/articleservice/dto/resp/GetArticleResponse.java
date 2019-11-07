package com.haipiao.articleservice.dto.resp;

import com.google.gson.annotations.SerializedName;
import com.haipiao.articleservice.dto.common.Tag;
import com.haipiao.articleservice.dto.common.Topic;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.resp.AbstractResponse;

public class GetArticleResponse extends AbstractResponse<GetArticleResponse.ArticleData> {

    public GetArticleResponse(StatusCode statusCode) {
        super(statusCode);
    }

    public static class ArticleData {
        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("text")
        private String text;

        @SerializedName("author")
        private Author author;

        @SerializedName("images")
        private Image[] images;

        @SerializedName("video_urls")
        private String[] videoUrls;

        @SerializedName("topics")
        private Topic[] topics;

        @SerializedName("collects")
        private int collects;

        @SerializedName("collected")
        private Boolean collected;

        @SerializedName("likes")
        private int likes;

        @SerializedName("liked")
        private Boolean liked;

        @SerializedName("shares")
        private int shares;

        @SerializedName("comments")
        private Author comments;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Author getAuthor() {
            return author;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setAuthor(Author author) { this.author = author; }

        public Image[] getImages() {
            return images;
        }

        public void setImages(Image[] images) {
            this.images = images;
        }

        public String[] getVideoUrls() { return videoUrls; }

        public void setVideoUrls(String[] videoUrls) { this.videoUrls = videoUrls; }

        public Topic[] getTopics() {
            return topics;
        }

        public void setTopics(Topic[] topics) {
            this.topics = topics;
        }

        public int getCollects() {
            return collects;
        }

        public void setCollects(int collects) {
            this.collects = collects;
        }

        public Boolean getCollected() {
            return collected;
        }

        public void setCollected(Boolean collected) {
            this.collected = collected;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public Boolean getLiked() {
            return liked;
        }

        public void setLiked(Boolean liked) {
            this.liked = liked;
        }

        public int getShares() {
            return shares;
        }

        public void setShares(int shares) {
            this.shares = shares;
        }

        public Author getComments() {
            return comments;
        }

        public void setComments(Author comments) {
            this.comments = comments;
        }

        public static class Image {
            @SerializedName("url")
            private String url;

            @SerializedName("url_medium")
            private String urlMedium;

            @SerializedName("url_small")
            private String urlSmall;

            @SerializedName("tags")
            private Tag[] tags;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getUrlMedium() {
                return urlMedium;
            }

            public void setUrlMedium(String urlMedium) {
                this.urlMedium = urlMedium;
            }

            public String getUrlSmall() { return urlSmall; }

            public void setUrlSmall(String urlSmall) { this.urlSmall = urlSmall; }

            public Tag[] getTags() {
                return tags;
            }

            public void setTags(Tag[] tags) {
                this.tags = tags;
            }
        }

        public static class Author {
            @SerializedName("id")
            private int id;

            @SerializedName("name")
            private String name;

            @SerializedName("profile_image_url")
            private String profileImageUrl;

            @SerializedName("profile_image_url_small")
            private String profileImageUrlSmall;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProfileImageUrl() {
                return profileImageUrl;
            }

            public void setProfileImageUrl(String profile_image_url) {
                this.profileImageUrl = profile_image_url;
            }

            public String getProfileImageUrlSmall() {
                return profileImageUrlSmall;
            }

            public void setProfileImageUrlSmall(String profileImageUrlSmall) {
                this.profileImageUrlSmall = profileImageUrlSmall;
            }
        }

        public static class Comments {
            @SerializedName("total_count")
            private int totalCount;

            @SerializedName("items")
            private Comment[] items;

            public int getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(int totalCount) {
                this.totalCount = totalCount;
            }

            public Comment[] getItems() {
                return items;
            }

            public void setItems(Comment[] items) {
                this.items = items;
            }

            public static class Comment {
                @SerializedName("commenter_id")
                private int commenterId;

                @SerializedName("commenter_name")
                private String commenterName;

                @SerializedName("comment")
                private String comment;

                public int getCommenterId() {
                    return commenterId;
                }

                public void setCommenterId(int commenterId) {
                    this.commenterId = commenterId;
                }

                public String getCommenterName() {
                    return commenterName;
                }

                public void setCommenterName(String commenterName) {
                    this.commenterName = commenterName;
                }

                public String getComment() {
                    return comment;
                }

                public void setComment(String comment) {
                    this.comment = comment;
                }
            }
        }
    }
}
