let player;
function onYouTubeIframeAPIReady() {
  player = new YT.Player('player', {});
}

async function analyzeVideo() {
  const videoId = getVideoId();
  player.loadVideoById(videoId, 0, "large");
  displayLoading('sentiment-container');
  return fetch(`/sentiment?video-id=${videoId}`)
  .then(response => {
    if (response.ok) {
      return response.json();
    } else {
      throw new Error(response.statusText);
    }
  })
  .then(videoAnalysis => {
    if(videoAnalysis.scoreAvailable) {
      displaySentiment(videoAnalysis);
    } else {
      throw new Error('Video has no available captions or comments to analyze.');
    }
  })
  .catch(error => {
    displaySentiment({ scoreAvailable: false, message: error.message});
    console.error(error);
  });
}
