NO_COMMENT_ERROR =  new Error('No comment inputted');
COMMENT_LENGTH_EXCEEDED_ERROR = new Error('Comment is too long. We are currently only able to analyze comments of up to approx. 3000 characters.');
PERSPECTIVE_API_ERROR =  new Error('The analysis feature is unavailable at this moment.');

function getToxicity() {
  let comment;
  try {
    comment = new Comment(getDomValue('comment-text'));
  } catch(error) {
    updateDom(error.message, 'toxicity-container');
    return;
  }
  updateDom('Calculating...', 'toxicity-container');
  getToxicityFromPerspectiveApi(comment).then((response) => {
    updateDom(toxicityToPercentString(response), 'toxicity-container');
  }).catch((error) =>  {
    updateDom(error.message, 'toxicity-container');
  });
}

function toxicityToPercentString(toxicity) {
  return (toxicity*100).toFixed(2).toString() + '%';
}
