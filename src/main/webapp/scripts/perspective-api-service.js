function getToxicityFromPerspectiveApi(comment) {
  const request = new Request(
    `https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=${API_KEY}`,
    {
      method: 'POST',
      body: comment.perspectiveString
    }
  );
  return fetch(request)
  .then(response => response.json())
  .then((responseJson) => {
    return responseJson.attributeScores.TOXICITY.summaryScore.value;
  })
  .catch((err) => {
    console.error('An error occurred with the Perspective API', err);
    throw PERSPECTIVE_API_ERROR;
  });
}
