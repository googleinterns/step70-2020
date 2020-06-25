function getToxicity() {
  API_KEY = '';
  const commentText = document.getElementById("comment-text").value;
  const commentObject = {
    'comment': {'text': commentText},
    'requestedAttributes': {'TOXICITY': {}},
    'languages': ['en']
  };
  const json = JSON.stringify(commentObject);
  const request = new Request(
    `https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=${API_KEY}`,
    {method: 'POST', body: json}
  );
  fetch(request)
    .then(response => response.json())
    .then((responseJson) => {
      const score = responseJson.attributeScores.TOXICITY.summaryScore.value;
      containerDOM = document.getElementById('toxicity-container');
      containerDOM.innerText = score.toFixed(2)*100 + '% likely to be toxic.';
    });
}
