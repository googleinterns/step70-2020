/*
 *  Class handles actions related to creating and analyzing new comments
 *  TODO: add methods to add new comment to youtube video using Youtube API
 */

class Comment{
  #MAX_COMMENTS  = 3000;
  constructor(text) {
    this.text = text;
    if(!this.text) {
      throw NO_COMMENT_ERROR;
    }
    if(this.text.length > this.#MAX_COMMENTS) {
      throw COMMENT_LENGTH_EXCEEDED_ERROR;
    }
    this.perspectiveString = this.makePerspectiveRequestString();
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
