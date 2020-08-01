/**
 * @param {String} linkUrl, to be analyzed
 * @returns {String} sentiment analysis message
 * Modified based on src/main/webapp/scripts/analyze.js
 */
const API_KEY = '';

async function getSentimentAnalysisMsg(linkUrl) {
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
 * Modified based on src/main/webapp/scripts/perspective.js
 * and src/main/webapp/scripts/perspective-api-service.js
 */
async function getToxicityAnalysisMsg(text) {
  const request = new Request(
    `https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=${API_KEY}`,
    {
      method: 'POST',
      body: makePerspectiveRequestString(text)
    }
  );
  return fetch(request)
    .then(response => response.json())
    .then((responseJson) => {
      return toxicityToPercentString(responseJson.attributeScores.TOXICITY.summaryScore.value);
    })
    .catch((err) => {
      console.error('An error occurred with the Perspective API', err);
      return 'The analysis feature is unavailable at this moment.';
    });
}

function makePerspectiveRequestString(text) {
  const bodyObject = {
    'comment': { 'text': text },
    'requestedAttributes': { 'TOXICITY': {} },
    'languages': ['en']
  };
  return JSON.stringify(bodyObject);
}

function toxicityToPercentString(toxicity) {
  return (toxicity * 100).toFixed(1).toString() + '% chance of being toxic';
}

export { getSentimentAnalysisMsg, getToxicityAnalysisMsg };
