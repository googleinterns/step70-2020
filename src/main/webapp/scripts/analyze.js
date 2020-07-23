async function analyzeVideo() {
  const videoId = document.getElementById('video-url').value.split('v=')[1];

  return fetch(`/sentiment?video-id=${videoId}`)
  .then(response => {
    if (response.status >= 200 && response.status <= 299) {
      return response.json();
    } else {
      throw new Error(response.statusText);
    }
  })
  .then(videoAnalysis => {
    if (videoAnalysis.scoreAvailable) {
      updateDom(videoAnalysis.score.toString(), 'sentiment-container');
    } else {
      updateDom('We couldn\'t analyze this video! Try again.',
          'sentiment-container');
    }
  })
  .catch(error => {
    updateDom(error.message, 'sentiment-container');
  });
}