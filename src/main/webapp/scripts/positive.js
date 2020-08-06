import { getVideoFromYoutubeApi } from './list-video-service.js';
import { VideoListDisplay } from './classes/recommended-video-display-class.js';
import { Video } from './classes/video-class.js';

function loadPositive() {
  return fetch('positive_videos')
  .then(response => response.json())
  .then((responseJson) => {
    console.log(responseJson)
    const recommended = new VideoListDisplay('positive-list-container');
    for(const result of responseJson) {
      positive.getVideoFromYoutubeApi(result.id).then((videoData) => {
        const video = new Video(videoData.items[0], result.score);
        recommended.addVideo(video);
      }).catch((error) => {
        updateDom('An error occurred with YouTube', 'positive-list-container');
        console.error(error);
      });
    }
  })
  .catch((error) => {
    updateDom('An error occurred getting videos', 'positive-list-container');
    console.error(error);
  });
}

let positive = { loadPositive, getVideoFromYoutubeApi };
displayLoading('positive-list-container');
loadApi().then(() => {
  positive.loadPositive();
});

export default positive;
