function postVideoComment(videoId, commentText) {
    return new Promise((resolve, reject) => {
        gapi.client.request(buildRequestBody(videoId, commentText))
            .then(res => res.json)
            .then(data => resolve(data))
            .catch(err => reject(new Error(err.result.error.message)));
    });
}

function buildRequestBody(videoId, commentContent) {
    return {
        'path': '/youtube/v3/commentThreads?part=snippet',
        'method': 'POST',
        'body': {
            'snippet': {
                'videoId': videoId,
                'topLevelComment': {
                    'snippet': {
                        'textOriginal': commentContent
                    },
                },
            },
        },
    };
}

export { postVideoComment };