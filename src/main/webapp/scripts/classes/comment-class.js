/*
 *  Class constructs Comment object from text string
 *  responds with comment object that is ready for Perspective API use or error
 */

class Comment{
  #MAX_COMMENTS = 3000;

  constructor(text) {
    this.text = text;
    if(!this.text) {
      console.error("User did not input any comment");
      throw NO_COMMENT_ERROR;
    }
    if(this.text.length > this.#MAX_COMMENTS) {
      console.error("User exceeded maximum comment length");
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
