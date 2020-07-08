/*
 *  Makes a new comment object form the text box
 *  If it is valid, sends a Request object to Perspective API and updates DOM
 *  If not valid, updates DOM with an error message
 */

 const NO_COMMENT_ERROR =  new Error('No comment inputted');
 const COMMENT_LENGTH_EXCEEDED_ERROR = new Error('Comment is too long. We are currently only able to analyze comments of up to approx. 3000 characters.');

 function toxicityToPercent(toxicity) {
   return (toxicity*100).toFixed(2).toString() + '%';
 }

function getToxicity() {
  let comment;
  try {
    comment = new Comment(document.getElementById('comment-text').value);
  } catch(err) {
    updateDom(err.message, 'toxicity-container');
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
