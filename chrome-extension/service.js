/**  
 * @param {String} linkUrl, to be analyzed
 * @returns {String} sentiment analysis message
 */
async function getSentimentAnaylsisMsg(linkUrl) {
  const videoId = getVideoId(linkUrl);
  return fetch(`https://step70-2020.appspot.com/sentiment?video-id=${videoId}`)
    .then(response => {
      if (response.ok) {
        return response.json();
      } else {
        throw new Error(response.statusText);
      }
    })
    .then(videoAnalysis => {
      if (videoAnalysis.scoreAvailable) {
        return sentimentScoreToNumerator(videoAnalysis.score) + ' out of 10';
      } else {
        return 'Video has no available captions or comments to analyze.';
      }
    })
    .catch(error => {
      return error.message;
    });
}

/**
 * @param {String} linkUrl
 * @returns {String} video id 
 * Based on src/main/webapp/scripts/script.js
 */
function getVideoId(linkUrl) {
  const regex = /^.*(?:(?:youtu\.be\/|v\/|vi\/|u\/\w\/|embed\/)|(?:(?:watch)?\?v(?:i)?=|\&v(?:i)?=))([^#\&\?]*).*/;
  return linkUrl.match(regex)[1];
}

/**
 * @param {float} score
 * @returns {float} convert score from -1 to 1 to fraction out of 10
 * Based on src/main/webapp/scripts/script.js
 */
function sentimentScoreToNumerator(score) {
  return (score / 2 * 10 + 5).toFixed(1);
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