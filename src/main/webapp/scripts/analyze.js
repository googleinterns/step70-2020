async function analyzeVideo() {
  const videoId = document.getElementById('video-url').value.split('v=')[1];

  return fetch(`/sentiment?video-id=${videoId}`)
  .then(response => {
    if (response.ok) {
      return response.json();
    } else {
      throw new Error(response.statusText);
    }
  })
  .then(videoAnalysis => {
    if (videoAnalysis.scoreAvailable) {
      updateDom(videoAnalysis.score.toString(), 'sentiment-container');
    } else {
      updateDom('Video has no available captions or comments to analyze.',
          'sentiment-container');
    }
  })
  .catch(error => {
    updateDom(error.message, 'sentiment-container');
  });
}