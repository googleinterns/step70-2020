function analyzeVideo() {
  const videoId = document.getElementById('video-url').value.split('v=')[1];
  const baseUrl = window.location.origin;
  let url = new URL('/sentiment', baseUrl);
  url.searchParams.append('video-id', videoId);

  fetch(url)
  .then(response => {
    if (response.status >= 200 && response.status <= 299) {
      return response.json();
    } else if (response.status >= 400 && response.status <= 499) {
      throw InvalidVideoException(response.statusText);
    } else {
      throw ServletFailureException(response.statusText);
    }
  })
  .then(videoAnalysis => {
    if (videoAnalysis.dataAvailable) {
      updateSentimentContainer(videoAnalysis.score);
    } else {
      updateSentimentContainer("We couldn't analyze this video! Try again.")
    }
  })
  .catch(error => {
    updateSentimentContainer(error.message);
  });
}

function updateSentimentContainer(text) {
  const container = document.getElementById('sentiment-container');
  container.innerText = text.toString();
}

function InvalidVideoException(message, metadata) {
  const error = new Error(message);
  error.metadata = metadata;
  return error;
}

function ServletFailureException(message, metadata) {
  const error = new Error(message);
  error.metadata = metadata;
  return error;
}