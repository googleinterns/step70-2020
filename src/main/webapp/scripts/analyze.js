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
    displaySentiment(videoAnalysis);
  })
  .catch(error => {
    displaySentiment({ scoreAvailable: false });
    console.error(error);
  });
}
