NO_COMMENT_ERROR =  new Error('No comment inputted');
COMMENT_LENGTH_EXCEEDED_ERROR = new Error('Comment is too long. We are currently only able to analyze comments of up to approx. 3000 characters.');
PERSPECTIVE_API_ERROR =  new Error('The analysis feature is unavailable at this moment.');

async function getToxicity() {
  let comment;
  try {
    comment = await new Comment(getDomValue('comment-text'));
    updateDom(toxicityToPercentString(comment.toxicity), 'toxicity-container');
  } catch(error) {
    updateDom(error.message, 'toxicity-container');
  }
}

function toxicityToPercentString(toxicity) {
  return (toxicity*100).toFixed(2).toString() + '%';
}
