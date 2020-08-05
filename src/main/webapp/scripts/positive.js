import { getVideoFromYoutubeApi } from './list-video-service.js';
import { VideoListDisplay } from './classes/recommended-video-display-class.js';
import { Video } from './classes/video-class.js';

function loadPositive() {
  loadApi().then(() => fetch('positive_videos'))
  .then(response => response.json())
  .then((responseJson) => {
    const recommended = new VideoListDisplay('positive-list-container');
    for(const result of responseJson) {
      getVideoFromYoutubeApi(result.videoId).then((videoData) => {
        const video = new Video(videoData.items[0], result.score);
        recommended.addVideo(video);
      });
    }
  })
  .catch((error) => {
    updateDom('An error occurred getting videos', 'positive-list-container');
    console.error(error);
  });
}

loadPositive();
