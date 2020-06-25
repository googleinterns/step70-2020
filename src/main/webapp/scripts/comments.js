const key = 'DEV_KEY_HERE'; //<- change as needed
const ytDiscovery = 'https://www.googleapis.com/discovery/v1/apis/youtube/v3/rest';
const languageDiscovery = 'https://language.googleapis.com/$discovery/rest?version=v1beta2';

function loadClient() {
    gapi.client.init({
        'apiKey': key,
        'discoveryDocs': [ytDiscovery, languageDiscovery],
    }).then(function() {
        return gapi.client.youtube.commentThreads.list({
            'part': ['snippet,replies'],
            'maxResults': 100,
            'textFormat': 'plainText',
            'videoId': 'kNovwPIWr3Q' //<- sample video for now
        }).then(function(response) {
            let commentsList = new Array();
            for (commentThread of response.result.items) {
                let commentContent = commentThread.snippet.topLevelComment.snippet.textDisplay;
                commentsList.push(commentContent);
            }

            fetch('/yt', {
                method: 'POST',
                body: JSON.stringify(commentsList),
                headers: {
                    'Content-type': 'application/json' 
                }
            });

        }, function(err) {
            console.error("Execute error", err);
        });
    });
}

gapi.load('client', loadClient);