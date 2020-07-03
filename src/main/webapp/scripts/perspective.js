/*
 *  Makes a new comment object form the text box
 *  If it is valid, sends a Request object to Perspective API and updates DOM
 *  If not valid, updates DOM with an error message
 */

 function toxicityToPercent(toxicity) {
   return (toxicity*100).toFixed(2);
 }

function getToxicity() {
  const comment = new Comment(document.getElementById("comment-text").value);
  if(!comment.isValid) {
    updateDom(comment.getError(), 'toxicity-container');
    return;
  }
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
      updateDom(toxicityToPercent(toxicity), 'toxicity-container');
    })
    .catch((err) => {
      updateDom('The analysis feature is unavailable at this moment.', 'toxicity-container');
      console.error('An error occurred with the Perspective API', err);
    });
}
