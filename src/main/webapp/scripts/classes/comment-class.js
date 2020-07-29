class Comment{
  #MAX_COMMENTS = 3000;

  constructor(text) {
    return (async() => {
      this.text = text;
      if(!this.text) {
        console.error("User did not input any comment");
        throw NO_COMMENT_ERROR;
      }
      if(this.text.length > this.#MAX_COMMENTS) {
        console.error("User exceeded maximum comment length");
        throw COMMENT_LENGTH_EXCEEDED_ERROR;
      }
      this.toxicity = await getToxicityFromPerspectiveApi(text);
      return this;
    })();
  }
}
