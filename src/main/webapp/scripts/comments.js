const key = 'DEV_KEY_HERE'; //<- change as needed
const ytDiscovery =
    'https://www.googleapis.com/discovery/v1/apis/youtube/v3/rest';

async function initClient() {
  await gapi.client.init({
    'apiKey': key,
    'discoveryDocs': [ytDiscovery],
  });
    
  getComments(function(commentsList) {
    updateSentiment(commentsList);
  });
}

function getComments(callback) {
  let commentsList = new Array();
  
  try {
    const request = gapi.client.youtube.commentThreads.list({
      'part': ['snippet'],
      'maxResults': 25,
      'order': 'relevance',
      'textFormat': 'plainText',
      'videoId': 'kNovwPIWr3Q' //<- sample video for now
    });
        
    return request.execute(function(response) {
      for (commentThread of response.result.items) {
        let commentContent =
            commentThread.snippet.topLevelComment.snippet.textDisplay;
        commentsList.push(commentContent);
      }
      callback(commentsList);
    });

  } catch (err) {
    console.error("Unable to retrieve comments", err);
    commentsList = [];
  }
}

function updateSentiment(commentsList) {
  fetch('/yt', {
    method: 'POST',
    body: JSON.stringify(commentsList),
    headers: {
      'Content-type': 'application/json' 
    }
  });
}

gapi.load('client', initClient);