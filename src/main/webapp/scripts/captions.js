
function getCaptions() {
  videoId = document.getElementById("video-url").value.split('v=')[1];
  fetch(`http://video.google.com/timedtext?lang=en&v=${videoId}`)
    .then(response => response.text())
    .then((responseText) => {
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(responseText,"text/xml");
      const textDom = xmlDoc.getElementsByTagName("text");
      const captions = [];
      for (var i = 0; i < textDom.length; i++) {
        captions.push(textDom[i].childNodes[0].nodeValue);
      }
      captionsJson = JSON.stringify(captions);
      /* TODO: Call a function to Sentiment Analysis API */
      const sentiment = "This feature is not ready yet, here are the captions for now: " + captionsJson;
      containerDOM = document.getElementById("sentiment-container");
      containerDOM.innerText = sentiment;
    });
}
