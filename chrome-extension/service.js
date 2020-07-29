/**  
 * @param {String} linkUrl, to be analyzed
 * @returns {String} sentiment analysis message
 */
async function getSentimentAnaylsisMsg(linkUrl) {
  // TODO use regular expression 
  const videoId = linkUrl.split('v=')[1];
  // TODO enable CORS in backend
  const proxyUrl = 'https://cors-anywhere.herokuapp.com/';
  return fetch(proxyUrl + `https://step70-2020.appspot.com/sentiment?video-id=${videoId}`)
    .then(response => {
      if (response.ok) {
        return response.json();
      } else {
        throw new Error(response.statusText);
      }
    })
    .then(videoAnalysis => {
      if (videoAnalysis.scoreAvailable) {
        return videoAnalysis.score.toString() + ' out of ±1';
      } else {
        return 'Video has no available captions or comments to analyze.';
      }
    })
    .catch(error => {
      return error.message;
    });
}

/**  
 * @param {String} text, to be analyzed
 * @returns {String} toxicity analysis message
 */
async function getToxicityAnaylsisMsg(text) {
  // TODO add service later
  return 'Toxicity score is 24';
}

export { getSentimentAnaylsisMsg, getToxicityAnaylsisMsg };