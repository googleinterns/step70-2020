/*
 *  Class handles actions related to creating and analyzing new comments
 *  TODO: add methods to add new comment to youtube video using Youtube API
 */

class Comment{
  #MAX_COMMENTS  = 3000;
  #NO_COMMENT_ERROR = 'No comment inputted';
  #COMMENT_LENGTH_EXCEEDED_ERROR = 'Comment is too long. We are currently only able to analyze comments of up to approx. 3000 characters.';
  constructor(text) {
    this.text = text;
    this.isValid = !this.getError();
    this.perspectiveString = this.makePerspectiveRequestString();
  }
  getError() {
    if(this.text == 0) {
      return this.#NO_COMMENT_ERROR;
    }
    if(this.text.length > this.#MAX_COMMENTS) {
      return this.#COMMENT_LENGTH_EXCEEDED_ERROR;
    }
    return 0;
  }
  makePerspectiveRequestString() {
    const bodyObject = {
      'comment': {'text': this.text},
      'requestedAttributes': {'TOXICITY': {}},
      'languages': ['en']
    };
    return JSON.stringify(bodyObject);
  }
}
