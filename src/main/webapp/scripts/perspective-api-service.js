/*
 *  Makes a new comment object form the text box
 *  If it is valid, sends a Request object to Perspective API and updates DOM
 *  If not valid, updates DOM with an error message
 */

function perspectiveApiCall(comment, callback) {
  const request = new Request(
    `https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=${API_KEY}`,
    {
      method: 'POST',
      body: comment.perspectiveString
    }
  );
  fetch(request)
  .then(response => response.json())
  .then((responseJson) => {
    const toxicity = responseJson.attributeScores.TOXICITY.summaryScore.value;
    callback(null, toxicity);
  })
  .catch((err) => {
    console.error('An error occurred with the Perspective API', err);
    callback(PERSPECTIVE_API_ERROR);
  });
}