package com.emillics.lemon.model;

import java.io.Serializable;

/**
 * @author Honey
 * Create on 2025-01-02 09:23:49
 */
public class Video implements Serializable {

    private static final long serialVersionUID = 1595360824217L;

    private String id;
    private String source;
    private String userId;
    private String userName;
    private String userAvatar;
    private String title;
    private String tags;
    private String type;
    private Long publishTime;
    private Long likeCount;
    private Long commentCount;
    private Long favouriteCount;
    private Long shareCount;
    private Long createTime;
    private String path;
    private String url;
    private Long duration;
    private String format;
    private Long size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(Long favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Video{");
        sb.append("id='").append(id).append('\'');
        sb.append(", source='").append(source).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", userAvatar='").append(userAvatar).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", tags='").append(tags).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", publishTime=").append(publishTime);
        sb.append(", likeCount=").append(likeCount);
        sb.append(", commentCount=").append(commentCount);
        sb.append(", favouriteCount=").append(favouriteCount);
        sb.append(", shareCount=").append(shareCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", path='").append(path).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", duration=").append(duration);
        sb.append(", format='").append(format).append('\'');
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }
}