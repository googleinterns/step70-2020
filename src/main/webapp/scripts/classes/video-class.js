class Video {
  constructor(videoListItem, sentiment) {
    this.id = videoListItem.id;
    //this.caption = videoListItem.contentDetails.caption;
    //this.categoryId = videoListItem.snippet.categoryId;
    this.channelTitle = videoListItem.snippet.channelTitle;
    //this.description = videoListItem.snippet.description;
    this.thumbnail = videoListItem.snippet.thumbnails.default.url;
    this.title = videoListItem.snippet.title;
    this.likeCount = videoListItem.statistics.likeCount;
    this.sentiment = sentiment;
  }
}/*
width="100%"
height="225"
preserveAspectRatio="xMidYMid slice"
focusable="false"
role="img"*/

export { Video }
