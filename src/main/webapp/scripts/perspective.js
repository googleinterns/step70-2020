loadScript('scripts/classes/comment-class.js');
loadScript('scripts/perspective-api-service.js');

NO_COMMENT_ERROR =  new Error('No comment inputted');
COMMENT_LENGTH_EXCEEDED_ERROR = new Error('Comment is too long. We are currently only able to analyze comments of up to approx. 3000 characters.');
PERSPECTIVE_API_ERROR =  new Error('The analysis feature is unavailable at this moment.');

function getToxicity() {
  let comment;
  try {
    comment = new Comment(document.getElementById('comment-text').value);
  } catch(error) {
    updateDom(error.message, 'toxicity-container');
  }
  perspectiveApiCall(comment, function(error, result) {
    if(error != null) {
      updateDom(error.message, 'toxicity-container');
      return;
    }
    updateDom(toxicityToPercentString(result), 'toxicity-container');
  });
}

function toxicityToPercentString(toxicity) {
  return (toxicity*100).toFixed(2).toString() + '%';
}
