class Video {
  constructor(videoListItem, sentiment) {
    this.id = videoListItem.id;
    this.thumbnail = videoListItem.snippet.thumbnails.medium.url;
    this.title = videoListItem.snippet.title;
    this.likeCount = videoListItem.statistics.likeCount;
    this.sentiment = sentiment;
  }
}

export { Video }
