package com.squeed.chromecast.hipstacaster.dto;

import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.*;
import com.flickr4java.flickr.stats.Stats;
import com.flickr4java.flickr.tags.Tag;

import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2013-10-03
 * Time: 14:24
 * To change this template use File | Settings | File Templates.
 */
public class PhotoDTO {

    private static final String DEFAULT_ORIGINAL_IMAGE_SUFFIX = "_o.jpg";

    private static final String SMALL_SQUARE_IMAGE_SUFFIX = "_s.jpg";

    private static final String SMALL_IMAGE_SUFFIX = "_m.jpg";

    private static final String THUMBNAIL_IMAGE_SUFFIX = "_t.jpg";

    private static final String MEDIUM_IMAGE_SUFFIX = ".jpg";

    private static final String LARGE_IMAGE_SUFFIX = "_b.jpg";

    private static final String LARGE_1600_IMAGE_SUFFIX = "_h.jpg";

    private static final String LARGE_2048_IMAGE_SUFFIX = "_k.jpg";

    private static final String SQUARE_LARGE_IMAGE_SUFFIX = "_q.jpg";

    private static final String SQUARE_320_IMAGE_SUFFIX = "_n.jpg";

    private static final String MEDIUM_640_IMAGE_SUFFIX = "_z.jpg";

    private static final String MEDIUM_800_IMAGE_SUFFIX = "_c.jpg";

    private Size squareSize;

    private Size smallSize;

    private Size thumbnailSize;

    private Size mediumSize;

    private Size largeSize;

    private Size large1600Size;

    private Size large2048Size;

    private Size originalSize;

    private Size squareLargeSize;

    private Size small320Size;

    private Size medium640Size;

    private Size medium800Size;

    private Size videoPlayer;

    private Size siteMP4;

    private Size videoOriginal;

    private String id;

    private User owner;

    private String secret;

    private String farm;

    private String server;

    private boolean favorite;

    private String license;

    private boolean primary;

    private String title;

    private String description;

    private boolean publicFlag;

    private boolean friendFlag;

    //private boolean familyFlag;

    private Date dateAdded;

    private Date datePosted;

    private Date dateTaken;

    private Date lastUpdate;

    private String takenGranularity;

    private Permissions permissions;

    private Editability editability;

    private int comments;

    private int views = -1;

    private int rotation;

    //private Collection<Note> notes;

    private Collection<Tag> tags;

    private Collection<String> urls;

    private String iconServer;

    private String iconFarm;

    private String url;

    //private GeoData geoData;

    private String originalFormat;

    private String originalSecret;

    private String placeId;

    private String media;

    private String mediaStatus;

    private String pathAlias;

    private int originalWidth;

    private int originalHeight;

    /**
     * Stats on views, comments and favorites. Only set on {@link com.flickr4java.flickr.stats.StatsInterface#getPopularPhotos} call.
     */
    private Stats stats;

    public PhotoDTO() {

    }

    public PhotoDTO(Photo photo) {
        setDateAdded(photo.getDateAdded());
        setLicense(photo.getLicense());
        setComments(photo.getComments());
        setDatePosted(photo.getDatePosted());
        setDateTaken(photo.getDateTaken());
        setDescription(photo.getDescription());
        setEditability(photo.getEditability());
        setFarm(photo.getFarm());
        //setGeoData(photo.getGeoData());
        setIconFarm(photo.getIconFarm());
        setIconServer(photo.getIconServer());
        setId(photo.getId());
        setLarge1600Size(photo.getLarge1600Size());
        setLarge2048Size(photo.getLarge2048Size());
        setLargeSize(photo.getLargeSize());
        setLastUpdate(photo.getLastUpdate());
        setMedia(photo.getMedia());
        setMediaStatus(photo.getMediaStatus());
        setMedium640Size(photo.getMedium640Size());
        setMedium800Size(photo.getMedium800Size());
        setMediumSize(photo.getMediumSize());
        setSmall320Size(photo.getSmall320Size());
        setSmallSize(photo.getSmallSize());
        setThumbnailSize(photo.getThumbnailSize());
        setOriginalFormat(photo.getOriginalFormat());
        setOriginalHeight(photo.getOriginalHeight());
        setOriginalWidth(photo.getOriginalWidth());
        setOriginalSize(photo.getOriginalSize());
        //setNotes(photo.getNotes());
        setOwner(photo.getOwner());
        setTitle(photo.getTitle());
        setUrl(photo.getUrl());
    }

    public Size getSquareSize() {
        return squareSize;
    }

    public void setSquareSize(Size squareSize) {
        this.squareSize = squareSize;
    }

    public Size getSmallSize() {
        return smallSize;
    }

    public void setSmallSize(Size smallSize) {
        this.smallSize = smallSize;
    }

    public Size getThumbnailSize() {
        return thumbnailSize;
    }

    public void setThumbnailSize(Size thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
    }

    public Size getMediumSize() {
        return mediumSize;
    }

    public void setMediumSize(Size mediumSize) {
        this.mediumSize = mediumSize;
    }

    public Size getLargeSize() {
        return largeSize;
    }

    public void setLargeSize(Size largeSize) {
        this.largeSize = largeSize;
    }

    public Size getLarge1600Size() {
        return large1600Size;
    }

    public void setLarge1600Size(Size large1600Size) {
        this.large1600Size = large1600Size;
    }

    public Size getLarge2048Size() {
        return large2048Size;
    }

    public void setLarge2048Size(Size large2048Size) {
        this.large2048Size = large2048Size;
    }

    public Size getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(Size originalSize) {
        this.originalSize = originalSize;
    }

    public Size getSquareLargeSize() {
        return squareLargeSize;
    }

    public void setSquareLargeSize(Size squareLargeSize) {
        this.squareLargeSize = squareLargeSize;
    }

    public Size getSmall320Size() {
        return small320Size;
    }

    public void setSmall320Size(Size small320Size) {
        this.small320Size = small320Size;
    }

    public Size getMedium640Size() {
        return medium640Size;
    }

    public void setMedium640Size(Size medium640Size) {
        this.medium640Size = medium640Size;
    }

    public Size getMedium800Size() {
        return medium800Size;
    }

    public void setMedium800Size(Size medium800Size) {
        this.medium800Size = medium800Size;
    }

    public Size getVideoPlayer() {
        return videoPlayer;
    }

    public void setVideoPlayer(Size videoPlayer) {
        this.videoPlayer = videoPlayer;
    }

    public Size getSiteMP4() {
        return siteMP4;
    }

    public void setSiteMP4(Size siteMP4) {
        this.siteMP4 = siteMP4;
    }

    public Size getVideoOriginal() {
        return videoOriginal;
    }

    public void setVideoOriginal(Size videoOriginal) {
        this.videoOriginal = videoOriginal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(boolean publicFlag) {
        this.publicFlag = publicFlag;
    }



    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getTakenGranularity() {
        return takenGranularity;
    }

    public void setTakenGranularity(String takenGranularity) {
        this.takenGranularity = takenGranularity;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public Editability getEditability() {
        return editability;
    }

    public void setEditability(Editability editability) {
        this.editability = editability;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

//    public Collection<Note> getNotes() {
//        return notes;
//    }
//
//    public void setNotes(Collection<Note> notes) {
//        this.notes = notes;
//    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public Collection<String> getUrls() {
        return urls;
    }

    public void setUrls(Collection<String> urls) {
        this.urls = urls;
    }

    public String getIconServer() {
        return iconServer;
    }

    public void setIconServer(String iconServer) {
        this.iconServer = iconServer;
    }

    public String getIconFarm() {
        return iconFarm;
    }

    public void setIconFarm(String iconFarm) {
        this.iconFarm = iconFarm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public GeoData getGeoData() {
//        return geoData;
//    }
//
//    public void setGeoData(GeoData geoData) {
//        this.geoData = geoData;
//    }

    public String getOriginalFormat() {
        return originalFormat;
    }

    public void setOriginalFormat(String originalFormat) {
        this.originalFormat = originalFormat;
    }

    public String getOriginalSecret() {
        return originalSecret;
    }

    public void setOriginalSecret(String originalSecret) {
        this.originalSecret = originalSecret;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getMediaStatus() {
        return mediaStatus;
    }

    public void setMediaStatus(String mediaStatus) {
        this.mediaStatus = mediaStatus;
    }

    public String getPathAlias() {
        return pathAlias;
    }

    public void setPathAlias(String pathAlias) {
        this.pathAlias = pathAlias;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(int originalWidth) {
        this.originalWidth = originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(int originalHeight) {
        this.originalHeight = originalHeight;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
