function analyzeVideo() {
  const videoId = document.getElementById("video-url").value.split('v=')[1];
  fetch('/sentiment', {
    method: 'POST',
    body: videoId
  });
}